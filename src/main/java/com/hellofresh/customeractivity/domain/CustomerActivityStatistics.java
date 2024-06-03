package com.hellofresh.customeractivity.domain;

import com.hellofresh.customeractivity.avro.CustomerActivityEvent;

import java.util.HashMap;
import java.util.Map;

public final class CustomerActivityStatistics {
    private final String countryCode;
    private final Map<String, Integer> userAgentCount;

    public CustomerActivityStatistics(String countryCode, Map<String, Integer> userAgentCount) {
        this.countryCode = countryCode;
        this.userAgentCount = userAgentCount;
    }

    public String getCountryCode() {
        return countryCode;
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

        return new CustomerActivityStatistics(this.countryCode, agentsCount);
    }


    public static CustomerActivityStatistics fromCustomerActivityEvent(CustomerActivityEvent customerActivityEvent) {
        return new CustomerActivityStatistics(
                customerActivityEvent.getCountry(),
                Map.of(customerActivityEvent.getUserAgent(), 1));
    }
}
