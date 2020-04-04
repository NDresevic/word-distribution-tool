package configuration;

import java.util.Map;

public interface Configuration {

    Object getParameter(String parameter);

    void addParameter(String parameter, Object value);

    Map<String, Object> getParameters();
}
