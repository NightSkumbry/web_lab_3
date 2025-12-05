package ru.ns.stats.controller;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ru.ns.stats.service.StatsService;

import java.io.IOException;
import java.util.logging.Logger;

@WebServlet("/api/stats")
public class StatsServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(StatsServlet.class.getName());

    @Inject
    private StatsService statsService;


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        logger.fine("Received GET request for statistics");
        String stats = statsService.getStatisticsJson();

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().println(stats);

        logger.fine("Statistics response sent successfully");
    }
}