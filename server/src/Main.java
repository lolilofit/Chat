import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {


    public static void main(String[] args) throws IOException {
        Server server = Server.getInstance();
        server.serverLaunch();
    }
}
