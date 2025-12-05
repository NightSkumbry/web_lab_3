package ru.ns.lab.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

import ru.ns.lab.model.HitResult;
import ru.ns.lab.repository.HitResultDAO;

public class HitResultsBean {
    private static final Logger logger = Logger.getLogger(HitResultsBean.class.getName());

    private List<HitResult> results;
    private final HitResultDAO hitResultDAO = new HitResultDAO();

    public HitResultsBean() {
        try {
            results = hitResultDAO.getAllResults();
            logger.info("Loaded " + results.size() + " hit results from database");
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error loading hit results from database: " + e.getMessage(), e);
            results = new ArrayList<>();
        }
    }


    public void addResult(HitResult row) {
        results.add(row);
        logger.fine("Added hit result to bean: " + row.getId());
    }

    public List<HitResult> getResults() {
        List<HitResult> reversed = new ArrayList<>(results);
        Collections.reverse(reversed);
        return reversed;
    }

    public void deleteAllResults() {
        results.clear();
        logger.info("Cleared all hit results from bean");
    }

    public void deleteResult(int toDeleteId) {
        results.removeIf(r -> r.getId() == toDeleteId);
        logger.fine("Removed hit result with ID: " + toDeleteId);
    }

}
