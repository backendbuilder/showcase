package com.showcase.bankaccountservice.controllers;

import com.showcase.bankaccountservice.exceptions.EntityNotFoundException;
import com.showcase.bankaccountservice.model.dtos.BankAccountCreateRequestDto;
import com.showcase.bankaccountservice.model.dtos.BankAccountResponseDto;
import com.showcase.bankaccountservice.security.SecurityMethods;
import com.showcase.bankaccountservice.services.BankAccountService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.core.AnyOf.anyOf;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BankAccountController.class)
@EnableMethodSecurity
class BankAccountControllerTest {

    public static final String BLANK = "";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_CLIENT = "ROLE_CLIENT";
    public static final String PARAMETER_ACCOUNTHOLDER = "accountHolder";
    public static final String PARAMETER_ID = "id";
    public static final String RESPONSE_MESSAGE_ENTITY_NOT_FOUND = "Entity not found";
    @Autowired
    private MockMvc mockMvc;

    @MockBean(name = "securityMethods") // als deze naam niet goed is werkt het niet!
    SecurityMethods securityMethods;

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
    void createBankAccountManual_WithRoleAdmin_AndExpectStatusCreated() throws Exception {
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
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(jwt().authorities(new SimpleGrantedAuthority(ROLE_ADMIN))))
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
    void createBankAccountManual_WithBlankAccountHolder_AndExpectStatusBadRequest() throws Exception {
        //prepare
        when(bankAccountService.createBankAccount(bankAccountCreateRequestDto)).thenReturn(bankAccountResponseDto1);

        // perform the PUT request and validate the response
        mockMvc.perform(
                        put("/accounts/create-manual")
                                .content("""
                                        {
                                        "balance" : "-1",
                                        "accountHolder" : ""
                                        }
                                        """)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(jwt().authorities(new SimpleGrantedAuthority(ROLE_ADMIN))))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(anyOf(
                        containsString("""
                        {"errors":["balance is not valid, must at least be 0","account holder can't be empty"]}"""),
                        containsString("""
                        {"errors":["account holder can't be empty","balance is not valid, must at least be 0"]}""")
                )));
    }

    @Test
    void createBankAccountManual_WithNegativeBalance_AndExpectStatusBadRequest() throws Exception {
        //prepare
        when(bankAccountService.createBankAccount(bankAccountCreateRequestDto)).thenReturn(bankAccountResponseDto1);

        // perform the PUT request and validate the response
        mockMvc.perform(
                        put("/accounts/create-manual")
                                .content("""
                                        {
                                        "balance" : "-1",
                                        "accountHolder" : "HarryBanks"
                                        }
                                        """)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(jwt().authorities(new SimpleGrantedAuthority(ROLE_ADMIN))))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("""
                        {"errors":["balance is not valid, must at least be 0"]}"""));
    }

    @Test
    void createBankAccountManual_WithRoleClient_AndExpectAccessDenied() throws Exception {
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
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(jwt().authorities(new SimpleGrantedAuthority(ROLE_CLIENT))))
                .andExpect(status().isForbidden()
                )
                .andExpect(content().string("Access Denied"));
    }

    @Test
    void createBankAccountManual_WithoutAuthentication_AndExpectStatusUnAuthorized() throws Exception {
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
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createBankAccount_WithRoleAdmin_AndExpectStatusCreated() throws Exception {
        //prepare
        when(bankAccountService.createNewBankAccount(any())).thenReturn(bankAccountResponseDto1);

        // perform the PUT request and validate the response
        mockMvc.perform(
                        put("/accounts/create")
                                .with(jwt().authorities(new SimpleGrantedAuthority(ROLE_ADMIN))))
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
    void createBankAccount_WithRoleClient_AndExpectStatusCreated() throws Exception {
        //prepare
        when(bankAccountService.createNewBankAccount(any())).thenReturn(bankAccountResponseDto1);

        // perform the PUT request and validate the response
        mockMvc.perform(
                        put("/accounts/create")
                                .with(jwt().authorities(new SimpleGrantedAuthority(ROLE_CLIENT))))
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
    void createBankAccount_WitWrongRole_AndExpectAccessDenied() throws Exception {
        //prepare
        when(bankAccountService.createNewBankAccount(any())).thenReturn(bankAccountResponseDto1);

        // perform the PUT request and validate the response
        mockMvc.perform(
                        put("/accounts/create")
                                .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER"))))
                .andExpect(status().isForbidden()
                )
                .andExpect(content().string("Access Denied"));
    }


    @Test
    void readBankAccountById_WithUserIsAccountHolder_AndExpectStatusOk() throws Exception {
        when(securityMethods.userIsAccountHolder2(any(), any())).thenReturn(true);
        when(bankAccountService.readBankAccountById(ID1)).thenReturn(bankAccountResponseDto1);

        // perform the GET request and validate the response
        mockMvc.perform(
                        get("/accounts/read-by-id").param(PARAMETER_ID, ID1)
                                .with(jwt().authorities(new SimpleGrantedAuthority(ROLE_CLIENT))))
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
    void readBankAccountById_AsAdmin_AndExpectStatusOk() throws Exception {
        when(securityMethods.userIsAccountHolder2(any(), any())).thenReturn(false);
        when(bankAccountService.readBankAccountById(ID1)).thenReturn(bankAccountResponseDto1);

        // perform the GET request and validate the response
        mockMvc.perform(
                        get("/accounts/read-by-id").param(PARAMETER_ID, ID1)
                                .with(jwt().authorities(new SimpleGrantedAuthority(ROLE_ADMIN))))
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
    void readBankAccountById_WithBlankId_AndExpectStatusBadRequest() throws Exception {
        when(securityMethods.userIsAccountHolder2(any(), any())).thenReturn(true);
        when(bankAccountService.readBankAccountById(ID1)).thenReturn(bankAccountResponseDto1);

        // perform the GET request and validate the response
        mockMvc.perform(
                        get("/accounts/read-by-id").param(PARAMETER_ID, BLANK)
                                .with(jwt().authorities(new SimpleGrantedAuthority(ROLE_CLIENT))))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("""
                        {"errors":["id must not be empty"]}"""));
    }


    @Test
    void readBankAccountById_NotAdminAndNotAccountHolder_AndExpectStatusForbidden() throws Exception {
        when(securityMethods.userIsAccountHolder2(any(), any())).thenReturn(false);
        when(bankAccountService.readBankAccountById(ID1)).thenReturn(bankAccountResponseDto1);

        // perform the GET request and validate the response
        mockMvc.perform(
                        get("/accounts/read-by-id").param(PARAMETER_ID, ID1)
                                .with(jwt().authorities(new SimpleGrantedAuthority(ROLE_CLIENT))))
                .andExpect(status().isForbidden());
    }

    @Test
    void readBankAccountById_Unauthenticated_AndExpectStatusUnAuthorized() throws Exception {
        when(securityMethods.userIsAccountHolder2(any(), any())).thenReturn(false);
        when(bankAccountService.readBankAccountById(ID1)).thenReturn(bankAccountResponseDto1);

        // perform the GET request and validate the response
        mockMvc.perform(
                        get("/accounts/read-by-id").param(PARAMETER_ID, ID1)
                                .with(csrf()))
                .andExpect(status().isUnauthorized());
    }


    @Test
    void readBankAccountById_WithNotExistingBankAccount_AndExpectNotFoundException() throws Exception {
        //prepare
        when(bankAccountService.readBankAccountById(ID1)).thenThrow(new EntityNotFoundException(RESPONSE_MESSAGE_ENTITY_NOT_FOUND));

        // perform the GET request and validate the response
        mockMvc.perform(
                        get("/accounts/read-by-id").param(PARAMETER_ID, ID1)
                                .with(jwt().authorities(new SimpleGrantedAuthority(ROLE_ADMIN))))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
                .andExpect(result ->  assertEquals(RESPONSE_MESSAGE_ENTITY_NOT_FOUND, result.getResolvedException().getMessage()));
    }

    @Test
    void readBankAccountsByAccountHolder_AsAdmin_AndExpectStatusOk() throws Exception {
        //prepare
        List<BankAccountResponseDto> bankAccounts = List.of(bankAccountResponseDto1, bankAccountResponseDto2);
        when(bankAccountService.readBankAccountsByAccountHolder(ACCOUNTHOLDER1)).thenReturn(bankAccounts);
        when(securityMethods.userIsAccountHolder1(any(), any())).thenReturn(false);

        // perform the GET request and validate the response
        mockMvc.perform(
                        get("/accounts/read-by-accountholder").param(PARAMETER_ACCOUNTHOLDER, ACCOUNTHOLDER1)
                                .with(jwt().authorities(new SimpleGrantedAuthority(ROLE_ADMIN))))
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
    void readBankAccountsByAccountHolder_AsAccountHolder_AndExpectStatusOk() throws Exception {
        //prepare
        List<BankAccountResponseDto> bankAccounts = List.of(bankAccountResponseDto1, bankAccountResponseDto2);
        when(securityMethods.userIsAccountHolder1(any(), any())).thenReturn(true);
        when(bankAccountService.readBankAccountsByAccountHolder(ACCOUNTHOLDER1)).thenReturn(bankAccounts);

        // perform the GET request and validate the response
        mockMvc.perform(
                        get("/accounts/read-by-accountholder").param(PARAMETER_ACCOUNTHOLDER, ACCOUNTHOLDER1)
                                .with(jwt().authorities(new SimpleGrantedAuthority(ROLE_CLIENT))))
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
    void readBankAccountsByAccountHolder_WithNoBankAccounts_AndExpectEmptyList() throws Exception {
        //prepare
        List<BankAccountResponseDto> bankAccounts = List.of();
        when(bankAccountService.readBankAccountsByAccountHolder(ACCOUNTHOLDER1)).thenReturn(bankAccounts);

        // perform the GET request and validate the response
        mockMvc.perform(
                        get("/accounts/read-by-accountholder").param(PARAMETER_ACCOUNTHOLDER, ACCOUNTHOLDER1)
                                .with(jwt().authorities(new SimpleGrantedAuthority(ROLE_ADMIN))))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                [
                ]
                """));
    }

    @Test
    void readBankAccountsByAccountHolder_WithBlankAccountHolder_AndExpectStatusBadRequest() throws Exception {
        //prepare
        List<BankAccountResponseDto> bankAccounts = List.of();
        when(bankAccountService.readBankAccountsByAccountHolder(ACCOUNTHOLDER1)).thenReturn(bankAccounts);

        // perform the GET request and validate the response
        mockMvc.perform(
                        get("/accounts/read-by-accountholder").param(PARAMETER_ACCOUNTHOLDER, BLANK)
                                .with(jwt().authorities(new SimpleGrantedAuthority(ROLE_ADMIN))))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("""
                        {"errors":["accountHolder must not be empty"]}"""));
    }

    @Test
    void readBankAccountsByAccountHolder_NotAuthorized_AndExpectStatusForbidden() throws Exception {
        //prepare
        List<BankAccountResponseDto> bankAccounts = List.of(bankAccountResponseDto1, bankAccountResponseDto2);
        when(securityMethods.userIsAccountHolder1(any(), any())).thenReturn(false);
        when(bankAccountService.readBankAccountsByAccountHolder(ACCOUNTHOLDER1)).thenReturn(bankAccounts);

        // perform the GET request and validate the response
        mockMvc.perform(
                        get("/accounts/read-by-accountholder").param(PARAMETER_ACCOUNTHOLDER, ACCOUNTHOLDER1)
                                .with(jwt().authorities(new SimpleGrantedAuthority(ROLE_CLIENT))))
                .andExpect(status().isForbidden());
    }

    @Test
    void readBankAccountsByAccountHolder_NotAuthenticated_AndExpectStatusForbidden() throws Exception {
        //prepare
        List<BankAccountResponseDto> bankAccounts = List.of(bankAccountResponseDto1, bankAccountResponseDto2);
        when(securityMethods.userIsAccountHolder1(any(), any())).thenReturn(false);
        when(bankAccountService.readBankAccountsByAccountHolder(ACCOUNTHOLDER1)).thenReturn(bankAccounts);

        // perform the GET request and validate the response
        mockMvc.perform(
                        get("/accounts/read-by-accountholder").param(PARAMETER_ACCOUNTHOLDER, ACCOUNTHOLDER1)
                                .with(csrf()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deleteBankAccount_WithNoneExistingBankAccountId_ShouldReturnStatusBadRequest() throws Exception {
        //prepare
        doThrow(new EntityNotFoundException(RESPONSE_MESSAGE_ENTITY_NOT_FOUND)).when(bankAccountService).deleteBankAccount(ID1);
        // perform the DELETE request and validate the response
        mockMvc.perform(
                        delete("/accounts/delete-by-id").param(PARAMETER_ID, ID1)
                                .with(jwt().authorities(new SimpleGrantedAuthority(ROLE_ADMIN))))
                .andExpect(status().isNotFound())
                .andExpect(content().string(RESPONSE_MESSAGE_ENTITY_NOT_FOUND));

    }

    @Test
    void deleteBankAccount_withBlankId_AndExpectStatusBadRequest() throws Exception {
        // perform the DELETE request and validate the response
        mockMvc.perform(
                        delete("/accounts/delete-by-id").param(PARAMETER_ID, BLANK)
                                .with(jwt().authorities(new SimpleGrantedAuthority(ROLE_ADMIN))))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("""
                        {"errors":["id must not be empty"]}"""));
    }

    @Test
    void deleteBankAccount_WithExistingBankAccountId_AndExpectStatusOk() throws Exception {
        // perform the DELETE request and validate the response
        mockMvc.perform(
                        delete("/accounts/delete-by-id").param(PARAMETER_ID, ID1)
                                .with(jwt().authorities(new SimpleGrantedAuthority(ROLE_ADMIN))))
                .andExpect(status().isOk());
    }


    @Test
    void deleteBankAccount_AsClient_AndExpectStatusForbidden() throws Exception {
        //prepare
        // perform the DELETE request and validate the response
        mockMvc.perform(
                        delete("/accounts/delete-by-id").param(PARAMETER_ID, ID1)
                                .with(jwt().authorities(new SimpleGrantedAuthority(ROLE_CLIENT))))
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteBankAccount_UnAuthenticated_AndExpectStatusForbidden() throws Exception {
        //prepare
        // perform the DELETE request and validate the response
        mockMvc.perform(
                        delete("/accounts/delete-by-id").param(PARAMETER_ID, ID1)
                                .with(csrf()))
                .andExpect(status().isUnauthorized());
    }
}