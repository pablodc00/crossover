package com.crossover.trial.properties.parser;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author alexey.zakharchenko@gmail.com
 */
public class JsonParserTest {
    private final ResourceParser parser = new JsonParser();

    @Test
    public void testCanParse() throws Exception {
        Assert.assertTrue(parser.canParse("json"));
        Assert.assertFalse(parser.canParse("properties"));
        Assert.assertFalse(parser.canParse(""));
        Assert.assertFalse(parser.canParse(null));
    }

    @Test
    public void testParse() throws Exception {

    }
}