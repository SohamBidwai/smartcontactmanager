package com.smartcontactmanager.helper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JWTHelper {

    //Following secret required 256 byte size.

    private String SECRET_KEY = "sbids@0508219764107916MyfirstProject";
    //On following line we convert above SECRET_KEY to SECRET_KEY with HS256 algorithm.
    private SecretKey SECRET_KEY_SPEC = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    //retrieve username from token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    //retrieve expiration date from token
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(SECRET_KEY_SPEC).build().parseClaimsJws(token).getBody();
    }

    //check if the token has expired
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    //generate token for a user Following UserDetails contains username and password
    public String generateToken(UserDetails userDetails) {
        //If suppose we need to pass other details of user so we pass to claims
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    //While creating a token
    //1. Defines claims of the token, Like issuer, expiration, Subject and the ID
    //2. Sign the JWT using the HS512 algo. and secret key
    //3.

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) //set token expiration period from when token is generate
                .signWith(SECRET_KEY_SPEC, SignatureAlgorithm.HS256)
                .compact();
    }

    //validate token
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}