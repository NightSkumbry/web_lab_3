package ru.ns.lab.controller;

import java.nio.charset.StandardCharsets;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RabbitPublisher {

    private Connection connection;
    private Channel channel;

    @PostConstruct
    public void init() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("rabbitmq");
        factory.setPort(5672);
        factory.setUsername("guest");
        factory.setPassword("guest");

        try {
            this.connection = factory.newConnection();
            this.channel = connection.createChannel();
            channel.queueDeclare("statistics_queue", false, false, false, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendEvent(String json) {
        if (channel == null || !channel.isOpen()) {
            System.err.println("RabbitMQ недоступен");
            return;
        }
        try {
            channel.basicPublish("", "statistics_queue", null, json.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PreDestroy
    public void cleanup() {
        try {
            if (channel != null) channel.close();
            if (connection != null) connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}