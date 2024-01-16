package com.sapient.PSBank.service;
import com.sapient.PSBank.dto.JwtRequest;
import com.sapient.PSBank.dto.JwtResponse;
import com.sapient.PSBank.jwt.JwtAuthenticationHelper;
import com.sapient.PSBank.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final AuthenticationManager manager;
    private final JwtAuthenticationHelper jwtAuthenticationHelper;
    private final UserDetailsService userDetailsService;
    @Autowired
    CustomerRepository customerRepository;

    public AuthService(AuthenticationManager manager, JwtAuthenticationHelper jwtAuthenticationHelper, UserDetailsService userDetailsService) {
        this.manager = manager;
        this.jwtAuthenticationHelper = jwtAuthenticationHelper;
        this.userDetailsService = userDetailsService;
    }
    private void doAuthenticate(String username,String password){
        UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(username,password);
        try {
            manager.authenticate(authenticationToken);
        }catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid Username or Password");
        }
    }
    public JwtResponse login(JwtRequest jwtRequest){
        doAuthenticate(jwtRequest.getUsername(),jwtRequest.getPassword());
        UserDetails userDetails= userDetailsService.loadUserByUsername(jwtRequest.getUsername());
        String token=jwtAuthenticationHelper.generateToken(userDetails);
        customerRepository.setToken(token,jwtRequest.getUsername());
        return JwtResponse.builder().jwtToken(token).build();
    }
}
