package transactionservice.model.dtos.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import transactionservice.model.dtos.TransactionRequestDto;
import transactionservice.model.dtos.PendingTransactionDto;
import transactionservice.model.entities.Transaction;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface TransactionMapper {

    Transaction transactionRequestDtoToTransaction(TransactionRequestDto dto);
    PendingTransactionDto transactionToPendingTransactionDto(Transaction transaction);

}
