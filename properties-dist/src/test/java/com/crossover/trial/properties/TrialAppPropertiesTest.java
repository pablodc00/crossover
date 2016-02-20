package com.crossover.trial.properties;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author alexey.zakharchenko@gmail.com
 */
public class TrialAppPropertiesTest {
    private TrialAppProperties props;

    @Before
    public void setUp() {
        props = new TrialAppProperties();
    }

    @Test
    public void testGetMissingProperties() throws Exception {
    }

    @Test
    public void testGetKnownProperties() throws Exception {
    }

    @Test
    public void testIsValid() throws Exception {
        props.setProperty("asd", "1");
        assertTrue(props.isValid());

        props.setProperty("asd", "");
        assertFalse(props.isValid());

        props.clear();
        assertTrue(props.isValid());

        props.setProperty("", "00");
        assertFalse(props.isValid());
    }

    @Test
    public void testGet() throws Exception {

    }

    @Test
    public void testSetProperty() throws Exception {
        props.setProperty("asd", "1");
        assertEquals("1", props.getStringValue("asd") );
        assertEquals("1", props.getStringValue("ASD") );

        props.setProperty("asd", "2");
        assertEquals("2", props.getStringValue("asd") );

        props.setProperty("ASD", "3");
        assertEquals("3", props.getStringValue("asd") );

        props.setProperty("nothing_else_matters", "10");
        props.setProperty("nothing.else.matters", "11");
        assertEquals("11", props.getStringValue("nothing_else_matters") );
        assertEquals("11", props.getStringValue("nothing.else_matters") );
        assertEquals("11", props.getStringValue("NOTHING.ELSE.matters") );
        assertEquals("11", props.getStringValue("NOTHING_ELSE_MATTERS") );

        props.setProperty("NOTHING.ELSE.MATTERS", "12");
        assertEquals("12", props.getStringValue("nothing_else_matters") );
        assertEquals("12", props.getStringValue("nothing.else_matters") );
        assertEquals("12", props.getStringValue("NOTHING.ELSE.matters") );
        assertEquals("12", props.getStringValue("NOTHING_ELSE_MATTERS") );
    }

    @Test
    public void testPropertyWrapper() throws Exception {
        TrialAppProperties.PropertyWrapper pw = new TrialAppProperties.PropertyWrapper("123");
        TrialAppProperties.PropertyWrapper pw2 = new TrialAppProperties.PropertyWrapper("123");
        assertEquals(pw, pw2);

        pw2 = new TrialAppProperties.PropertyWrapper("");
        Assert.assertNotEquals(pw, pw2);

        pw = new TrialAppProperties.PropertyWrapper("");
        assertEquals(pw, pw2);

        pw = new TrialAppProperties.PropertyWrapper("TEST");
        pw2 = new TrialAppProperties.PropertyWrapper("test");
        assertEquals(pw, pw2);

        pw = new TrialAppProperties.PropertyWrapper("TEST_1");
        pw2 = new TrialAppProperties.PropertyWrapper("test_1");
        assertEquals(pw, pw2);

        pw = new TrialAppProperties.PropertyWrapper("TEST_1");
        pw2 = new TrialAppProperties.PropertyWrapper("TEST.1");
        assertEquals(pw, pw2);

        pw = new TrialAppProperties.PropertyWrapper("TEST_SMTH");
        pw2 = new TrialAppProperties.PropertyWrapper("test.smth");
        assertEquals(pw, pw2);
    }

}