package com.sasindu.springsecurity.security.jwt;


import com.sasindu.springsecurity.entities.AppUser;
import com.sasindu.springsecurity.helpers.HelperUtilMethods;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.InvalidKeyException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class JWTUtils {
    @Value("${jwt.access.secret}")
    private String accessTokenSecret;

    @Value("${jwt.access.expiration.minutes}")
    private String accessTokenExpirationMinutes;

    @Value("${jwt.refresh.secret}")
    private String refreshTokenSecret;

    @Value("${jwt.refresh.expiration.days}")
    private String refreshTokenExpirationDays;


    /**
     * Generate the access token
     *
     * @param user - The user object
     * @return The access token
     */
    public String generateAccessToken(AppUser user) {
        try {
            Map<String, Object> claims = Map.of(
                    "roles", List.of(user.getRole()),
                    "userId", user.getId(),
                    "email", user.getEmail()
            );
            long expirationTime = Long.parseLong(accessTokenExpirationMinutes) * 60 * 1000;
            return generateToken(user.getUsername(), claims, expirationTime, accessKey());
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Generate the refresh token
     *
     * @param user - The user object
     * @return The refresh token
     */
    public String generateRefreshToken(AppUser user) {
        try {
            Map<String, Object> claims = Map.of("userId", user.getId());
            long expirationTime = Long.parseLong(refreshTokenExpirationDays) * 24 * 60 * 60 * 1000;
            return generateToken(user.getUsername(), claims, expirationTime, refreshKey());
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }



    /**
     * Check if the access token is valid
     * @param token The access token
     * @return The validation status - boolean
     */
    public boolean isAccessTokenValid(String token) {
        try {
            return isTokenValid(token, accessKey());
        }
        catch(JwtException e){
            throw new JwtException(e.getMessage());
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Get the username from the access token
     * @param token The access token
     * @return The username
     */
    public String getEmailFromAccessToken(String token) {
        try {
            return extractClaimsFromToken(token, accessKey()).getSubject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Get the user id from the refresh token
     * @param token The refresh token
     * @return The user id
     */
    public Long getUserIdFromRefreshToken(String token) {
        try {
            return extractClaimsFromToken(token, refreshKey()).get("userId", Long.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Check if the refresh token is valid
     * @param token The refresh token
     * @return The validation status - boolean
     */
    public boolean isRefreshTokenValid(String token) {
        try {
            return isTokenValid(token, refreshKey());
        }
        catch (JwtException e) {
            throw new JwtException(e.getMessage());
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    //! PRIVATE METHODS >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> //


    /**
     * Generate the token
     *
     * @param subject The subject of the token
     * @param claims The claims of the token
     * @param expirationTime The expiration time of the token
     * @param key The key to be used for signing
     * @return The generated token
     */
    private String generateToken(String subject, Map<String, Object> claims, long expirationTime, SecretKey key) {
        try {
            return Jwts.builder()
                    .subject(subject)
                    .claims(claims)
                    .issuedAt(new Date(System.currentTimeMillis()))
                    .expiration(new Date(System.currentTimeMillis() + expirationTime))
                    .signWith(key)
                    .compact();
        } catch (InvalidKeyException | NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * This method used to validate the token
     *
     * @param token The token to be validated
     * @param secretKey The secret key to be used for validation
     *  @return The validation status - boolean
     */
    private boolean isTokenValid(String token, SecretKey secretKey) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parse(token);
            return true;
        } catch (ExpiredJwtException | MalformedJwtException | SecurityException |IllegalArgumentException e) {
            throw new JwtException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    /**
     * Create a Key object from the access secret
     *
     * @return Key object
     */
    private SecretKey accessKey() {
        return Keys.hmacShaKeyFor(HelperUtilMethods.hexStringToByteArray(accessTokenSecret));
    }


    /**
     * Create a Key object from the refresh secret
     *
     * @return Key object
     */
    private SecretKey refreshKey() {
        return Keys.hmacShaKeyFor(HelperUtilMethods.hexStringToByteArray(refreshTokenSecret));
    }


    /**
     * Extract the username from the access token
     *
     * @param token The access token
     * @return The username
     */
    private Claims extractClaimsFromToken(String token, SecretKey key) {
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException e) {
            throw new JwtException(e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }

}
