/*----------------------------------------------------------------------------*/
/* Source File:   REDISHASHCACHESERVICETEST.KT                                */
/* Copyright (c), 2023 The Musketeers                                         */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Jun.27/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.themusketeers.sbnative.service

import com.themusketeers.sbnative.common.consts.GlobalConstants.INT_THREE
import com.themusketeers.sbnative.common.consts.GlobalConstants.INT_TWO
import com.themusketeers.sbnative.common.consts.GlobalConstants.INT_ZERO
import com.themusketeers.sbnative.common.consts.GlobalConstants.LONG_ONE
import com.themusketeers.sbnative.common.consts.GlobalConstants.LONG_THREE
import com.themusketeers.sbnative.common.consts.GlobalConstants.LONG_ZERO
import java.util.Arrays
import java.util.LinkedList
import kotlin.collections.Map.Entry
import java.util.Queue
import java.util.Stack
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EmptySource
import org.junit.jupiter.params.provider.NullSource
import org.junit.jupiter.params.provider.ValueSource
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyList
import org.mockito.ArgumentMatchers.anyMap
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.reset
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.data.redis.core.Cursor
import org.springframework.data.redis.core.HashOperations
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ScanCursor
import org.springframework.data.redis.core.ScanIteration
import org.springframework.data.redis.core.ScanOptions

/**
 * Unit Tests for [RedisHashCacheService].
 *
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
@ExtendWith(MockitoExtension::class)
internal class RedisHashCacheServiceTest {
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

    @Mock
    private lateinit var redisTemplate: RedisTemplate<String, String>

    @Mock
    private lateinit var hashOperations: HashOperations<String, Any, Any>
    private lateinit var redisHashCacheService: RedisHashCacheService<String, String>

    @BeforeEach
    fun beforeEach() {
        redisHashCacheService = RedisHashCacheService(REDIS_HASH, redisTemplate)
        reset(redisTemplate, hashOperations)
    }

    @Test
    @DisplayName("Verify that a key exists.")
    fun shouldCheckAKeyExistsInRedis() {
        `when`(redisTemplate.opsForHash<Any, Any>()).thenReturn(hashOperations)
        `when`(hashOperations.hasKey(anyString(), anyString())).thenReturn(true)

        val keyExist = redisHashCacheService.exists(REDIS_THE_KEY)

        assertThat(keyExist)
            .isNotNull()
            .isTrue()
        verify(redisTemplate).opsForHash<Any, Any>()
        verify(hashOperations).hasKey(anyString(), anyString())
    }

    @Test
    @DisplayName("Verify that a key does not exist.")
    fun shouldCheckAKeyDoesNotExistInRedis() {
        `when`(redisTemplate.opsForHash<Any, Any>()).thenReturn(hashOperations)
        `when`(hashOperations.hasKey(anyString(), anyString())).thenReturn(false)

        val keyExist = redisHashCacheService.exists(REDIS_THE_KEY)

        assertThat(keyExist)
            .isNotNull()
            .isFalse()

        verify(redisTemplate).opsForHash<Any, Any>()
        verify(hashOperations).hasKey(anyString(), anyString())
    }

    @Test
    @DisplayName("Verify that a key/value is inserted.")
    fun shouldInsertAStringIntoRedisAssumesKeyHoldsHash() {
        `when`(redisTemplate.opsForHash<Any, Any>()).thenReturn(hashOperations)
        doNothing().`when`(hashOperations).put(anyString(), anyString(), anyString())

        redisHashCacheService.insert(REDIS_THE_KEY, REDIS_THE_INFO_SAVED)

        verify(redisTemplate).opsForHash<Any, Any>()
        verify(hashOperations).put(anyString(), anyString(), anyString())
    }

    @Test
    @DisplayName("Verify we can add a bulk key/value data.")
    fun shouldPerformABulkInsertIntoRedis() {
        val keyValueMap = expectedKeyValueMap()

        `when`(redisTemplate.opsForHash<Any, Any>()).thenReturn(hashOperations)
        doNothing().`when`(hashOperations).putAll(anyString(), anyMap())

        redisHashCacheService.multiInsert(keyValueMap)

        verify(redisTemplate).opsForHash<Any, Any>()
        verify(hashOperations).putAll(anyString(), anyMap())
    }

    @Test
    @DisplayName("Verify that a key/value is retrieved.")
    fun shouldRetrieveAStringFromRedis() {
        `when`(redisTemplate.opsForHash<Any, Any>()).thenReturn(hashOperations)
        `when`(hashOperations[anyString(), anyString()]).thenReturn(REDIS_THE_INFO_SAVED)

        val retrievedInfo = redisHashCacheService.retrieve(REDIS_THE_KEY)

        assertThat(retrievedInfo)
            .isNotNull()
            .isEqualTo(REDIS_THE_INFO_SAVED)

        verify(redisTemplate).opsForHash<Any, Any>()
        verify(hashOperations)[anyString(), anyString()]
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    @ValueSource(strings = ["*", "ITEM*", "*ITEM*"])
    @DisplayName("Verify we are able to retrieve a bulk of keys from Redis given a key pattern.")
    fun givenAKeyPatternThenRetrieveKeyListFromRedis(keyPattern: String?) {
        val multipleKeys = assignMultipleKeys()
        val cursor: Cursor<Entry<Any, Any>> = buildScanCursor()

        `when`(redisTemplate.opsForHash<Any, Any>()).thenReturn(hashOperations)
        `when`<Cursor<Entry<Any, Any>>>(hashOperations.scan(anyString(), any())).thenReturn(cursor)

        val retrievedStringList = redisHashCacheService.multiRetrieveKeyList(keyPattern)

        assertThat(retrievedStringList)
            .hasSize(INT_THREE)
            .containsExactlyElementsOf(multipleKeys)
        verify(redisTemplate).opsForHash<Any, Any>()
        verify(hashOperations).scan(anyString(), any())
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    @ValueSource(strings = ["*", "ITEM*", "*ITEM*"])
    @DisplayName("Verify we are able to retrieve a bulk of data from Redis given a key pattern.")
    fun givenAKeyPatternThenRetrieveAListOfStringFromRedis(keyPattern: String?) {
        val multipleValues = assignMultipleValues()
        val cursor = buildScanCursor()

        `when`(redisTemplate.opsForHash<Any, Any>()).thenReturn(hashOperations)
        `when`(hashOperations.scan(anyString(), any())).thenReturn(cursor)

        val retrievedStringList = redisHashCacheService.multiRetrieveList(keyPattern)

        assertThat(retrievedStringList)
            .isNotNull()
            .hasSize(INT_THREE)
            .containsExactlyElementsOf(multipleValues)

        verify(redisTemplate).opsForHash<Any, Any>()
        verify(hashOperations).scan(anyString(), any())
    }

    @Test
    @DisplayName("Verify we are able to retrieve a bulk of data from Redis given a list of keys to look for.")
    fun givenAListOfKeysThenRetrieveAListOfStringFromRedis() {
        val multipleKeys = assignMultipleKeys()
        val multipleValues = assignMultipleValuesAsObjectList()
        val multipleValuesExpected = assignMultipleValues()

        `when`(redisTemplate.opsForHash<Any, Any>()).thenReturn(hashOperations)
        `when`(hashOperations.multiGet(anyString(), anyList())).thenReturn(multipleValues)

        val retrievedStringList = redisHashCacheService.multiRetrieveList(multipleKeys)

        assertThat(retrievedStringList)
            .isNotNull()
            .hasSize(INT_THREE)
            .containsExactlyInAnyOrderElementsOf(multipleValuesExpected)
        verify(redisTemplate).opsForHash<Any, Any>()
        verify(hashOperations).multiGet(anyString(), anyList())
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    @ValueSource(strings = ["*", "ITEM*", "*ITEM*"])
    @DisplayName("Verify we are able to retrieve a map containing the key/value pairs from Redis using a key pattern.")
    fun givenAKeyPatternThenRetrieveAMapWithKeyValueDataFromRedis(keyPattern: String?) {
        val expectedKeyValueMap = expectedKeyValueMap()
        val cursor = buildScanCursor()

        `when`(redisTemplate.opsForHash<Any, Any>()).thenReturn(hashOperations)
        `when`(hashOperations.scan(anyString(), any())).thenReturn(cursor)

        val retrievedStringMap = redisHashCacheService.multiRetrieveMap(keyPattern)

        assertThat(retrievedStringMap)
            .isNotNull()
            .hasSize(INT_THREE)
            .containsExactlyInAnyOrderEntriesOf(expectedKeyValueMap)

        verify(redisTemplate).opsForHash<Any, Any>()
        verify(hashOperations).scan(anyString(), any())
    }

    @Test
    @DisplayName("Verify we are able to retrieve a map containing the key/value pairs from Redis using a collection containing the keys to look for.")
    fun givenListOfKeysThenRetrieveAMapWithKeyValueDataFromRedis() {
        val multipleKeys = assignMultipleKeys()
        val multipleValues = assignMultipleValuesAsObjectList()
        val expectedKeyValueMap = expectedKeyValueMap()

        `when`(redisTemplate.opsForHash<Any, Any>()).thenReturn(hashOperations)
        `when`(hashOperations.multiGet(anyString(), anyList())).thenReturn(multipleValues)

        val retrievedKeyValueMap = redisHashCacheService.multiRetrieveMap(multipleKeys)

        assertThat(retrievedKeyValueMap)
            .isNotNull()
            .hasSize(INT_THREE)
            .containsExactlyInAnyOrderEntriesOf(expectedKeyValueMap)

        verify(redisTemplate).opsForHash<Any, Any>()
        verify(hashOperations).multiGet(anyString(), anyList())
    }

    @Test
    @DisplayName("Verify we are able to retrieve a map containing the key/value pairs from Redis using a collection containing the keys to look for, but some keys are not found, whose values are null in the response from Redis but filter out by the service.")
    fun givenListOfKeysThenRetrieveAMapWithKeyValueDataFromRedisDiscardingNotFoundKeys() {
        val multipleKeys = assignMultipleKeys()
        val multipleKeysTransformed = multipleKeys.stream()
            .map { key: String -> key as Any }
            .toList()
        val multipleValues = assignMultipleValuesWithNullAsObjectList()
        val expectedKeyValueMap = expectedKeyValueExcludeNullItemMap()

        `when`(redisTemplate.opsForHash<Any, Any>()).thenReturn(hashOperations)
        //`when`(redisHashCacheService.multiRetrieveList(multipleKeys)).thenReturn(multipleValues as List<String>)
        `when`(hashOperations.multiGet(REDIS_HASH, multipleKeysTransformed)).thenReturn(multipleValues)

        val retrievedKeyValueMap = redisHashCacheService.multiRetrieveMap(multipleKeys)

        assertThat(retrievedKeyValueMap)
            .isNotNull()
            .hasSize(INT_TWO)
            .containsExactlyInAnyOrderEntriesOf(expectedKeyValueMap)

        //verify(redisTemplate).opsForHash<Any, Any>()
        //verify(hashOperations).multiGet(REDIS_HASH, multipleKeys)
    }

    @Test
    @DisplayName("Verify we are able to retrieve a map containing all of the key/value pairs from Redis.")
    fun shouldRetrieveAllMapWithKeyValueDataFromRedis() {
        val expectedKeyValueMap = expectedKeyValueMap()
        val keyValueMap = expectedKeyValueMapAsObjects()

        `when`(redisTemplate.opsForHash<Any, Any>()).thenReturn(hashOperations)
        `when`(hashOperations.entries(anyString())).thenReturn(keyValueMap)

        val retrievedStringMap = redisHashCacheService.multiRetrieveMap()

        assertThat(retrievedStringMap)
            .isNotNull()
            .hasSize(INT_THREE)
            .containsExactlyInAnyOrderEntriesOf(expectedKeyValueMap)

        verify(redisTemplate).opsForHash<Any, Any>()
        verify(hashOperations).entries(anyString())
    }

    @Test
    @DisplayName("Verify we can remove a key from Redis")
    fun givenAKeyAndItDoesNotExistThenRemoveReturnsFalse() {
        `when`(redisTemplate.opsForHash<Any, Any>()).thenReturn(hashOperations)
        `when`(hashOperations.delete(anyString(), anyString())).thenReturn(LONG_ZERO)

        val isRemoved = redisHashCacheService.delete(REDIS_THE_KEY)

        assertThat(isRemoved)
            .isNotNull()
            .isFalse()

        verify(redisTemplate).opsForHash<Any, Any>()
        verify(hashOperations).delete(anyString(), anyString())
    }

    @Test
    @DisplayName("Verify we cannot remove a key from Redis because key is no present.")
    fun givenAKeyThenRemove() {
        `when`(redisTemplate.opsForHash<Any, Any>()).thenReturn(hashOperations)
        `when`(hashOperations.delete(anyString(), anyString())).thenReturn(LONG_ONE)

        val isRemoved = redisHashCacheService.delete(REDIS_THE_KEY)

        assertThat(isRemoved)
            .isNotNull()
            .isTrue()

        verify(redisTemplate).opsForHash<Any, Any>()
        verify(hashOperations).delete(anyString(), anyString())
    }

    @Test
    @DisplayName("Verify we can remove a key from Redis")
    fun givenAListOfKeysThenBulkRemove() {
        val multipleKeys = assignMultipleValues()

        `when`(redisTemplate.opsForHash<Any, Any>()).thenReturn(hashOperations)
        `when`(hashOperations.delete(REDIS_HASH, multipleKeys)).thenReturn(LONG_THREE)

        val numKeysRemoved = redisHashCacheService.delete(multipleKeys)

        assertThat(numKeysRemoved)
            .isNotNull()
            .isEqualTo(LONG_THREE)

        verify(redisTemplate).opsForHash<Any, Any>()
        verify(hashOperations).delete(REDIS_HASH, multipleKeys)
    }

    @Test
    @DisplayName("Verify we retrieve all the items in the Cache Hash")
    fun shouldCountAllItemsInCacheHash() {
        `when`(redisTemplate.opsForHash<Any, Any>()).thenReturn(hashOperations)
        `when`(hashOperations.size(anyString())).thenReturn(LONG_THREE)

        val numItems = redisHashCacheService.count()

        assertThat(numItems)
            .isNotNull()
            .isEqualTo(LONG_THREE)

        verify(redisTemplate).opsForHash<Any, Any>()
        verify(hashOperations).size(anyString())
    }

    @Test
    @DisplayName("Verify we retrieve selected items in the Cache Hash")
    fun shouldCountSelectedItemsInCacheHash() {
        val cursor = buildScanCursor()

        `when`(redisTemplate.opsForHash<Any, Any>()).thenReturn(hashOperations)
        `when`(hashOperations.scan(anyString(), any())).thenReturn(cursor)

        val numItems = redisHashCacheService.count(KEY_PATTERN_AA)

        assertThat(numItems)
            .isNotNull()
            .isEqualTo(LONG_THREE)

        verify(redisTemplate).opsForHash<Any, Any>()
        verify(hashOperations).scan(anyString(), any())
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
        return listOf(REDIS_VALUE_ITEM_1234, REDIS_VALUE_ITEM_4567, REDIS_VALUE_ITEM_8901)
    }

    private fun assignMultipleValuesAsObjectList(): List<Any?> {
        return listOf<Any?>(REDIS_VALUE_ITEM_1234, REDIS_VALUE_ITEM_4567, REDIS_VALUE_ITEM_8901)
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

    private fun createKeyValueMapAsObjects(): LinkedHashMap<Any, Any> {
        // We need the key map to be in the order of insertion.
        val expectedMap = LinkedHashMap<Any, Any>()

        expectedMap[REDIS_KEY_ITEM_1234] = REDIS_VALUE_ITEM_1234
        expectedMap[REDIS_KEY_ITEM_4567] = REDIS_VALUE_ITEM_4567
        expectedMap[REDIS_KEY_ITEM_8901] = REDIS_VALUE_ITEM_8901

        return expectedMap
    }

    private fun buildScanCursor(): Cursor<Entry<Any, Any>> {
        val values: LinkedList<ScanIteration<Entry<Any, Any>>> = LinkedList<ScanIteration<Entry<Any, Any>>>()

        values.add(
            createIteration(
                LONG_ZERO,
                java.util.Map.entry(REDIS_KEY_ITEM_1234, REDIS_VALUE_ITEM_1234),
                java.util.Map.entry(REDIS_KEY_ITEM_4567, REDIS_VALUE_ITEM_4567),
                java.util.Map.entry(REDIS_KEY_ITEM_8901, REDIS_VALUE_ITEM_8901)
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
    private fun createIteration(cursorId: Long, vararg values: MutableMap.MutableEntry<Any, Any>): ScanIteration<Entry<Any, Any>> {
        return ScanIteration<Entry<Any, Any>>(
            cursorId,
            if (values.size > INT_ZERO) Arrays.asList<Entry<Any, Any>>(*values) else emptyList<Entry<Any, Any>>()
        )
    }

    private inner class CapturingCursor(values: Queue<ScanIteration<Entry<Any, Any>>>) : ScanCursor<Entry<Any, Any>>() {
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
}