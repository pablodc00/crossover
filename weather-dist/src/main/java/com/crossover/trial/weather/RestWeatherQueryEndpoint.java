package com.crossover.trial.weather;

import com.crossover.trial.weather.dto.AirportData;
import com.crossover.trial.weather.dto.AtmosphericInformation;
import com.google.gson.Gson;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * The Weather App REST endpoint allows clients to query, update and check health stats. Currently, all data is
 * held in memory. The end point deploys to a single container
 *
 * @author code test administrator
 */
@Path("/query")
public class RestWeatherQueryEndpoint {
    private final static Logger log = Logger.getLogger("WeatherQuery");

    /** earth radius in KM */
    public static final double EARTH_RADIUS = 6372.8;
    public static final double DEFAULT_SEARCH_RADIUS = 1000;

    /** shared gson json to object factory */
    private static final Gson gson = new Gson();

    // CR: Create map for O(1) search instead of searching list with O(n) each time
    /** all known airports */
    protected static List<AirportData> airportData = new CopyOnWriteArrayList<>();

    /** atmospheric information for each airport, idx corresponds with airportData */
    protected static List<AtmosphericInformation> atmosphericInformation = new CopyOnWriteArrayList<>();

    /**
     * Internal performance counter to better understand most requested information, this map can be improved but
     * for now provides the basis for future performance optimizations. Due to the stateless deployment architecture
     * we don't want to write this to disk, but will pull it off using a REST request and aggregate with other
     * performance metrics {@link #ping()}
     */
    // CR: For both, Use concurrent hashmap
    // CR: Use <> for Java8
    public static Map<AirportData, Integer> requestFrequency = new ConcurrentHashMap<AirportData, Integer>();
    public static Map<Double, Integer> radiusFreq = new ConcurrentHashMap<Double, Integer>();

    /**
     * Retrieve service health including total size of valid data points and request frequency information.
     *
     * @return health stats for the service as a string
     */
    @GET
    @Path("/ping")
    // CR: Use DTO instead of Map, leverage automatic conversion to json @Produces(MediaType.APPLICATION_JSON)
    // CR: Too complex and long method. Simplify code. Possibly break into sub-methods
    public String ping() {
        Map<String, Object> retval = new HashMap<>();

        int datasize = 0;
        for (AtmosphericInformation ai : atmosphericInformation) {
            // we only count recent readings
                // updated in the last day
                // CR: Move number (age) to config
                // CR: Use sorted collection of update times, for this, don't loop through everything
            if (ai.getLastUpdateTime() > System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1)) {
                datasize++;
            }
        }
        retval.put("datasize", datasize);

        Map<String, Double> freq = new HashMap<>();
        // fraction of queries
        // CR: Leads to NaN. Return 0 if requestFrequency.size() == 0, or return nothing assuming 0 in contract
        for (AirportData data : airportData) {
            double frac = (double)requestFrequency.getOrDefault(data, 0) / requestFrequency.size();
            freq.put(data.getIata(), frac);
        }
        retval.put("iata_freq", freq);


        // CR: 1000 to config
        int m = radiusFreq.keySet().stream()
                .max(Double::compare)
                .orElse(DEFAULT_SEARCH_RADIUS).intValue() + 1;


        // CR: This code needs explanation. I cannot get fully what is it intended to do
        int[] hist = new int[m];
        for (Map.Entry<Double, Integer> e : radiusFreq.entrySet()) {
            int i = e.getKey().intValue() % 10;
            hist[i] += e.getValue();
        }

        // CR: Don't return zeros, make them part of contract
        retval.put("radius_freq", hist);

        return gson.toJson(retval);
    }

    /**
     * // CR: Reads data from path, not json. Fix doc
     * Given a query in json format {'iata': CODE, 'radius': km} extracts the requested airport information and
     * return a list of matching atmosphere information.
     *
     * @param iata the iataCode
     * @param radiusString the radius in km
     *
     * @return a list of atmospheric information
     */
    @GET
    @Path("/weather/{iata}/{radius}")
    @Produces(MediaType.APPLICATION_JSON)
    // CR: radiusString -> double radius parameter
    // CR: Rename method to contain semantics in its name
    public Response get(@PathParam("iata") String iata, @PathParam("radius") String radiusString) {
        double radius = radiusString == null || radiusString.trim().isEmpty() ? 0 : Double.valueOf(radiusString);
        updateRequestFrequency(iata, radius);

        List<AtmosphericInformation> retval = new ArrayList<>();
        if (radius == 0) {
            int idx = getAirportDataIdx(iata);
            retval.add(atmosphericInformation.get(idx));
        } else {
            AirportData ad = findAirportData(iata);
            // CR: Its better to pre-calc all distances and store them
            for (int i=0;i< airportData.size(); i++){
                if (calculateDistance(ad, airportData.get(i)) <= radius){
                    AtmosphericInformation ai = atmosphericInformation.get(i);
                    // CR: What is the sense of the if ?
                    retval.add(ai);
                }
            }
        }

        // CR: Static import
        return Response.status(Response.Status.OK).entity(retval).build();
    }


    /**
     * Records information about how often requests are made
     *
     * @param iata an iata code
     * @param radius query radius
     */
    // CR: Not thread-safe
    // CR: private static
    public void updateRequestFrequency(String iata, Double radius) {
        AirportData airportData = findAirportData(iata);
        requestFrequency.put(airportData, requestFrequency.getOrDefault(airportData, 0) + 1);
        // CR: What is the sense of this?
        radiusFreq.put(radius, radiusFreq.getOrDefault(radius, 0));
    }

    /**
     * Given an iataCode find the airport data
     *
     * @param iataCode as a string
     * @return airport data or null if not found
     */
    // CR: Replace with map.get()
    public static AirportData findAirportData(String iataCode) {
        return airportData.stream()
            .filter(ap -> ap.getIata().equals(iataCode))
            .findFirst().orElse(null);
    }

    /**
     * Given an iataCode find the airport data
     *
     * @param iataCode as a string
     * @return airport data or null if not found
     */
    // CR: Remove when list is replaced with map
    public static int getAirportDataIdx(String iataCode) {
        AirportData ad = findAirportData(iataCode);
        return airportData.indexOf(ad);
    }

    /**
     * Haversine distance between two airports.
     *
     * @param ad1 airport 1
     * @param ad2 airport 2
     * @return the distance in KM
     */
    // CR: private static
    // CR: Possibly better to pre-calc all distances and store them
    private static double calculateDistance(AirportData ad1, AirportData ad2) {
        double deltaLat = Math.toRadians(ad2.getLatitude() - ad1.getLatitude());
        double deltaLon = Math.toRadians(ad2.getLongitude() - ad1.getLongitude());
        double a =  Math.pow(Math.sin(deltaLat / 2), 2) + Math.pow(Math.sin(deltaLon / 2), 2)
                * Math.cos(ad1.getLatitude()) * Math.cos(ad2.getLatitude());
        double c = 2 * Math.asin(Math.sqrt(a));

        return EARTH_RADIUS * c;
    }
}
