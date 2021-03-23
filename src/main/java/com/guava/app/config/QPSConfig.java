package com.guava.app.config;

import com.google.common.util.concurrent.RateLimiter;
import com.guava.app.annotations.RequestRateLimiter;
import com.guava.app.common.QPSCache;
import com.guava.app.common.QPSPojo;
import com.guava.app.exception.BizException;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.Map;

@Component
public class QPSConfig implements ApplicationContextAware {

    private ApplicationContext applicationContext;


    @PostConstruct
    private void initQPS() {
        Map<String, Object> beansWithAnnotationMap = this.applicationContext.getBeansWithAnnotation(Controller.class);
        beansWithAnnotationMap.forEach((k, v) -> {
            initQPS(v);
        });
    }

    private void initQPS(Object obj) {
        Class<?> objClass = obj.getClass();
        RequestMapping annotation = objClass.getAnnotation(RequestMapping.class);
        Method[] methods = objClass.getMethods();
        initQPS(annotation, methods);
    }

    private void initQPS(RequestMapping annotation, Method[] methods) {
        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];
            RequestRateLimiter rateLimiterAnnotation = method.getAnnotation(RequestRateLimiter.class);
            if (rateLimiterAnnotation == null) continue;
            String url = createUrl(annotation, method);
            RateLimiter limiter = createQPS(rateLimiterAnnotation);
            QPSPojo qpsPojo = new QPSPojo();
            qpsPojo.setUrl(url);
            qpsPojo.setLimiter(limiter);
            qpsPojo.setMsg(rateLimiterAnnotation.msg());
            qpsPojo.setTimeout(rateLimiterAnnotation.acquireTokenTimeout());
            qpsPojo.setUnit(rateLimiterAnnotation.unit());
            QPSCache.putQps(qpsPojo);
        }
    }


    private String createUrl(RequestMapping annotation, Method method) {
        StringBuffer urlBuffer = new StringBuffer();
        if (annotation != null) urlBuffer.append(annotation.value()[0]);
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        if (requestMapping != null) {
            return urlBuffer.append(requestMapping.value()[0]).toString();
        }
        GetMapping getMapping = method.getAnnotation(GetMapping.class);
        if (getMapping != null) {
            return urlBuffer.append(getMapping.value()[0]).toString();
        }
        PostMapping postMapping = method.getAnnotation(PostMapping.class);
        if (postMapping != null) {
            return urlBuffer.append(postMapping.value()[0]).toString();
        }
        DeleteMapping deleteMapping = method.getAnnotation(DeleteMapping.class);
        if (deleteMapping != null) {
            return urlBuffer.append(deleteMapping.value()[0]).toString();
        }
        PutMapping putMapping = method.getAnnotation(PutMapping.class);
        if (getMapping != null) {
            return urlBuffer.append(putMapping.value()[0]).toString();
        }
        throw new BizException("限流初始化失败，无法方法限流的mapping！");
    }


    private RateLimiter createQPS(RequestRateLimiter rateLimiterAnnotation) {
        double qps = rateLimiterAnnotation.QPS();
        RateLimiter rateLimiter = RateLimiter.create(qps);
        return rateLimiter;
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
