package com.crossover.trial.weather;

import com.crossover.trial.weather.dto.AtmosphericInformation;
import com.crossover.trial.weather.dto.DataPoint;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class WeatherEndpointTest {

    private WeatherQueryEndpoint query = new RestWeatherQueryEndpoint();

    private WeatherCollector update = new RestWeatherCollectorEndpoint();

    private Gson gson = new Gson();

    private DataPoint dp;

    @Before
    public void setUp() throws Exception {
        RestWeatherCollectorEndpoint.init();
        dp = new DataPoint(10, 20, 22, 30, 10);
        update.updateWeather("BOS", "wind", gson.toJson(dp));
        query.get("BOS", "0").getEntity();
    }

    @Test
    public void testPing() throws Exception {
        String ping = query.ping();
        JsonElement pingResult = new JsonParser().parse(ping);
        assertEquals(1, pingResult.getAsJsonObject().get("datasize").getAsInt());
        assertEquals(5, pingResult.getAsJsonObject().get("iata_freq").getAsJsonObject().entrySet().size());
    }

    @Test
    public void testGet() throws Exception {
        List<AtmosphericInformation> ais = (List<AtmosphericInformation>) query.get("BOS", "0").getEntity();
        assertEquals(ais.get(0).getWind(), dp);
    }

    @Test
    public void testGetNearby() throws Exception {
        // check datasize response
        update.updateWeather("JFK", "wind", gson.toJson(dp));
        dp = new DataPoint(10, 20, 40, 30, 10);
        update.updateWeather("EWR", "wind", gson.toJson(dp));
        dp = new DataPoint(10, 20, 30, 30, 10);
        update.updateWeather("LGA", "wind", gson.toJson(dp));

        List<AtmosphericInformation> ais = (List<AtmosphericInformation>) query.get("JFK", "200").getEntity();
        assertEquals(3, ais.size());
    }

    @Test
    public void testUpdate() throws Exception {

        DataPoint windDp = new DataPoint(10, 20, 22, 30, 10);
        update.updateWeather("BOS", "wind", gson.toJson(windDp));
        query.get("BOS", "0").getEntity();

        String ping = query.ping();
        JsonElement pingResult = new JsonParser().parse(ping);
        assertEquals(1, pingResult.getAsJsonObject().get("datasize").getAsInt());

        DataPoint cloudCoverDp = new DataPoint(10, 60, 100, 50, 4);
        update.updateWeather("BOS", "cloudcover", gson.toJson(cloudCoverDp));

        List<AtmosphericInformation> ais = (List<AtmosphericInformation>) query.get("BOS", "0").getEntity();
        assertEquals(ais.get(0).getWind(), windDp);
        assertEquals(ais.get(0).getCloudCover(), cloudCoverDp);
    }

}