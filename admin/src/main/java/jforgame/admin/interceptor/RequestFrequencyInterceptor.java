package jforgame.admin.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
@Slf4j
public class RequestFrequencyInterceptor implements HandlerInterceptor {

    private final ConcurrentMap<String, Integer> requestCounts = new ConcurrentHashMap<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        log.info("requestUrl:" + request.getRequestURI() + ",host:" + request.getRemoteAddr());
        String url = request.getRequestURI();
        requestCounts.put(url, requestCounts.getOrDefault(url, 0) + 1);
        return true;
    }


}