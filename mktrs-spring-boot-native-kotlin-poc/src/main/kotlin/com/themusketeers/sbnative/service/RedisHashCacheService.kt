/*----------------------------------------------------------------------------*/ /* Source File:   REDISHASHCACHESERVICE.JAVA                                  */ /* Copyright (c), 2023 The Musketeers                                         */ /*----------------------------------------------------------------------------*/ /*-----------------------------------------------------------------------------
 History
 Jun.27/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.themusketeers.sbnative.service

import com.themusketeers.sbnative.common.consts.GlobalConstants.INT_ZERO
import com.themusketeers.sbnative.service.intr.RedisCacheService
import java.util.Map.Entry
import java.util.function.Function
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
 * @author COQ - Carlos Adolfo Ortiz Q.
 * @see AbstractBaseRedisCacheService
 * @see RedisCacheService
 */
class RedisHashCacheService<K : Any, V : Any>(cacheName: String, redisTemplate: RedisTemplate<K, V>) : AbstractBaseRedisCacheService<K, V>(cacheName, redisTemplate), RedisCacheService<K, V> {
    override fun exists(key: K): Boolean {
        return redisTemplate.opsForHash<Any, Any>().hasKey(cacheName as K, key)
    }

    override fun count(): Long {
        return redisTemplate.opsForHash<Any, Any>().size(cacheName as K)
    }

    override fun count(keyPattern: String): Long {
        return java.lang.Long.valueOf(multiRetrieveList(keyPattern).size.toLong())
    }

    override fun insert(key: K, info: V) {
        redisTemplate.opsForHash<Any, Any>().put(cacheName as K, key, info)
    }

    override fun multiInsert(map: Map<K, V>) {
        redisTemplate.opsForHash<Any, Any>().putAll(cacheName as K, map)
    }

    override fun retrieve(key: K): V {
        return redisTemplate.opsForHash<Any, Any>().get(cacheName as K, key) as V
    }

    override fun multiRetrieveKeyList(keyPattern: String): List<K> {
        val hOps = redisTemplate.opsForHash<Any, Any>()
        val scanOptions = ScanOptions.scanOptions().match(verifyKeyPattern(keyPattern)).build()

        hOps.scan(cacheName as K, scanOptions).use { mapCursor ->
            return mapCursor
                .stream()
                .map<K>(Function<Entry<Any, Any>, K> { cursorItem: Entry<Any, Any> -> (cursorItem as Entry<K, V>).key })
                .collect(Collectors.toList<K>())
        }
    }

    override fun multiRetrieveList(keyPattern: String): List<V> {
        val hOps = redisTemplate.opsForHash<Any, Any>()
        val scanOptions = ScanOptions.scanOptions().match(verifyKeyPattern(keyPattern)).build()

        hOps.scan(cacheName as K, scanOptions).use { mapCursor ->
            return mapCursor
                .stream()
                .map<V>(Function<Entry<Any, Any>, V> { cursorItem: Entry<Any, Any> -> (cursorItem as Entry<K, V>).value })
                .collect(Collectors.toList<V>())
        }
    }

    override fun multiRetrieveList(keys: Collection<K>): List<V> {
        return redisTemplate
            .opsForHash<Any?, Any>()
            .multiGet(
                cacheName as K,
                keys.stream()
                    .map { key: K -> key as Any }
                    .collect(Collectors.toList())
            )
            .stream()
            .map { value: Any -> value as V }
            .collect(Collectors.toList())
    }

    override fun multiRetrieveMap(): Map<K, V> {
        return redisTemplate.opsForHash<Any, Any>().entries(cacheName as K) as Map<K, V>
    }

    override fun multiRetrieveMap(keyPattern: String): Map<K, V> {
        val hOps = redisTemplate.opsForHash<Any, Any>()
        val scanOptions = ScanOptions.scanOptions().match(verifyKeyPattern(keyPattern)).build()
        hOps.scan(cacheName as K, scanOptions).use { mapCursor ->
            return mapCursor
                .stream()
                .map<Map.Entry<K, V>?>(Function<Map.Entry<Any, Any>, Entry<K, V>?> { cursorItem: Entry<Any, Any>? -> cursorItem as Entry<K, V>? })
                .collect<Map<K, V>, Any>(Collectors.toMap<Entry<K, V>?, K, V>(Function<Entry<K, V>?, K> { java.util.Map.Entry.key }, Function<Entry<K, V>?, V> { java.util.Map.Entry.value }))
        }
    }

    override fun multiRetrieveMap(keys: Collection<K>): Map<K, V> {
        return fillCacheMapUsing(
            ArrayList(keys),
            multiRetrieveList(keys)
        )
    }

    override fun delete(key: K): Boolean {
        return redisTemplate.opsForHash<Any, Any>().delete(cacheName as K, key) != INT_ZERO.toLong()
    }

    override fun delete(keys: Collection<K>): Long {
        return redisTemplate.opsForHash<Any, Any>().delete(cacheName as K, *keys.toTypedArray())
    }
}
