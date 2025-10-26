package com.development.practice.redis.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        /*
          Lettuce(Redis와 연결시켜주는 라이브러리)를 활용해 Redis 연결을 관리하는 객체 생성
          Redis 서버에 대한 정보(host, port)를 설정
         */
        return new LettuceConnectionFactory(host, port);
    }
}
