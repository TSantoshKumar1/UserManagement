package com.service.demo.serviceImpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.demo.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    @Autowired
    private RedisTemplate redisTemplate;
    private Logger logger = LoggerFactory.getLogger(RedisService.class);


    public <T> T get(String key, Class<T> User) {
        try {
            Object o = redisTemplate.opsForValue().get(key);
            if (o == null) {
                logger.warn("Key [{}] not found in Redis", key);
                return null;
            }
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(o.toString(), User);
        } catch (Exception e) {

            logger.info("excption", e);
            return null;
        }
    }

    public void set(String key , Object o ,Long ttl){
        try {
            ObjectMapper mapper =  new ObjectMapper();
            String json = mapper.writeValueAsString(o);
            redisTemplate.opsForValue().set(key,json,ttl , TimeUnit.SECONDS);
        } catch (JsonProcessingException e) {
           logger.info("exception",e);
        }

    }

}
