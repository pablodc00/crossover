package com.crossover.trial.properties.type;

import com.amazonaws.regions.Regions;
import org.junit.Test;

import java.net.URL;
import java.util.Date;

import static com.crossover.trial.properties.type.ClassFinders.find;
import static org.junit.Assert.assertEquals;

/**
 * @author alexey.zakharchenko@gmail.com
 */
public class ClassFindersTest {

    @Test
    public void testFind() throws Exception {
        assertEquals(String.class, find("asd"));
        assertEquals(String.class, find("asd dsf sdf435"));
        assertEquals(Integer.class, find("1"));
        assertEquals(Integer.class, find("0"));
        assertEquals(Integer.class, find("-1"));
        assertEquals(Long.class, find("1324324324324324324"));
        assertEquals(Double.class, find("11.22"));

        assertEquals(Boolean.class, find("true"));
        assertEquals(Boolean.class, find("false"));
        assertEquals(Boolean.class, find("TRUE"));
        assertEquals(Boolean.class, find("FALSE"));

        assertEquals(Date.class, find("11-02-2013"));
        //assertEquals(Date.class, find("11-02-11"));
        //assertEquals(Date.class, find("11/02/11"));
        assertEquals(Date.class, find("11/02/2013"));

        assertEquals(URL.class, find("https://authserver/v1/auth"));
        assertEquals(URL.class, find("http://localhost"));
        assertEquals(URL.class, find("http://"));

        assertEquals(Regions.class, find("sa-east-1"));

        assertEquals(Class.class, find("java.lang.String"));
    }

}