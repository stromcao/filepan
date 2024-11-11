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

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, PrivAuthenticationFilter privAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.privAuthenticationFilter = privAuthenticationFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // 根据需要禁用 CSRF
                .authorizeRequests()
                .antMatchers("/user/login").permitAll() // 登录路径不需要事先验证
                .antMatchers("/user/register").permitAll() // 注册路径不需要事先验证
                .antMatchers("/auth").authenticated() // 访问 /auth 需要认证
                .anyRequest().authenticated() // 其他路径需要认证
                .and()
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(privAuthenticationFilter, JwtAuthenticationFilter.class);
    }
}
