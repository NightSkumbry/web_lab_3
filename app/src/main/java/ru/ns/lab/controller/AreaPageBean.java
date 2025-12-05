package ru.ns.lab.controller;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.SQLException;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;
import java.util.logging.Level;

import com.google.gson.Gson;

import ru.ns.lab.model.HitResult;
import ru.ns.lab.model.Stats;
import ru.ns.lab.repository.HitResultDAO;
import ru.ns.lab.service.area.abs.IArea;
import ru.ns.lab.service.area.variant.Var6386_lab3;
import ru.ns.lab.service.params.Params;
import ru.ns.lab.service.params.ParamsException;
import ru.ns.lab.service.params.ParamsParser;

public class AreaPageBean {
    private static final Logger logger = Logger.getLogger(AreaPageBean.class.getName());

    private String x = "0";
    private String y = "0";
    private String r = "1";
    private String clickX = "";
    private String clickY = "";
    private int toDeleteId;

    private HitResultsBean hitResultsBean;
    private RabbitPublisher rabbitPublisher;

    private final Gson gson = new Gson();
    private final HitResultDAO hitResultDAO = new HitResultDAO();
    private final IArea area = new Var6386_lab3();
    private final ParamsParser parser = new ParamsParser();
    private final List<Double> allowedRValues = List.of(1d, 1.25d, 1.5d, 1.75d, 2d, 2.25d, 2.5d, 2.75d, 3d, 3.25d, 3.5d, 3.75d, 4d);
    private Stats lastStats;

    public void updateStats() {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create("http://wildfly-stats:8080/statsMicro/api/stats"))
                .timeout(Duration.ofSeconds(3))
                .build();

            HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
            if (res.statusCode() == 200) {
                this.lastStats = new Gson().fromJson(res.body(), Stats.class);
                logger.fine("Fetched stats: " + res.body());
            } else {
                logger.severe("Failed to fetch stats, status code: " + res.statusCode());
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error fetching stats from statistics service", e);
        }
    }


    public void checkFormHit() {
        logger.fine("Checking hit for X: " + x + ", Y: " + y + ", R: " + r);
        Params params;

        try {
            params = new Params(parser.parseX(x), parser.parseY(y), parser.parseR(r));
        }
        catch (ParamsException e) {
            logger.log(Level.WARNING, "Parameter parsing error: " + e.getMessage(), e);
            return;
        }

        logger.fine("Parsed params: " + params);

        boolean hit = area.checkHit(params.getX(), params.getY(), params.getR());
        double maxMissR = allowedRValues
            .stream()
            .filter(r_val -> !area.checkHit(params.getX(), params.getY(), r_val))
            .max(Double::compare)
            .orElse(0d);

        logger.fine("Hit result: " + hit + ", Max miss R: " + maxMissR);
        logger.fine(area.getLog());


        HitResult hitResult = new HitResult(
            params.getX(),
            params.getY(),
            params.getR(),
            hit,
            maxMissR
        );
        try {
            int generatedId = hitResultDAO.addResult(hitResult);
            hitResult.setId(generatedId);
            hitResultsBean.addResult(hitResult);

            CompletableFuture.runAsync(() -> {
                rabbitPublisher.sendEvent(gson.toJson(hitResult));
            });

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error adding hit result to database: " + e.getMessage(), e);
        }

    }

    public void checkFreeHit() {
        logger.fine("Checking hit for X: " + x + ", Y: " + y + ", R: " + r);
        Params params;

        try {
            params = new Params(parser.parseX(clickX), parser.parseY(clickY), parser.parseR(r));
        }
        catch (ParamsException e) {
            logger.log(Level.WARNING, "Parameter parsing error: " + e.getMessage(), e);
            return;
        }

        logger.fine("Parsed params: " + params);

        boolean hit = area.checkHit(params.getX(), params.getY(), params.getR());
        double maxMissR = allowedRValues
            .stream()
            .filter(r_val -> !area.checkHit(params.getX(), params.getY(), r_val))
            .max(Double::compare)
            .orElse(0d);

        logger.fine("Hit result: " + hit + ", Max miss R: " + maxMissR);
        logger.fine(area.getLog());


        HitResult hitResult = new HitResult(
            params.getX(),
            params.getY(),
            params.getR(),
            hit,
            maxMissR
        );

        try {
            int generatedId = hitResultDAO.addResult(hitResult);
            hitResult.setId(generatedId);
            hitResultsBean.addResult(hitResult);

            CompletableFuture.runAsync(() -> {
                rabbitPublisher.sendEvent(gson.toJson(hitResult));
            });

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error adding hit result to database: " + e.getMessage(), e);
        }
    }

    public void deleteAllResults() {
        try {
            hitResultDAO.deleteAllResults();
            logger.info("Deleted all hit results from database");
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error deleting all hit results from database: " + e.getMessage(), e);
        }
        hitResultsBean.deleteAllResults();
    }

    public void deleteResult() {
        try {
            hitResultDAO.deleteResultById(toDeleteId);
            logger.info("Deleted hit result with ID: " + toDeleteId + " from database");
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error deleting hit result from database: " + e.getMessage(), e);
        }
        hitResultsBean.deleteResult(toDeleteId);
    }



    public int getToDeleteId() {
        return toDeleteId;
    }

    public void setToDeleteId(int toDeleteId) {
        this.toDeleteId = toDeleteId;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public String getR() {
        return r;
    }

    public void setR(String r) {
        this.r = r;
    }

    public String getClickX() {
        return clickX;
    }

    public void setClickX(String xClick) {
        this.clickX = xClick;
    }

    public String getClickY() {
        return clickY;
    }

    public void setClickY(String yClick) {
        this.clickY = yClick;
    }

    public HitResultsBean getHitResultsBean() {
        return hitResultsBean;
    }

    public void setHitResultsBean(HitResultsBean hitResultsBean) {
        this.hitResultsBean = hitResultsBean;
    }

    public RabbitPublisher getRabbitPublisher() {
        return rabbitPublisher;
    }

    public void setRabbitPublisher(RabbitPublisher rabbitPublisher) {
        this.rabbitPublisher = rabbitPublisher;
    }

    public Stats getLastStats() {
        if (lastStats == null) {
            updateStats();
        }
        return lastStats;
    }
}
