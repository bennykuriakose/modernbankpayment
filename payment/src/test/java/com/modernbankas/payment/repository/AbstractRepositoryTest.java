package com.modernbankas.payment.repository;

import com.modernbankas.payment.entity.AccountEntity;
import com.modernbankas.payment.entity.CustomerTransactionEntity;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

@DataJpaTest
public class AbstractRepositoryTest {
    @Autowired
    public TestEntityManager testEntityManager;
    public CustomerTransactionEntity createTransactionEntity() {
        CustomerTransactionEntity customerTransactionEntity=new CustomerTransactionEntity();
        customerTransactionEntity.setId(1L);
        customerTransactionEntity.setTransactionType("debit");
        customerTransactionEntity.setAmount(100.0);
        customerTransactionEntity.setTransactionTimeStamp(LocalDateTime.now());
        customerTransactionEntity.setCurrency("Nok");
        customerTransactionEntity.setcPart(1238L);
        AccountEntity accountEntity= new AccountEntity();
        accountEntity.setBalance(100.0);
        accountEntity.setCurrency("nok");
        customerTransactionEntity.setAccountEntity(accountEntity);
        testEntityManager.persist(accountEntity);
        return testEntityManager.persist(customerTransactionEntity);
    }
@Transactional
    public Long createAccountEntity() {
        AccountEntity accountEntity= new AccountEntity();
        accountEntity.setBalance(10.0);
        accountEntity.setCurrency("nok");
        return testEntityManager.persist(accountEntity).getAccountNumber();
    }
}
