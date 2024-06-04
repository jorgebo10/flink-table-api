package com.hellofresh.customeractivity.infrastructure.kafka;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.Properties;

public class UserStaticsSink {
    private final String bootstrapServers;
    private final String topic;

    public UserStaticsSink(String bootstrapServers, String topic) {
        this.bootstrapServers = bootstrapServers;
        this.topic = topic;
    }

    public KafkaSink<String> create(StreamExecutionEnvironment environment) {
        Properties properties = new Properties();
        properties.setProperty("transaction.timeout.ms", "7200000"); // e.g., 2 hours

        return KafkaSink.<String>builder()
                .setBootstrapServers(bootstrapServers)
                .setKafkaProducerConfig(properties)
                .setRecordSerializer(KafkaRecordSerializationSchema.builder()
                        .setTopic(topic)
                        .setValueSerializationSchema(new SimpleStringSchema())
                        .build()
                )
                .build();
    }
}
