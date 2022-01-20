package com.bell.arithmetic.partner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class PService {
    @Autowired
    private StringRedisTemplate redisTemplate;




}
