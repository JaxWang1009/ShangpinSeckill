package com.ecommerce.common.security;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson2.JSON;
import com.ecommerce.common.context.UserContext;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类
 * 用于生成和解析JWT token
 * 
 * @author ecommerce-team
 * @since 1.0.0
 */
@Slf4j
@Component
public class JwtTokenUtil {
    
    /**
     * JWT密钥
     */
    @Value("${jwt.secret:ecommerce-secret}")
    private String secret;
    
    /**
     * token过期时间（毫秒）
     */
    @Value("${jwt.expiration:7200000}")
    private Long expiration;
    
    /**
     * 生成token
     * 
     * @param userContext 用户上下文
     * @return JWT token
     */
    public String generateToken(UserContext userContext) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userContext.getUserId());
        claims.put("username", userContext.getUsername());
        claims.put("role", userContext.getRole());
        claims.put("authorities", JSON.toJSONString(userContext.getAuthorities()));
        
        return createToken(claims, userContext.getUsername());
    }
    
    /**
     * 创建token
     * 
     * @param claims 声明信息
     * @param subject 主题
     * @return JWT token
     */
    private String createToken(Map<String, Object> claims, String subject) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);
        
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }
    
    /**
     * 从token中获取用户名
     * 
     * @param token JWT token
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }
    
    /**
     * 从token中获取过期时间
     * 
     * @param token JWT token
     * @return 过期时间
     */
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }
    
    /**
     * 从token中获取指定声明
     * 
     * @param token JWT token
     * @param claimsResolver 声明解析器
     * @return 声明值
     */
    public <T> T getClaimFromToken(String token, java.util.function.Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }
    
    /**
     * 从token中获取所有声明
     * 
     * @param token JWT token
     * @return 所有声明
     */
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }
    
    /**
     * 检查token是否过期
     * 
     * @param token JWT token
     * @return 是否过期
     */
    public Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }
    
    /**
     * 验证token
     * 
     * @param token JWT token
     * @param username 用户名
     * @return 是否有效
     */
    public Boolean validateToken(String token, String username) {
        final String tokenUsername = getUsernameFromToken(token);
        return (username.equals(tokenUsername) && !isTokenExpired(token));
    }
    
    /**
     * 从token中解析用户上下文
     * 
     * @param token JWT token
     * @return 用户上下文
     */
    public UserContext getUserContextFromToken(String token) {
        try {
            Claims claims = getAllClaimsFromToken(token);
            UserContext userContext = new UserContext();
            userContext.setUserId(Long.valueOf(claims.get("userId").toString()));
            userContext.setUsername(claims.get("username").toString());
            userContext.setRole(claims.get("role").toString());
            userContext.setAuthorities(JSON.parseArray(claims.get("authorities").toString(), String.class).toArray(new String[0]));
            return userContext;
        } catch (Exception e) {
            log.error("解析token失败: {}", e.getMessage());
            return null;
        }
    }
    
    /**
     * 生成UUID作为token key
     * 
     * @return UUID
     */
    public String generateTokenKey() {
        return IdUtil.fastSimpleUUID();
    }
} 