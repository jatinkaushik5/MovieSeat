package com.MovieSeat.movieSeat.Configuration;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtHelper {

    private  final  String secretKey=  "MySuperSecretKeyForJwtGeneration123456";
    private final long expirationTime=
            4*60*60*24*1000;


    private Key getSignKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }


    public String generateToken(UserDetails userDetails){
        Map<String,Object> claims=new HashMap<>();

        claims.put("authorities",
                userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList())
                );

        return createToken(claims,userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String username) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+expirationTime))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();

    }

    // =========================
    // 🔹 VALIDATE TOKEN
    // =========================

    public boolean validateToken(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername())
                && !isTokenExpired(token);
    }

    // =========================
    // 🔹 EXTRACT DATA
    // =========================

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // =========================
    // 🔹 EXPIRY CHECK
    // =========================

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

}
