
import java.io.IOException;

public class SendChat extends Command{
    String source;
    String chatName;
    String message;

    SendChat(String src, String chat, String mes) {
        super(SendChat.class.getName());
        source = src;
        chatName = chat;
        message = mes;
    }

    public void doWork() {
        try {
            Server.getInstance().sendAllChat(chatName, message, source);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

