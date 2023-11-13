package com.showcase.sharedlibrary.deserializers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.showcase.sharedlibrary.dtos.ProcessedTransactionDto;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class ProcessedTransactionDtoDeserializer implements Deserializer<ProcessedTransactionDto>{
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        Deserializer.super.configure(configs, isKey);
    }

    @Override
    public ProcessedTransactionDto deserialize(String topic, Headers headers, byte[] data) {
        return deserialize(topic, data);
    }

    @Override
    public ProcessedTransactionDto deserialize(String topic, byte[] data) {
        try {
            if (data == null) {
                System.out.println("Null received at deserializing");
                return null;
            }
            System.out.println("Deserializing...");
            return objectMapper.readValue(data, ProcessedTransactionDto.class);
        } catch (Exception e) {
            throw new SerializationException("Error when deserializing byte[] to ProcessedTransactionDto");
        }
    }

    @Override
    public void close() {
        Deserializer.super.close();
    }
}
