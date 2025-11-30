package ru.ns.lab.controller;

import com.google.gson.Gson;

import ru.ns.lab.model.HitResult;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class KafkaEventPublisher {

    private final KafkaProducer<String, String> producer;
    private final String topicName;
    private final Gson gson;


    public KafkaEventPublisher(String bootstrapServers, String topicName) {
        this.topicName = topicName;
        this.gson = new Gson();

        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        this.producer = new KafkaProducer<>(props);
    }


    public void publishHitResult(HitResult event) {
        try {
            String eventJson = gson.toJson(event);

            ProducerRecord<String, String> record = new ProducerRecord<>(topicName, eventJson);

            producer.send(record);

            System.out.println("Событие отправлено в Kafka: " + eventJson);

        } catch (Exception e) {
            System.err.println("Ошибка при отправке события в Kafka: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void close() {
        if (producer != null) {
            producer.close();
        }
    }
}