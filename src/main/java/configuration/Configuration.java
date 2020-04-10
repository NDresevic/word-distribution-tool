package configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Configuration {

    private static Map<String, String> parameters = new HashMap<>();

    /**
     * Reads and stores configuration parameters form the given file (in this case 'app.properties').
     */
    public static void readConfiguration(String fileName) {
        try {
            InputStream input = Configuration.class.getClassLoader().getResourceAsStream(fileName);
            Properties properties = new Properties();
            if (input == null) {
                System.out.println("Error, unable to find \"" + fileName + "\"");
                return;
            }

            properties.load(input);
            properties.forEach((key, value) -> parameters.put(key.toString(), value.toString()));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public static String getParameter(String parameter) {
        return parameters.get(parameter);
    }
}
