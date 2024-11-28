package com.filepan.config;

import com.filepan.filter.JwtAuthenticationFilter;
import com.filepan.filter.PrivAuthenticationFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final PrivAuthenticationFilter privAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter,
                          PrivAuthenticationFilter privAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.privAuthenticationFilter = privAuthenticationFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 添加无状态session配置
                .and()
                .authorizeRequests()
                // 白名单路径
                .antMatchers(
                        "/user/login",
                        "/user/register",
                        "/swagger-ui/**",  // Swagger UI路径
                        "/v3/api-docs/**",  // OpenAPI路径
                        "/error"            // 错误页面
                ).permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(privAuthenticationFilter, JwtAuthenticationFilter.class)
                .exceptionHandling();
    }

}