package com.sapient.PSBank.controller;

import com.sapient.PSBank.entity.Customer;
import com.sapient.PSBank.entity.Transaction;
import com.sapient.PSBank.jwt.JwtAuthenticationHelper;
import com.sapient.PSBank.service.CustomerService;
import jakarta.validation.Valid;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/customer")
@CrossOrigin(origins = "http://localhost:3000/")
//@SecurityRequirements
public class CustomerController {
    private final CustomerService customerService;
    private final JwtAuthenticationHelper jwtAuthenticationHelper;

    public CustomerController(CustomerService customerService, JwtAuthenticationHelper jwtAuthenticationHelper) {
        this.customerService = customerService;
        this.jwtAuthenticationHelper = jwtAuthenticationHelper;
    }

    @PostMapping("/register")
    ResponseEntity<?> registerCustomer(@Valid @RequestBody  Customer customer){
        try {
            if(customerService.addCustomer(customer)) return new ResponseEntity<>("Registered",HttpStatus.CREATED);
            else return new ResponseEntity<>("Not Registered", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getStackTrace(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/getCustomer")
    ResponseEntity<Customer> getByID(@RequestHeader("Authorization") String bearerToken){
        try {
            Customer customer=customerService.getByID(jwtAuthenticationHelper.getIdFromToken(bearerToken.substring(7)));
            customer.setTransactions(null);
            customer.setAddress(null);
            customer.setPassword(null);
            return new ResponseEntity<>(customer, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(new Customer(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/deposit")
    ResponseEntity<String> customerDeposit(@RequestHeader("Authorization") String bearerToken,@RequestBody Customer customer){
        try {
            if(customerService.customerDeposit(jwtAuthenticationHelper.getIdFromToken(bearerToken.substring(7)), customer.getBalance())) return new ResponseEntity<>("Deposited",HttpStatus.OK);
            else return new ResponseEntity<>("Not Deposited", HttpStatus.NOT_FOUND);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping("/withdraw")
    ResponseEntity<String> customerWithdraw(@RequestHeader("Authorization") String bearerToken,@RequestBody Customer customer){
        try {
            if(customerService.customerWithdraw(jwtAuthenticationHelper.getIdFromToken(bearerToken.substring(7)), customer.getBalance())) return new ResponseEntity<>("Withdrawn",HttpStatus.OK);
            else return new ResponseEntity<>("Not Withdrawn", HttpStatus.NOT_FOUND);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping("/signout")
    ResponseEntity<String> signOut(@RequestHeader("Authorization") String bearerToken){
        try {
            customerService.signOut(jwtAuthenticationHelper.getIdFromToken(bearerToken.substring(7)));
            return new ResponseEntity<>("Signed Out",HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @Builder
    static class LoanBody{
        String type;
        double amt;
        int years;
    }
    @PutMapping("/loan")
    ResponseEntity<String> customerLoan(@RequestHeader("Authorization") String bearerToken,@RequestBody LoanBody loanBody){
        try {
            if(customerService.applyLoan(jwtAuthenticationHelper.getIdFromToken(bearerToken.substring(7)), loanBody.amt, loanBody.years,loanBody.type)) return new ResponseEntity<>("Generated "+loanBody.type,HttpStatus.OK);
            else return new ResponseEntity<>("Not Generated", HttpStatus.NOT_FOUND);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @Builder
    static class FDBody{
        double amt;
        int years;
    }
    @PutMapping("/fd")
    ResponseEntity<String> customerFD(@RequestHeader("Authorization") String bearerToken,@RequestBody FDBody fdBody){
        try {
            if(customerService.createFD(jwtAuthenticationHelper.getIdFromToken(bearerToken.substring(7)), fdBody.amt,fdBody.years)) return new ResponseEntity<>("Generated FD",HttpStatus.OK);
            else return new ResponseEntity<>("Not Generated", HttpStatus.NOT_FOUND);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @DeleteMapping("/delete")
    ResponseEntity<String> deleteCustomer(@RequestHeader("Authorization") String bearerToken){
        try {
            if(customerService.deleteCustomer(jwtAuthenticationHelper.getIdFromToken(bearerToken.substring(7))))return new ResponseEntity<>("Deleted",HttpStatus.OK);
            else return new ResponseEntity<>("Not Deleted", HttpStatus.NOT_FOUND);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/transactions")
    ResponseEntity<List<Transaction>> getTransactions(@RequestHeader("Authorization") String bearerToken){
        try {
            List<Transaction> transactionList=customerService.getTransactions(jwtAuthenticationHelper.getIdFromToken(bearerToken.substring(7)));
            if(!transactionList.isEmpty())return new ResponseEntity<>(transactionList,HttpStatus.OK);
            else return new ResponseEntity<>(transactionList,HttpStatus.NOT_FOUND);
        }
        catch (Exception e){
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/fds")
    ResponseEntity<List<Transaction>> getFDs(@RequestHeader("Authorization") String bearerToken){
        try {
            List<Transaction> fdList=customerService.getFDs(jwtAuthenticationHelper.getIdFromToken(bearerToken.substring(7)));
            if(!fdList.isEmpty())return new ResponseEntity<>(fdList,HttpStatus.OK);
            else return new ResponseEntity<>(fdList,HttpStatus.NOT_FOUND);
        }
        catch (Exception e){
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/loans")
    ResponseEntity<List<Transaction>> getLoans(@RequestHeader("Authorization") String bearerToken){
        try {
            List<Transaction> loanList=customerService.getLoans(jwtAuthenticationHelper.getIdFromToken(bearerToken.substring(7)));
            if(!loanList.isEmpty())return new ResponseEntity<>(loanList,HttpStatus.OK);
            else return new ResponseEntity<>(loanList,HttpStatus.NOT_FOUND);
        }
        catch (Exception e){
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
