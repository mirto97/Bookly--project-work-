package com.generation.bookly.security;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.generation.bookly.model.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtService {

    // In produzione, questa chiave deve essere segreta e complessa!
    private static final String SECRET_KEY = "12345678912345678912345678912345";

    public String generateToken(User user) { // Assicurati che l'import sia com.generation.bookly.model.User
        Map<String, Object> extraClaims = new HashMap<>();
        
        // Aggiungiamo i claims solo se l'utente non è nullo
        if (user.getRole() != null) {
            extraClaims.put("role", user.getRole().name());
        }
        
        // Corretto: usiamo 'user' e non 'string'
        extraClaims.put("firstName", user.getFirstName()); 

        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(user.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                // Scadenza: 10 ore
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        boolean isExpired = isTokenExpired(token);
        System.out.println("DEBUG - Username match: " + username.equalsIgnoreCase(userDetails.getUsername()));
        System.out.println("DEBUG - Token expired: " + isExpired);
        return (username.equalsIgnoreCase(userDetails.getUsername())) && !isExpired;
    }

    private Key getSigningKey() {
        byte[] keyBytes = SECRET_KEY.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        return io.jsonwebtoken.security.Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token) // Questa riga lancerà un'eccezione se il token è corrotto/scaduto
                .getBody();
    }
    
 // --- METODO MANCANTE 1 ---
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // --- METODO MANCANTE 2 ---
    private boolean isTokenExpired(String token) {
    	return extractClaim(token, Claims::getExpiration).before(new java.util.Date());
    }
    // Altri metodi helper per estrarre claims e scadenza...
}