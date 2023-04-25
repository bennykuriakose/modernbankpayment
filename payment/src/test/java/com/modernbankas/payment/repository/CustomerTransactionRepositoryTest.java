package com.modernbankas.payment.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static org.assertj.core.api.Assertions.assertThat;


public class CustomerTransactionRepositoryTest extends AbstractRepositoryTest {
    @Autowired
    private CustomerTransactionRepository customerTransactionRepository;
    @Test
    @DisplayName("Test FindAccountOrderByTransactionTimeStampDesc ")
    void testFindAccountOrderByTransactionTimeStampDesc(){
        var expected = createTransactionEntity();
        var result= customerTransactionRepository.
                findAllByAccountAccountNumber(expected.getAccountEntity().getAccountNumber(),
                        PageRequest.of(0, 10, Sort.Direction.DESC, "transactionTimeStamp"));
       //verify
        Assertions.assertEquals(expected.getAmount(),result.get(0).getAmount());
    }


}

