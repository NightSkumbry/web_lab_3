package ru.ns.lab.repository;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;

import ru.ns.common.model.Stats;

public class StatsDAO {
    private static final Logger logger = Logger.getLogger(StatsDAO.class.getName());

    public Stats getStats() {
        logger.fine("Initiating request to statistics service");
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create("http://wildfly-stats:8080/statsMicro/api/stats"))
                .timeout(Duration.ofSeconds(3))
                .build();

            HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
            logger.fine("Received response from statistics service, status code: " + res.statusCode());

            if (res.statusCode() == 200) {
                Stats result = new Gson().fromJson(res.body(), Stats.class);
                logger.fine("Successfully fetched and parsed stats: " + res.body());
                return result;
            } else {
                logger.warning("Failed to fetch stats, status code: " + res.statusCode() + ", body: " + res.body());
                return null;
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error fetching stats from statistics service", e);
            return null;
        }
    }
}
