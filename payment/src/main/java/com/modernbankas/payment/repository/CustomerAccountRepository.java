package com.modernbankas.payment.repository;

import com.modernbankas.payment.entity.AccountEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository class for Account Entity  ,CRUD operations
 */
@Repository
public interface CustomerAccountRepository extends CrudRepository<AccountEntity,Long> {
}
