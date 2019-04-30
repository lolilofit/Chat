import java.io.IOException;

class Broadcast extends Command {
    String source;
    String message;

    Broadcast(String src, String mes) {
        super(Broadcast.class.getName());
        source = src;
        message = mes;
    }

    void doWork() {
        Server.getInstance().sendAll(source, message);
    }

}

