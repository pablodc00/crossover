package com.crossover.trial.properties.parser;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import static com.crossover.trial.properties.util.Utils.unquote;
import static java.lang.String.valueOf;

/**
 * @author alexey.zakharchenko@gmail.com
 */
public class JsonParser implements ResourceParser {
    @Override
    public boolean canParse(String type) {
        return "json".equals(type);
    }

    @Override
    public Properties parse(InputStream is) {
        JsonReader reader = Json.createReader(is);
        JsonObject obj = reader.readObject();
        Properties props = new Properties();
        for (Map.Entry e : obj.entrySet()) {
            String key = valueOf(e.getKey());
            String value = unquote(valueOf(e.getValue()));
            props.setProperty(key, value);
        }

        return props;
    }
}
