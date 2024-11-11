package com.filepan.filter;

import com.filepan.exception.AuthorizationException;
import com.filepan.service.JwtService;
import com.filepan.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class PrivAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, java.io.IOException {
        String path = request.getRequestURI();
        if (path.equals("/user/login")) {
            // 跳过登录路径，不进行 JWT 验证
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        System.out.println("PrivFilter authHeader: " + authHeader);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            return;
        }

        String token = authHeader.substring(7);
        String username = jwtService.validateTokenAndGetUsername(token);
        int userid = jwtService.validateTokenAndGetUserId(token);

        if (username == null || userService.selctPriv(userid) == null) {
            throw new AuthorizationException("无权限");
        }

        filterChain.doFilter(request, response);
    }
}
