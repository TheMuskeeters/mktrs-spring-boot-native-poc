/*----------------------------------------------------------------------------*/
/* Source File:   REDISCONFIG.KT                                              */
/* Copyright (c), 2023 The Musketeers                                         */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Jul.06/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.themusketeers.sbnative.common.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer

/**
 * Configure Redis Template type. Used in communicating with Redis Server.
 *
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
@Configuration
class RedisConfig {
    /**
     * Registers a bean to handle all Redis operations.
     *
     * @param redisConnectionFactory A reference bean indicating the definition to the Redis Connection Factory representation.
     * @return A reference bean to a [RedisTemplate]
     */
    @Bean
    fun redisTemplate(redisConnectionFactory: RedisConnectionFactory): RedisTemplate<*, *> {
        val redisTemplate = RedisTemplate<String, Any>()
        val stringRedisSerializer = StringRedisSerializer()
        val serializer = GenericJackson2JsonRedisSerializer()

        redisTemplate.connectionFactory = redisConnectionFactory
        redisTemplate.valueSerializer = serializer
        redisTemplate.hashValueSerializer = serializer
        redisTemplate.keySerializer = stringRedisSerializer
        redisTemplate.hashKeySerializer = stringRedisSerializer
        redisTemplate.setEnableTransactionSupport(true)

        return redisTemplate
    }
}
