package com.showcase.bankaccountservice.model;

import com.showcase.bankaccountservice.model.dtos.BankAccountResponseDto;
import com.showcase.bankaccountservice.model.dtos.BankAccountCreateRequestDto;
import com.showcase.bankaccountservice.model.dtos.BankAccountUpdateRequestDto;
import com.showcase.bankaccountservice.model.entities.BankAccount;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING )
public interface BankAccountMapper {

    BankAccountMapper MAPPER = Mappers.getMapper(BankAccountMapper.class);

    BankAccountResponseDto entityToReponseDto(BankAccount bankAccount);

    BankAccount createRequestDtoToEntity(BankAccountCreateRequestDto bankAccountCreateRequestDto);

    BankAccount updateRequestDtoToEntity(BankAccountUpdateRequestDto bankAccountUpdateRequestDto);

}