/*----------------------------------------------------------------------------*/
/* Source File:   REDISCACHECONFIG.KT                                         */
/* Copyright (c), 2023 The Musketeers                                         */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Jul.06/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.themusketeers.sbnative.common.config

import com.themusketeers.sbnative.common.consts.GlobalConstants.MOVIE_RECORD_CACHE_KEY
import com.themusketeers.sbnative.service.RedisHashCacheService
import com.themusketeers.sbnative.service.RedisRegionCacheService
import com.themusketeers.sbnative.service.intr.RedisCacheService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.data.redis.core.RedisTemplate

/**
 * Defines the beans necessary to activate [RedisCacheService] implementation
 * by using a profile.
 *
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
@Configuration
class RedisCacheConfig {
    @Profile("redis-region")
    @Bean("redisMovieRecordCacheService")
    fun redisRegionCacheService(redisTemplate: RedisTemplate<String, Any>): RedisCacheService<String, Any> {
        return RedisRegionCacheService(MOVIE_RECORD_CACHE_KEY, redisTemplate)
    }

    @Profile("redis-hash")
    @Bean("redisMovieRecordCacheService")
    fun redisHashCacheService(redisTemplate: RedisTemplate<String, Any>): RedisCacheService<String, Any> {
        return RedisHashCacheService(MOVIE_RECORD_CACHE_KEY, redisTemplate)
    }
}
