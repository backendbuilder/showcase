package com.showcase.sharedlibrary.deserializers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.showcase.sharedlibrary.dtos.PendingTransactionDto;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class PendingTransactionDtoDeserializer implements Deserializer<PendingTransactionDto> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        Deserializer.super.configure(configs, isKey);
    }

    @Override
    public PendingTransactionDto deserialize(String topic, Headers headers, byte[] data) {
        return deserialize(topic, data);
    }

    @Override
    public PendingTransactionDto deserialize(String topic, byte[] data) {
        try {
            if (data == null) {
                System.out.println("Null received at deserializing");
                return null;
            }
            System.out.println("Deserializing...");
            return objectMapper.readValue(data, PendingTransactionDto.class);
        } catch (Exception e) {
            throw new SerializationException("Error when deserializing byte[] to ProcessedTransactionDto");
        }
    }

    @Override
    public void close() {
        Deserializer.super.close();
    }
}
