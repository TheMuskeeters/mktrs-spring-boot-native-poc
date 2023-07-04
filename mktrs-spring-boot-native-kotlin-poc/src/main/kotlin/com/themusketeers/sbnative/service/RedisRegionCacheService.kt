/*----------------------------------------------------------------------------*/
/* Source File:   REDISREGIONCACHESERVICE.JAVA                                */
/* Copyright (c), 2023 The Musketeers                                         */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Jun.27/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.themusketeers.sbnative.service

import com.themusketeers.sbnative.service.intr.RedisCacheService
import java.util.stream.Collectors
import org.apache.commons.collections4.CollectionUtils
import org.apache.commons.lang3.StringUtils
import org.springframework.data.redis.core.RedisTemplate

/**
 * Implements a Region Redis Cache Service. All the &lt;K&gt; beans are assumed to be of `String` type, but
 * they are manipulated in order to include the `cacheRegion` prefix to each usage of them to actually refer to
 * all the Key/Value pairs stored in the Redis Store.
 *
 * @param <K> Indicates the Redis key type (usually a String) to work with.
 * @param <V> Indicates the Redis Value type to work with.
 * @param cacheName     Represents the 'Cache Region' name. It is used as part of a key such as `myregion.item`.
 * @param redisTemplate References the abstraction to communicate to a Redis server.
 * @see AbstractBaseRedisCacheService
 * @see org.springframework.data.redis.core.RedisTemplate
 * @author COQ - Carlos Adolfo Ortiz Q.
 *
 * @see RedisCacheService
 */
class RedisRegionCacheService<K, V>(cacheName: String, redisTemplate: RedisTemplate<K, V>) : AbstractBaseRedisCacheService<K, V>(cacheName, redisTemplate), RedisCacheService<K, V> {

    override fun exists(key: K): Boolean = redisTemplate.hasKey(buildRegionKey(key))
    override fun count(): Long = count(StringUtils.EMPTY)
    override fun count(keyPattern: String?): Long = CollectionUtils.emptyIfNull(redisTemplate.keys(buildRegionKeyPattern(keyPattern))).size.toLong()
    override fun insert(key: K, info: V) = redisTemplate.opsForValue().set(buildRegionKey(key), info)
    override fun multiInsert(map: Map<K, V>) = redisTemplate.opsForValue().multiSet(transformKeyForRegionInMap(map))
    override fun retrieve(key: K): V? = redisTemplate.opsForValue().get(buildRegionKey(key) as Any)
    override fun multiRetrieveKeyList(keyPattern: String?): List<K> =
        removeCacheRegion(
            CollectionUtils.emptyIfNull(redisTemplate.keys(buildRegionKeyPattern(keyPattern)))
                .stream()
                .collect(Collectors.toList())
        )

    override fun multiRetrieveList(keyPattern: String?): List<V> =
        redisTemplate
            .opsForValue()
            .multiGet(CollectionUtils.emptyIfNull(redisTemplate.keys(buildRegionKeyPattern(keyPattern))))!!

    override fun multiRetrieveList(keys: Collection<K>): List<V> =
        redisTemplate
            .opsForValue()
            .multiGet(transformKeyForRegionInList(keys))!!

    override fun multiRetrieveMap(): Map<K, V> = multiRetrieveMap(StringUtils.EMPTY)

    override fun multiRetrieveMap(keyPattern: String?): Map<K, V> {
        val keys = CollectionUtils.emptyIfNull(redisTemplate.keys(buildRegionKeyPattern(keyPattern)))
            .stream()
            .collect(Collectors.toList())
        val values = redisTemplate.opsForValue().multiGet(keys)

        return fillCacheMapRegionUsing(keys, values!!)
    }

    override fun multiRetrieveMap(keys: Collection<K>): Map<K, V> {
        val keysInRegion: List<K> = transformKeyForRegionInList(keys)
        val values = multiRetrieveList(keys)

        return fillCacheMapRegionUsing(keysInRegion, values)
    }

    override fun delete(key: K): Boolean = redisTemplate.delete(buildRegionKey(key))
    override fun delete(keys: Collection<K>): Long = redisTemplate.delete(transformKeyForRegionInList(keys))
}
