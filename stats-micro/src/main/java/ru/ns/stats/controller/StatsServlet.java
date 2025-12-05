package ru.ns.stats.controller;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ru.ns.stats.service.StatsService;

import java.io.IOException;

@WebServlet("/api/stats")
public class StatsServlet extends HttpServlet {
    @Inject
    private StatsService statsService;


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("statsService = " + statsService);
        String stats = statsService.getStatisticsJson();
        System.out.println("Serving statistics: " + stats);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().println(stats);
    }
}