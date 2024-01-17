package com.sapient.PSBank.service;
import com.sapient.PSBank.entity.Customer;
import com.sapient.PSBank.entity.Transaction;
import com.sapient.PSBank.functions.DateAndTime;
import com.sapient.PSBank.repository.CustomerRepository;
import com.sapient.PSBank.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@NoArgsConstructor
@Service
public class CustomerService {
    CustomerRepository customerRepository;
    TransactionRepository transactionRepository;
    DateAndTime dateAndTime=new DateAndTime();
    private static final Logger logger= LoggerFactory.getLogger(CustomerService.class);

    @Autowired
    public CustomerService(CustomerRepository customerRepository, TransactionRepository transactionRepository) {
        this.customerRepository = customerRepository;
        this.transactionRepository = transactionRepository;
    }
    public boolean addCustomer(Customer customer){
        if(!check(customer.getId())) {
            BCryptPasswordEncoder bCryptPasswordEncoder=new BCryptPasswordEncoder();
            String encodedPassword=bCryptPasswordEncoder.encode(customer.getPassword());
            customer.setPassword(encodedPassword);
            customerRepository.save(customer);
            if(check(customer.getId())) {
                logger.info("Saved the new customer");
                return true;
            }
            else logger.info("Some internal failure happened");
        }
        else logger.error("Customer with same id was available previously.");
        return false;
    }

    public Customer getByID(String id){
        Optional<Customer> arr=customerRepository.findById(id);
        Customer customer=null;
        if(arr.isPresent()){customer=arr.get();}
        if(!ObjectUtils.isEmpty(customer)) {
            logger.info("Successfully found a customer with given id");
            return customer;
        }
        else{
            logger.error("There was no customer with the given id");
            return null;
        }
    }
    public boolean customerDeposit(String id,double amt) throws Exception {
        Customer customer=getByID(id);
        if(amt>0 && !ObjectUtils.isEmpty(customer) && (amt+customer.getBalance()<100000000f)) {
            customer.setBalance(customer.getBalance() + amt);
            customerRepository.save(customer);
            Transaction transaction = new Transaction(dateAndTime.getDateAndTime(), "Deposit", amt,0, customer);
            transactionRepository.save(transaction);
            logger.info("Deposited "+amt+" rupees in the account of "+id);
            return true;
        }
        return false;
    }
    public boolean customerWithdraw(String id,double amt) throws Exception {
        Customer customer=getByID(id);
        if(amt>0 && !ObjectUtils.isEmpty(customer) && (customer.getBalance()-amt>1000)) {
            customer.setBalance(customer.getBalance() - amt);
            customerRepository.save(customer);
            Transaction transaction = new Transaction(dateAndTime.getDateAndTime(), "Withdraw", amt,0, customer);
            transactionRepository.save(transaction);
            logger.info("Withdrawn "+amt+" rupees from the account of "+id);
            return true;
        }
        return false;
    }
    public boolean applyLoan(String id,double amt,int years,String type) throws Exception {
        Customer customer=getByID(id);
        if(amt>0 && !ObjectUtils.isEmpty(customer)) {
            Transaction transaction = new Transaction(dateAndTime.getDateAndTime(), type, amt,years, customer);
            transactionRepository.save(transaction);
            logger.info("Provided "+amt+" rupees of "+type+" to the account of "+id);
            return true;
        }
        return false;
    }
    public boolean createFD(String id,double amt,int years) throws Exception {
        Customer customer=getByID(id);
        if(amt>0 && !ObjectUtils.isEmpty(customer)) {
            Transaction transaction = new Transaction(dateAndTime.getDateAndTime(), "FD", amt,years, customer);
            transactionRepository.save(transaction);
            logger.info("Generated FD of "+amt+" rupees for the account of "+id);
            return true;
        }
        return false;
    }

    public boolean deleteCustomer(String id){
        if(check(id)) {
            transactionRepository.deleteByCustomerID(id);
            customerRepository.deleteById(id);
            logger.info("Customer with "+id+" is deleted successfully.");
            return true;
        }
        else logger.error("Customer does not exist.");
        return false;
    }
    public List<Customer> getAll(){
        List<Customer>customerList=new ArrayList<>();
        customerRepository.findAll().forEach(customerList::add);
        return customerList;
    }
    public boolean check(String id){
        return customerRepository.existsById(id);
    }
    public void signOut(String id){
        logger.info("setting token");
        customerRepository.setToken("",id);}
    public List<Transaction> getTransactions(String id) {
        return transactionRepository.getPastTransactionList(id);
    }
    public List<Transaction> getFDs(String id) {
        return transactionRepository.getFDList(id);
    }
    public List<Transaction> getLoans(String id) {
        return transactionRepository.getLoanList(id);
    }
}
