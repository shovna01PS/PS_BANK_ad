package com.sapient.PSBank.repository;

import com.sapient.PSBank.entity.Customer;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, String> {
    @Transactional
    @Query("SELECT c.currentToken FROM Customer c where c.id = :customerId")
    String getCurrentToken(String customerId);
    @Transactional
    @Modifying
    @Query("update Customer c set c.currentToken = :newToken where c.id = :customerId")
    void setToken(String newToken,String customerId);
}
