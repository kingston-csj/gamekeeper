package jforgame.admin.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

/**
 * 跨域配置
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

//    private CorsConfiguration buildConfig() {
//        CorsConfiguration corsConfiguration = new CorsConfiguration();
//        corsConfiguration.setAllowCredentials(true);  //sessionid 多次访问一致
//
//        // 允许访问的客户端域名
//        List<String> allowedOriginPatterns = new ArrayList<>();
//        allowedOriginPatterns.add("*");
//        corsConfiguration.setAllowedOriginPatterns(allowedOriginPatterns);
////        corsConfiguration.addAllowedOrigin("*"); // 允许任何域名使用
//        corsConfiguration.addAllowedHeader("*"); // 允许任何头
//        corsConfiguration.addAllowedMethod("*"); // 允许任何方法（post、get等）
//        return corsConfiguration;
//    }
//
//    @Bean
//    public CorsFilter corsFilter() {
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", buildConfig()); // 对接口配置跨域设置
//        return new CorsFilter(source);
//    }


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // 设置允许跨域的路径
                .allowedOriginPatterns("*")  // 设置允许跨域请求的源
                .allowedMethods("GET", "POST", "PUT", "DELETE")  // 设置允许的请求方法
                .allowedHeaders("*")  // 设置允许的请求头
                .maxAge(3600)
                .allowCredentials(true);  // 设置是否允许发送Cookie
    }
}