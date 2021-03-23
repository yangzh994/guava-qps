package com.guava.app.annotations;


import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestRateLimiter {

    double QPS() default 0D;

    long acquireTokenTimeout() default 100;

    TimeUnit unit() default TimeUnit.MILLISECONDS;

    String msg() default "请求过于繁忙请稍后再试！";
}
