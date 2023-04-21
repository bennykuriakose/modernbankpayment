package com.modernbankas.payment.controller;

import com.modernbankas.payment.model.AccountResponse;
import com.modernbankas.payment.model.Customer;
import com.modernbankas.payment.model.TransactionHistoryResponse;
import com.modernbankas.payment.model.TransferMoney;
import com.modernbankas.payment.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
@RequestMapping(path = "/accounts")
public class AccountController {
    Logger logger= Logger.getLogger(AccountController.class.getName());

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }
    @GetMapping("{accountNumber}/balance")
    public ResponseEntity<AccountResponse> getAccountBalance(@PathVariable Long accountNumber){
        logger.info("Fetch account balance for - {}"+accountNumber);
        return new ResponseEntity<>(accountService.fetchAccountBalance(accountNumber),HttpStatus.OK);
    }
    @PostMapping("/transfer")
    public ResponseEntity transferMoney(@Validated TransferMoney transferMoney){
        logger.info("transfer money initiated for account "+transferMoney.recieverId());
        accountService.transferMoney(transferMoney);
        return new ResponseEntity(HttpStatus.OK);
    }
    @GetMapping("/{accountNumber}/statements/mini")
    public ResponseEntity<TransactionHistoryResponse> getMiniStatement(@PathVariable Long accountNumber){
        logger.info("mini statement flow initiated for account "+accountNumber);
        return new ResponseEntity(accountService.fetchAccountHistory(accountNumber),HttpStatus.OK);
    }
    @PostMapping("/create")
    public ResponseEntity createAccount(@RequestBody Customer customer)
    {
        logger.info("Creating User profile and account for Customer {}"+customer.customerName());
        accountService.createAccount(customer);
        return new ResponseEntity(HttpStatus.OK);

    }
}
