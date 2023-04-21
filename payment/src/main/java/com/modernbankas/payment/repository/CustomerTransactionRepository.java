package com.modernbankas.payment.repository;

import com.modernbankas.payment.entity.CustomerTransactionEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerTransactionRepository extends CrudRepository<CustomerTransactionEntity,Long> {
    //List<CustomerTransactionEntity> findFirst20ByAccountOrderByTransactionTimeStampDesc(Long accountNumber, Pageable paging);

    List<CustomerTransactionEntity> findAccountByOrderByTransactionTimeStampDesc(Long accountNumber, Pageable paging);
}
