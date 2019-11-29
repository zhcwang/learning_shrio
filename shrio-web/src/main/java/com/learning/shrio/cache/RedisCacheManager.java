package com.learning.shrio.cache;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 权限信息缓存管理器
 */
public class RedisCacheManager implements CacheManager {
    /**
     * redis权限缓存，等同于一个map
     */
    @Autowired
    private RedisCache redisCache;

    @Override
    public <K, V> Cache<K, V> getCache(String s) throws CacheException {
        System.out.println("get cache:" + s);
        return redisCache;
    }
}
