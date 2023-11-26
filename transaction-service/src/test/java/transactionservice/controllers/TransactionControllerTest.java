package transactionservice.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.web.servlet.MockMvc;
import transactionservice.model.dtos.TransactionRequestDto;
import transactionservice.model.dtos.TransactionResponseDto;
import transactionservice.services.TransactionService;

import java.math.BigDecimal;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionController.class)
@EnableMethodSecurity
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    @Test
    void createBankAccountManual_AsAdmin_AndExpectStatusCreated() throws Exception {
        //prepare

        String sender = "IBAN1111";
        String recipient = "IBAN2222";
        BigDecimal amount = BigDecimal.valueOf(200);
        String accountHolder = "HarryBanks";
        String transactionId = "ID1111";
        TransactionRequestDto requestDto = new TransactionRequestDto(sender, recipient, amount, accountHolder);
        TransactionResponseDto transactionResponseDto = new TransactionResponseDto(transactionId, sender, recipient, amount, accountHolder);

        when(transactionService.initiateTransaction(requestDto)).thenReturn(transactionResponseDto);

        // perform the PUT request and validate the response
        mockMvc.perform(

                        put("/transactions/create-transaction")
                                .content("""
                                        {
                                            "sender" : "IBAN1111",
                                            "recipient" : "IBAN2222",
                                            "amount" : 200,
                                            "accountHolder" : "HarryBanks"
                                        }
                                        """)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
                .andExpect(status().isOk()
                )
                .andExpect(content().json("""
               {
                "id": "ID1111",
                "sender": "IBAN1111",
                "recipient": "IBAN2222",
                "amount": 200,
                "accountHolder": "HarryBanks"
               }
               """));
    }

    @Test
    void createBankAccountManual_UserIsAccountHolder_AndExpectStatusOk() throws Exception {
        //prepare

        String sender = "IBAN1111";
        String recipient = "IBAN2222";
        BigDecimal amount = BigDecimal.valueOf(200);
        String accountHolder = "HarryBanks";
        String transactionId = "ID1111";
        TransactionRequestDto requestDto = new TransactionRequestDto(sender, recipient, amount, accountHolder);
        TransactionResponseDto transactionResponseDto = new TransactionResponseDto(transactionId, sender, recipient, amount, accountHolder);

        Jwt custom_jwt = Jwt.withTokenValue("token-value")
                .header("alg", "none")
                .claim("preferred_username", "HarryBanks")
                .build();

        when(transactionService.initiateTransaction(requestDto)).thenReturn(transactionResponseDto);

        // perform the PUT request and validate the response
        mockMvc.perform(

                        put("/transactions/create-transaction")
                                .content("""
                                        {
                                            "sender" : "IBAN1111",
                                            "recipient" : "IBAN2222",
                                            "amount" : 200,
                                            "accountHolder" : "HarryBanks"
                                        }
                                        """)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(jwt().jwt(custom_jwt).authorities(new SimpleGrantedAuthority("ROLE_CLIENT"))))
                .andExpect(status().isOk()
                )
                .andExpect(content().json("""
               {
                "id": "ID1111",
                "sender": "IBAN1111",
                "recipient": "IBAN2222",
                "amount": 200,
                "accountHolder": "HarryBanks"
               }
               """));
    }

    @Test
    void createBankAccountManual_UserIsNotAccountHolder_AndExpectStatusForbidden() throws Exception {
        //prepare

        String sender = "IBAN1111";
        String recipient = "IBAN2222";
        BigDecimal amount = BigDecimal.valueOf(200);
        String accountHolder = "HarryBanks";
        String transactionId = "ID1111";
        TransactionRequestDto requestDto = new TransactionRequestDto(sender, recipient, amount, accountHolder);
        TransactionResponseDto transactionResponseDto = new TransactionResponseDto(transactionId, sender, recipient, amount, accountHolder);

        Jwt custom_jwt = Jwt.withTokenValue("token-value")
                .header("alg", "none")
                .claim("preferred_username", "Wrong_username_in_jwt")
                .build();

        when(transactionService.initiateTransaction(requestDto)).thenReturn(transactionResponseDto);

        // perform the PUT request and validate the response
        mockMvc.perform(

                        put("/transactions/create-transaction")
                                .content("""
                                        {
                                            "sender" : "IBAN1111",
                                            "recipient" : "IBAN2222",
                                            "amount" : 200,
                                            "accountHolder" : "HarryBanks"
                                        }
                                        """)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(jwt().jwt(custom_jwt).authorities(new SimpleGrantedAuthority("ROLE_CLIENT"))))
                .andExpect(status().isForbidden()
                );
    }

    @Test
    void createBankAccountManual_UnAuthenticated_AndExpectStatusUnAuthorized() throws Exception {
        //prepare

        String sender = "IBAN1111";
        String recipient = "IBAN2222";
        BigDecimal amount = BigDecimal.valueOf(200);
        String accountHolder = "HarryBanks";
        String transactionId = "ID1111";
        TransactionRequestDto requestDto = new TransactionRequestDto(sender, recipient, amount, accountHolder);
        TransactionResponseDto transactionResponseDto = new TransactionResponseDto(transactionId, sender, recipient, amount, accountHolder);

        when(transactionService.initiateTransaction(requestDto)).thenReturn(transactionResponseDto);

        // perform the PUT request and validate the response
        mockMvc.perform(

                        put("/transactions/create-transaction")
                                .content("""
                                        {
                                            "sender" : "IBAN1111",
                                            "recipient" : "IBAN2222",
                                            "amount" : 200,
                                            "accountHolder" : "HarryBanks"
                                        }
                                        """)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf()))
                .andExpect(status().isUnauthorized());
    }

}