package com.sapient.PSBank.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
@Component
public class JwtAuthenticationHelper {
    private final String secret=System.getenv("SECRET");
    private static final long JWT_TOKEN_VALIDITY = (long)60*60;
    public String getIdFromToken(String token){
        Claims claims=getClaimsFromToken(token);
        return claims.getSubject();
    }
    public boolean isTokenExpired(String token){
        Date expDate=getClaimsFromToken(token).getExpiration();
        return expDate.before(new Date());
    }
    public String generateToken(UserDetails userDetails){
        Map<String,Object> claims=new HashMap<>();
        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+JWT_TOKEN_VALIDITY*1000))
                .signWith(getSecretKey(secret))
                .compact();
    }
    public Claims getClaimsFromToken(String token){
        return Jwts.parser()
                .verifyWith(getSecretKey(secret))
                .build().parseSignedClaims(token)
                .getPayload();
    }
    private SecretKey getSecretKey(String key){
        byte[] decodedKey= Base64.getDecoder().decode(key);
        return Keys.hmacShaKeyFor(decodedKey);
    }
}
