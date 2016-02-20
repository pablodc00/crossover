package com.crossover.trial.properties.loader;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Predefined set of resource loaders
 *
 * @author alexey.zakharchenko@gmail.com
 */
public final class Loaders {
    private static final Collection<ResourceLoader> delegates = new ArrayList<>(2);
    static {
        delegates.add(new UrlResourceLoader());
        delegates.add(new ClasspathResourceLoader());
    }

    private static ResourceLoader getLoaderFor(String uri) {
        for (ResourceLoader rl : delegates)
            if (rl.canLoad(uri))
                return rl;

        throw new UnsupportedOperationException("Unsupported uri: "+ uri);
    }

    public static InputStream load(String uri) {
        return getLoaderFor(uri).load(uri);
    }
}
