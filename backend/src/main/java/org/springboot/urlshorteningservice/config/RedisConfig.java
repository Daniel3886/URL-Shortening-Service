package org.springboot.urlshorteningservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import tools.jackson.databind.jsontype.BasicPolymorphicTypeValidator;

import java.time.Duration;

@Configuration
public class RedisConfig {

    @Value("${app.ttl}")
    private Duration cacheTtl;

    private static final GenericJacksonJsonRedisSerializer CACHE_VALUE_SERIALIZER =
            GenericJacksonJsonRedisSerializer.builder()
                    .enableDefaultTyping(
                            BasicPolymorphicTypeValidator.builder()
                                    .allowIfSubType("org.springboot.urlshorteningservice.dto")
                                    .build()
                    )
                    .build();

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(cacheTtl)
                .disableCachingNullValues()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(
                        CACHE_VALUE_SERIALIZER
                ));

        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(cacheConfiguration)
                .build();
    }
}
