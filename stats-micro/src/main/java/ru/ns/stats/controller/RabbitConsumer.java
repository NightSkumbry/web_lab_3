package ru.ns.stats.controller;

import com.rabbitmq.client.*;

import ru.ns.stats.service.StatsService;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.util.logging.Logger;
import java.util.logging.Level;


@Singleton
@Startup
public class RabbitConsumer {
    private static final Logger logger = Logger.getLogger(RabbitConsumer.class.getName());

    @Inject
    private StatsService statsService;

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

                connection = factory.newConnection();
                channel = connection.createChannel();

                channel.queueDeclare("statistics_queue", false, false, false, null);
                logger.info("RabbitMQ connection established and queue declared");

                DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                    try {
                        String json = new String(delivery.getBody(), "UTF-8");
                        logger.fine("Received statistics json: " + json);
                        statsService.recordStatistics(json);
                        channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                    } catch (Exception e) {
                        logger.log(Level.SEVERE, "Error processing RabbitMQ message", e);
                    }
                };
                CancelCallback cancelCallback = consumerTag -> {
                    logger.warning("Message consumer was cancelled: " + consumerTag);
                };
                channel.basicConsume("statistics_queue", false, deliverCallback, cancelCallback);
                logger.info("Started consuming messages from statistics_queue");
                return;
            }
            catch (Exception e) {
                attempts++;
                logger.log(Level.SEVERE, "Error initializing RabbitMQ consumer", e);
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

    @PreDestroy
    public void cleanup() {
        try {
            if (channel != null && channel.isOpen()) {
                channel.close();
                logger.info("RabbitMQ channel closed");
            }
            if (connection != null && connection.isOpen()) {
                connection.close();
                logger.info("RabbitMQ connection closed");
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error closing RabbitMQ resources", e);
        }
    }
}