/*----------------------------------------------------------------------------*/ /* Source File:   REDISHASHCACHESERVICETEST.JAVA                              */ /* Copyright (c), 2023 The Musketeers                                         */ /*----------------------------------------------------------------------------*/ /*-----------------------------------------------------------------------------
 History
 Jun.27/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.themusketeers.sbnative.service

import java.lang.Boolean
import java.util.Map.Entry
import kotlin.Any
import kotlin.Long
import kotlin.String
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EmptySource
import org.junit.jupiter.params.provider.NullSource
import org.junit.jupiter.params.provider.ValueSource
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.data.redis.core.*

/**
 * Unit Tests for [RedisHashCacheService].
 *
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
@ExtendWith(MockitoExtension::class)
internal class RedisHashCacheServiceTest {
    @Mock
    private lateinit var redisTemplate: RedisTemplate<String, String>

    @Mock
    private lateinit var hashOperations: HashOperations<String, Any, Any>
    private lateinit var redisHashCacheService: RedisHashCacheService<String, String>

    @BeforeEach
    fun beforeEach() {
        redisHashCacheService = RedisHashCacheService(REDIS_HASH, redisTemplate)
        Mockito.reset(redisTemplate, hashOperations)
    }

    @Test
    @DisplayName("Verify that a key exists.")
    fun shouldCheckAKeyExistsInRedis() {
        Mockito.`when`(redisTemplate!!.opsForHash<Any, Any?>()).thenReturn(hashOperations)
        Mockito.`when`(hashOperations!!.hasKey(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(Boolean.TRUE)
        val keyExist = redisHashCacheService!!.exists(REDIS_THE_KEY)
        Assertions.assertThat(keyExist)
            .isNotNull()
            .isTrue()
        Mockito.verify(redisTemplate).opsForHash<Any, Any>()
        Mockito.verify(hashOperations).hasKey(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())
    }

    @Test
    @DisplayName("Verify that a key does not exist.")
    fun shouldCheckAKeyDoesNotExistInRedis() {
        Mockito.`when`(redisTemplate!!.opsForHash<Any, Any?>()).thenReturn(hashOperations)
        Mockito.`when`(hashOperations!!.hasKey(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(Boolean.FALSE)
        val keyExist = redisHashCacheService!!.exists(REDIS_THE_KEY)
        Assertions.assertThat(keyExist)
            .isNotNull()
            .isFalse()
        Mockito.verify(redisTemplate).opsForHash<Any, Any>()
        Mockito.verify(hashOperations).hasKey(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())
    }

    @Test
    @DisplayName("Verify that a key/value is inserted.")
    fun shouldInsertAStringIntoRedisAssumesKeyHoldsHash() {
        Mockito.`when`(redisTemplate!!.opsForHash<Any, Any?>()).thenReturn(hashOperations)
        Mockito.doNothing().`when`(hashOperations).put(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString())
        redisHashCacheService!!.insert(REDIS_THE_KEY, REDIS_THE_INFO_SAVED)
        Mockito.verify(redisTemplate).opsForHash<Any, Any>()
        Mockito.verify(hashOperations).put(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString())
    }

    @Test
    @DisplayName("Verify we can add a bulk key/value data.")
    fun shouldPerformABulkInsertIntoRedis() {
        val keyValueMap = expectedKeyValueMap()
        Mockito.`when`(redisTemplate!!.opsForHash<Any, Any?>()).thenReturn(hashOperations)
        Mockito.doNothing().`when`(hashOperations).putAll(ArgumentMatchers.anyString(), ArgumentMatchers.anyMap())
        redisHashCacheService!!.multiInsert(keyValueMap)
        Mockito.verify(redisTemplate).opsForHash<Any, Any>()
        Mockito.verify(hashOperations).putAll(ArgumentMatchers.anyString(), ArgumentMatchers.anyMap())
    }

    @Test
    @DisplayName("Verify that a key/value is retrieved.")
    fun shouldRetrieveAStringFromRedis() {
        Mockito.`when`(redisTemplate!!.opsForHash<Any, Any?>()).thenReturn(hashOperations)
        Mockito.`when`(hashOperations!![ArgumentMatchers.anyString(), ArgumentMatchers.anyString()]).thenReturn(REDIS_THE_INFO_SAVED)
        val retrievedInfo = redisHashCacheService!!.retrieve(REDIS_THE_KEY)
        Assertions.assertThat(retrievedInfo)
            .isNotNull()
            .isEqualTo(REDIS_THE_INFO_SAVED)
        Mockito.verify(redisTemplate).opsForHash<Any, Any>()
        Mockito.verify(hashOperations)[ArgumentMatchers.anyString(), ArgumentMatchers.anyString()]
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    @ValueSource(strings = ["*", "DM*", "*DM*"])
    @DisplayName("Verify we are able to retrieve a bulk of keys from Redis given a key pattern.")
    fun givenAKeyPatternThenRetrieveKeyListFromRedis(keyPattern: String?) {
        val multipleKeys = assignMultipleKeys()
        val cursor: Cursor<Entry<Any, Any>> = buildScanCursor()
        Mockito.`when`(redisTemplate!!.opsForHash<Any, Any?>()).thenReturn(hashOperations)
        Mockito.`when`<Cursor<Entry<Any, Any>>>(hashOperations!!.scan(ArgumentMatchers.anyString(), ArgumentMatchers.any<ScanOptions>())).thenReturn(cursor)
        val retrievedStringList = redisHashCacheService!!.multiRetrieveKeyList(keyPattern!!)
        Assertions.assertThat<String>(retrievedStringList)
            .hasSize(INT_THREE)
            .containsExactlyElementsOf(multipleKeys)
        Mockito.verify(redisTemplate).opsForHash<Any, Any>()
        Mockito.verify(hashOperations).scan(ArgumentMatchers.anyString(), ArgumentMatchers.any())
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    @ValueSource(strings = ["*", "DM*", "*DM*"])
    @DisplayName("Verify we are able to retrieve a bulk of data from Redis given a key pattern.")
    fun givenAKeyPatternThenRetrieveAListOfStringFromRedis(keyPattern: String?) {
        val multipleValues = assignMultipleValues()
        val cursor: Cursor<Entry<Any, Any?>> = buildScanCursor()
        Mockito.`when`(redisTemplate!!.opsForHash<Any, Any?>()).thenReturn(hashOperations)
        Mockito.`when`<Cursor<Entry<Any, Any?>>>(hashOperations!!.scan(ArgumentMatchers.anyString(), ArgumentMatchers.any<ScanOptions>())).thenReturn(cursor)
        val retrievedStringList = redisHashCacheService!!.multiRetrieveList(keyPattern!!)
        Assertions.assertThat<String>(retrievedStringList)
            .isNotNull()
            .hasSize(INT_THREE)
            .containsExactlyElementsOf(multipleValues)
        Mockito.verify(redisTemplate).opsForHash<Any, Any>()
        Mockito.verify(hashOperations).scan(ArgumentMatchers.anyString(), ArgumentMatchers.any())
    }

    @Test
    @DisplayName("Verify we are able to retrieve a bulk of data from Redis given a list of keys to look for.")
    fun givenAListOfKeysThenRetrieveAListOfStringFromRedis() {
        val multipleKeys = assignMultipleKeys()
        val multipleValues = assignMultipleValuesAsObjectList()
        val multipleValuesExpected = assignMultipleValues()
        Mockito.`when`(redisTemplate!!.opsForHash<Any, Any?>()).thenReturn(hashOperations)
        Mockito.`when`(hashOperations!!.multiGet(ArgumentMatchers.anyString(), ArgumentMatchers.anyList())).thenReturn(multipleValues)
        val retrievedStringList = redisHashCacheService!!.multiRetrieveList(multipleKeys)
        Assertions.assertThat<String>(retrievedStringList)
            .isNotNull()
            .hasSize(INT_THREE)
            .containsExactlyInAnyOrderElementsOf(multipleValuesExpected)
        Mockito.verify(redisTemplate).opsForHash<Any, Any>()
        Mockito.verify(hashOperations).multiGet(ArgumentMatchers.anyString(), ArgumentMatchers.anyList())
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    @ValueSource(strings = ["*", "DM*", "*DM*"])
    @DisplayName("Verify we are able to retrieve a map containing the key/value pairs from Redis using a key pattern.")
    fun givenAKeyPatternThenRetrieveAMapWithKeyValueDataFromRedis(keyPattern: String?) {
        val expectedKeyValueMap = expectedKeyValueMap()
        val cursor: Cursor<Entry<Any, Any?>> = buildScanCursor()
        Mockito.`when`(redisTemplate!!.opsForHash<Any, Any?>()).thenReturn(hashOperations)
        Mockito.`when`<Cursor<Entry<Any, Any?>>>(hashOperations!!.scan(ArgumentMatchers.anyString(), ArgumentMatchers.any<ScanOptions>())).thenReturn(cursor)
        val retrievedStringMap = redisHashCacheService!!.multiRetrieveMap(keyPattern!!)
        Assertions.assertThat<String, String>(retrievedStringMap)
            .isNotNull()
            .hasSize(INT_THREE)
            .containsExactlyInAnyOrderEntriesOf(expectedKeyValueMap)
        Mockito.verify(redisTemplate).opsForHash<Any, Any>()
        Mockito.verify(hashOperations).scan(ArgumentMatchers.anyString(), ArgumentMatchers.any())
    }

    @Test
    @DisplayName("Verify we are able to retrieve a map containing the key/value pairs from Redis using a collection containing the keys to look for.")
    fun givenListOfKeysThenRetrieveAMapWithKeyValueDataFromRedis() {
        val multipleKeys = assignMultipleKeys()
        val multipleValues = assignMultipleValuesAsObjectList()
        val expectedKeyValueMap = expectedKeyValueMap()
        Mockito.`when`(redisTemplate!!.opsForHash<Any, Any?>()).thenReturn(hashOperations)
        Mockito.`when`(hashOperations!!.multiGet(ArgumentMatchers.anyString(), ArgumentMatchers.anyList())).thenReturn(multipleValues)
        val retrievedKeyValueMap = redisHashCacheService!!.multiRetrieveMap(multipleKeys)
        Assertions.assertThat<String, String>(retrievedKeyValueMap)
            .isNotNull()
            .hasSize(INT_THREE)
            .containsExactlyInAnyOrderEntriesOf(expectedKeyValueMap)
        Mockito.verify(redisTemplate).opsForHash<Any, Any>()
        Mockito.verify(hashOperations).multiGet(ArgumentMatchers.anyString(), ArgumentMatchers.anyList())
    }

    @Test
    @DisplayName("Verify we are able to retrieve a map containing the key/value pairs from Redis using a collection containing the keys to look for, but some keys are not found, whose values are null in the response from Redis but filter out by the service.")
    fun givenListOfKeysThenRetrieveAMapWithKeyValueDataFromRedisDiscardingNotFoundKeys() {
        val multipleKeys = assignMultipleKeys()
        val multipleValues = assignMultipleValuesWithNullAsObjectList()
        val expectedKeyValueMap = expectedKeyValueExcludeNullItemMap()
        Mockito.`when`(redisTemplate!!.opsForHash<Any, Any?>()).thenReturn(hashOperations)
        Mockito.`when`(hashOperations!!.multiGet(ArgumentMatchers.anyString(), ArgumentMatchers.anyList())).thenReturn(multipleValues)
        val retrievedKeyValueMap = redisHashCacheService!!.multiRetrieveMap(multipleKeys)
        Assertions.assertThat<String, String>(retrievedKeyValueMap)
            .isNotNull()
            .hasSize(INT_TWO)
            .containsExactlyInAnyOrderEntriesOf(expectedKeyValueMap)
        Mockito.verify(redisTemplate).opsForHash<Any, Any>()
        Mockito.verify(hashOperations).multiGet(ArgumentMatchers.anyString(), ArgumentMatchers.anyList())
    }

    @Test
    @DisplayName("Verify we are able to retrieve a map containing all of the key/value pairs from Redis.")
    fun shouldRetrieveAllMapWithKeyValueDataFromRedis() {
        val expectedKeyValueMap = expectedKeyValueMap()
        val keyValueMap = expectedKeyValueMapAsObjects()
        Mockito.`when`(redisTemplate!!.opsForHash<Any, Any?>()).thenReturn(hashOperations)
        Mockito.`when`(hashOperations!!.entries(ArgumentMatchers.anyString())).thenReturn(keyValueMap)
        val retrievedStringMap = redisHashCacheService!!.multiRetrieveMap()
        Assertions.assertThat<String, String>(retrievedStringMap)
            .isNotNull()
            .hasSize(INT_THREE)
            .containsExactlyInAnyOrderEntriesOf(expectedKeyValueMap)
        Mockito.verify(redisTemplate).opsForHash<Any, Any>()
        Mockito.verify(hashOperations).entries(ArgumentMatchers.anyString())
    }

    @Test
    @DisplayName("Verify we can remove a key from Redis")
    fun givenAKeyAndItDoesNotExistThenRemoveReturnsFalse() {
        Mockito.`when`(redisTemplate!!.opsForHash<Any, Any?>()).thenReturn(hashOperations)
        Mockito.`when`<Long>(hashOperations!!.delete(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(LONG_ZERO)
        val isRemoved = redisHashCacheService!!.delete(REDIS_THE_KEY)
        Assertions.assertThat(isRemoved)
            .isNotNull()
            .isFalse()
        Mockito.verify(redisTemplate).opsForHash<Any, Any>()
        Mockito.verify(hashOperations).delete(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())
    }

    @Test
    @DisplayName("Verify we cannot remove a key from Redis because key is no present.")
    fun givenAKeyThenRemove() {
        Mockito.`when`(redisTemplate!!.opsForHash<Any, Any?>()).thenReturn(hashOperations)
        Mockito.`when`<Long>(hashOperations!!.delete(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(LONG_ONE)
        val isRemoved = redisHashCacheService!!.delete(REDIS_THE_KEY)
        Assertions.assertThat(isRemoved)
            .isNotNull()
            .isTrue()
        Mockito.verify(redisTemplate).opsForHash<Any, Any>()
        Mockito.verify(hashOperations).delete(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())
    }

    @Test
    @DisplayName("Verify we can remove a key from Redis")
    fun givenAListOfKeysThenBulkRemove() {
        val multipleKeys = assignMultipleValues()
        Mockito.`when`(redisTemplate!!.opsForHash<Any, Any?>()).thenReturn(hashOperations)
        Mockito.`when`<Long>(hashOperations!!.delete(REDIS_HASH, *multipleKeys.toTypedArray())).thenReturn(LONG_THREE)
        val numKeysRemoved = redisHashCacheService!!.delete(multipleKeys)
        Assertions.assertThat(numKeysRemoved)
            .isNotNull()
            .isEqualTo(LONG_THREE)
        Mockito.verify(redisTemplate).opsForHash<Any, Any>()
        Mockito.verify(hashOperations).delete(REDIS_HASH, *multipleKeys.toTypedArray())
    }

    @Test
    @DisplayName("Verify we retrieve all the items in the Cache Hash")
    fun shouldCountAllItemsInCacheHash() {
        Mockito.`when`(redisTemplate!!.opsForHash<Any, Any?>()).thenReturn(hashOperations)
        Mockito.`when`<Long>(hashOperations!!.size(ArgumentMatchers.anyString())).thenReturn(LONG_THREE)
        val numItems = redisHashCacheService!!.count()
        Assertions.assertThat(numItems)
            .isNotNull()
            .isEqualTo(LONG_THREE)
        Mockito.verify(redisTemplate).opsForHash<Any, Any>()
        Mockito.verify(hashOperations).size(ArgumentMatchers.anyString())
    }

    @Test
    @DisplayName("Verify we retrieve selected items in the Cache Hash")
    fun shouldCountSelectedItemsInCacheHash() {
        val cursor: Cursor<Entry<Any, Any?>> = buildScanCursor()
        Mockito.`when`(redisTemplate!!.opsForHash<Any, Any?>()).thenReturn(hashOperations)
        Mockito.`when`<Cursor<Entry<Any, Any?>>>(hashOperations!!.scan(ArgumentMatchers.anyString(), ArgumentMatchers.any<ScanOptions>())).thenReturn(cursor)
        val numItems = redisHashCacheService!!.count(KEY_PATTERN_AA)
        Assertions.assertThat(numItems)
            .isNotNull()
            .isEqualTo(LONG_THREE)
        Mockito.verify(redisTemplate).opsForHash<Any, Any>()
        Mockito.verify(hashOperations).scan(ArgumentMatchers.anyString(), ArgumentMatchers.any())
    }

    private fun assignMultipleKeys(): Set<String> {
        // We need the key set to be in the order of insertion.
        val keySet = LinkedHashSet<String>()
        keySet.add(REDIS_KEY_ITEM_1234)
        keySet.add(REDIS_KEY_ITEM_4567)
        keySet.add(REDIS_KEY_ITEM_8901)
        return keySet
    }

    private fun assignMultipleValues(): List<String> {
        return java.util.List.of(REDIS_VALUE_ITEM_1234, REDIS_VALUE_ITEM_4567, REDIS_VALUE_ITEM_8901)
    }

    private fun assignMultipleValuesAsObjectList(): List<Any?> {
        return java.util.List.of<Any?>(REDIS_VALUE_ITEM_1234, REDIS_VALUE_ITEM_4567, REDIS_VALUE_ITEM_8901)
    }

    private fun assignMultipleValuesWithNullAsObjectList(): List<Any?> {
        val valueList = ArrayList<Any?>()
        valueList.add(REDIS_VALUE_ITEM_1234)
        valueList.add(null)
        valueList.add(REDIS_VALUE_ITEM_8901)
        return valueList
    }

    private fun expectedKeyValueMap(): Map<String, String> {
        return createKeyValueMap()
    }

    private fun expectedKeyValueExcludeNullItemMap(): Map<String, String> {
        // We need the key map to be in the order of insertion.
        val expectedMap = LinkedHashMap<String, String>()
        expectedMap[REDIS_KEY_ITEM_1234] = REDIS_VALUE_ITEM_1234
        expectedMap[REDIS_KEY_ITEM_8901] = REDIS_VALUE_ITEM_8901
        return expectedMap
    }

    private fun expectedKeyValueMapAsObjects(): Map<Any, Any?> {
        return createKeyValueMapAsObjects()
    }

    private fun createKeyValueMap(): LinkedHashMap<String, String> {
        // We need the key map to be in the order of insertion.
        val expectedMap = LinkedHashMap<String, String>()
        expectedMap[REDIS_KEY_ITEM_1234] = REDIS_VALUE_ITEM_1234
        expectedMap[REDIS_KEY_ITEM_4567] = REDIS_VALUE_ITEM_4567
        expectedMap[REDIS_KEY_ITEM_8901] = REDIS_VALUE_ITEM_8901
        return expectedMap
    }

    private fun createKeyValueMapAsObjects(): LinkedHashMap<Any, Any?> {
        // We need the key map to be in the order of insertion.
        val expectedMap = LinkedHashMap<Any, Any?>()
        expectedMap[REDIS_KEY_ITEM_1234] = REDIS_VALUE_ITEM_1234
        expectedMap[REDIS_KEY_ITEM_4567] = REDIS_VALUE_ITEM_4567
        expectedMap[REDIS_KEY_ITEM_8901] = REDIS_VALUE_ITEM_8901
        return expectedMap
    }

    private fun buildScanCursor(): Cursor<Entry<Any, Any?>> {
        val values: LinkedList<ScanIteration<Entry<Any, Any>>> = LinkedList<ScanIteration<Entry<Any, Any>>>()
        values.add(
            createIteration(
                LONG_ZERO,
                java.util.Map.entry<Any, Any>(REDIS_KEY_ITEM_1234, REDIS_VALUE_ITEM_1234),
                java.util.Map.entry<Any, Any>(REDIS_KEY_ITEM_4567, REDIS_VALUE_ITEM_4567),
                java.util.Map.entry<Any, Any>(REDIS_KEY_ITEM_8901, REDIS_VALUE_ITEM_8901)
            )
        )
        return initCursor(values)
    }

    private fun initCursor(values: Queue<ScanIteration<Entry<Any, Any>>>): CapturingCursor {
        val cursor = CapturingCursor(values)
        cursor.open()
        return cursor
    }

    @SafeVarargs
    private fun createIteration(cursorId: Long, vararg values: Entry<Any, Any>): ScanIteration<Entry<Any, Any>> {
        return ScanIteration<Entry<Any, Any>>(
            cursorId,
            if (values.size > INT_ZERO) Arrays.asList<Entry<Any, Any>>(*values) else emptyList<Entry<Any, Any>>()
        )
    }

    private inner class CapturingCursor internal constructor(values: Queue<ScanIteration<Entry<Any, Any>>>) : ScanCursor<Entry<Any, Any>>() {
        private val values: Queue<ScanIteration<Entry<Any, Any>>>
        private var cursors: Stack<Long>? = null

        init {
            this.values = values
        }

        override fun doScan(cursorId: Long, options: ScanOptions): ScanIteration<Entry<Any, Any>> {
            if (cursors == null) {
                cursors = Stack()
            }
            cursors!!.push(cursorId)
            return values.poll()
        }
    }

    companion object {
        private const val REDIS_HASH = "hash"
        private const val REDIS_THE_KEY = "thekey"
        private const val REDIS_THE_INFO_SAVED = "The Info saved"
        private const val REDIS_KEY_ITEM_1234 = "ITEM1234"
        private const val REDIS_KEY_ITEM_4567 = "ITEM4567"
        private const val REDIS_KEY_ITEM_8901 = "ITEM8901"
        private const val REDIS_VALUE_ITEM_1234 = "VALUE 1234"
        private const val REDIS_VALUE_ITEM_4567 = "VALUE 4567"
        private const val REDIS_VALUE_ITEM_8901 = "VALUE 8901"
        private const val KEY_PATTERN_AA = "AA*"
    }
}