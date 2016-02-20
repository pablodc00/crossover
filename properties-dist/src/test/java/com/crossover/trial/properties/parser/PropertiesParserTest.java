package com.crossover.trial.properties.parser;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author alexey.zakharchenko@gmail.com
 */
public class PropertiesParserTest {
    private final ResourceParser parser = new PropertiesParser();
    @Test
    public void testCanParse() throws Exception {
        Assert.assertTrue(parser.canParse("properties"));
        Assert.assertFalse(parser.canParse("json"));
        Assert.assertFalse(parser.canParse(""));
        Assert.assertFalse(parser.canParse(null));
    }

    @Test
    public void testParse() throws Exception {

    }
}