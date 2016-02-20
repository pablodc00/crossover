package com.crossover.trial.properties.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author alexey.zakharchenko@gmail.com
 */
public final class Utils {
    private static final Logger log = LoggerFactory.getLogger(Utils.class);

    /** Platform-dependent end of line */
    public static final String EOL = System.getProperty("line.separator");

    /**
     * @param key Properties key
     * @return Normalized representation of the given key (lower case, trimmed, dots instead of _)
     */
    public static String normalizeKey(String key) {
        if (key == null) return "null";

        return key.trim().toLowerCase().replace('_', '.');
    }

    /**
     * @param s a String
     *
     * @return Whether s contains nothing
     */
    public static boolean isVoid(String s) {
        return s == null || s.trim().length() == 0;
    }

    public static String getSuffix(String uri) {
        int index = uri.lastIndexOf('.');
        if (index < 0 || index >= uri.length())
            throw new IllegalArgumentException("The given uri has no suffix: " + uri);

        return uri.substring(index+1);
    }

    public static String unquote(String s) {
        return s.replaceAll("^\"(.+)\"$", "$1");
    }
}
