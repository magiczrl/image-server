package com.cn.image.context;

import com.cn.aqb.cache.redis.RedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

/**
 * The type Cache wrapper.
 */
@Component
public class CacheWrapper {

    private static final String REDIS_KEY_PREFIX = "IMG_SERVER#";

    @Autowired
    private RedisClient redisClient;

    /**
     * 统一key
     * @param key
     * @return
     */
    private String wrapKey(String key) {
        return REDIS_KEY_PREFIX.concat(":").concat(key);
    }

    /**
     * Put.
     *
     * @param <T> the type parameter
     * @param key   the key
     * @param val   the val
     */
    public <T extends Serializable> void put(String key, T val) {
        redisClient.put(wrapKey(key), val);
    }

    /**
     * Put.
     *
     * @param <T> the type parameter
     * @param key   the key
     * @param val   the val
     * @param seconds   the seconds
     */
    public <T extends Serializable> void put(String key, T val, int seconds) {
        redisClient.put(wrapKey(key), val, seconds);
    }

    /**
     * Put if absent boolean.
     *
     * @param <T> the type parameter
     * @param key   the key
     * @param val   the val
     * @param seconds   the seconds
     * @return the boolean
     */
    public <T extends Serializable> boolean putIfAbsent(String key, T val, int seconds) {
        return redisClient.putIfAbsent(wrapKey(key), val, seconds);
    }

    /**
     * GeT val.
     *
     * @param <T> the type parameter
     * @param key   the key
     * @return the t
     */
    public <T extends Serializable> T get(String key) {
        return redisClient.get(wrapKey(key));
    }

    /**
     * GeT val.
     *
     * @param <T>    the type parameter
     * @param key      the key
     * @param aClass the a class
     * @return the t
     */
    public <T extends Serializable> T get(String key, Class<T> aClass) {
        return redisClient.get(wrapKey(key), aClass);
    }

    /**
     * Delete long.
     *
     * @param key the key
     * @return the long
     */
    public long delete(String key) {
        return redisClient.delete(wrapKey(key));
    }

    /**
     * Incr long.
     *
     * @param key the key
     * @param by the by
     * @return the long
     */
    public long incr(String key, int by) {
        return redisClient.incr(wrapKey(key), by);
    }

    /**
     * Expire.
     *
     * @param key the key
     * @param seconds the seconds
     */
    public void expire(String key, int seconds) {
        redisClient.expire(wrapKey(key), seconds);
    }

    /**
     *
     * @param key
     * @param val
     * @param <T>
     */
    public <T extends Serializable> void lpush(String key, T val) {
        redisClient.lpush(wrapKey(key), val);
    }

    /**
     *
     * @param key
     * @param val
     * @param <T>
     */
    public <T extends Serializable> void rpush(String key, T val) {
        redisClient.rpush(wrapKey(key), val);
    }

    /**
     *
     * @param key
     * @param <T>
     * @return
     */
    public <T extends Serializable> T lpop(String key) {
        return redisClient.lpop(wrapKey(key));
    }

    /**
     *
     * @param key
     * @param <T>
     * @return
     */
    public <T extends Serializable> T rpop(String key) {
        return redisClient.rpop(wrapKey(key));
    }

    /**
     *
     * @param key
     * @param aClass
     * @param <T>
     * @return
     */
    public <T extends Serializable> T rpop(String key, Class<T> aClass) {
        return redisClient.rpop(wrapKey(key), aClass);
    }

    /**
     *
     * @param key
     * @param timeout
     * @param aClass
     * @param <T>
     * @return
     */
    public <T extends Serializable> T brpop(String key, int timeout, Class<T> aClass) {
        return redisClient.brpop(wrapKey(key), timeout, aClass);
    }

    /**
     *
     * @param key
     * @param index
     * @param <T>
     * @return
     */
    public <T extends Serializable> T rindex(String key, long index) {
        return redisClient.rindex(wrapKey(key), index);
    }

    /**
     *
     * @param key
     * @return
     */
    public long rindex(String key) {
        return redisClient.rindex(wrapKey(key));
    }

    /**
     *
     * @param key
     * @param field
     * @param aClass
     * @param <T>
     */
    public <T extends Serializable> void hset(String key, String field, T aClass) {
        redisClient.hset(wrapKey(key), field, aClass);
    }

    /**
     *
     * @param key
     * @param field
     * @param <T>
     * @return
     */
    public <T extends Serializable> T hget(String key, String field) {
        return redisClient.hget(wrapKey(key), field);
    }

    /**
     *
     * @param key
     * @param field
     */
    public void hdel(String key, String field) {
        redisClient.hdel(wrapKey(key), field);
    }

    /**
     *
     * @param key
     * @return
     */
    public long hlen(String key) {
        return redisClient.hlen(wrapKey(key));
    }

    /**
     *
     * @param pattern
     * @return
     */
    public Set<String> hkeys(String pattern) {
        return redisClient.keys(wrapKey(pattern));
    }

    /**
     *
     * @param key
     * @param val
     * @param <T>
     */
    public <T extends Serializable> void sadd(String key, T val) {
        redisClient.sadd(wrapKey(key), val);
    }

    /**
     *
     * @param key
     * @param <T>
     * @return
     */
    public <T extends Serializable> T spop(String key) {
        return redisClient.spop(wrapKey(key));
    }

    /**
     *
     * @param key
     * @param function
     * @param <T>
     * @param <J>
     * @return
     */
    public <T extends Serializable, J> T execute(String key, Function<J, T> function) {
        return redisClient.execute(wrapKey(key), function);
    }

    /**
     *
     * @param key
     * @param begin
     * @param end
     * @param <T>
     * @return
     */
    public <T extends Serializable> List<T> lRange(String key, long begin, long end) {
        return redisClient.lRange(wrapKey(key), begin, end);
    }

    /**
     *
     * @param key
     * @return
     */
    public Set<String> keys(String key) {
        return redisClient.keys(wrapKey(key));
    }

    /**
     *
     * @param key
     * @return
     */
    public long ttl(String key) {
        return redisClient.ttl(wrapKey(key));
    }

    /**
     *
     * @param key
     * @param timestamp
     * @return
     */
    public long expireAt(String key, long timestamp) {
        return redisClient.expireAt(wrapKey(key), timestamp);
    }
}
