package com.kingston.jforgame.admin.security;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.util.DigestUtils;

import com.kingston.jforgame.admin.user.service.UserService;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	UserService userService;


//	@Bean
//	public CorsConfigurationSource corsConfigurationSource() {
//		final CorsConfiguration configuration = new CorsConfiguration();
//		//指定允许跨域的请求(*所有)：http://wap.ivt.guansichou.com
//		configuration.setAllowedOrigins(Arrays.asList("*"));
//		configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH"));
//		// setAllowCredentials(true) is important, otherwise:
//		// The value of the 'Access-Control-Allow-Origin' header in the response must not be the wildcard '*' when the request's credentials mode is 'include'.
//		configuration.setAllowCredentials(true);
//		// setAllowedHeaders is important! Without it, OPTIONS preflight request
//		// will fail with 403 Invalid CORS request
//		configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "X-User-Agent", "Content-Type"));
//		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//		source.registerCorsConfiguration("/**", configuration);
//		return source;
//	}

	@Bean
	public CorsFilter corsFilter() {
		final UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new      UrlBasedCorsConfigurationSource();
		final CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.setAllowCredentials(true);
		corsConfiguration.addAllowedOrigin("*");
		corsConfiguration.addAllowedHeader("*");
		corsConfiguration.addAllowedMethod("*");
		urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
		return new CorsFilter(urlBasedCorsConfigurationSource);
	}
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		auth.userDetailsService(userService).passwordEncoder(new PasswordEncoder() {
//			@Override
//			public String encode(CharSequence charSequence) {
//				return DigestUtils.md5DigestAsHex(charSequence.toString().getBytes());
//			}
//
//			@Override
//			public boolean matches(CharSequence charSequence, String s) {
//				return s.equals(DigestUtils.md5DigestAsHex(charSequence.toString().getBytes()));
//			}
//		});
		// 使用自定义身份验证组件
		auth.authenticationProvider(new JwtAuthenticationProvider(userService));
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry
				= http.authorizeRequests();
		registry.requestMatchers(CorsUtils::isPreFlightRequest).permitAll();//让Spring security放行所有preflight request
//		http.authorizeRequests().anyRequest().authenticated(); // 其他的路径都是登录后即可访问
		http.csrf().disable()
				.cors();
//				.and().formLogin().loginPage("/login_page").successHandler(new AuthenticationSuccessHandler() {
//					@Override
//					public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
//							HttpServletResponse httpServletResponse, Authentication authentication)
//							throws IOException, ServletException {
//						httpServletResponse.setContentType("application/json;charset=utf-8");
//						PrintWriter out = httpServletResponse.getWriter();
//						out.write("{\"status\":\"success\",\"msg\":\"登录成功\"}");
//						out.flush();
//						out.close();
//					}
//				}).failureHandler(new AuthenticationFailureHandler() {
//					@Override
//					public void onAuthenticationFailure(HttpServletRequest httpServletRequest,
//							HttpServletResponse httpServletResponse, AuthenticationException e)
//							throws IOException, ServletException {
//						httpServletResponse.setContentType("application/json;charset=utf-8");
//						PrintWriter out = httpServletResponse.getWriter();
//						out.write("{\"status\":\"error\",\"msg\":\"登录失败\"}");
//						out.flush();
//						out.close();
//					}
//				}).loginProcessingUrl("/login").usernameParameter("userName").passwordParameter("password").permitAll()
//				.and().logout().permitAll().and().csrf().disable().exceptionHandling();


		 http.authorizeRequests().antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
				// web jars
				.antMatchers("/webjars/**").permitAll()
				// 查看SQL监控（druid）
				.antMatchers("/druid/**").permitAll()
				// 首页和登录页面
				.antMatchers("/").permitAll()
				.antMatchers("/login").permitAll()
				 .antMatchers("/server").hasAnyRole("ADMIN")
				// 其他所有请求需要身份认证
				.anyRequest().authenticated();
		// 退出登录处理器
		http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
		// token验证过滤器
		http.addFilterBefore(new JwtAuthenticationFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class);
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/img/**", "/index.html", "/static/**");
	}

}