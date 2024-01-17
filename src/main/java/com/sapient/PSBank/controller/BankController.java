package com.sapient.PSBank.controller;

import com.sapient.PSBank.entity.Customer;
import com.sapient.PSBank.service.CustomerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bank")
public class BankController {
    final CustomerService customerService;

    public BankController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/getAll")
    List<Customer> getAll(){return customerService.getAll();}
}
