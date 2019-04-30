public class About extends  Command{
    String source;

    About(String name) {
        super(About.class.getName());
        source = name;
    }

    public void doWork() {
        Server.getInstance().sendAnswer(source, "commands :: (broadcast), (create_chat:chat_name:users_in_chat),"
                + " (send_to_chat:chat_name:message), (send_message_to:user_name:message), (show_users)");
    }
}
