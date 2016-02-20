package com.crossover.trial.properties.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author alexey.zakharchenko@gmail.com
 */
public class PropertiesParser implements ResourceParser {
    private static final Logger log = LoggerFactory.getLogger(PropertiesParser.class);

    @Override
    public boolean canParse(String type) {
        return "properties".equals(type);
    }

    @Override
    public Properties parse(InputStream is) throws IOException {
        Properties props = new Properties();
        props.load(is);

        return props;
    }
}
