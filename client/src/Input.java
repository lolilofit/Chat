import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

//communication between console and sockets
public class Input extends Thread {
    String userName;
    Socket client;
    BufferedReader console_in;
    BufferedWriter out;


    Input(Socket _client) {
        client = _client;

        try {
            console_in = new BufferedReader(new InputStreamReader(System.in));
            out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        String cmd;

        try {
            userName = console_in.readLine();
            out.write(userName + "\n");
            out.flush();
            cmd = console_in.readLine();
            out.write(cmd + "\n");
            out.flush();

            while (true) {
                try {
                    cmd = console_in.readLine();

                    Command message = null;
                    String[] param = cmd.split(":");

                    if(param.length == 3) {
                        message = Factory.getInstance().create_op_three(param[0], userName, param[1], param[2]);
                    }
                    if(param.length == 2) {
                        message = Factory.getInstance().create_op_two(param[0], userName, param[1]);
                    }
                    if(param.length == 1) {
                        message = Factory.getInstance().create_op_one(param[0], userName);
                    }

                    Gson gson;
                    GsonBuilder builder = new GsonBuilder();
                    gson = builder.create();

                    out.write(gson.toJson(message) + "\n");
                    out.flush();

                }
                catch (SocketException e) {
                    System.out.println("Connection closed.");
                    client.close();
                    System.exit(1);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Connection closed.");
            try {
                client.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            System.exit(1);

        }
    }
}