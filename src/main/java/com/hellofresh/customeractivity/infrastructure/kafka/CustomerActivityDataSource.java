package com.hellofresh.customeractivity.infrastructure.kafka;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.formats.avro.registry.confluent.ConfluentRegistryAvroDeserializationSchema;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.flightsafe.avro.CustomerActivityEvent;


public final class CustomerActivityDataSource {
    private final String brokers;
    private final String topic;
    private final String groupId;
    private final String schemaRegistryUrl;

    public CustomerActivityDataSource(String brokers, String topic, String groupId, String schemaRegistryUrl) {
        this.brokers = brokers;
        this.topic = topic;
        this.groupId = groupId;
        this.schemaRegistryUrl = schemaRegistryUrl;
    }

    public DataStreamSource<CustomerActivityEvent> create(StreamExecutionEnvironment environment) {
        KafkaSource<CustomerActivityEvent> source = KafkaSource.<CustomerActivityEvent>builder()
                .setBootstrapServers(brokers)
                .setTopics(topic)
                .setGroupId(groupId)
                .setStartingOffsets(OffsetsInitializer.earliest())
                .setValueOnlyDeserializer(ConfluentRegistryAvroDeserializationSchema.forSpecific(CustomerActivityEvent.class, schemaRegistryUrl))
                .build();

        return environment.fromSource(source, WatermarkStrategy.noWatermarks(), "Kafka Source");
    }
}
