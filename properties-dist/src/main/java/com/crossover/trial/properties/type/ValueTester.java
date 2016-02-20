package com.crossover.trial.properties.type;

/**
 * @author alexey.zakharchenko@gmail.com
 */
public interface ValueTester extends ValueChecker {

    default boolean accepts(String value) throws Exception {
        tryParse(value);

        return true;
    }

    /**
     * Tries to parse the given String. If everything ok, returns silently
     *
     * @param value a String
     *
     * @throws Exception On parse error
     */
    void tryParse(String value) throws Exception;
}
