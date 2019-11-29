package com.learning.shrio.cache;

import com.learning.shrio.session.JedisUtils;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

import java.util.Collection;
import java.util.Set;

/**
 * redis缓存实现类
 * @param <K>
 * @param <V>
 */
@Component
public class RedisCache<K, V>  implements Cache<K, V> {
    @Autowired
    private JedisUtils jedisUtils;

    private static final String CACHE_PREFIX = "shrio-cache:";

    private byte[] getKey(K key){
        if(key instanceof String){
            return (CACHE_PREFIX + key).getBytes();
        }
        return SerializationUtils.serialize(key);
    }

    @Override
    public V get(K k) throws CacheException {
        System.out.println("从redis中获取权限数据。");
        byte[] value = jedisUtils.getValue(getKey(k));
        if (value != null) return (V)SerializationUtils.deserialize(value);
        return null;
    }

    @Override
    public V put(K k, V v) throws CacheException {
        byte[] key = getKey(k);
        byte[] value = SerializationUtils.serialize(v);
        jedisUtils.set(key, value);
        jedisUtils.expire(key, 600);
        return v;
    }

    @Override
    public V remove(K k) throws CacheException {
        byte[] key = getKey(k);
        byte[] value = jedisUtils.getValue(key);
        jedisUtils.delete(key);
        if (value != null) return (V)SerializationUtils.deserialize(value);
        return null;
    }

    @Override
    public void clear() throws CacheException {
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public Set<K> keys() {
        return null;
    }

    @Override
    public Collection<V> values() {
        return null;
    }
}
