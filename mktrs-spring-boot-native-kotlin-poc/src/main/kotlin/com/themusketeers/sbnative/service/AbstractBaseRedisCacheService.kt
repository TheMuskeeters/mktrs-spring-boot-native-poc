/*----------------------------------------------------------------------------*/
/* Source File:   ABSTRACTREDISCACHESERVICE.KT                                */
/* Copyright (c), 2023 The Musketeers                                         */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Jun.27/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.themusketeers.sbnative.service

import com.themusketeers.sbnative.common.consts.GlobalConstants.DOT
import com.themusketeers.sbnative.common.consts.GlobalConstants.INT_ONE
import com.themusketeers.sbnative.common.consts.GlobalConstants.INT_ZERO
import com.themusketeers.sbnative.common.consts.GlobalConstants.WILD_CARD_ASTERISK
import java.util.stream.IntStream
import org.apache.commons.lang3.StringUtils
import org.springframework.data.redis.core.RedisTemplate

/**
 * Includes helpers for Cache Service implementations.
 *
 * @param <K> Indicates the Redis key type (usually a String) to work with.
 * @param <V> Indicates the Redis Value type to work with.
 * @param cacheName     Represents the 'Cache Region' name. It is used as part of a key such as @{code myregion.item}.
 * @param redisTemplate References the abstraction to communicate to a Redis server.
 * @see org.springframework.data.redis.core.RedisTemplate
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
open class AbstractBaseRedisCacheService<K, V>(protected val cacheName: String, protected val redisTemplate: RedisTemplate<K, V>) {
    /**
     * Creates a joined map from the items stored in each list supplied. It is used in `Cache Region`
     * implementations.
     *
     * @param keys   Contains a list of indices (Strings) representing the type of data in the cache.
     * @param values Contains a list of types stored in the cache associated to a `key`.
     * @return A map containing the combination of `keys` and `values`.
     */
    protected fun fillCacheMapRegionUsing(keys: List<K>, values: List<V>): HashMap<K, V> {
        // Keys must have the 'Cache Region' removed.
        val keysWithNoRegion = removeCacheRegion(keys)

        return fillCacheMapUsing(keysWithNoRegion, values)
    }

    /**
     * Creates a joined map from the items stored in each list supplied. It is used in `Cache Hash`
     * implementations.
     * <br></br>
     * **NOTE:**If a value for a key is not found, then it is filtered out.
     *
     * @param keys   Contains a list of indices (Strings) representing the type of data in the cache.
     * @param values Contains a list of types stored in the cache associated with a `key`.
     * @return A concrete map type with the combined data.
     */
    protected fun fillCacheMapUsing(keys: List<K>, values: List<V>): HashMap<K, V> {
        val map = LinkedHashMap<K, V>()

        IntStream.range(INT_ZERO, keys.size)
            .forEach { keyPos: Int ->
                val key = keys[keyPos]
                val value: V? = values[keyPos]
                if (value != null) {
                    map[key] = value
                }
            }

        return map
    }

    /**
     * Removes the `Cache Region or Cache Name` contained in the supplied list of keys.
     *
     * @param keys Contains a list of indices (Strings) representing the type of data in the cache.
     * @return A list without the `Cache Region`.
     */
    protected fun removeCacheRegion(keys: List<K>): List<K> {
        return keys.stream()
            .map { key: K -> removeCacheRegionFrom(key) }
            .toList()
    }

    /**
     * Converts a list from the `Collection` contract to the `List` to comply with API definitions.
     *
     * @param keys Contains a list of indices (Strings) representing the type of data in the cache.
     * @return The converted List of keys.
     */
    protected fun transformKeyForRegionInList(keys: Collection<K>): List<K> {
        return keys.stream()
            .map { key: K -> buildRegionKey(key) }
            .toList()
    }

    /**
     * Transforms a map which contains the `Cache Region` as part of the key to a map without this.
     *
     * @param map Contains the information to be processed.
     * @return A new map without the `Cache Region` as part of the key
     */
    protected fun transformKeyForRegionInMap(map: Map<K, V>): Map<K, V> {
        val newMap = LinkedHashMap<K, V>()

        map.forEach { (k: K, v: V) -> newMap[buildRegionKey(k)] = v }

        return newMap
    }

    /**
     * Processes the `key` supplied by removing the `Cache Region` it holds.
     *
     * @param key Contains the index (String) representing the type of data in the cache.
     * @return A new &lt;K&gt; without the `Cache Region`.
     */
    protected fun removeCacheRegionFrom(key: K): K {
        // If key already contains the 'Cache Region' it does nothing.
        if (key is String) {
            if (key.contains(cacheName)) {
                return key.substring(cacheName.length + INT_ONE) as K
            }
        }

        return key
    }

    /**
     * Given the `key` it prefixes the `Cache Region`. Example, if `key` is `item` and the
     * `Cache Region` is `myregion` then the result is `myregion.item`.
     *
     * @param key Contains the index (String) representing the type of data in the cache.
     * @return A modified key in the format `[Cache Region].[key]`, e.g., `myregion.item`.
     */
    protected fun buildRegionKey(key: K): K {
        // If key already contains the 'Cache Name' it does nothing.
        if (key is String) {
            if (!key.contains(cacheName)) {
                return (cacheName + DOT + key) as K
            }
        }

        return key
    }

    /**
     * Verifies that the supplied `keyPattern` when set to null then it gets modified to an empty string and if
     * the wild card usd to filter Redis keys is not present then it appends it.
     *
     * @param keyPattern Denotes the expression to use for filtering the keys to be matched, usually a wild card
     * (an asterisk) is added. This must not be `NULL`. Example criteria could be `*12*`
     * which looks for keys that contains any characters then has a '12' in the middle and ends with
     * any characters. Here, if you pass an empty string, the system will interpret this as if you
     * want to count all items, otherwise the pattern limits the items being considered. Take into
     * account that this is case-sensitive.
     * @return A new key pattern to work with the Redis Server.
     */
    protected fun verifyKeyPattern(keyPattern: String?): String {
        var keyPattern = keyPattern

        if (null == keyPattern) {
            keyPattern = StringUtils.EMPTY
        }

        if (!keyPattern.contains(WILD_CARD_ASTERISK)) {
            keyPattern += WILD_CARD_ASTERISK
        }

        return keyPattern
    }

    /**
     * Builds a {code key} in the format `[Cache Region].[key]`, e.g., `myregion.item`.
     *
     * @param keyPattern Denotes the expression to use for filtering the keys to be matched, usually a wild card
     * (an asterisk) is added. This must not be `NULL`. Example criteria could be `*12*`
     * which looks for keys that contains any characters then has a '12' in the middle and ends with
     * any characters. Here, if you pass an empty string, the system will interpret this as if you
     * want to count all items, otherwise the pattern limits the items being considered. Take into
     * account that this is case-sensitive.
     * @return A new key with prefixed with the `Cache Region`.
     */
    protected fun buildRegionKeyPattern(keyPattern: String?): K {
        return (cacheName + DOT + verifyKeyPattern(keyPattern)) as K
    }
}
