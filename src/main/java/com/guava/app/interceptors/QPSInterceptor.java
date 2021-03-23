package com.guava.app.interceptors;

import com.google.common.util.concurrent.RateLimiter;
import com.guava.app.common.QPSCache;
import com.guava.app.common.QPSPojo;
import com.guava.app.exception.QPSException;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class QPSInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        QPSPojo qps = QPSCache.getQps(request.getRequestURI());
        if (qps == null) return true;
        RateLimiter limiter = qps.getLimiter();
        boolean tryAcquire = limiter.tryAcquire(1, qps.getTimeout(), qps.getUnit());
        if (!tryAcquire) throw new QPSException(qps.getMsg());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
