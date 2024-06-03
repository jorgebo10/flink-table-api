package com.hellofresh.customeractivity.infrastructure.kafka;

import com.hellofresh.customeractivity.domain.CustomerActivityStatistics;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class UserStaticsSink {
    public KafkaSink<CustomerActivityStatistics> create(StreamExecutionEnvironment environment) {
        //return new KafkaSink<>("user-statistics");
        return null;
    }
}
