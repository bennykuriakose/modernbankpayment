package com.modernbankas.payment.service.impl;

import com.modernbankas.payment.controller.AccountController;
import com.modernbankas.payment.entity.AccountEntity;
import com.modernbankas.payment.entity.CustomerEntity;
import com.modernbankas.payment.entity.CustomerTransactionEntity;
import com.modernbankas.payment.enums.TrasactionTypeEnum;
import com.modernbankas.payment.exception.PaymentException;
import com.modernbankas.payment.model.AccountResponse;
import com.modernbankas.payment.model.Customer;
import com.modernbankas.payment.model.TransactionHistoryResponse;
import com.modernbankas.payment.model.TransferMoney;
import com.modernbankas.payment.repository.CustomerAccountRepository;
import com.modernbankas.payment.repository.CustomerTransactionRepository;
import com.modernbankas.payment.service.AccountService;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {
    Logger logger= Logger.getLogger(AccountServiceImpl.class.getName());

    private static final Double MINIMUMBALANCE = 10.0;

    public  final int historysize;
    private CustomerAccountRepository customerAccountRepository;

    private CustomerTransactionRepository customerTransactionRepository;

    public AccountServiceImpl(@Value("${account.history.size}")int historysize, CustomerAccountRepository customerAccountRepository, CustomerTransactionRepository customerTransactionRepository) {
        this.historysize = historysize;
        this.customerAccountRepository = customerAccountRepository;
        this.customerTransactionRepository = customerTransactionRepository;

    }

    @Override
    public AccountResponse fetchAccountBalance(Long accountNumber) {
        AccountEntity accountEntity= getAccountDetails(accountNumber);
        logger.info("Account balance successfully fetched for accountNumber"+accountNumber);
        return new AccountResponse(accountEntity.getAccountNumber(),accountEntity.getBalance(),accountEntity.getCurrency());

    }
    /**
     * Accept account number as input and return last specified number of transactions
     * @param accountNumber
     * @return
     */
    @Override
    public List<TransactionHistoryResponse> fetchAccountHistory(Long accountNumber) {
        //validate account number
        AccountEntity accountEntity=getAccountDetails(accountNumber);
        Pageable paging = PageRequest.of(0, historysize);
        //Account number is valid fetching history
        List<CustomerTransactionEntity>customerTransactionEntities=
                customerTransactionRepository.findAccountByOrderByTransactionTimeStampDesc(accountNumber,paging);
        logger.info("Account history successfully fetched for accountNumber"+accountNumber);
        return  customerTransactionEntities.stream()
                .map(en-> new TransactionHistoryResponse(accountNumber,en.getAmount(),
                        en.getCurrency(),en.getTransactionType(),en.getTransactionTimeStamp().toString()))
                .collect(Collectors.toList());
    }

    @Override
    public void createAccount(Customer customer) {
        customerAccountRepository.save(createNewAccount(customer));
    }
    private CustomerEntity createCustomerEntity(Customer customer) {
        CustomerEntity customerEntity=new CustomerEntity();
        BeanUtils.copyProperties(customer,customerEntity);
        return customerEntity;
    }
    private AccountEntity createNewAccount(Customer customer) {
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setBalance(0.0);
        accountEntity.setCurrency("NOK");
        accountEntity.setAccountType("savings");
        accountEntity.setCustomer(createCustomerEntity(customer));
        return accountEntity;
    }
    /**
     * Transfer money between provided two accounts after checking balance
     * @param transferMoney
     */
    @Override
    @Transactional
    public void transferMoney(TransferMoney transferMoney) {
        AccountEntity senderDetails=getAccountDetails(transferMoney.senderId());
        AccountEntity receiverDetails=getAccountDetails(transferMoney.recieverId());
        //check eligibility for the transaction
        checkAccountBalance(senderDetails,transferMoney.amount());
        senderDetails.setBalance(senderDetails.getBalance()-transferMoney.amount());
        receiverDetails.setBalance(receiverDetails.getBalance()+transferMoney.amount());
        createTransactionHistory(senderDetails,receiverDetails,transferMoney.amount());
        performTransaction(senderDetails,receiverDetails);
        logger.info("Account transfer successfully done from accountNumber"+transferMoney.senderId());
    }

    private AccountEntity getAccountDetails(Long accountNumber) {
        return customerAccountRepository.findById(accountNumber).orElseThrow(() ->
                new PaymentException("Account cannot be found"));
    }


    private void createTransactionHistory(AccountEntity senderDetails, AccountEntity receiverDetails, Double amount) {
        List<CustomerTransactionEntity> customerTransactionEntityList=new ArrayList<>();
        customerTransactionEntityList.add(updateTransactionHistory(senderDetails,amount,TrasactionTypeEnum.DEBIT.name()));
        customerTransactionEntityList.add(updateTransactionHistory(receiverDetails,amount,TrasactionTypeEnum.CREDIT.name()));
        customerTransactionRepository.saveAll(customerTransactionEntityList);
    }

    private void performTransaction(AccountEntity senderEntity, AccountEntity receiverEntity) {
        updateAccountDetails(senderEntity);
        updateAccountDetails(receiverEntity);
    }

    /**
     * update account entity transaction history to save
     *
     * @param accountEntity
     * @param amount
     * @param transactionType
     * @return
     */
    private CustomerTransactionEntity updateTransactionHistory(AccountEntity accountEntity, Double amount, String transactionType) {
        CustomerTransactionEntity customerTransactionEntity= new CustomerTransactionEntity();
        customerTransactionEntity.setAccountEntity(accountEntity);
        customerTransactionEntity.setCurrency("NOK");
        customerTransactionEntity.setTransactionType(transactionType);
        customerTransactionEntity.setAmount(amount);
        return customerTransactionEntity;
    }

    private void updateAccountDetails(AccountEntity accountEntity) {
        customerAccountRepository.save(accountEntity);
    }

    /**
     * Check if the sender have enough money to perform transaction
     * @param senderDetails
     * @param amount
     */
    private void checkAccountBalance(AccountEntity senderDetails, Double amount) {
        if (senderDetails.getBalance()-amount<MINIMUMBALANCE)
            throw new PaymentException("Insufficient funds available");
    }
}

