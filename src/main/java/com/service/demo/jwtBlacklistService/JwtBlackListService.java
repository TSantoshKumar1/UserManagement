package com.service.demo.jwtBlacklistService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class JwtBlackListService {

     @Autowired
     private StringRedisTemplate redisTemplate;
     private static final String BLACKLIST_PREFIX = "blacklist:";  // Key prefix for blacklisted tokens

        // Add token to blacklist
        public void blacklistToken(String token) {
            redisTemplate.opsForValue().set(BLACKLIST_PREFIX + token, "true");
        }

        // Check if the token is blacklisted
        public boolean isTokenBlacklisted(String token) {
            return redisTemplate.hasKey(BLACKLIST_PREFIX + token);
        }
    }

