package com.hellofresh.customeractivity.job;

import com.hellofresh.customeractivity.domain.CustomerActivityStatistics;
import com.hellofresh.customeractivity.domain.ProcessCustomerActivityStaticsFunction;
import com.hellofresh.customeractivity.infrastructure.kafka.CustomerActivityDataSource;
import com.hellofresh.customeractivity.infrastructure.kafka.UserStaticsSink;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.flightsafe.avro.CustomerActivityEvent;

import static org.apache.flink.streaming.api.windowing.time.Time.minutes;

public class UserStaticsJob {
    public static void main(String[] args) {
        //This is the entrypoint for any Flink application. Exec env will be automatically selected either local or remote
        StreamExecutionEnvironment executionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<CustomerActivityEvent> flightDataDataStream = new CustomerActivityDataSource(
                "http://localhost:9000",
                "my-topic",
                "my-group-id",
                "http://localhost:8081"
        ).create(executionEnvironment);
        KafkaSink<CustomerActivityStatistics> statisticsKafkaSink = new UserStaticsSink().create(executionEnvironment);

        flightDataDataStream
                .name("customeractivitystatics_source").uid("customeractivitystatics_source")
                .map(CustomerActivityStatistics::fromCustomerActivityEvent)
                .keyBy(CustomerActivityStatistics::getCountryCode)
                .window(TumblingEventTimeWindows.of(minutes(1)))
                .reduce(CustomerActivityStatistics::merge, new ProcessCustomerActivityStaticsFunction())
                .name("customeractivitystatics_reduce").uid("customeractivitystatics_reduce")
                .sinkTo(statisticsKafkaSink)
                .name("customeractivitystatics_sink");
    }
}
