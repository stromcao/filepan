package com.filepan.service;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {
    private static final String SECRET_KEY = "filepan";

    public String generateToken(String username) {
        System.out.println("generate Key:" + SECRET_KEY);
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 60)) // 60小时
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public String validateTokenAndGetUsername(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject();
        } catch (JwtException | IllegalArgumentException e) {
            // token 无效或过期
            return null;
        }
    }
    // 验证 JWT 并提取用户 ID
    public Integer validateTokenAndGetUserId(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
            return Integer.parseInt(claims.getSubject());
        } catch (JwtException | IllegalArgumentException e) {
            // 打印错误日志或抛出异常
            return null; // 表示 token 无效或过期
        }
    }
    // 新增：验证 JWT 并返回 Claims
    public Claims validateToken(String token) {
//        try {
//            System.out.println("Validate Token: "+ token);
//            System.out.println("claims1111: " + Jwts.parser()
//                    .setSigningKey(SECRET_KEY)
//                    .parseClaimsJws(token));
//            return Jwts.parser()
//                    .setSigningKey(SECRET_KEY)
//                    .parseClaimsJws(token)
//                    .getBody();
//        } catch (JwtException | IllegalArgumentException e) {
//            // 打印错误日志或记录异常
//            System.out.println("Token validation failed: " + e.getMessage());
//            // 打印错误日志或记录异常
//            return null; // 返回 null 表示 token 无效或过期
//        }
        try {
            System.out.println("Validate Token: " + token);
            System.out.println("SECRET_KEY: " + SECRET_KEY); // 不建议生产环境中打印密钥

            Jws<Claims> parsedToken = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token);

            System.out.println("Parsed Token Header: " + parsedToken.getHeader());
            System.out.println("Parsed Token Signature: " + parsedToken.getSignature());

            return parsedToken.getBody();
        } catch (JwtException | IllegalArgumentException e) {
            System.out.println("Token validation failed: " + e.getMessage());
            e.printStackTrace(); // 打印完整的堆栈跟踪帮助调试
            return null;
        }
    }
}
