/*----------------------------------------------------------------------------*/
/* Source File:   REDISHASHCACHESERVICE.JAVA                                  */
/* Copyright (c), 2023 The Musketeers                                         */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Jun.27/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.themusketeers.sbnative.service

import com.themusketeers.sbnative.common.consts.GlobalConstants.INT_ZERO
import com.themusketeers.sbnative.service.intr.RedisCacheService
import kotlin.collections.Map.Entry
import java.util.stream.Collectors
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ScanOptions

/**
 * Implements a Hash Redis Cache Service. All the &lt;K&gt; beans are assumed to be of `String` type.
 *
 * @param <K> Indicates the Redis key type (usually a String) to work with.
 * @param <V> Indicates the Redis Value type to work with.
 * @param cacheName     Represents the 'Cache Hash' name. It is used as part of a key such as @{code item}.
 * @param redisTemplate redisTemplate References the abstraction to communicate to a Redis server.
 * @see AbstractBaseRedisCacheService
 * @see RedisCacheService
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
class RedisHashCacheService<K, V>(cacheName: String, redisTemplate: RedisTemplate<K, V>) : AbstractBaseRedisCacheService<K, V>(cacheName, redisTemplate), RedisCacheService<K, V> {

    override fun exists(key: K): Boolean = redisTemplate.opsForHash<Any, Any>().hasKey(cacheName as K, key as Any)
    override fun count(): Long = redisTemplate.opsForHash<Any, Any>().size(cacheName as K)
    override fun count(keyPattern: String?): Long = multiRetrieveList(keyPattern).size.toLong()
    override fun insert(key: K, info: V) = redisTemplate.opsForHash<Any, Any>().put(cacheName as K, key as Any, info as Any)
    override fun multiInsert(map: Map<K, V>) = redisTemplate.opsForHash<Any, Any>().putAll(cacheName as K, map)
    override fun retrieve(key: K): V = redisTemplate.opsForHash<Any, Any>().get(cacheName as K, key as Any) as V

    override fun multiRetrieveKeyList(keyPattern: String?): List<K> {
        val hOps = redisTemplate.opsForHash<Any, Any>()
        val scanOptions = ScanOptions.scanOptions().match(verifyKeyPattern(keyPattern)).build()

        hOps.scan(cacheName as K, scanOptions).use { mapCursor ->
            return mapCursor
                .stream()
                .map<K>(Function<Entry<Any, Any>, K> { cursorItem: Entry<Any, Any> -> (cursorItem as Entry<K, V>).key })
                .toList()
        }
    }

    override fun multiRetrieveList(keyPattern: String?): List<V> {
        val hOps = redisTemplate.opsForHash<Any, Any>()
        val scanOptions = ScanOptions.scanOptions().match(verifyKeyPattern(keyPattern)).build()

        hOps.scan(cacheName as K, scanOptions).use { mapCursor ->
            return mapCursor
                .stream()
                .map<V>(Function<Entry<Any, Any>, V> { cursorItem: Entry<Any, Any> -> (cursorItem as Entry<K, V>).value })
                .toList()
        }
    }

    override fun multiRetrieveList(keys: Collection<K>): List<V> {
        return redisTemplate
            .opsForHash<Any, Any>()
            .multiGet(
                cacheName as K,
                keys.stream()
                    .map { key: K -> key as Any }
                    .toList()
            )
            .stream()
            .map { value: Any -> value as V }
            .toList()
    }

    override fun multiRetrieveMap(): Map<K, V> = redisTemplate.opsForHash<Any, Any>().entries(cacheName as K) as Map<K, V>

    override fun multiRetrieveMap(keyPattern: String?): Map<K, V> {
        val hOps = redisTemplate.opsForHash<Any, Any>()
        val scanOptions = ScanOptions.scanOptions().match(verifyKeyPattern(keyPattern)).build()

        hOps.scan(cacheName as K, scanOptions).use { mapCursor ->
            return mapCursor
                .stream()
                .map<Map.Entry<K, V>?>(Function<Map.Entry<Any, Any>, Entry<K, V>?> { cursorItem: Entry<Any, Any>? -> cursorItem as Entry<K, V>? })
                .collect<Map<K, V>, Any>(Collectors.toMap<Entry<K, V>?, K, V>(Function<Entry<K, V>?, K> { Entry.key }, Function<Entry<K, V>?, V> { java.util.Map.Entry.value }))
        }
    }

    override fun multiRetrieveMap(keys: Collection<K>): Map<K, V> = fillCacheMapUsing(ArrayList(keys), multiRetrieveList(keys))
    override fun delete(key: K): Boolean = redisTemplate.opsForHash<Any, Any>().delete(cacheName as K, key) != INT_ZERO.toLong()
    override fun delete(keys: Collection<K>): Long = redisTemplate.opsForHash<Any, Any>().delete(cacheName as K, *keys.toTypedArray())
}
