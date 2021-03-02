package com.cn.image.context;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.cn.aqb.cache.redis.ShardedRedisClient;
import com.cn.aqb.cache.serialize.ProtostuffSerializer;

/**
 * @Desc 
 * @author magiczrl@foxmail.com
 * @date 2020年9月14日 下午3:55:23
 */
@Configuration
public class Dal {

    /**
     * redis
     * 
     * @return
     */
    @Bean(name = "redisClient", initMethod = "init", destroyMethod = "close")
    @ConfigurationProperties(prefix = "redis")
    public ShardedRedisClient cacheClient() {
        ShardedRedisClient redisClient = new ShardedRedisClient();
        redisClient.setSerializer(new ProtostuffSerializer());
        return redisClient;
    }

}

/**
 * Revision history
 * -------------------------------------------------------------------------
 * 
 * Date Author Note
 * -------------------------------------------------------------------------
 * 2019年5月28日 Z.R.L create
 */
