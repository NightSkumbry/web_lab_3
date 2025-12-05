package ru.ns.lab.controller;

import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;
import java.util.logging.Level;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RabbitPublisher {
    private static final Logger logger = Logger.getLogger(RabbitPublisher.class.getName());

    private Connection connection;
    private Channel channel;

    @PostConstruct
    public void init() {
        int attempts = 0;
        int maxAttempts = 10;
        long delayMs = 3000;

        while (attempts < maxAttempts) {
            try {
                ConnectionFactory factory = new ConnectionFactory();
                String rabbitHost = "rabbitmq";
                factory.setHost(rabbitHost);
                factory.setPort(5672);
                factory.setUsername("guest");
                factory.setPassword("guest");

                this.connection = factory.newConnection();
                this.channel = connection.createChannel();
                channel.queueDeclare("statistics_queue", false, false, false, null);
                logger.info("RabbitMQ publisher initialized successfully");
                return;
            }
            catch (Exception e) {
                attempts++;
                logger.log(Level.SEVERE, "Error initializing RabbitMQ publisher", e);
                if (attempts < maxAttempts) {
                    try {
                        Thread.sleep(delayMs);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
            }
        }
        throw new RuntimeException("Failed to connect to RabbitMQ after " + maxAttempts + " attempts");
    }

    public void sendEvent(String json) {
        if (channel == null || !channel.isOpen()) {
            logger.severe("RabbitMQ is unavailable");
            return;
        }
        try {
            channel.basicPublish("", "statistics_queue", null, json.getBytes(StandardCharsets.UTF_8));
            logger.fine("Event sent to statistics queue: " + json);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error sending event to RabbitMQ", e);
        }
    }

    @PreDestroy
    public void cleanup() {
        try {
            if (channel != null) channel.close();
            if (connection != null) connection.close();
            logger.info("RabbitMQ publisher resources closed");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error closing RabbitMQ publisher resources", e);
        }
    }
}