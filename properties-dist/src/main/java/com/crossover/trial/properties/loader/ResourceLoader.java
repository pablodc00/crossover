package com.crossover.trial.properties.loader;

import java.io.InputStream;

/**
 * Loads resrouce given its URI
 *
 * @author alexey.zakharchenko@gmail.com
 */
public interface ResourceLoader {
    /**
     * @param uri
     * @return true If the given URI can be loaded by this loader
     */
    boolean canLoad(String uri);

    /**
     * @param uri URI to load
     *
     * @return InputStream to read resource's contents
     * @throws IllegalArgumentException If unable to load
     * @throws UnsupportedOperationException If the given URI is not supported
     */
    InputStream load(String uri);
}
