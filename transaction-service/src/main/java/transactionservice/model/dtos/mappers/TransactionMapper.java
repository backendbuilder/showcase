package transactionservice.model.dtos.mappers;

import com.showcase.sharedlibrary.dtos.PendingTransactionDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import transactionservice.model.dtos.TransactionRequestDto;
import transactionservice.model.entities.Transaction;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface TransactionMapper {

    Transaction transactionRequestDtoToTransaction(TransactionRequestDto dto);
    PendingTransactionDto transactionToPendingTransactionDto(Transaction transaction);

}
