package com.hellofresh.customeractivity.domain;

import com.hellofresh.customeractivity.avro.CustomerActivityEvent;

import java.util.HashMap;
import java.util.Map;

public class CustomerActivityStatistics {
    private String countryCode;
    private Map<String, Integer> userAgentCount;

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public void setUserAgentCount(Map<String, Integer> userAgentCount) {
        this.userAgentCount = userAgentCount;
    }

    public Map<String, Integer> getUserAgentCount() {
        return userAgentCount;
    }

    public CustomerActivityStatistics merge(CustomerActivityStatistics other) {
        assert this.countryCode.equals(other.countryCode);

        Map<String, Integer> agentsCount = new HashMap<>();

        for (Map.Entry<String, Integer> entry : this.userAgentCount.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            Integer sum = other.getUserAgentCount().merge(key, value, Integer::sum);
            agentsCount.put(key, sum);
        }

        CustomerActivityStatistics customerActivityStatistics = new CustomerActivityStatistics();
        customerActivityStatistics.setCountryCode(this.countryCode);
        customerActivityStatistics.setUserAgentCount(agentsCount);

        return customerActivityStatistics;
    }


    public static CustomerActivityStatistics fromCustomerActivityEvent(CustomerActivityEvent customerActivityEvent) {
        CustomerActivityStatistics customerActivityStatistics = new CustomerActivityStatistics();
        customerActivityStatistics.setCountryCode(customerActivityEvent.getCountryCode());
        customerActivityStatistics.setUserAgentCount(Map.of(customerActivityEvent.getUserAgent(), 1));
        return customerActivityStatistics;
    }
}
