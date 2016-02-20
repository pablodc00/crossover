package com.crossover.trial.properties.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author alexey.zakharchenko@gmail.com
 */
public interface ResourceParser {
    /**
     *
     * @param type Type of stream (file). Usually is file's extention
     *
     * @return true If this parser is capable of parsing the given type
     */
    boolean canParse(String type);

    /**
     * @param is InputStream
     *
     * @return Parsed is into Properties instance
     * @throws IOException On parse error
     */
    Properties parse(InputStream is) throws IOException;
}
