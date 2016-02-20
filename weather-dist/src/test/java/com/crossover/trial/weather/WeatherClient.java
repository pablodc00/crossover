package com.crossover.trial.weather;

import com.crossover.trial.weather.dto.DataPoint;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

/**
 * A reference implementation for the weather client. Consumers of the REST API can look at WeatherClient
 * to understand API semantics. This existing client populates the REST endpoint with dummy data useful for
 * testing.
 *
 * @author code test administrator
 */
// CR: Move to test package
class WeatherClient {
    // CR: To config
    private static final String BASE_URI = "http://localhost:8080";
    /** end point for read queries */
    private WebTarget query;

    /** end point to supply updates */
    private WebTarget collect;

    public WeatherClient() {
        Client client = ClientBuilder.newClient();
        query = client.target(BASE_URI + "/query");
        // CR: use BASE_URI
        collect = client.target("http://localhost:8080/collect");
    }

    public void pingCollect() {
        WebTarget path = collect.path("/ping");
        Response response = path.request().get();
        // CR: Log instead of sout; don't use OS-specific line endings
        System.out.print("collect.ping: " + response.readEntity(String.class) + "\n");
    }

    public void pingQuery() {
        // CR: Error in path? Why /ping?
        WebTarget path = query.path("/ping");
        Response response = path.request().get();
        // CR: Log instead of sout
        System.out.println("query.ping: " + response.readEntity(String.class));
    }

    public void populate() {
        WebTarget path = collect.path("/weather/BOS/wind");
        DataPoint dp = new DataPoint(1,4, 4, 10, 10);

        // CR: post is not used
        Response post = path.request().post(Entity.entity(dp, "application/json"));
    }

    public void query() {
        WebTarget path = query.path("/weather/BOS/0");
        Response response = path.request().get();
        // CR: Log
        System.out.println("query.get:" + response.readEntity(String.class));
    }

    public static void main(String[] args) {
        WeatherClient wc = new WeatherClient();
        wc.pingCollect();
        wc.populate();
        wc.query();
        wc.pingQuery();
        // CR: Log
        System.out.print("complete");
        System.exit(0);
    }
}
