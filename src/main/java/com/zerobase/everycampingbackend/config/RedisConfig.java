package com.zerobase.everycampingbackend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@RequiredArgsConstructor
@EnableRedisRepositories
public class RedisConfig {

    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private int port;

//    @Value("${spring.redis.sentinel.master}")
//    private String master;
//    @Value("${spring.redis.sentinel.port1}")
//    private int port1;
//    @Value("${spring.redis.sentinel.port2}")
//    private int port2;
//    @Value("${spring.redis.sentinel.port3}")
//    private int port3;

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        StringRedisSerializer serializer = new StringRedisSerializer();

        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(serializer);
        redisTemplate.setValueSerializer(serializer);
        redisTemplate.setHashKeySerializer(serializer);
        redisTemplate.setHashValueSerializer(serializer);

        return redisTemplate;
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactoryLocal() {
        RedisStandaloneConfiguration conf = new RedisStandaloneConfiguration();
        conf.setHostName(host);
        conf.setPort(port);
        return new LettuceConnectionFactory(conf);
    }

//    @Bean
//    @Profile("prod")
//    public RedisConnectionFactory redisConnectionFactoryProd() {
//        RedisSentinelConfiguration conf = new RedisSentinelConfiguration()
//            .master(master)
//            .sentinel(host, port1)
//            .sentinel(host, port2)
//            .sentinel(host, port3);
//        return new LettuceConnectionFactory(conf);
//    }
}