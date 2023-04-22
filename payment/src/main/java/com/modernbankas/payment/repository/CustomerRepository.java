package com.modernbankas.payment.repository;

import com.modernbankas.payment.entity.CustomerEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
/**
 * Repository class for Customer Entity  ,CRUD operations
 */
@Repository
public interface CustomerRepository extends CrudRepository<CustomerEntity,Long> {
}
