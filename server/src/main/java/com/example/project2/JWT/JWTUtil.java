package com.example.project2.JWT;
import java.util.Date;
import java.util.Map;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

/**
 * JWT utility which will be use on all the endpoint
 */
public class JWTUtil {
    private static final String SECRET_KEY = "013f6975c4498c9e2266130e4325126d24f85404c4f3b8f499d394fea8b74d61";

    /**
     * Generate the token based on the username
     * And signed it with secret key
     * @param username The username which will be use in token generation
     * @return The token string
     */
    public static String generateToken(String username, String id){
        return Jwts.builder()
            .setSubject(username)
            .setId(id)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour
            .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
            .compact();
    }

    /**
     * Generate the token based on the username and claims
     * This is an overload method that allow you to pass in custom claims
     * @param username The username which will be use in the token generation
     * @param claims The custom claims that contain other values
     * @return The token string
     */
    public static String generateToken(String username, Map<String, Object> claims){
        return Jwts.builder()
            .setSubject(username)
            .setClaims(claims)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour
            .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
            .compact();
    }

    /**
     * Parse the token to extract information
     * @param token The token to extract information
     * @return The Claims
     */
    public static Claims parseToken(String token){
        return Jwts.parserBuilder()
            .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    /**
     * Check if the token is valid
     * @param token The token to extract information
     * @return True or false
     */
    public static boolean isValid(String token)
    {
        try
        {
            Claims claims = parseToken(token);

            //Check if the time is expired
            if(claims.getExpiration().getTime() < (new Date()).getTime())
            {
                return false;
            }

            //Check if the subject is null
            if(claims.getSubject() == null)
            {
                //TODO Add username check here
                return false;
            }

            //Check if the ID is null or less than 0
            if(claims.getId() == null || Integer.parseInt(claims.getId()) <= 0)
            {
                //TODO Add userID check here
                return false;
            }

            return true;
        }
        catch(Exception e)
        {
            return false;
        }
    }
}
