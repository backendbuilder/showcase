package com.showcase.transactionservice.model.dtos.mappers;

import com.showcase.sharedlibrary.dtos.PendingTransactionDto;
import com.showcase.sharedlibrary.dtos.ProcessedTransactionDto;
import com.showcase.transactionservice.model.dtos.TransactionRequestDto;
import com.showcase.transactionservice.model.dtos.TransactionResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import com.showcase.transactionservice.model.entities.Transaction;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface TransactionMapper {

    Transaction transactionRequestDtoToTransaction(TransactionRequestDto dto);

    PendingTransactionDto transactionToPendingTransactionDto(Transaction transaction);

    @Mapping(source = "transactionStatus", target = "status")
    Transaction processedTransactionDtoToTransaction(ProcessedTransactionDto dto);

    TransactionResponseDto pendingTransactionDtoToToTransactionResponseDto(PendingTransactionDto dto);

}
