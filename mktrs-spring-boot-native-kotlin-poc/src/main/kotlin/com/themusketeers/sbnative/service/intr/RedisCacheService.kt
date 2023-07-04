/*----------------------------------------------------------------------------*/
/* Source File:   REDISCACHESERVICE.JAVA                                      */
/* Copyright (c), 2023 The Musketeers                                         */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Jun.27/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.themusketeers.sbnative.service.intr

/**
 * Defines the contract necessary for making Redis server operations. Eventually this will be a wrapper around
 * the good `redisTemplate` abstraction.
 *
 * @param <K> Indicates the Redis key type (usually a String) to work with.
 * @param <V> Indicates the Redis Value type to work with.
 * @see org.springframework.data.redis.core.RedisTemplate
 *
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
interface RedisCacheService<K, V> {
    /**
     * Determines the presence of the 'key' in the database.
     *
     * @param key Indicates the Redis key type (usually a String) to work with, it must be not null.
     * @return A [Boolean] true if it is there.
     */
    fun exists(key: K): Boolean

    /**
     * Determines the number of items stored in the cache.
     *
     * @return The number of items stored in the cache.
     */
    fun count(): Long

    /**
     * Determines the number of items stored in the cache using the `keyPattern` to filter items to consider.
     *
     * @param keyPattern Denotes the expression to use for filtering the keys to be matched, usually a wild card
     * (a asterisk) is added. This must not be `NULL`. Example criteria could be `*12*`
     * which looks for keys that contains any characters then has a '12' in the middle and ends with
     * any characters. Here, if you pass an empty string, the system will interpret this as if you
     * want to count all items, otherwise the pattern limits the items being considered. Take into
     * account that this is case-sensitive.
     * @return The number of items stored in the cache.
     */
    fun count(keyPattern: String?): Long

    /**
     * Performs the addition of the pair (key, value) into the Redis store.
     *
     * @param key  Indicates the Redis key type (usually a String) to work with, it must be not null.
     * @param info Indicates the data to be stored, it must be not null.
     */
    fun insert(key: K, info: V)

    /**
     * Performs the addition of a bulk of pairs (key, value) into the Redis store. Keep in mind tha the keys must
     * be valid values according to the implementer details. For instance, if the implemetation detail is to use
     * 'region key' then each key must have it prefixed the region, somehow the 'map' may not come with those
     * prefixed as this is the responsibility of the implementer.
     *
     * @param map Contains the (key, value) pairs to be added to the Redis store.
     */
    fun multiInsert(map: Map<K, V>)

    /**
     * Looks for the given 'key' in the Redis Store.
     *
     * @param key Indicates the Redis key type (usually a String) to work with, it must be not null.
     * @return The bean present in the Redis Store or `NULL` if not there.
     */
    fun retrieve(key: K): V?

    /**
     * Look for the keys stored in the Redis Store that matches the `keyPattern`. Implementers can manipulate
     * the returned set to fit their needs.
     *
     * @param keyPattern Denotes the expression to use for filtering the keys to be matched, usually a wild card
     * (an asterisk) is added. This must not be `NULL`. Example criteria could be `*12*`
     * which looks for keys that contains any characters then has a '12' in the middle and ends with
     * any characters
     * @return A list of &lt;K&gt; beans present in the Redis Store.
     */
    fun multiRetrieveKeyList(keyPattern: String?): List<K>

    /**
     * Find all `keys` matching the given `keyPattern`. The implementer can manipulate the given
     * `keyPattern` to suit its needs.
     *
     * @param keyPattern Denotes the expression to use for filtering the keys to be matched, usually a wild card
     * (an asterisk) is added. This must not be `NULL`. Example criteria could be `*12*`
     * which looks for keys that contains any characters then has a '12' in the middle and ends with
     * any characters. Here, if you pass an empty string, the system will interpret this as if you
     * want to count all items, otherwise the pattern limits the items being considered. Take into
     * account that this is case-sensitive.
     * @return A list of &lt;V&gt; beans present in the Redis Store.
     */
    fun multiRetrieveList(keyPattern: String?): List<V>

    /**
     * Find all `key/value` in the Redis Store according to the list of `keys`. Implementers are
     * responsible for manipulating these `keys` to suit their needs.
     *
     * @param keys Contains a list of &lt;K&gt; beans to extract from the Redis Store. Normally the &lt;K&gt; type is
     * a `String`.
     * @return A list of &lt;V&gt; beans present in the Redis Store.
     */
    fun multiRetrieveList(keys: Collection<K>): List<V>

    /**
     * Retrieves all the `keys` stored in the cache.
     *
     * @return A Map of &lt;K, V&gt; beans present in the Redis Store. Implementer can manipulate the keys.
     */
    fun multiRetrieveMap(): Map<K, V>

    /**
     * Find all `key/value` matching the given `keyPattern`. The implementer can manipulate
     * the given `keyPattern` to suit its needs.
     *
     * @param keyPattern Denotes the expression to use for filtering the keys to be matched, usually a wild card
     * (an asterisk) is added. This must not be `NULL`. Example criteria could be `*12*`
     * which looks for keys that contains any characters then has a '12' in the middle and ends with
     * any characters. Here, if you pass an empty string, the system will interpret this as if you
     * want to count all items, otherwise the pattern limits the items being considered. Take into
     * account that this is case-sensitive.
     * @return A Map of &lt;K, V&gt; beans present in the Redis Store. Implementer can manipulate the keys.
     */
    fun multiRetrieveMap(keyPattern: String?): Map<K, V>

    /**
     * Find all `key/value` data matching the given `keys`. The implementer can manipulate the given
     * `keys` to suit its needs.
     * <br></br>
     * **NOTE:** If a key is not found then it must not be included in the response map, that is, it is considered
     * as not found if it is not present in the map.
     *
     * @param keys Contains a list of &lt;K&gt; beans to extract from the Redis Store. Normally the &lt;K&gt; type is
     * a `String`.
     * @return A Map of &lt;K, V&gt; beans present in the Redis Store. Implementer can manipulate the keys.
     */
    fun multiRetrieveMap(keys: Collection<K>): Map<K, V>

    /**
     * Removes the key from the store.
     *
     * @param key Indicates the Redis key type (usually a String) to work with, it must be not null.
     * @return A [Boolean] true if removed.
     */
    fun delete(key: K): Boolean

    /**
     * Removes all the matching `keys`. The implementer can manipulate the given `keys` to suit its needs.
     *
     * @param keys Contains a list of &lt;K&gt; beans to extract from the Redis Store. Normally the &lt;K&gt; type is
     * a `String`.
     * @return The number of `keys` removed from the Redis Store.
     */
    fun delete(keys: Collection<K>): Long
}
