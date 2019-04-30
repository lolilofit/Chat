
import java.io.IOException;

class MessageToUser extends Command {

    String source;
    String destination;
    String message;

    MessageToUser(String src, String dst, String mes) {
        super(MessageToUser.class.getName());
        source = src;
        destination = dst;
        message = mes;
    }

    @Override
    void doWork() {
        try {
            Server.getInstance().setClientMessage(source, destination, message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

