package com.showcase.sharedlibrary.serializers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.showcase.sharedlibrary.dtos.ProcessedTransactionDto;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class ProcessedTransactionDtoSerializer implements Serializer<ProcessedTransactionDto> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        Serializer.super.configure(configs, isKey);
    }

    @Override
    public byte[] serialize(String topic, Headers headers, ProcessedTransactionDto data) {
        return Serializer.super.serialize(topic, headers, data);
    }

    @Override
    public byte[] serialize(String topic, ProcessedTransactionDto dto) {
        try {
            if (dto == null){
                System.out.println("Null received at serializing");
                return null;
            }
            System.out.println("Serializing...");
            return objectMapper.writeValueAsBytes(dto);
        } catch (Exception e) {
            throw new SerializationException("Error when serializing TransactionDto to byte[]");
        }
    }

    @Override
    public void close() {
        Serializer.super.close();
    }
}
