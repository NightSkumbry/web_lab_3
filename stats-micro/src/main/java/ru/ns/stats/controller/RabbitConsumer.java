package ru.ns.stats.controller;

import com.rabbitmq.client.*;

import ru.ns.stats.service.StatsService;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;


@Singleton
@Startup
public class RabbitConsumer {
    @Inject
    private StatsService statsService;

    private Connection connection;
    private Channel channel;

    @PostConstruct
    public void init() {
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

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                try {
                    String json = new String(delivery.getBody(), "UTF-8");
                    System.out.println("Received statistics json: " + json);
                    statsService.recordStatistics(json);
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            };
            CancelCallback cancelCallback = consumerTag -> { };
            channel.basicConsume("statistics_queue", false, deliverCallback, cancelCallback);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PreDestroy
    public void cleanup() {
        try {
            if (channel != null && channel.isOpen()) {
                channel.close();
            }
            if (connection != null && connection.isOpen()) {
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}