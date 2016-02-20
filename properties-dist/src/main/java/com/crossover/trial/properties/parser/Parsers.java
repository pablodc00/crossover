package com.crossover.trial.properties.parser;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Predefined set of resource parsers
 *
 * @author alexey.zakharchenko@gmail.com
 */
public class Parsers {
    private static final Collection<ResourceParser> delegates = new ArrayList<>(2);
    static {
        delegates.add(new PropertiesParser());
        delegates.add(new JsonParser());
    }

    public static ResourceParser getParserFor(String type) {
        for (ResourceParser p : delegates)
            if (p.canParse(type))
                return p;

        throw new UnsupportedOperationException("Unsupported type: " + type);
    }
}
