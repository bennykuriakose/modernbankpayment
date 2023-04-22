package com.modernbankas.payment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.modernbankas.payment.model.AccountResponse;
import com.modernbankas.payment.model.Customer;
import com.modernbankas.payment.model.TransferMoney;
import com.modernbankas.payment.service.AccountService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = AccountController.class)
public class AccountControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @InjectMocks
    private AccountController accountController;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Test fetch balance for valid account number")
    public void testFetchBalance() throws Exception {
        //mock
        Mockito.when(accountService.fetchAccountBalance(Mockito.anyLong())).thenReturn(getAccountBalance());
        //perform
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/accounts/{accountNumber}/balance", 123L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        //verify
        Mockito.verify(accountService).fetchAccountBalance(Mockito.anyLong());
    }

    @Test
    @DisplayName("Test transfer for valid account number")
    public void testTransferBalance() throws Exception {
        //perform
        mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/accounts/transfer")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(getTransferRequest()))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        //verify
        Mockito.verify(accountService).transferMoney(Mockito.any());
    }

    @Test
    @DisplayName("Test mini statement for valid account number")
    public void testGetMinistatement() throws Exception {
        //perform
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/accounts/{accountNo}/statements/mini", 123L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        //verify
        Mockito.verify(accountService).fetchAccountHistory(Mockito.any());
    }

    @Test
    @DisplayName("Test create account")
    public void testCreateAccount() throws Exception {
        //perform
        mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/accounts/create", 123L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(getCustomer()))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        //verify
        Mockito.verify(accountService).createAccount(Mockito.any());
    }

    private Customer getCustomer() {
        return new Customer("test", "test@g.com", "94008464", "25-03-1995");
    }

    private TransferMoney getTransferRequest() {
        return new TransferMoney(123L, 124L, 100.0);
    }

    private AccountResponse getAccountBalance() {
        return new AccountResponse(123L, 200.0, "NOK");
    }
}
