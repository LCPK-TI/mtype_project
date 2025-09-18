package com.lcpk.mtype.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
	// Redis와 상호작용 하기 위한 빈 생성.
	// key, value를 다루기 쉬운 String 형태로 직렬화
	@Bean
	public StringRedisTemplate redisTemplate(RedisConnectionFactory connectionFactory) {
		// Template 객체 생성
		StringRedisTemplate template = new StringRedisTemplate(connectionFactory);
		
		// Key, Value, HashKey, HashValue을 String 으로 직렬화 방식 설정 => Redis 데이터 저장 시 깨지지 않고, CLI에서 읽을 수 있음.
		StringRedisSerializer serializer = new StringRedisSerializer();
		template.setKeySerializer(serializer);
		template.setValueSerializer(serializer);
		template.setHashKeySerializer(serializer);
		template.setHashValueSerializer(serializer);
		
		return template;
	}
}
