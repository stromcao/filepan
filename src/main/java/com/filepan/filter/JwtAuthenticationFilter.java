package com.filepan.filter;

import com.filepan.service.JwtService;
import com.filepan.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        if (path.equals("/user/login")) {
            // 跳过登录路径，不进行 JWT 验证
            filterChain.doFilter(request, response);
            return;
        }

        // 从请求头中获取 JWT
        String authorizationHeader = request.getHeader("Authorization");
        System.out.println("authorizationHeader: " + authorizationHeader);
        // 检查 JWT 是否存在，并且使用 Bearer 作为前缀
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            System.out.println("token: " + token);
            try {
                // 验证 JWT
                Claims claims = jwtService.validateToken(token);
                System.out.println("claims :" + claims);
                // 将 JWT 中的用户信息保存到请求的上下文中 (如设置到 SecurityContext)
                String username = claims.get("username", String.class);
                request.setAttribute("username", username);

            } catch (Exception e) {
                // 如果 JWT 解析失败，返回 401 未授权
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                System.out.println("JWT 解析失败，返回 401 未授权");
                return;
            }
        } else {
            // Token 缺失，返回 401 未授权
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            System.out.println("Token 缺失，返回 401 未授权");
            return;
        }

        // 将请求继续传递给下一个过滤器
        filterChain.doFilter(request, response);
    }
}
