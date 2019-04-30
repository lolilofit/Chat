import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

class ShowUsers extends Command{

    String sender;

    ShowUsers(String src) {
        super(ShowUsers.class.getName());
        sender = src;
    }

    void doWork() {
        String names = Server.getInstance().showUsers();
        Server.getInstance().sendAnswer(sender, names);
    }

}