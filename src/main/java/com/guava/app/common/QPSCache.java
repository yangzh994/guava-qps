package com.guava.app.common;


import java.util.HashMap;
import java.util.Map;

public class QPSCache {

    private static final Map<String, QPSPojo> LIMIT_CACHE = new HashMap<>();

    public static QPSPojo getQps(String url) {
        return LIMIT_CACHE.get(url);
    }

    public static QPSPojo putQps(QPSPojo qpsPojo) {
        return LIMIT_CACHE.put(qpsPojo.getUrl(), qpsPojo);
    }

}
