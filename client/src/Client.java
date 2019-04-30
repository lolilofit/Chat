import com.google.gson.*;

import java.io.*;
import java.lang.reflect.Type;
import java.net.Socket;
import java.net.SocketException;


public class Client {

    public void launchClient() throws IOException, ClassNotFoundException {
        Socket client = new Socket("localhost", 789);
        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        String init = "";
        boolean acess_accept = false;

        System.out.println("Your Login?:");
        Input input = new Input(client);
        input.start();
        init = in.readLine();
        System.out.println(init);

        while(!init.equals("Exit")) {
            try {
                init = in.readLine();
                Gson gson = new Gson();
                GsonBuilder builder = new GsonBuilder();
                gson = builder.create();

                JsonElement parser = new JsonParser().parse(init);
                JsonObject object = parser.getAsJsonObject();
                String className = object.get("className").toString();
                className = className.replaceAll("[^A-Za-zА-Яа-я0-9]", "");


                Command cmd = gson.fromJson(init, (Type) Class.forName(className));
                cmd.doWork();
            }
            catch (SocketException e) {
                System.out.println("Connection closed.");
                client.close();
                System.exit(1);
            }
 }

        in.close();
        client.close();

    }
}
