package com.api.login.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${login.config.secret-key}")
    private String JWT_KEY;
    @Value("${login.config.expiration}")
    private long JWT_EXPIRATION;
    @Value("${login.config.refresh-token-expiration}")
    private long REFRESH_EXPIRATION;

    public String extractUsername(String token) {
        return extractClaim(token, DecodedJWT::getSubject);
    }

    public <T> T extractClaim(String token, Function<DecodedJWT, T> claimsResolver) {
        final DecodedJWT decodedJWT = extractAllClaims(token);
        return claimsResolver.apply(decodedJWT);
    }

    private DecodedJWT extractAllClaims(String token) {
        return JWT.require(getSignInKey())
                .build()
                .verify(token);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(userDetails, null);
    }

    public String generateToken(UserDetails userDetails, String extraClaims) {
        return buildToken(userDetails, JWT_EXPIRATION, extraClaims);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return buildToken(userDetails, REFRESH_EXPIRATION, null);
    }

    private String buildToken(UserDetails userDetails, long expiration, String extraClaims) {
        return JWT.create()
                .withSubject(userDetails.getUsername())
                .withClaim("role", extraClaims)
                .withIssuer("auth-app")
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + expiration))
                .sign(getSignInKey());
    }

    private Algorithm getSignInKey() {
        return Algorithm.HMAC256(JWT_KEY);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, DecodedJWT::getExpiresAt);
    }

}
