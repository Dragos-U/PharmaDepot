package com.bearsoft.pharmadepot.services.security;


import com.bearsoft.pharmadepot.models.security.SecurityPharmacy;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtFilterService {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;
    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;
    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;

    public String extractPharmacyName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(SecurityPharmacy securityPharmacy) {
        return generateToken(new HashMap<>(), securityPharmacy);
    }
    public String generateToken(
            Map<String, Object> extraClaims,
            SecurityPharmacy securityPharmacy
    ) {
        return buildToken(extraClaims, securityPharmacy, jwtExpiration);
    }

    public String generateRefreshToken(SecurityPharmacy securityPharmacy) {
        return buildToken(new HashMap<>(), securityPharmacy, refreshExpiration);
    }

    private String buildToken(
            Map<String, Object> extraClaims,
            SecurityPharmacy securityPharmacy,
            long expiration) {

        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(securityPharmacy.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, SecurityPharmacy securityPharmacy) {
        final String username = extractPharmacyName(token);
        return (username.equals(securityPharmacy.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
