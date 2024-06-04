package jforgame.admin.security;

import jforgame.admin.core.ServerAppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	ServerAppConfig serverAppConfig;

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		// 使用自定义身份验证组件
		auth.authenticationProvider(new JwtAuthenticationProvider(userDetailsService));
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// 禁用 csrf, 由于使用的是JWT，我们这里不需要csrf
		http.csrf().disable()
				.authorizeRequests()
				// 跨域预检请求
				.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
				.antMatchers("/static/**").permitAll()
				// 首页和登录页面
				.antMatchers("/").permitAll()
				.antMatchers("/login").permitAll()
				// 验证码
				.antMatchers("/captcha.jpg").permitAll()
				// 服务监控
				.antMatchers("/actuator/**").permitAll()
				// 对外提供服务(内网生效)
				.antMatchers("/admin/api/**").access(String.format("hasIpAddress('%s')",serverAppConfig.getAnonymousClientIp()))
				// 其他所有请求需要身份认证
				.anyRequest().authenticated();
		// 退出登录处理器
		http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
		// token验证过滤器
		http.addFilterBefore(new JwtAuthenticationFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class);
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}

}