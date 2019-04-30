import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.*;

public class Server {

    private int clientMaxNum = 10;
    private Map<String, Socket> socketMap;
    private ArrayList<ServerThread> serverThreadList;
    private Map<String, ArrayList<String>> chats;
    private Server thisServer = null;
    private static volatile Server instance;

    private Server() {

        socketMap = new HashMap<>(clientMaxNum);
        serverThreadList = new ArrayList<>();
        chats = new HashMap<>();
    }

    public static Server getInstance() {
        if (instance == null) {
            synchronized (Server.class) {
                if (instance == null) {
                    instance = new Server();
                }
            }
        }
        return instance;
    }


    public void setClientMessage(String sender, String destination, String message) throws IOException {
            sendAnswer(destination, sender + " said:" + message);
            sendAnswer(sender, sender + " said:" + message);
        }

        public void sendAnswer(String name, String mes) {
        Gson gson = new Gson();
        GsonBuilder builder = new GsonBuilder();
        ServerAnswer answer = new ServerAnswer("", name, mes);

        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socketMap.get(name).getOutputStream()));
            writer.write(gson.toJson(answer) + "\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendAll(String sender, String mes) {
        for(Map.Entry<String, Socket> pair : socketMap.entrySet()) {
            sendAnswer(pair.getKey(), sender + " said: " + mes);
        }
    }

    public String showUsers() {
        String names = "";
        for(Map.Entry<String, Socket> pair : socketMap.entrySet()) {
            names += pair.getKey() + " ";
        }
        return names;
    }

    public void sendAllChat(String chat_name, String message, String initiator) throws IOException {
            ArrayList<String> chat = chats.get(chat_name);
            if(chat != null) {
                for (int i =0; i< chat.size(); i++)
                    setClientMessage(initiator, chat.get(i), message);
            }
            else {

            }
        }

    public int createChat(String name, String request) {

      String names[] = request.split(" ");

      ArrayList<String> chat = new ArrayList<>();
      for(int i = 0; i < names.length; i++)
          if(!names[i].equals("") && socketMap.containsKey(names[i]))
          chat.add(names[i]);
      chats.put(name.replace(" ", ""), chat);

      return chats.size() - 1;
      }


    public void serverLaunch() throws IOException {
        ServerSocket server = new ServerSocket(789);

        thisServer = this;

        Thread newUsers = new Thread(new Runnable() {
            @Override
            public void run() {

                Properties property = new Properties();
                try {
                    property.load(getClass().getResourceAsStream("password.properties"));
                } catch (IOException e) {
                    Thread.currentThread().isInterrupted();
                    e.printStackTrace();
                }

                while (true) {
                    try {
                        Socket client = server.accept();
                        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));


                        String userName = in.readLine();
                        String passwd = in.readLine();

                        ServerThread clientThread;
                        synchronized (Server.class) {
                            if (property.containsKey(userName) && property.getProperty(userName.replace(" ", "")).equals(passwd)) {

                                System.out.println("user accessed.");
                                socketMap.put(userName, client);

                                out.write("Access accepted." + "\n");
                                out.flush();
                                clientThread = new ServerThread(userName, client, in, out, thisServer);
                                serverThreadList.add(clientThread);

                            } else {
                                System.out.println("Access declined.");
                                out.write("Wrong login or password." + "\n");
                                out.flush();
                                socketMap.remove(userName);
                                serverThreadList.remove(serverThreadList.size() - 1);
                                client.close();
                            }
                        }
                    } catch (Throwable e) {

                    }
                }
            }
        });

        newUsers.setDaemon(true);
        newUsers.start();

        while (true) {
            synchronized (Server.class) {
                for (int i = 0; i < serverThreadList.size(); i++) {
                    if (serverThreadList.get(i) != null) {
                        if (serverThreadList.get(i).isInterrupted()) {
                            serverThreadList.remove(i);

                            Map<String, Socket> newSockets = new HashMap<>();

                            for(Map.Entry<String, Socket> pair : socketMap.entrySet()) {
                                if(!pair.getValue().isClosed())
                                    newSockets.put(pair.getKey(), pair.getValue());
                            }

                            socketMap.clear();
                            socketMap = newSockets;

                        } else if (!serverThreadList.get(i).isAlive() && !serverThreadList.get(i).isInterrupted())
                            serverThreadList.get(i).start();

                    }
                }
            }
        }
    }

}


