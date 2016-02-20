package com.crossover.trial.properties;

import org.junit.Test;

import java.util.Arrays;

/**
 * @author alexey.zakharchenko@gmail.com
 */
public class TrialAppPropertiesManagerTest {
    private final TrialAppPropertiesManager man = new TrialAppPropertiesManager();

    @Test
    public void testLoadPropsAndPrint() throws Exception {
        AppProperties props = man.loadProps(
                Arrays.asList("classpath:jdbc.properties", "classpath:config.json", "classpath:aws.properties")
        );

        man.printProperties(props, System.out);
    }
}