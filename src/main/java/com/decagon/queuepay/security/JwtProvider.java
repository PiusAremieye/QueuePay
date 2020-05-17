package com.decagon.queuepay.security;

import com.decagon.queuepay.exceptions.CustomException;
import com.decagon.queuepay.models.user.User;
import com.decagon.queuepay.repositories.UserRepository;
import com.decagon.queuepay.service.MyUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Component
public class JwtProvider {

    @Value("${queuepay.jwtSecretKey}")
    private String secretKey;

    @Value("${queuepay.jwtExpirationMs}")
    private Long validity;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @PostConstruct
    protected void init(){
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    protected String generateToken(String subject, Long validityPeriod) {
        Claims claims = Jwts.claims().setSubject(subject);
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityPeriod);

        return Jwts.builder()
          .setClaims(claims)
          .setIssuedAt(now)
          .setExpiration(validity)
          .signWith(SignatureAlgorithm.HS256, secretKey)
          .compact();
    }

    public String createToken(String username) {
        return generateToken(username, validity);
    }

    public String getUsername(String token){
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private Claims getAllClaims(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

    public Boolean isTokenExpired(String token) {
        return getAllClaims(token).getExpiration().before(new Date());
    }

    public String resolveToken(HttpServletRequest request){
        String bearer = request.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")){
            return bearer.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token){
        boolean isValid = false;
        try{
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            isValid = true;
        }catch (JwtException | IllegalArgumentException ex){
            ex.getMessage();
        }
        return isValid;
    }

    public User resolveUser(HttpServletRequest request){
        String token = this.resolveToken(request);
        String username = this.getUsername(token);
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()){
            return user.get();
        }
        throw new CustomException("User does not exists", HttpStatus.NOT_FOUND);
    }

}
