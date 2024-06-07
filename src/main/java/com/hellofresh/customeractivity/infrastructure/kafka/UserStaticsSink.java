package com.hellofresh.customeractivity.infrastructure.kafka;

import com.hellofresh.browserstatics.avro.BrowserStaticsEvent;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.formats.avro.registry.confluent.ConfluentRegistryAvroSerializationSchema;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class UserStaticsSink {
    public final String schemaRegistryUrl;
    private final String topic;
    private final String brokerUrl;

    public UserStaticsSink(String brokerUrl, String topic, String schemaRegistryUrl) {
        this.brokerUrl = brokerUrl;
        this.schemaRegistryUrl = schemaRegistryUrl;
        this.topic = topic;
    }

    public KafkaSink<BrowserStaticsEvent> create() {
        return KafkaSink.<BrowserStaticsEvent>builder()
                .setBootstrapServers(brokerUrl)
                .setRecordSerializer(
                        KafkaRecordSerializationSchema.builder()
                                .setValueSerializationSchema(
                                        ConfluentRegistryAvroSerializationSchema
                                                .forSpecific(
                                                        BrowserStaticsEvent.class,
                                                        topic + "-value",
                                                        schemaRegistryUrl))
                                .setTopic(topic)
                                .build())
                .build();

    }
}
