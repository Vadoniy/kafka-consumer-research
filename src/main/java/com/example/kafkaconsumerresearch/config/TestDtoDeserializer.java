package com.example.kafkaconsumerresearch.config;

import com.example.kafkaconsumerresearch.dto.TestDto;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.kafka.common.serialization.Deserializer;
import org.springframework.kafka.support.serializer.DeserializationException;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class TestDtoDeserializer implements Deserializer<TestDto> {
    @Override
    public TestDto deserialize(String topic, byte[] data) {
        try {
            if (data == null) {
                System.err.println("Null is received, nothing to deserialize.");
                return null;
            }

            final var buffer = ByteBuffer.wrap(data);
            final var id = buffer.getLong();
            final var length = buffer.getInt();
            final var nameBytes = ArrayUtils.subarray(data, data.length - length, data.length);
            final var name = new String(nameBytes, StandardCharsets.UTF_8);

            return new TestDto(id, name);
        } catch (Exception exception) {
            throw new DeserializationException("Error during deserialization data", data, false, exception);
        }
    }
}
