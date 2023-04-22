package com.modernbankas.payment.repository;

import com.modernbankas.payment.entity.CustomerTransactionEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository class for CustomerTransaction Entity  ,CRUD operations
 */
@Repository
public interface CustomerTransactionRepository extends CrudRepository<CustomerTransactionEntity, Long> {
    //List<CustomerTransactionEntity> findFirst20ByAccountOrderByTransactionTimeStampDesc(Long accountNumber, Pageable paging);

    /**
     * find operation on customer transactions based on account number
     *
     * @param accountNumber
     * @param paging
     * @return list of transactions based on the page size and account number order by created date
     */
    List<CustomerTransactionEntity> findAllByAccountAccountNumber(Long accountNumber, Pageable paging);
}
