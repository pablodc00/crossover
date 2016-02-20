package com.crossover.trial.properties.type;

/**
 * Checks a string to be of a specific type
 *
 * @author alexey.zakharchenko@gmail.com
 */
public interface ValueChecker {
    /**
     * @param value a String
     *
     * @return true If the given value is accepted by this checker
     *
     * @throws Exception On any error happened on working with the given value
     */
    boolean accepts(String value) throws Exception;
}
