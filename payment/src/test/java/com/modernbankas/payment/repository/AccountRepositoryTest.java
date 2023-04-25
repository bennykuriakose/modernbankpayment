package com.modernbankas.payment.repository;

import com.modernbankas.payment.entity.AccountEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
public class AccountRepositoryTest {
    @Autowired
    private CustomerAccountRepository customerAccountRepository;
    private Long savedAccountId;

    @BeforeEach
    public void insertUsers() {
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setBalance(10.0);
        accountEntity.setCurrency("nok");
        savedAccountId = customerAccountRepository.save(accountEntity).getAccountNumber();
    }

    @Test
    public void testConcurrentOperations() throws InterruptedException {
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                AccountEntity account = customerAccountRepository.findById(savedAccountId).orElse(null);
                try {
                    Thread.sleep(6000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                account.setBalance(12.0);
                assertThrows(ObjectOptimisticLockingFailureException.class, () -> {
                    //perform
                    customerAccountRepository.save(account);
                });

            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                AccountEntity account = customerAccountRepository.findById(savedAccountId).orElse(null);
                account.setBalance(100.0);
                customerAccountRepository.save(account);
            }
        });
        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }
}
