package com.example.mysecurity.utils;



import io.jsonwebtoken.*;
import org.joda.time.DateTime;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * JWT工具类
 */


public class JwtUtils {

    public static final String JWT_KEY_USERNAME = "username";
    public static final int EXPIRE_MINUTES = 600;

    /**
     * 私钥加密token
     */
    public static String generateToken(String username, PrivateKey privateKey, int expireMinutes) {

        return Jwts.builder()
                .claim(JWT_KEY_USERNAME, username)
                .setExpiration(DateTime.now().plusMinutes(expireMinutes).toDate())
                .signWith(SignatureAlgorithm.RS256, privateKey)
                .compact();
    }

    /**
     * 从token解析用户
     *
     * @param token
     * @param publicKey
     * @return
     * @throws Exception
     */
    public static String getUsernameFromToken(String token, PublicKey publicKey){
        String username = "";
        Claims body = null;
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token);
            body = claimsJws.getBody();
        }catch (ExpiredJwtException e){
            body = e.getClaims();
        }
        username = (String) body.get(JWT_KEY_USERNAME);
        return username;
    }
}