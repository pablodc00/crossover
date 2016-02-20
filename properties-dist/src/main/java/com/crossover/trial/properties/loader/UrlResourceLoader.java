package com.crossover.trial.properties.loader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Loads resource given URL
 *
 * @author alexey.zakharchenko@gmail.com
 */
public class UrlResourceLoader implements ResourceLoader {
    private static final Logger log = LoggerFactory.getLogger(UrlResourceLoader.class);

    @Override
    public boolean canLoad(String uri) {
        return uri != null &&
                (uri.startsWith("http:") || uri.startsWith("https:") || uri.startsWith("file:"));
    }

    @Override
    public InputStream load(String uri) {
        if (!canLoad(uri))
            throw new UnsupportedOperationException("Unsupported uri: " + uri);

        try {
            return new URL(uri).openStream();
        } catch (IOException e) {
            log.error("Unable to load uri {}", uri, e);
            throw new IllegalArgumentException(e);
        }
    }
}
