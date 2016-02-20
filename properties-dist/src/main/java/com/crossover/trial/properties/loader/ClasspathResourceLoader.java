package com.crossover.trial.properties.loader;

import java.io.InputStream;

/**
 * Loads resource from classpath
 *
 * @author alexey.zakharchenko@gmail.com
 */
public class ClasspathResourceLoader implements ResourceLoader {
    @Override
    public boolean canLoad(String uri) {
        return uri != null && uri.startsWith("classpath:");
    }

    @Override
    public InputStream load(String uri) {
        if (!canLoad(uri))
            throw new UnsupportedOperationException("Unsupported uri: " + uri);

        return getClass().getClassLoader().getResourceAsStream(uri.replace("classpath:", ""));
    }
}
