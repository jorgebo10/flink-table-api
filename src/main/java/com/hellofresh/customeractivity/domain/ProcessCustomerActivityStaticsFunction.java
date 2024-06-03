package com.hellofresh.customeractivity.domain;

import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

public class ProcessCustomerActivityStaticsFunction extends ProcessWindowFunction<CustomerActivityStatistics, CustomerActivityStatistics, String, TimeWindow> {
    private ValueStateDescriptor<CustomerActivityStatistics> stateDescriptor;

    @Override
    public void open(Configuration configuration) throws Exception {
        stateDescriptor = new ValueStateDescriptor<>("customer-activity-statistics", CustomerActivityStatistics.class);
        super.open(configuration);
    }

    @Override
    public void process(String emailAddress, //key
                        ProcessWindowFunction<CustomerActivityStatistics, CustomerActivityStatistics, String, TimeWindow>.Context context,
                        Iterable<CustomerActivityStatistics> customerActivityStatistics, //every user statistics in the window for the same key
                        Collector<CustomerActivityStatistics> out) throws Exception {
        //key is provided implicitly for this state
        ValueState<CustomerActivityStatistics> state = context.globalState().getState(stateDescriptor);
        CustomerActivityStatistics accumulatedStats = state.value();

        for (CustomerActivityStatistics userStatic : customerActivityStatistics) {
            if (accumulatedStats == null) { //first time
                accumulatedStats = userStatic;
            } else {
                accumulatedStats = accumulatedStats.merge(userStatic);
            }
        }

        state.update(accumulatedStats);
        out.collect(accumulatedStats);
    }
}
