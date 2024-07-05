package jforgame.admin.security;

import jforgame.admin.core.ServerAppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    ServerAppConfig serverAppConfig;

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        return new JwtAuthenticationProvider(userDetailsService);
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http, @Autowired AuthenticationManager authenticationManage) throws Exception {
        http.authorizeHttpRequests()
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers("/static/**").permitAll()
                .requestMatchers("/").permitAll()
                .requestMatchers("/login").permitAll()
                .requestMatchers("/captcha.jpg").permitAll()
                .requestMatchers("/index.html").permitAll()
                .requestMatchers("/favicon.ico").permitAll()
                // 对服务器接口使用ip白名单
                .requestMatchers("/admin/api/**")
                .access(new WebExpressionAuthorizationManager(String.format("hasIpAddress('%s')",serverAppConfig.getAnonymousClientIp())))
                .anyRequest().authenticated();

        http.csrf().disable();
        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
        http.addFilterBefore(new JwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfiguration) throws Exception {
        return authConfiguration.getAuthenticationManager();
    }
}