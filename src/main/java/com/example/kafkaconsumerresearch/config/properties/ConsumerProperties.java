package com.example.kafkaconsumerresearch.config.properties;

import java.util.Map;

public interface ConsumerProperties {
    String getKeyDeserializer();
    String getValueDeserializer();
    String getGroupId();
    default Map<String, String> getProperties() {
        return null;
    }
}
