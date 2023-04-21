package com.modernbankas.payment.service;

import com.modernbankas.payment.model.AccountResponse;
import com.modernbankas.payment.model.Customer;
import com.modernbankas.payment.model.TransactionHistoryResponse;
import com.modernbankas.payment.model.TransferMoney;

import java.util.List;

public interface AccountService {
    AccountResponse fetchAccountBalance(Long accountNumber);

    void transferMoney(TransferMoney transferMoney);

    List<TransactionHistoryResponse> fetchAccountHistory(Long accountNumber);

    void createAccount(Customer customer);
}
