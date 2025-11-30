package ru.ns.lab.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.ns.lab.model.HitResult;
import ru.ns.lab.repository.HitResultDAO;

public class HitResultsBean {
    private List<HitResult> results;
    private final HitResultDAO hitResultDAO = new HitResultDAO();

    public HitResultsBean() {
        try {
            results = hitResultDAO.getAllResults();
        } catch (SQLException e) {
            System.out.println("Error loading hit results from database: " + e.getMessage());
        }
    }


    public void addResult(HitResult row) {
        results.add(row);
    }

    public List<HitResult> getResults() {
        List<HitResult> reversed = new ArrayList<>(results);
        Collections.reverse(reversed);
        return reversed;
    }

    public void deleteAllResults() {
        results.clear();
    }

    public void deleteResult(int toDeleteId) {
        results.removeIf(r -> r.getId() == toDeleteId);
    }

}
