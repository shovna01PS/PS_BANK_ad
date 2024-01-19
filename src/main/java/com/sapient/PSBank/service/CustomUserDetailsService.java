package com.sapient.PSBank.service;
import com.sapient.PSBank.repository.CustomerRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private CustomerRepository customerRepository;
    public CustomUserDetailsService(CustomerRepository customerRepository){this.customerRepository=customerRepository;}
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.customerRepository.findById(username).orElseThrow(()->new UsernameNotFoundException("Customer with the given id is not found"));
    }
}
