package com.example.booksapi.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager(
            @Value("${books.cache.ttl-seconds:300}") long ttlSeconds,
            @Value("${books.cache.max-size:500}") long maxSize) {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("bookSearches");
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(Duration.ofSeconds(ttlSeconds))
                .maximumSize(maxSize)
                .recordStats());
        return cacheManager;
    }
}
