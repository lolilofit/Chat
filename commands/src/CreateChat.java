public class CreateChat extends Command{
    String iniciator;
    String chatName;
    String users;

    public CreateChat(String src, String name, String chatUsers) {
        super(CreateChat.class.getName());
        iniciator = src;
        chatName = name;
        users = chatUsers;
    }

    public  void doWork() {
        Server.getInstance().createChat(chatName, users);
    }
}
