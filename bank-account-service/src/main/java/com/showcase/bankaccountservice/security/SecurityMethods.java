package com.showcase.bankaccountservice.security;

import com.showcase.bankaccountservice.model.dtos.BankAccountResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class SecurityMethods {

    public SecurityMethods() {
    }

    public boolean userIsAccountHolder1(Authentication authentication, String accountHolder){
        return ((Jwt)authentication.getPrincipal()).getClaimAsString(StandardClaimNames.PREFERRED_USERNAME).equals(accountHolder);
    }

    public boolean userIsAccountHolder2(ResponseEntity<BankAccountResponseDto> responseEntity, Jwt jwt){
        BankAccountResponseDto responseDto = responseEntity.getBody();
        if (responseDto != null){
            return responseDto.accountHolder().equals(jwt.getClaimAsString(StandardClaimNames.PREFERRED_USERNAME));
        } else return false;
    }

    public String getAutheticatedUsername(Jwt jwt){
        return jwt.getClaimAsString(StandardClaimNames.PREFERRED_USERNAME);
    }


}
