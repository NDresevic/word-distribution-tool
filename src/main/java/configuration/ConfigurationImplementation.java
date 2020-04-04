package configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ConfigurationImplementation implements Configuration {

    private Map<String, Object> parameters;

    public ConfigurationImplementation(String filename) {
        this.parameters = new HashMap<>();
        this.loadConfiguration(filename);
    }

    private Map loadConfiguration(String filename) {
        try {
            InputStream input = ConfigurationImplementation.class.getClassLoader().getResourceAsStream(filename);
            Properties properties = new Properties();
            if (input == null) {
                System.out.println("Error, unable to find \"" + filename + "\"");
                return null;
            }

            properties.load(input);
            properties.forEach((key, value) -> parameters.put(key.toString(), value));
//            System.out.println(parameters);
//            parameters.forEach((key, value) -> System.out.println("Key : " + key + ", Value : " + value));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return parameters;
    }

    @Override
    public Map<String, Object> getParameters() {
        return this.parameters;
    }

    @Override
    public Object getParameter(String parameter) {
        return this.parameters.get(parameter);
    }

    @Override
    public void addParameter(String parameter, Object value) {
        this.parameters.put(parameter, value);
    }
}
