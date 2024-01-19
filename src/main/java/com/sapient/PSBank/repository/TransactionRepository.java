package com.sapient.PSBank.repository;

import com.sapient.PSBank.entity.Transaction;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface TransactionRepository extends CrudRepository<Transaction, Integer> {
    @Transactional
    @Modifying
    @Query("DELETE FROM Transaction t WHERE t.customer.id = :customerId")
    void deleteByCustomerID(String customerId);
    @Transactional
    @Query("SELECT t FROM Transaction t where (type = 'Deposit' OR type = 'Withdraw') and t.customer.id = :customerId ORDER BY id DESC LIMIT 20")
    List<Transaction> getPastTransactionList(String customerId);
    @Transactional
    @Query("SELECT t FROM Transaction t where (type = 'FD') and t.customer.id = :customerId ORDER BY id DESC LIMIT 20")
    List<Transaction> getFDList(String customerId);
    @Transactional
    @Query("SELECT t FROM Transaction t where type like '%Loan%' and t.customer.id = :customerId ORDER BY id DESC LIMIT 20")
    List<Transaction> getLoanList(String customerId);
}
