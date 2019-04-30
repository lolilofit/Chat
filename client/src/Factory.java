import java.util.Properties;

public class Factory {
    private Properties property;
    private static volatile Factory instance;


    private Factory() {
        try {
            property = new Properties();
            property.load(Factory.class.getResourceAsStream("classes.properties"));
        } catch (Exception e) {
            System.out.println("Facrory failed");
            return;
        }
    }

    public static Factory getInstance() {

        if (instance == null) {
            synchronized (Factory.class) {
                if (instance == null) {
                    instance = new Factory();
                }
            }
        }
        return instance;
    }

    public Command create_op_three(java.lang.String key, String source, String dest, String mes) {

        Command return_class = null;
        try {
            Class<?> _class = Class.forName(property.getProperty(key));
            return_class = (Command) _class.getDeclaredConstructor(String.class, String.class, String.class).newInstance(source, dest, mes);
        } catch (Exception e) {}
        return return_class;
    }


    public Command create_op_two(java.lang.String key, String source, String mes) {
        Command return_class = null;
        try {
            Class<?> _class = Class.forName(property.getProperty(key));
            return_class = (Command) _class.getDeclaredConstructor(String.class, String.class).newInstance(source, mes);
        } catch (Exception e) {}
        return return_class;
    }


    public Command create_op_one(java.lang.String key, String source) {
        Command return_class = null;
        try {
            Class<?> _class = Class.forName(property.getProperty(key));
            return_class = (Command) _class.getDeclaredConstructor(String.class).newInstance(source);
        } catch (Exception e) {}
        return return_class;
    }
}
