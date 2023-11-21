package transactionservice.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import transactionservice.model.dtos.TransactionRequestDto;
import transactionservice.model.dtos.TransactionResponseDto;
import transactionservice.services.TransactionService;

import java.math.BigDecimal;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    @Test
        //@WithMockUser(username = "user", roles = "USER")
    void createBankAccountManualAndExpectStatusCreated() throws Exception {
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
                                .contentType(MediaType.APPLICATION_JSON))
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

}