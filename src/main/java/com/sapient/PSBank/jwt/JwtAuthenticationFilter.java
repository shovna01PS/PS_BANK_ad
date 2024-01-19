package com.sapient.PSBank.jwt;

import com.sapient.PSBank.repository.CustomerRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtAuthenticationHelper jwtAuthenticationHelper;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtAuthenticationHelper jwtAuthenticationHelper, UserDetailsService userDetailsService) {
        this.jwtAuthenticationHelper = jwtAuthenticationHelper;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestHeader=request.getHeader("Authorization");
        String id;
        String token;
        if(requestHeader!=null && requestHeader.startsWith("Bearer")){
            token=requestHeader.substring(7);
            id=jwtAuthenticationHelper.getIdFromToken(token);
//             && customerRepository.getCurrentToken(id).equals(token)
            if(id!=null && SecurityContextHolder.getContext().getAuthentication()==null){
                UserDetails userDetails= userDetailsService.loadUserByUsername(id);
                if(!jwtAuthenticationHelper.isTokenExpired(token)){
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=new UsernamePasswordAuthenticationToken(token,null,userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
        }
        filterChain.doFilter(request,response);
    }
}
