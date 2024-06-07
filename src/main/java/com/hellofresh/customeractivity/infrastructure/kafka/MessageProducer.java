package com.hellofresh.customeractivity.infrastructure.kafka;

import com.hellofresh.customeractivity.avro.CustomerActivityEvent;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.Properties;

public class MessageProducer {
    final static Logger logger = LoggerFactory.getLogger(MessageProducer.class);

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "1");
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
        props.put(KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8082");
        props.put(KafkaAvroSerializerConfig.AUTO_REGISTER_SCHEMAS, "true");
        props.put(ProducerConfig.LINGER_MS_CONFIG, 500);

        try (KafkaProducer<String, CustomerActivityEvent> producer = new KafkaProducer<>(props)) {
            final CustomerActivityEvent customerActivityEvent = new CustomerActivityEvent("1", "CHD", "CHROME");
            final ProducerRecord<String, CustomerActivityEvent> record = new ProducerRecord<>("my-topic", customerActivityEvent.getUserUuid(), customerActivityEvent);
            producer.send(record, ((metadata, exception) -> {
                logger.info("Produced record to topic {} partition {} at offset {}",
                        metadata.topic(), metadata.partition(), metadata.offset());
            }));
        }
    }
}
