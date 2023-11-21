package transactionservice.model.dtos.mappers;

import com.showcase.sharedlibrary.dtos.PendingTransactionDto;
import com.showcase.sharedlibrary.dtos.ProcessedTransactionDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import transactionservice.model.dtos.TransactionRequestDto;
import transactionservice.model.dtos.TransactionResponseDto;
import transactionservice.model.entities.Transaction;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface TransactionMapper {

    Transaction transactionRequestDtoToTransaction(TransactionRequestDto dto);

    PendingTransactionDto transactionToPendingTransactionDto(Transaction transaction);

    @Mapping(source = "transactionStatus", target = "status")
    Transaction processedTransactionDtoToTransaction(ProcessedTransactionDto dto);

    TransactionResponseDto pendingTransactionDtoToToTransactionResponseDto(PendingTransactionDto dto);

}
