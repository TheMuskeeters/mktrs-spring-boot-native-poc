/*----------------------------------------------------------------------------*/ /* Source File:   REDISREGIONCACHEINTEGERKEYSERVICETEST.JAVA                  */ /* Copyright (c), 2023 The Musketeers                                         */ /*----------------------------------------------------------------------------*/ /*-----------------------------------------------------------------------------
 History
 Jun.27/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.themusketeers.sbnative.service

import java.lang.Boolean
import kotlin.Int
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
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ValueOperations

/**
 * Unit tests for [RedisRegionCacheService] using an Integer Key to cover this case when working with keys.
 * <br></br><br></br>
 * **NOTE:**This Unit test file is based on [RedisRegionCacheServiceTest], thus any new test added there
 * should be added here as well to have unit tests in sync.
 *
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
@ExtendWith(MockitoExtension::class)
internal class RedisRegionCacheIntegerKeyServiceTest {
    @Mock
    private val redisTemplate: RedisTemplate<Int, String?>? = null

    @Mock
    private val valueOperations: ValueOperations<Int, String?>? = null
    private var redisRegionCacheService: RedisRegionCacheService<Int, String?>? = null
    @BeforeEach
    fun beforeEach() {
        redisRegionCacheService = RedisRegionCacheService(REDIS_REGION, redisTemplate)
        Mockito.reset(redisTemplate, valueOperations)
    }

    @Test
    @DisplayName("Verify that a key exists.")
    fun shouldCheckAKeyExistsInRedis() {
        Mockito.`when`(redisTemplate!!.hasKey(ArgumentMatchers.anyInt())).thenReturn(Boolean.TRUE)
        val keyExist = redisRegionCacheService!!.exists(REDIS_THE_KEY)
        Assertions.assertThat(keyExist)
            .isNotNull()
            .isTrue()
        Mockito.verify(redisTemplate).hasKey(ArgumentMatchers.anyInt())
    }

    @Test
    @DisplayName("Verify that a key does not exist.")
    fun shouldCheckAKeyDoesNotExistInRedis() {
        Mockito.`when`(redisTemplate!!.hasKey(ArgumentMatchers.anyInt())).thenReturn(Boolean.FALSE)
        val keyExist = redisRegionCacheService!!.exists(REDIS_THE_KEY)
        Assertions.assertThat(keyExist)
            .isNotNull()
            .isFalse()
        Mockito.verify(redisTemplate).hasKey(ArgumentMatchers.anyInt())
    }

    @Test
    @DisplayName("Verify that a key/value is inserted.")
    fun shouldInsertAStringIntoRedis() {
        Mockito.`when`(redisTemplate!!.opsForValue()).thenReturn(valueOperations)
        Mockito.doNothing().`when`(valueOperations)[ArgumentMatchers.anyInt()] = ArgumentMatchers.anyString()
        redisRegionCacheService!!.insert(REDIS_THE_KEY, REDIS_THE_INFO_SAVED)
        Mockito.verify(redisTemplate).opsForValue()
        Mockito.verify(valueOperations)[ArgumentMatchers.anyInt()] = ArgumentMatchers.anyString()
    }

    @Test
    @DisplayName("Verify that a key/value is inserted. This tests cover a case when 'buildRegionKey' has the region set already.")
    fun shouldInsertAStringIntoRedisAssumesKeyHoldsRegion() {
        Mockito.`when`(redisTemplate!!.opsForValue()).thenReturn(valueOperations)
        Mockito.doNothing().`when`(valueOperations)[ArgumentMatchers.anyInt()] = ArgumentMatchers.anyString()
        redisRegionCacheService!!.insert(REDIS_THE_KEY, REDIS_THE_INFO_SAVED)
        Mockito.verify(redisTemplate).opsForValue()
        Mockito.verify(valueOperations)[ArgumentMatchers.anyInt()] = ArgumentMatchers.anyString()
    }

    @Test
    @DisplayName("Verify we can add a bulk key/value data.")
    fun shouldPerformABulkInsertIntoRedis() {
        val keyValueMap = expectedKeyValueMap()
        Mockito.`when`(redisTemplate!!.opsForValue()).thenReturn(valueOperations)
        Mockito.doNothing().`when`(valueOperations).multiSet(ArgumentMatchers.anyMap())
        redisRegionCacheService!!.multiInsert(keyValueMap)
        Mockito.verify(redisTemplate).opsForValue()
        Mockito.verify(valueOperations).multiSet(ArgumentMatchers.anyMap())
    }

    @Test
    @DisplayName("Verify that a key/value is retrieved.")
    fun shouldRetrieveAStringFromRedis() {
        Mockito.`when`(redisTemplate!!.opsForValue()).thenReturn(valueOperations)
        Mockito.`when`(valueOperations!![ArgumentMatchers.any()]).thenReturn(REDIS_THE_INFO_SAVED)
        val retrievedInfo = redisRegionCacheService!!.retrieve(REDIS_THE_KEY)
        Assertions.assertThat(retrievedInfo)
            .isNotNull()
            .isEqualTo(REDIS_THE_INFO_SAVED)
        Mockito.verify(redisTemplate).opsForValue()
        Mockito.verify(valueOperations)[ArgumentMatchers.any()]
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    @ValueSource(strings = ["*", "DM*", "*DM*"])
    @DisplayName("Verify we are able to retrieve a bulk of keys from Redis given a key pattern.")
    fun givenAKeyPatternThenRetrieveKeyListFromRedis(keyPattern: String?) {
        val multipleKeys = assignMultipleKeys()
        Mockito.`when`(redisTemplate!!.keys(ArgumentMatchers.any())).thenReturn(multipleKeys)
        val retrievedStringList = redisRegionCacheService!!.multiRetrieveKeyList(keyPattern!!)
        Assertions.assertThat<Int>(retrievedStringList)
            .hasSize(INT_THREE)
            .containsExactlyElementsOf(multipleKeys)
        Mockito.verify(redisTemplate).keys(ArgumentMatchers.any())
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    @ValueSource(strings = ["*", "DM*", "*DM*"])
    @DisplayName("Verify we are able to retrieve a bulk of data from Redis given a key pattern.")
    fun givenAKeyPatternThenRetrieveAListOfStringFromRedis(keyPattern: String?) {
        val multipleKeys = assignMultipleKeys()
        val multipleValues = assignMultipleValues()
        Mockito.`when`(redisTemplate!!.keys(ArgumentMatchers.any())).thenReturn(multipleKeys)
        Mockito.`when`(redisTemplate.opsForValue()).thenReturn(valueOperations)
        Mockito.`when`(valueOperations!!.multiGet(ArgumentMatchers.anySet())).thenReturn(multipleValues)
        val retrievedStringList = redisRegionCacheService!!.multiRetrieveList(keyPattern!!)
        Assertions.assertThat<String?>(retrievedStringList)
            .isNotNull()
            .hasSize(INT_THREE)
            .containsExactlyElementsOf(multipleValues)
        Mockito.verify(redisTemplate).keys(ArgumentMatchers.any())
        Mockito.verify(redisTemplate).opsForValue()
        Mockito.verify(valueOperations).multiGet(ArgumentMatchers.anySet())
    }

    @Test
    @DisplayName("Verify we are able to retrieve a bulk of data from Redis given a list of keys to look for.")
    fun givenAListOfKeysThenRetrieveAListOfStringFromRedis() {
        val multipleKeys = assignMultipleKeys()
        val multipleValues = assignMultipleValues()
        Mockito.`when`(redisTemplate!!.opsForValue()).thenReturn(valueOperations)
        Mockito.`when`(valueOperations!!.multiGet(ArgumentMatchers.anyList())).thenReturn(multipleValues)
        val retrievedStringList = redisRegionCacheService!!.multiRetrieveList(multipleKeys)
        Assertions.assertThat<String?>(retrievedStringList)
            .isNotNull()
            .hasSize(INT_THREE)
            .containsExactlyInAnyOrderElementsOf(multipleValues)
        Mockito.verify(redisTemplate).opsForValue()
        Mockito.verify(valueOperations).multiGet(ArgumentMatchers.anyList())
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    @ValueSource(strings = ["*", "DM*", "*DM*"])
    @DisplayName("Verify we are able to retrieve a map containing the key/value pairs from Redis using a key pattern.")
    fun givenAKeyPatternThenRetrieveAMapWithKeyValueDataFromRedis(keyPattern: String?) {
        val keyList = assignMultipleKeys()
        val valueList = assignMultipleValues()
        val expectedKeyValueMap = expectedKeyValueMap()
        Mockito.`when`(redisTemplate!!.keys(ArgumentMatchers.any())).thenReturn(keyList)
        Mockito.`when`(redisTemplate.opsForValue()).thenReturn(valueOperations)
        Mockito.`when`(valueOperations!!.multiGet(ArgumentMatchers.anyList())).thenReturn(valueList)
        val retrievedStringMap = redisRegionCacheService!!.multiRetrieveMap(keyPattern!!)
        Assertions.assertThat<Int, String?>(retrievedStringMap)
            .isNotNull()
            .hasSize(INT_THREE)
            .containsExactlyInAnyOrderEntriesOf(expectedKeyValueMap)
        Mockito.verify(redisTemplate).keys(ArgumentMatchers.any())
        Mockito.verify(redisTemplate).opsForValue()
        Mockito.verify(valueOperations).multiGet(ArgumentMatchers.anyList())
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    @ValueSource(strings = ["*", "DM*", "*DM*"])
    @DisplayName("Verify we are able to retrieve a map containing the key/value pairs from Redis using a key pattern discarding not found keys (when its value is null).")
    fun givenAKeyPatternThenRetrieveAMapWithKeyValueDataFromRedisDiscardingNotFoundKeys(keyPattern: String?) {
        val keyList = assignMultipleKeys()
        val valueList = assignMultipleValuesWithNullAsObjectList()
        val expectedKeyValueMap = expectedKeyValueExcludeNullItemMap()
        Mockito.`when`(redisTemplate!!.keys(ArgumentMatchers.any())).thenReturn(keyList)
        Mockito.`when`(redisTemplate.opsForValue()).thenReturn(valueOperations)
        Mockito.`when`(valueOperations!!.multiGet(ArgumentMatchers.anyList())).thenReturn(valueList)
        val retrievedStringMap = redisRegionCacheService!!.multiRetrieveMap(keyPattern!!)
        Assertions.assertThat<Int, String?>(retrievedStringMap)
            .isNotNull()
            .hasSize(INT_TWO)
            .containsExactlyInAnyOrderEntriesOf(expectedKeyValueMap)
        Mockito.verify(redisTemplate).keys(ArgumentMatchers.any())
        Mockito.verify(redisTemplate).opsForValue()
        Mockito.verify(valueOperations).multiGet(ArgumentMatchers.anyList())
    }

    @Test
    @DisplayName("Verify we are able to retrieve a map containing the key/value pairs from Redis using a collection containing the keys to look for.")
    fun givenListOfKeysThenRetrieveAMapWithKeyValueDataFromRedis() {
        val multipleKeys = assignMultipleKeys()
        val multipleValues = assignMultipleValues()
        val expectedKeyValueMap = expectedKeyValueMap()
        Mockito.`when`(redisTemplate!!.opsForValue()).thenReturn(valueOperations)
        Mockito.`when`(valueOperations!!.multiGet(ArgumentMatchers.anyList())).thenReturn(multipleValues)
        val retrievedKeyValueMap = redisRegionCacheService!!.multiRetrieveMap(multipleKeys)
        Assertions.assertThat<Int, String?>(retrievedKeyValueMap)
            .isNotNull()
            .hasSize(INT_THREE)
            .containsExactlyInAnyOrderEntriesOf(expectedKeyValueMap)
        Mockito.verify(redisTemplate).opsForValue()
        Mockito.verify(valueOperations).multiGet(ArgumentMatchers.anyList())
    }

    @Test
    @DisplayName("Verify we are able to retrieve a map containing the key/value pairs from Redis using a collection containing the keys to look for, discarding not found keys (which value is null) and discarded by the service.")
    fun givenListOfKeysThenRetrieveAMapWithKeyValueDataFromRedisDiscardingNotFoundKeys() {
        val multipleKeys = assignMultipleKeys()
        val multipleValues = assignMultipleValuesWithNullAsObjectList()
        val expectedKeyValueMap = expectedKeyValueExcludeNullItemMap()
        Mockito.`when`(redisTemplate!!.opsForValue()).thenReturn(valueOperations)
        Mockito.`when`(valueOperations!!.multiGet(ArgumentMatchers.anyList())).thenReturn(multipleValues)
        val retrievedKeyValueMap = redisRegionCacheService!!.multiRetrieveMap(multipleKeys)
        Assertions.assertThat<Int, String?>(retrievedKeyValueMap)
            .isNotNull()
            .hasSize(INT_TWO)
            .containsExactlyInAnyOrderEntriesOf(expectedKeyValueMap)
        Mockito.verify(redisTemplate).opsForValue()
        Mockito.verify(valueOperations).multiGet(ArgumentMatchers.anyList())
    }

    @Test
    @DisplayName("Verify we are able to retrieve a map containing all of the key/value pairs from Redis.")
    fun shouldRetrieveAllMapWithKeyValueDataFromRedis() {
        val keyList = assignMultipleKeys()
        val valueList = assignMultipleValues()
        val expectedKeyValueMap = expectedKeyValueMap()
        Mockito.`when`(redisTemplate!!.keys(ArgumentMatchers.any())).thenReturn(keyList)
        Mockito.`when`(redisTemplate.opsForValue()).thenReturn(valueOperations)
        Mockito.`when`(valueOperations!!.multiGet(ArgumentMatchers.anyList())).thenReturn(valueList)
        val retrievedStringMap = redisRegionCacheService!!.multiRetrieveMap()
        Assertions.assertThat<Int, String?>(retrievedStringMap)
            .isNotNull()
            .hasSize(INT_THREE)
            .containsExactlyInAnyOrderEntriesOf(expectedKeyValueMap)
        Mockito.verify(redisTemplate).keys(ArgumentMatchers.any())
        Mockito.verify(redisTemplate).opsForValue()
        Mockito.verify(valueOperations).multiGet(ArgumentMatchers.anyList())
    }

    @Test
    @DisplayName("Verify we can remove a key from Redis")
    fun givenAKeyThenRemove() {
        Mockito.`when`(redisTemplate!!.delete(ArgumentMatchers.anyInt())).thenReturn(Boolean.TRUE)
        val isRemoved = redisRegionCacheService!!.delete(REDIS_THE_KEY)
        Assertions.assertThat(isRemoved)
            .isNotNull()
            .isTrue()
        Mockito.verify(redisTemplate).delete(ArgumentMatchers.anyInt())
    }

    @Test
    @DisplayName("Verify we can remove a key from Redis")
    fun givenAListOfKeysThenBulkRemove() {
        val multipleKeys = assignMultipleKeys()
        Mockito.`when`<Long>(redisTemplate!!.delete(ArgumentMatchers.anyList<Int>())).thenReturn(LONG_THREE)
        val numKeysRemoved = redisRegionCacheService!!.delete(multipleKeys)
        Assertions.assertThat(numKeysRemoved)
            .isNotNull()
            .isEqualTo(LONG_THREE)
        Mockito.verify(redisTemplate).delete(ArgumentMatchers.anyList())
    }

    @Test
    @DisplayName("Verify we retrieve all the items in the Cache Region")
    fun shouldCountAllItemsInCacheRegion() {
        val multipleKeys = assignMultipleKeys()
        Mockito.`when`(redisTemplate!!.keys(ArgumentMatchers.any())).thenReturn(multipleKeys)
        val numItems = redisRegionCacheService!!.count()
        Assertions.assertThat(numItems)
            .isNotNull()
            .isEqualTo(LONG_THREE)
        Mockito.verify(redisTemplate).keys(ArgumentMatchers.any())
    }

    @Test
    @DisplayName("Verify we retrieve selected items in the Cache Region")
    fun shouldCountSelectedItemsInCacheRegion() {
        val multipleKeys = assignMultipleWWKeys()
        Mockito.`when`(redisTemplate!!.keys(ArgumentMatchers.any())).thenReturn(multipleKeys)
        val numItems = redisRegionCacheService!!.count(KEY_PATTERN_AA)
        Assertions.assertThat(numItems)
            .isNotNull()
            .isEqualTo(LONG_TWO)
        Mockito.verify(redisTemplate).keys(ArgumentMatchers.any())
    }

    private fun assignMultipleKeys(): Set<Int> {
        // We need the key set to be in the order of insertion.
        val keySet = LinkedHashSet<Int>()
        keySet.add(REDIS_KEY_1234)
        keySet.add(REDIS_KEY_4567)
        keySet.add(REDIS_KEY_8901)
        return keySet
    }

    private fun assignMultipleValues(): List<String?> {
        return java.util.List.of(REDIS_VALUE_1234, REDIS_VALUE_4567, REDIS_VALUE_8901)
    }

    private fun assignMultipleValuesWithNullAsObjectList(): List<String?> {
        val valueList = ArrayList<String?>()
        valueList.add(REDIS_VALUE_1234)
        valueList.add(null)
        valueList.add(REDIS_VALUE_8901)
        return valueList
    }

    private fun assignMultipleWWKeys(): Set<Int> {
        // We need the key set to be in the order of insertion.
        val keySet = LinkedHashSet<Int>()
        keySet.add(REDIS_KEY_12345)
        keySet.add(REDIS_KEY_56789)
        return keySet
    }

    private fun expectedKeyValueMap(): Map<Int, String?> {
        return createKeyValueMap()
    }

    private fun expectedKeyValueExcludeNullItemMap(): Map<Int, String> {
        // We need the key map to be in the order of insertion.
        val expectedMap = LinkedHashMap<Int, String>()
        expectedMap[REDIS_KEY_1234] = REDIS_VALUE_1234
        expectedMap[REDIS_KEY_8901] = REDIS_VALUE_8901
        return expectedMap
    }

    private fun createKeyValueMap(): LinkedHashMap<Int, String?> {
        // We need the key map to be in the order of insertion.
        val expectedMap = LinkedHashMap<Int, String?>()
        expectedMap[REDIS_KEY_1234] = REDIS_VALUE_1234
        expectedMap[REDIS_KEY_4567] = REDIS_VALUE_4567
        expectedMap[REDIS_KEY_8901] = REDIS_VALUE_8901
        return expectedMap
    }

    companion object {
        private const val REDIS_REGION = "region"
        private const val REDIS_THE_INFO_SAVED = "The Info saved"
        private const val REDIS_VALUE_4567 = "ITEM 4567"
        private const val REDIS_VALUE_8901 = "ITEM 8901"
        private const val REDIS_VALUE_1234 = "ITEM 1234"
        private const val KEY_PATTERN_AA = "AA*"
        private const val REDIS_THE_KEY = 123
        private const val REDIS_KEY_1234 = 1234
        private const val REDIS_KEY_4567 = 4567
        private const val REDIS_KEY_8901 = 8901
        private const val REDIS_KEY_12345 = 12345
        private const val REDIS_KEY_56789 = 56789
    }
}
