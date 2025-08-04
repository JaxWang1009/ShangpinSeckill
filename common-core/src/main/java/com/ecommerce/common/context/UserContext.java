package com.ecommerce.common.context;

import lombok.Data;

/**
 * 用户上下文类
 * 使用ThreadLocal存储当前用户信息，实现用户操作留痕
 * 
 * @author ecommerce-team
 * @since 1.0.0
 */
@Data
public class UserContext {
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 用户角色
     */
    private String role;
    
    /**
     * 用户权限列表
     */
    private String[] authorities;
    
    /**
     * ThreadLocal存储用户上下文
     */
    private static final ThreadLocal<UserContext> USER_CONTEXT = new ThreadLocal<>();
    
    /**
     * 设置用户上下文
     * 
     * @param userContext 用户上下文
     */
    public static void setUserContext(UserContext userContext) {
        USER_CONTEXT.set(userContext);
    }
    
    /**
     * 获取用户上下文
     * 
     * @return 用户上下文
     */
    public static UserContext getUserContext() {
        return USER_CONTEXT.get();
    }
    
    /**
     * 获取当前用户ID
     * 
     * @return 用户ID
     */
    public static Long getCurrentUserId() {
        UserContext context = getUserContext();
        return context != null ? context.getUserId() : null;
    }
    
    /**
     * 获取当前用户名
     * 
     * @return 用户名
     */
    public static String getCurrentUsername() {
        UserContext context = getUserContext();
        return context != null ? context.getUsername() : null;
    }
    
    /**
     * 获取当前用户角色
     * 
     * @return 用户角色
     */
    public static String getCurrentUserRole() {
        UserContext context = getUserContext();
        return context != null ? context.getRole() : null;
    }
    
    /**
     * 获取当前用户权限
     * 
     * @return 用户权限列表
     */
    public static String[] getCurrentUserAuthorities() {
        UserContext context = getUserContext();
        return context != null ? context.getAuthorities() : new String[0];
    }
    
    /**
     * 清除用户上下文
     */
    public static void clear() {
        USER_CONTEXT.remove();
    }
} 