package com.crossover.trial.properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import static com.crossover.trial.properties.loader.Loaders.load;
import static com.crossover.trial.properties.parser.Parsers.getParserFor;
import static com.crossover.trial.properties.util.Utils.getSuffix;
import static java.lang.String.valueOf;

/**
 * A simple main method to load and print properties. You should feel free to change this class
 * or to create additional class. You may add addtional methods, but must implement the
 * AppPropertiesManager API contract.
 *
 * Note: a default constructor is required
 *
 * @author code test administrator
 */
public class TrialAppPropertiesManager implements AppPropertiesManager {
    private static final Logger log = LoggerFactory.getLogger(TrialAppPropertiesManager.class);
    public TrialAppPropertiesManager() {}

    @Override
    public AppProperties loadProps(List<String> propUris) {
        final TrialAppProperties taps = new TrialAppProperties();

        for (String uri : propUris) {
            try (InputStream is = load(uri)) {
                Properties props = getParserFor(getSuffix(uri))
                        .parse(is);

                taps.setProperties(props);

            } catch (Exception e) {
                log.error("Unable to load properties {}", uri, e);
                throw new RuntimeException(e);
            }
        }

        return taps;
    }

    @Override
    public void printProperties(AppProperties props, PrintStream sync) {
        // I don't know why this method has <?> generic, should have <String> instead
        //  But I'm not supposed to change the interface
        final List<?> keysObj = props.getKnownProperties();
        final List<String> keys = new ArrayList<>(keysObj.size());
        keysObj.forEach(o -> keys.add(valueOf(o)));

        Collections.sort(keys);

        for (Object key : keys) {
            sync.print(key);
            sync.print(", ");
            Object value = props.get(valueOf(key));
            sync.println(value);
        }
    }
}
