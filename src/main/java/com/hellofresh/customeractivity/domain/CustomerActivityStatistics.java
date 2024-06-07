package com.hellofresh.customeractivity.domain;

import com.hellofresh.customeractivity.avro.CustomerActivityEvent;

import java.util.HashMap;
import java.util.Map;

public class CustomerActivityStatistics {
    private String countryCode;
    private Map<String, Long> userAgentCount;

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public void setUserAgentCount(Map<String, Long> userAgentCount) {
        this.userAgentCount = userAgentCount;
    }

    public Map<String, Long> getUserAgentCount() {
        return userAgentCount;
    }

    public CustomerActivityStatistics merge(CustomerActivityStatistics other) {
        assert this.countryCode.equals(other.countryCode);

        Map<String, Long> agentsCount = new HashMap<>();

        for (Map.Entry<String, Long> entry : this.userAgentCount.entrySet()) {
            String key = entry.getKey();
            Long value = entry.getValue();
            Long sum = other.getUserAgentCount().merge(key, value, Long::sum);
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
        customerActivityStatistics.setUserAgentCount(Map.of(customerActivityEvent.getUserAgent(), 1L));
        return customerActivityStatistics;
    }

    @Override
    public String toString() {
        return "CustomerActivityStatistics{" +
                "countryCode='" + countryCode + '\'' +
                ", userAgentCount=" + userAgentCount +
                '}';
    }
}
