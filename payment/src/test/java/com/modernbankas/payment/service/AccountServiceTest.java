package com.modernbankas.payment.service;

import com.modernbankas.payment.entity.AccountEntity;
import com.modernbankas.payment.entity.CustomerTransactionEntity;
import com.modernbankas.payment.enums.TrasactionTypeEnum;
import com.modernbankas.payment.exception.PaymentException;
import com.modernbankas.payment.model.AccountResponse;
import com.modernbankas.payment.model.TransactionHistoryResponse;
import com.modernbankas.payment.model.TransferMoney;
import com.modernbankas.payment.repository.CustomerAccountRepository;
import com.modernbankas.payment.repository.CustomerTransactionRepository;
import com.modernbankas.payment.service.impl.AccountServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class AccountServiceTest {
    private static final Long ACCOUNTNUMBER = 1234L;
    @Mock
    private CustomerAccountRepository customerAccountRepository;
    @Mock
    private CustomerTransactionRepository customerTransactionRepository;

    private int historysize = 20;
    @InjectMocks
    private AccountService accountService = new AccountServiceImpl(historysize, customerAccountRepository, customerTransactionRepository);

    @Test
    @DisplayName("Test fetching account balance")
    public void fetchAccountBalanceTest() {
        when(customerAccountRepository.findById(any())).thenReturn(Optional.of(getAccountEntity(ACCOUNTNUMBER, 200.0)));
        AccountResponse accountResponse = accountService.fetchAccountBalance(ACCOUNTNUMBER);
        assertEquals(accountResponse.accountNo(), ACCOUNTNUMBER);
        verify(customerAccountRepository).findById(ACCOUNTNUMBER);
    }

    @ParameterizedTest(name = "Scenario {index} {0}")
    @MethodSource("getInput")
    public void transferMoneyTest(String name, AccountEntity sender, AccountEntity receiver, Double amount, Integer times) {
        ArgumentCaptor<AccountEntity> captorParam1 = ArgumentCaptor.forClass(AccountEntity.class);
        //mock
        when(customerAccountRepository.findById(sender.getAccountNumber())).thenReturn(Optional.of(sender));
        when(customerAccountRepository.findById(receiver.getAccountNumber())).thenReturn(Optional.of(receiver));
        when(customerAccountRepository.save(captorParam1.capture())).thenReturn(null);

        //perform
        accountService.transferMoney(new TransferMoney(sender.getAccountNumber()
                , receiver.getAccountNumber(), amount));
        //verify
        assertEquals(receiver.getBalance(), captorParam1.getValue().getBalance());
        verify(customerAccountRepository, times(times)).save(any());
        verify(customerTransactionRepository, times(1)).saveAll(any());
    }

    @ParameterizedTest(name = "Scenario {index} Test failure reason {0}")
    @MethodSource("getFailureInput")
    void testAccountTransferException(String name, Long senderId, Long receiverId,
                                      Optional<AccountEntity> sender, Optional<AccountEntity> receiver, String message) {

        //mock
        when(customerAccountRepository.findById(senderId)).thenReturn(sender);
        when(customerAccountRepository.findById(receiverId)).thenReturn(receiver);

        PaymentException exception = assertThrows(PaymentException.class, () -> {
            //perform
            accountService.transferMoney(new TransferMoney(senderId
                    , receiverId, 10.0));
        });
        //verify
        assertEquals(message, exception.getMessage());
    }

    @ParameterizedTest(name = "Scenario {index} {0}")
    @MethodSource("getTransactionHistoryInput")
    public void transactionHistoryTest(String name, Long accountNo, List<CustomerTransactionEntity> customerTransactionEntityList, Integer size) {
        //mock
        when(customerAccountRepository.findById(any())).thenReturn(Optional.of(getAccountEntity(123L, 100.0)));
        when(customerTransactionRepository.findAccountByOrderByTransactionTimeStampDesc(accountNo, PageRequest.of(0, 20))).thenReturn(customerTransactionEntityList);
        //perform
        List<TransactionHistoryResponse> transactionHistoryResponses = accountService.fetchAccountHistory(accountNo);
        //verify
        assertEquals(transactionHistoryResponses.size(), size);
        verify(customerTransactionRepository).findAccountByOrderByTransactionTimeStampDesc(any(), any());
    }

    public static Stream<Arguments> getFailureInput() {
        return Stream.of(Arguments.of("Invalid sender account number", 123L, 124L, Optional.empty(),
                        Optional.of(getAccountEntity(1234L, 200.0)), "Account cannot be found"),
                Arguments.of("Invalid receiver account number", 123L, 124L, Optional.of(getAccountEntity(123L, 20L)
                ), Optional.empty(), "Account cannot be found"),
                Arguments.of("Insufficient balance", 123L, 124L, Optional.of(getAccountEntity(123L, 10L))
                        , Optional.of(getAccountEntity(1234L, 200.0)), "Insufficient funds available"));
    }

    public static Stream<Arguments> getInput() {
        return Stream.of(Arguments.of("Transfer amount from available balance", getAccountEntity(123L, 100L)
                        , getAccountEntity(1234L, 200.0), 10.0, 2),
                Arguments.of("Transfer full amount from limit balance", getAccountEntity(123L, 20L)
                        , getAccountEntity(1234L, 200.0), 10.0, 2));
    }

    public static Stream<Arguments> getTransactionHistoryInput() {
        return Stream.of(Arguments.of("Valid account no with 20 transaction", 123L, getCustomerTransactionEntities(20), 20),
                Arguments.of("Valid account with 0 transaction", 123L, getCustomerTransactionEntities(0), 0),
                Arguments.of("Valid account with less than 20 transaction", 123L, getCustomerTransactionEntities(12), 12));
    }

    private static ArrayList<CustomerTransactionEntity> getCustomerTransactionEntities(int initialCapacity) {
        ArrayList<CustomerTransactionEntity>customerTransactionEntities=new ArrayList<>();
        for (int i = 0; i < initialCapacity; i++) {
            CustomerTransactionEntity customerTransactionEntity= new CustomerTransactionEntity();
            customerTransactionEntity.setCurrency("NOK");
            customerTransactionEntity.setTransactionType(TrasactionTypeEnum.DEBIT.name());
            customerTransactionEntity.setAmount(100.0);
            customerTransactionEntity.setTransactionTimeStamp(LocalDateTime.now());
            customerTransactionEntities.add(customerTransactionEntity) ;
        }
        return customerTransactionEntities;
    }

    private static AccountEntity getAccountEntity(Long accountNumber, double balance) {
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setBalance(balance);
        accountEntity.setAccountNumber(accountNumber);
        return accountEntity;
    }
}
