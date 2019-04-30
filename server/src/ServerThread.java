import com.google.gson.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.Socket;
import java.net.SocketException;

public class ServerThread extends Thread {

    private String login;
    private Socket userSocket;
    private BufferedReader in;
    private BufferedWriter out;
    private Server server;

    public ServerThread(String _login, Socket client, BufferedReader _in, BufferedWriter _out, Server _server) {
        login = _login;
        userSocket = client;
        in = _in;
        out = _out;
        server = _server;
    }


    @Override
    public void run() {
        try {
            String client_message = "";
            while (!Thread.currentThread().isInterrupted()) {

                    try {
                        client_message = in.readLine();
                    } catch (SocketException ex) {
                        System.out.println("client exit");
                        synchronized (Server.class) {
                            try {
                                userSocket.close();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                            Thread.currentThread().interrupt();
                        }
                        break;
                    }

                    Gson gson;
                    GsonBuilder builder = new GsonBuilder();
                    gson = builder.create();

                    JsonElement parser = new JsonParser().parse(client_message);
                    JsonObject object = parser.getAsJsonObject();
                    String className = object.get("className").toString();
                    className = className.replaceAll("[^A-Za-zА-Яа-я0-9]", "");


                    Command cmd = gson.fromJson(client_message, (Type) Class.forName(className));

                    synchronized (Server.class) {
                        cmd.doWork();
                    }
            }
        } catch(Throwable e) {
            e.printStackTrace();
            System.out.println("finish user thread");
            synchronized (Server.class) {
                try {
                    userSocket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                Thread.currentThread().interrupt();
            }
        }

    }
}
