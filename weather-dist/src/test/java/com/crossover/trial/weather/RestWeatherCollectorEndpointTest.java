package com.crossover.trial.weather;

import org.junit.Before;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author alexey.zakharchenko@gmail.com
 */
public class RestWeatherCollectorEndpointTest {

    @Before
    public void setUp() throws Exception {
//        airportData.clear();
//        atmosphericInformation.clear();
//        requestFrequency.clear();

        String l;

        try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("airports.dat")) {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            while ( (l = br.readLine()) != null) {
                String[] split = l.split(",");
                //addAirport(split[0], Double.valueOf(split[1]), Double.valueOf(split[2]));
            }
        }
    }
}