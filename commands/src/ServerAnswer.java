
class ServerAnswer extends Command {
    String source;
    String destination;
    String message;

    ServerAnswer(String src, String dst, String mes) {
        super(ServerAnswer.class.getName());
        source = src;
        destination = dst;
        message = mes;
    }

    void doWork() {
        System.out.println(message);
    }
}


