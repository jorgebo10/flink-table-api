package com.hellofresh.customeractivity.job;

import com.hellofresh.customeractivity.avro.CustomerActivityEvent;
import com.hellofresh.customeractivity.domain.CustomerActivityStatistics;
import com.hellofresh.customeractivity.domain.ProcessCustomerActivityStaticsFunction;
import com.hellofresh.customeractivity.infrastructure.kafka.CustomerActivityDataSource;
import com.hellofresh.customeractivity.infrastructure.kafka.UserStaticsSink;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;

import static org.apache.flink.streaming.api.windowing.time.Time.minutes;

public class CustomerActivityJob {
    public static void main(String[] args) {
        //This is the entrypoint for any Flink application. Exec env will be automatically selected either local or remote
        StreamExecutionEnvironment executionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment();
        executionEnvironment.getConfig().enableForceAvro();

        DataStreamSource<CustomerActivityEvent> flightDataDataStream = new CustomerActivityDataSource(
                "http://localhost:9092",
                "customer-activity-event",
                "my-group-id",
                "http://localhost:8081"
        ).create(executionEnvironment);

        KafkaSink<String> output = new UserStaticsSink(
                "http://localhost:9092",
                "output-topic"
        ).create(executionEnvironment);

        flightDataDataStream
                .name("customeractivitystatics_source").uid("customeractivitystatics_source")
                .map(CustomerActivityStatistics::fromCustomerActivityEvent)
                .keyBy(CustomerActivityStatistics::getCountryCode)
                .window(TumblingEventTimeWindows.of(minutes(1)))
                .reduce(CustomerActivityStatistics::merge, new ProcessCustomerActivityStaticsFunction())
                .name("customeractivitystatics_reduce").uid("customeractivitystatics_reduce")
                .map(CustomerActivityStatistics::toString)
                .name("customeractivitystatics_map_to_string").uid("customeractivitystatics_map_to_string")
                .sinkTo(output)
                .name("customeractivitystatics_sink").uid("customeractivitystatics_sink");
    }
}
