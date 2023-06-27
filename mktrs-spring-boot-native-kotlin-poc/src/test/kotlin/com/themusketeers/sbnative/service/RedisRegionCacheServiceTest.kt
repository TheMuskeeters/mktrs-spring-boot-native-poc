/*----------------------------------------------------------------------------*/ /* Source File:   REDISREGIONCACHESERVICETEST.JAVA                            */ /* Copyright (c), 2023 The Musketeers                                         */ /*----------------------------------------------------------------------------*/ /*-----------------------------------------------------------------------------
 History
 Jun.27/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.themusketeers.sbnative.service

import java.lang.Boolean
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
 * Unit tests for [RedisRegionCacheService].
 *
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
@ExtendWith(MockitoExtension::class)
internal class RedisRegionCacheServiceTest {
    @Mock
    private lateinit var redisTemplate: RedisTemplate<String, String>

    @Mock
    private lateinit var valueOperations: ValueOperations<String, String>
    private lateinit var redisRegionCacheService: RedisRegionCacheService<String, String>

    @BeforeEach
    fun beforeEach() {
        redisRegionCacheService = RedisRegionCacheService(REDIS_REGION, redisTemplate)
        Mockito.reset(redisTemplate, valueOperations)
    }

    @Test
    @DisplayName("Verify that a key exists.")
    fun shouldCheckAKeyExistsInRedis() {
        Mockito.`when`(redisTemplate!!.hasKey(ArgumentMatchers.anyString())).thenReturn(Boolean.TRUE)
        val keyExist = redisRegionCacheService!!.exists(REDIS_THE_KEY)
        Assertions.assertThat(keyExist)
            .isNotNull()
            .isTrue()
        Mockito.verify(redisTemplate).hasKey(ArgumentMatchers.anyString())
    }

    @Test
    @DisplayName("Verify that a key does not exist.")
    fun shouldCheckAKeyDoesNotExistInRedis() {
        Mockito.`when`(redisTemplate!!.hasKey(ArgumentMatchers.anyString())).thenReturn(Boolean.FALSE)
        val keyExist = redisRegionCacheService!!.exists(REDIS_THE_KEY)
        Assertions.assertThat(keyExist)
            .isNotNull()
            .isFalse()
        Mockito.verify(redisTemplate).hasKey(ArgumentMatchers.anyString())
    }

    @Test
    @DisplayName("Verify that a key/value is inserted.")
    fun shouldInsertAStringIntoRedis() {
        Mockito.`when`(redisTemplate!!.opsForValue()).thenReturn(valueOperations)
        Mockito.doNothing().`when`(valueOperations)[ArgumentMatchers.anyString()] = ArgumentMatchers.anyString()
        redisRegionCacheService!!.insert(REDIS_THE_KEY, REDIS_THE_INFO_SAVED)
        Mockito.verify(redisTemplate).opsForValue()
        Mockito.verify(valueOperations)[ArgumentMatchers.anyString()] = ArgumentMatchers.anyString()
    }

    @Test
    @DisplayName("Verify that a key/value is inserted. This tests cover a case when 'buildRegionKey' has the region set already.")
    fun shouldInsertAStringIntoRedisAssumesKeyHoldsRegion() {
        Mockito.`when`(redisTemplate!!.opsForValue()).thenReturn(valueOperations)
        Mockito.doNothing().`when`(valueOperations)[ArgumentMatchers.anyString()] = ArgumentMatchers.anyString()
        redisRegionCacheService!!.insert(REDIS_REGION + DOT + REDIS_THE_KEY, REDIS_THE_INFO_SAVED)
        Mockito.verify(redisTemplate).opsForValue()
        Mockito.verify(valueOperations)[ArgumentMatchers.anyString()] = ArgumentMatchers.anyString()
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
        Mockito.`when`(valueOperations!![ArgumentMatchers.anyString()]).thenReturn(REDIS_THE_INFO_SAVED)
        val retrievedInfo = redisRegionCacheService!!.retrieve(REDIS_THE_KEY)
        Assertions.assertThat(retrievedInfo)
            .isNotNull()
            .isEqualTo(REDIS_THE_INFO_SAVED)
        Mockito.verify(redisTemplate).opsForValue()
        Mockito.verify(valueOperations)[ArgumentMatchers.anyString()]
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    @ValueSource(strings = ["*", "DM*", "*DM*"])
    @DisplayName("Verify we are able to retrieve a bulk of keys from Redis given a key pattern.")
    fun givenAKeyPatternThenRetrieveKeyListFromRedis(keyPattern: String?) {
        val multipleKeys = assignMultipleKeys()
        Mockito.`when`(redisTemplate!!.keys(ArgumentMatchers.anyString())).thenReturn(multipleKeys)
        val retrievedStringList = redisRegionCacheService!!.multiRetrieveKeyList(keyPattern!!)
        Assertions.assertThat<String>(retrievedStringList)
            .hasSize(INT_THREE)
            .containsExactlyElementsOf(multipleKeys)
        Mockito.verify(redisTemplate).keys(ArgumentMatchers.anyString())
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    @ValueSource(strings = ["*", "DM*", "*DM*"])
    @DisplayName("Verify we are able to retrieve a bulk of data from Redis given a key pattern.")
    fun givenAKeyPatternThenRetrieveAListOfStringFromRedis(keyPattern: String?) {
        val multipleKeys = assignMultipleKeys()
        val multipleValues = assignMultipleValues()
        Mockito.`when`(redisTemplate!!.keys(ArgumentMatchers.anyString())).thenReturn(multipleKeys)
        Mockito.`when`(redisTemplate.opsForValue()).thenReturn(valueOperations)
        Mockito.`when`(valueOperations!!.multiGet(ArgumentMatchers.anySet())).thenReturn(multipleValues)
        val retrievedStringList = redisRegionCacheService!!.multiRetrieveList(keyPattern!!)
        Assertions.assertThat<String?>(retrievedStringList)
            .isNotNull()
            .hasSize(INT_THREE)
            .containsExactlyElementsOf(multipleValues)
        Mockito.verify(redisTemplate).keys(ArgumentMatchers.anyString())
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
        Mockito.`when`(redisTemplate!!.keys(ArgumentMatchers.anyString())).thenReturn(keyList)
        Mockito.`when`(redisTemplate.opsForValue()).thenReturn(valueOperations)
        Mockito.`when`(valueOperations!!.multiGet(ArgumentMatchers.anyList())).thenReturn(valueList)
        val retrievedStringMap = redisRegionCacheService!!.multiRetrieveMap(keyPattern!!)
        Assertions.assertThat<String, String?>(retrievedStringMap)
            .isNotNull()
            .hasSize(INT_THREE)
            .containsExactlyInAnyOrderEntriesOf(expectedKeyValueMap)
        Mockito.verify(redisTemplate).keys(ArgumentMatchers.anyString())
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
        Mockito.`when`(redisTemplate!!.keys(ArgumentMatchers.anyString())).thenReturn(keyList)
        Mockito.`when`(redisTemplate.opsForValue()).thenReturn(valueOperations)
        Mockito.`when`(valueOperations!!.multiGet(ArgumentMatchers.anyList())).thenReturn(valueList)
        val retrievedStringMap = redisRegionCacheService!!.multiRetrieveMap(keyPattern!!)
        Assertions.assertThat<String, String?>(retrievedStringMap)
            .isNotNull()
            .hasSize(INT_TWO)
            .containsExactlyInAnyOrderEntriesOf(expectedKeyValueMap)
        Mockito.verify(redisTemplate).keys(ArgumentMatchers.anyString())
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
        Assertions.assertThat<String, String?>(retrievedKeyValueMap)
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
        Assertions.assertThat<String, String?>(retrievedKeyValueMap)
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
        Mockito.`when`(redisTemplate!!.keys(ArgumentMatchers.anyString())).thenReturn(keyList)
        Mockito.`when`(redisTemplate.opsForValue()).thenReturn(valueOperations)
        Mockito.`when`(valueOperations!!.multiGet(ArgumentMatchers.anyList())).thenReturn(valueList)
        val retrievedStringMap = redisRegionCacheService!!.multiRetrieveMap()
        Assertions.assertThat<String, String?>(retrievedStringMap)
            .isNotNull()
            .hasSize(INT_THREE)
            .containsExactlyInAnyOrderEntriesOf(expectedKeyValueMap)
        Mockito.verify(redisTemplate).keys(ArgumentMatchers.anyString())
        Mockito.verify(redisTemplate).opsForValue()
        Mockito.verify(valueOperations).multiGet(ArgumentMatchers.anyList())
    }

    @Test
    @DisplayName("Verify we can remove a key from Redis")
    fun givenAKeyThenRemove() {
        Mockito.`when`(redisTemplate!!.delete(ArgumentMatchers.anyString())).thenReturn(Boolean.TRUE)
        val isRemoved = redisRegionCacheService!!.delete(REDIS_THE_KEY)
        Assertions.assertThat(isRemoved)
            .isNotNull()
            .isTrue()
        Mockito.verify(redisTemplate).delete(ArgumentMatchers.anyString())
    }

    @Test
    @DisplayName("Verify we can remove a key from Redis")
    fun givenAListOfKeysThenBulkRemove() {
        val multipleKeys = assignMultipleKeys()
        Mockito.`when`<Long>(redisTemplate!!.delete(ArgumentMatchers.anyList<String>())).thenReturn(LONG_THREE)
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
        Mockito.`when`(redisTemplate!!.keys(ArgumentMatchers.anyString())).thenReturn(multipleKeys)
        val numItems = redisRegionCacheService!!.count()
        Assertions.assertThat(numItems)
            .isNotNull()
            .isEqualTo(LONG_THREE)
        Mockito.verify(redisTemplate).keys(ArgumentMatchers.anyString())
    }

    @Test
    @DisplayName("Verify we retrieve selected items in the Cache Region")
    fun shouldCountSelectedItemsInCacheRegion() {
        val multipleKeys = assignMultipleWWKeys()
        Mockito.`when`(redisTemplate!!.keys(ArgumentMatchers.anyString())).thenReturn(multipleKeys)
        val numItems = redisRegionCacheService!!.count(KEY_PATTERN_WW)
        Assertions.assertThat(numItems)
            .isNotNull()
            .isEqualTo(LONG_TWO)
        Mockito.verify(redisTemplate).keys(ArgumentMatchers.anyString())
    }

    private fun assignMultipleKeys(): Set<String> {
        // We need the key set to be in the order of insertion.
        val keySet = LinkedHashSet<String>()
        keySet.add(REDIS_KEY_ITEM_1234)
        keySet.add(REDIS_KEY_ITEM_4567)
        keySet.add(REDIS_KEY_ITEM_8901)
        return keySet
    }

    private fun assignMultipleValues(): List<String?> {
        return java.util.List.of(REDIS_VALUE_DM_1234, REDIS_VALUE_DM_4567, REDIS_VALUE_DM_8901)
    }

    private fun assignMultipleValuesWithNullAsObjectList(): List<String?> {
        val valueList = ArrayList<String?>()
        valueList.add(REDIS_VALUE_DM_1234)
        valueList.add(null)
        valueList.add(REDIS_VALUE_DM_8901)
        return valueList
    }

    private fun assignMultipleWWKeys(): Set<String> {
        // We need the key set to be in the order of insertion.
        val keySet = LinkedHashSet<String>()
        keySet.add(REDIS_KEY_ITEM_W_1234)
        keySet.add(REDIS_KEY_ITEM_W_5678)
        return keySet
    }

    private fun expectedKeyValueMap(): Map<String, String?> {
        return createKeyValueMap()
    }

    private fun expectedKeyValueExcludeNullItemMap(): Map<String, String> {
        // We need the key map to be in the order of insertion.
        val expectedMap = LinkedHashMap<String, String>()
        expectedMap[REDIS_KEY_ITEM_1234] = REDIS_VALUE_DM_1234
        expectedMap[REDIS_KEY_ITEM_8901] = REDIS_VALUE_DM_8901
        return expectedMap
    }

    private fun createKeyValueMap(): LinkedHashMap<String, String?> {
        // We need the key map to be in the order of insertion.
        val expectedMap = LinkedHashMap<String, String?>()
        expectedMap[REDIS_KEY_ITEM_1234] = REDIS_VALUE_DM_1234
        expectedMap[REDIS_KEY_ITEM_4567] = REDIS_VALUE_DM_4567
        expectedMap[REDIS_KEY_ITEM_8901] = REDIS_VALUE_DM_8901
        return expectedMap
    }

    companion object {
        private const val REDIS_REGION = "region"
        private const val REDIS_THE_KEY = "thekey"
        private const val REDIS_THE_INFO_SAVED = "The Info saved"
        private const val REDIS_KEY_ITEM_1234 = "ITEM1234"
        private const val REDIS_KEY_ITEM_4567 = "ITEM4567"
        private const val REDIS_KEY_ITEM_8901 = "ITEM8901"
        private const val REDIS_KEY_ITEM_W_1234 = "ITEMW1234"
        private const val REDIS_KEY_ITEM_W_5678 = "ITEMW5678"
        private const val REDIS_VALUE_DM_1234 = "ITEM 1234"
        private const val REDIS_VALUE_DM_4567 = "ITEM 4567"
        private const val REDIS_VALUE_DM_8901 = "ITEM 8901"
        private const val KEY_PATTERN_WW = "WW*"
    }
}
