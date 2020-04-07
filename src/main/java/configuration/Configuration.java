package configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Configuration {

    private static Map<String, String> parameters = new HashMap<>();

    public static Map loadConfiguration(String filename) {
        try {
            InputStream input = Configuration.class.getClassLoader().getResourceAsStream(filename);
            Properties properties = new Properties();
            if (input == null) {
                System.out.println("Error, unable to find \"" + filename + "\"");
                return null;
            }

            properties.load(input);
            properties.forEach((key, value) -> parameters.put(key.toString(), value.toString()));
//            parameters.forEach((key, value) -> System.out.println("Key : " + key + ", Value : " + value));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return parameters;
    }

    public static String getParameter(String parameter) {
        return parameters.get(parameter);
    }
}
