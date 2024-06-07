package com.hellofresh.customeractivity.job;

import com.hellofresh.browserstatics.avro.BrowserStaticsEvent;
import com.hellofresh.customeractivity.avro.CustomerActivityEvent;
import com.hellofresh.customeractivity.domain.CustomerActivityStatistics;
import com.hellofresh.customeractivity.domain.ProcessCustomerActivityStaticsFunction;
import com.hellofresh.customeractivity.infrastructure.kafka.CustomerActivityDataSource;
import com.hellofresh.customeractivity.infrastructure.kafka.UserStaticsSink;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;

import static org.apache.flink.streaming.api.windowing.time.Time.seconds;

public class CustomerActivityJob {

    public static final String BROKER_URL = "http://localhost:9092";
    public static final String INPUT_TOPIC = "my-topic";
    public static final String CONSUMER_GROUP = "my-group";
    public static final String SCHEMA_REGISTRY_URL = "http://localhost:8082";
    public static final String OUTPUT_TOPIC = "my-output";

    public static void main(String[] args) throws Exception {
        //This is the entrypoint for any Flink application. Exec env will be automatically selected either local or remote
        StreamExecutionEnvironment executionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment();
        executionEnvironment.getConfig().enableForceAvro();

        DataStreamSource<CustomerActivityEvent> flightDataDataStream = new CustomerActivityDataSource(
                BROKER_URL,
                INPUT_TOPIC,
                CONSUMER_GROUP,
                SCHEMA_REGISTRY_URL
        ).create(executionEnvironment);

        KafkaSink<BrowserStaticsEvent> output = new UserStaticsSink(
                BROKER_URL,
                OUTPUT_TOPIC,
                SCHEMA_REGISTRY_URL
        ).create();

        flightDataDataStream.name("customeractivitystatics_source").uid("customeractivitystatics_source")
                .map(CustomerActivityStatistics::fromCustomerActivityEvent)
                .keyBy(CustomerActivityStatistics::getCountryCode)
                .window(TumblingEventTimeWindows.of(seconds(5)))
                .reduce(CustomerActivityStatistics::merge, new ProcessCustomerActivityStaticsFunction())
                .name("customeractivitystatics_reduce").uid("customeractivitystatics_reduce")
                .map((MapFunction<CustomerActivityStatistics, BrowserStaticsEvent>) value -> new BrowserStaticsEvent(value.getCountryCode(), value.getUserAgentCount()))
                .name("customeractivitystatics_map_to_string").uid("customeractivitystatics_map_to_string")
                .sinkTo(output)
                .name("customeractivitystatics_sink").uid("customeractivitystatics_sink");

        executionEnvironment.execute("Flink test");
    }
}
