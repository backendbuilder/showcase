package com.showcase.bankaccountservice.controllers;

import com.showcase.bankaccountservice.Exceptions.EntityNotFoundException;
import com.showcase.bankaccountservice.model.dtos.BankAccountCreateRequestDto;
import com.showcase.bankaccountservice.model.dtos.BankAccountResponseDto;
import com.showcase.bankaccountservice.services.BankAccountService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BankAccountController.class)
class BankAccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BankAccountService bankAccountService;

    private static BankAccountCreateRequestDto bankAccountCreateRequestDto;
    private static BankAccountResponseDto bankAccountResponseDto1;
    private static BankAccountResponseDto bankAccountResponseDto2;
    private final static String ACCOUNTHOLDER1 = "HarryBanks";
    private final static String ID1 = "1111";
    private final static String ID2 = "2222";


    @BeforeAll
    static void setup()
    {
        BigDecimal amount = BigDecimal.valueOf(1000);
        bankAccountCreateRequestDto = new BankAccountCreateRequestDto(amount, ACCOUNTHOLDER1);
        bankAccountResponseDto1 = new BankAccountResponseDto(ID1, amount, ACCOUNTHOLDER1);
        bankAccountResponseDto2 = new BankAccountResponseDto(ID2, amount, ACCOUNTHOLDER1);
    }


    @Test
    //@WithMockUser(username = "user", roles = "USER")
    void createBankAccountManualAndExpectStatusCreated() throws Exception {
        //prepare
        when(bankAccountService.createBankAccount(bankAccountCreateRequestDto)).thenReturn(bankAccountResponseDto1);

        // perform the PUT request and validate the response
        mockMvc.perform(
                        put("/accounts/create-manual")
                                .content("""
                                        {
                                        "balance" : 1000,
                                        "accountHolder" : "HarryBanks"
                                        }
                                        """)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()
                )
                .andExpect(content().json("""
               
               {
                   "id": "1111",
                   "balance": 1000,
                   "accountHolder": "HarryBanks"
               }
                """));
    }

    @Test
    void createBankAccountAndExpectStatusCreated() throws Exception {
        //prepare
        when(bankAccountService.createNewBankAccount()).thenReturn(bankAccountResponseDto1);

        // perform the PUT request and validate the response
        mockMvc.perform(
                        put("/accounts/create"))
                .andExpect(status().isCreated()
                )
                .andExpect(content().json("""
               
               {
                   "id": "1111",
                   "balance": 1000,
                   "accountHolder": "HarryBanks"
               }
                """));
    }


    @Test
    void readBankAccountAndExpectStatusOk() throws Exception {
        //prepare

        when(bankAccountService.readBankAccountById(ID1)).thenReturn(bankAccountResponseDto1);

        // perform the GET request and validate the response
        mockMvc.perform(
                        get("/accounts/read-by-id").param("id", ID1))
                .andExpect(status().isOk())
                .andExpect(content().json("""
               
               {
                   "id": "1111",
                   "balance": 1000,
                   "accountHolder": "HarryBanks"
               }
                """));
    }

    @Test
    void readBankAccountAndExpectNotFoundException() throws Exception {
        //prepare
        when(bankAccountService.readBankAccountById(ID1)).thenThrow(new EntityNotFoundException("Entity not found"));

        // perform the PUT request and validate the response
        mockMvc.perform(
                        get("/accounts/read-by-id").param("id", ID1))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
                .andExpect(result ->  assertEquals("Entity not found", result.getResolvedException().getMessage()));
    }

    @Test
    void readBankAccountsAndExpectStatusOk() throws Exception {
        //prepare
        List<BankAccountResponseDto> bankAccounts = List.of(bankAccountResponseDto1, bankAccountResponseDto2);
        when(bankAccountService.readBankAccountsByAccountHolder(ACCOUNTHOLDER1)).thenReturn(bankAccounts);

        // perform the GET request and validate the response
        mockMvc.perform(
                        get("/accounts/read-by-accountholder").param("accountHolder", ACCOUNTHOLDER1))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                [
                    {
                        "id":"1111",
                        "balance":1000,
                        "accountHolder":"HarryBanks"
                    },
                    {
                        "id":"2222",
                        "balance":1000,
                        "accountHolder":"HarryBanks"
                    }
                ]
                """));
    }

    @Test
    void readBankAccountsAndExpectEmptyList() throws Exception {
        //prepare
        List<BankAccountResponseDto> bankAccounts = List.of();
        when(bankAccountService.readBankAccountsByAccountHolder(ACCOUNTHOLDER1)).thenReturn(bankAccounts);

        // perform the GET request and validate the response
        mockMvc.perform(
                        get("/accounts/read-by-accountholder").param("accountHolder", ACCOUNTHOLDER1))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                [
                ]
                """));
    }

    @Test
    void deleteBankAccountShouldThrowNotEntityNotFoundException() throws Exception {
        //prepare
        doThrow(new EntityNotFoundException("Entity not found")).when(bankAccountService).deleteBankAccount(ID1);
        // perform the DELETE request and validate the response
        mockMvc.perform(
                        delete("/accounts/delete-by-id").param("id", ID1))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
                .andExpect(result ->  assertEquals("Entity not found", result.getResolvedException().getMessage()));
    }

    @Test
    void deleteBankAccountShouldShouldExpectStatusOk() throws Exception {
        //prepare
        // perform the DELETE request and validate the response
        mockMvc.perform(
                        delete("/accounts/delete-by-id").param("id", ""))
                .andExpect(status().isOk());
    }
}