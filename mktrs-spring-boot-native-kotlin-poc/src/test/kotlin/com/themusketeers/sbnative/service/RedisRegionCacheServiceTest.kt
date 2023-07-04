/*----------------------------------------------------------------------------*/
/* Source File:   REDISREGIONCACHESERVICETEST.KT                              */
/* Copyright (c), 2023 The Musketeers                                         */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Jun.27/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.themusketeers.sbnative.service

import com.themusketeers.sbnative.common.consts.GlobalConstants.DOT
import com.themusketeers.sbnative.common.consts.GlobalConstants.INT_THREE
import com.themusketeers.sbnative.common.consts.GlobalConstants.INT_TWO
import com.themusketeers.sbnative.common.consts.GlobalConstants.LONG_THREE
import com.themusketeers.sbnative.common.consts.GlobalConstants.LONG_TWO
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EmptySource
import org.junit.jupiter.params.provider.NullSource
import org.junit.jupiter.params.provider.ValueSource
import org.mockito.ArgumentMatchers.anyList
import org.mockito.ArgumentMatchers.anyMap
import org.mockito.ArgumentMatchers.anySet
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.reset
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ValueOperations

/**
 * Unit tests for [RedisRegionCacheService].
 *
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
@ExtendWith(MockitoExtension::class)
class RedisRegionCacheServiceTest {
    companion object {
        private const val REDIS_REGION = "region"
        private const val REDIS_THE_KEY = "thekey"
        private const val REDIS_THE_INFO_SAVED = "The Info saved"
        private const val REDIS_KEY_ITEM_1234 = "ITEM1234"
        private const val REDIS_KEY_ITEM_4567 = "ITEM4567"
        private const val REDIS_KEY_ITEM_8901 = "ITEM8901"
        private const val REDIS_KEY_ITEM_W_1234 = "ITEMW1234"
        private const val REDIS_KEY_ITEM_W_5678 = "ITEMW5678"
        private const val REDIS_VALUE_ITEM_1234 = "ITEM 1234"
        private const val REDIS_VALUE_ITEM_4567 = "ITEM 4567"
        private const val REDIS_VALUE_ITEM_8901 = "ITEM 8901"
        private const val KEY_PATTERN_AA = "AA*"
    }

    @Mock
    private lateinit var redisTemplate: RedisTemplate<String, String>

    @Mock
    private lateinit var valueOperations: ValueOperations<String, String>
    private lateinit var redisRegionCacheService: RedisRegionCacheService<String, String>

    @BeforeEach
    fun beforeEach() {
        redisRegionCacheService = RedisRegionCacheService(REDIS_REGION, redisTemplate)
        reset(redisTemplate, valueOperations)
    }

    @Test
    @DisplayName("Verify that a key exists.")
    fun shouldCheckAKeyExistsInRedis() {
        `when`(redisTemplate.hasKey(anyString())).thenReturn(true)

        val keyExist = redisRegionCacheService.exists(REDIS_THE_KEY)

        assertThat(keyExist)
            .isNotNull()
            .isTrue()

        verify(redisTemplate).hasKey(anyString())
    }

    @Test
    @DisplayName("Verify that a key does not exist.")
    fun shouldCheckAKeyDoesNotExistInRedis() {
        `when`(redisTemplate.hasKey(anyString())).thenReturn(false)

        val keyExist = redisRegionCacheService.exists(REDIS_THE_KEY)

        assertThat(keyExist)
            .isNotNull()
            .isFalse()

        verify(redisTemplate).hasKey(anyString())
    }

    @Test
    @DisplayName("Verify that a key/value is inserted.")
    fun shouldInsertAStringIntoRedis() {
        `when`(redisTemplate.opsForValue()).thenReturn(valueOperations)
        doNothing().`when`(valueOperations)[anyString()] = anyString()

        redisRegionCacheService.insert(REDIS_THE_KEY, REDIS_THE_INFO_SAVED)

        verify(redisTemplate).opsForValue()
        verify(valueOperations)[anyString()] = anyString()
    }

    @Test
    @DisplayName("Verify that a key/value is inserted. This tests cover a case when 'buildRegionKey' has the region set already.")
    fun shouldInsertAStringIntoRedisAssumesKeyHoldsRegion() {
        `when`(redisTemplate.opsForValue()).thenReturn(valueOperations)
        doNothing().`when`(valueOperations)[anyString()] = anyString()

        redisRegionCacheService.insert(REDIS_REGION + DOT + REDIS_THE_KEY, REDIS_THE_INFO_SAVED)

        verify(redisTemplate).opsForValue()
        verify(valueOperations)[anyString()] = anyString()
    }

    @Test
    @DisplayName("Verify we can add a bulk key/value data.")
    fun shouldPerformABulkInsertIntoRedis() {
        val keyValueMap = expectedKeyValueMap()

        `when`(redisTemplate.opsForValue()).thenReturn(valueOperations)
        doNothing().`when`(valueOperations).multiSet(anyMap())

        redisRegionCacheService.multiInsert(keyValueMap)

        verify(redisTemplate).opsForValue()
        verify(valueOperations).multiSet(anyMap())
    }

    @Test
    @DisplayName("Verify that a key/value is retrieved.")
    fun shouldRetrieveAStringFromRedis() {
        `when`(redisTemplate.opsForValue()).thenReturn(valueOperations)
        `when`(valueOperations[anyString()]).thenReturn(REDIS_THE_INFO_SAVED)

        val retrievedInfo = redisRegionCacheService.retrieve(REDIS_THE_KEY)

        assertThat(retrievedInfo)
            .isNotNull()
            .isEqualTo(REDIS_THE_INFO_SAVED)

        verify(redisTemplate).opsForValue()
        verify(valueOperations)[anyString()]
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    @ValueSource(strings = ["*", "ITEM*", "*ITEM*"])
    @DisplayName("Verify we are able to retrieve a bulk of keys from Redis given a key pattern.")
    fun givenAKeyPatternThenRetrieveKeyListFromRedis(keyPattern: String?) {
        val multipleKeys = assignMultipleKeys()

        `when`(redisTemplate.keys(anyString())).thenReturn(multipleKeys)

        val retrievedStringList = redisRegionCacheService.multiRetrieveKeyList(keyPattern!!)

        assertThat(retrievedStringList)
            .hasSize(INT_THREE)
            .containsExactlyElementsOf(multipleKeys)

        verify(redisTemplate).keys(anyString())
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    @ValueSource(strings = ["*", "ITEM*", "*ITEM*"])
    @DisplayName("Verify we are able to retrieve a bulk of data from Redis given a key pattern.")
    fun givenAKeyPatternThenRetrieveAListOfStringFromRedis(keyPattern: String?) {
        val multipleKeys = assignMultipleKeys()
        val multipleValues = assignMultipleValues()

        `when`(redisTemplate.keys(anyString())).thenReturn(multipleKeys)
        `when`(redisTemplate.opsForValue()).thenReturn(valueOperations)
        `when`(valueOperations.multiGet(anySet())).thenReturn(multipleValues)

        val retrievedStringList = redisRegionCacheService.multiRetrieveList(keyPattern!!)

        assertThat(retrievedStringList)
            .isNotNull()
            .hasSize(INT_THREE)
            .containsExactlyElementsOf(multipleValues)

        verify(redisTemplate).keys(anyString())
        verify(redisTemplate).opsForValue()
        verify(valueOperations).multiGet(anySet())
    }

    @Test
    @DisplayName("Verify we are able to retrieve a bulk of data from Redis given a list of keys to look for.")
    fun givenAListOfKeysThenRetrieveAListOfStringFromRedis() {
        val multipleKeys = assignMultipleKeys()
        val multipleValues = assignMultipleValues()

        `when`(redisTemplate.opsForValue()).thenReturn(valueOperations)
        `when`(valueOperations.multiGet(anyList())).thenReturn(multipleValues)

        val retrievedStringList = redisRegionCacheService.multiRetrieveList(multipleKeys)

        assertThat(retrievedStringList)
            .isNotNull()
            .hasSize(INT_THREE)
            .containsExactlyInAnyOrderElementsOf(multipleValues)

        verify(redisTemplate).opsForValue()
        verify(valueOperations).multiGet(anyList())
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    @ValueSource(strings = ["*", "ITEM*", "*ITEM*"])
    @DisplayName("Verify we are able to retrieve a map containing the key/value pairs from Redis using a key pattern.")
    fun givenAKeyPatternThenRetrieveAMapWithKeyValueDataFromRedis(keyPattern: String?) {
        val keyList = assignMultipleKeys()
        val valueList = assignMultipleValues()
        val expectedKeyValueMap = expectedKeyValueMap()

        `when`(redisTemplate.keys(anyString())).thenReturn(keyList)
        `when`(redisTemplate.opsForValue()).thenReturn(valueOperations)
        `when`(valueOperations.multiGet(anyList())).thenReturn(valueList)

        val retrievedStringMap = redisRegionCacheService.multiRetrieveMap(keyPattern!!)

        assertThat(retrievedStringMap)
            .isNotNull()
            .hasSize(INT_THREE)
            .containsExactlyInAnyOrderEntriesOf(expectedKeyValueMap)

        verify(redisTemplate).keys(anyString())
        verify(redisTemplate).opsForValue()
        verify(valueOperations).multiGet(anyList())
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    @ValueSource(strings = ["*", "ITEM*", "*ITEM*"])
    @DisplayName("Verify we are able to retrieve a map containing the key/value pairs from Redis using a key pattern discarding not found keys (when its value is null).")
    fun givenAKeyPatternThenRetrieveAMapWithKeyValueDataFromRedisDiscardingNotFoundKeys(keyPattern: String?) {
        val keyList = assignMultipleKeys()
        val valueList = assignMultipleValuesWithNullAsObjectList()
        val expectedKeyValueMap = expectedKeyValueExcludeNullItemMap()

        `when`(redisTemplate.keys(anyString())).thenReturn(keyList)
        `when`(redisTemplate.opsForValue()).thenReturn(valueOperations)
        `when`(valueOperations.multiGet(anyList())).thenReturn(valueList)

        val retrievedStringMap = redisRegionCacheService.multiRetrieveMap(keyPattern)

        assertThat(retrievedStringMap)
            .isNotNull()
            .hasSize(INT_TWO)
            .containsExactlyInAnyOrderEntriesOf(expectedKeyValueMap)

        verify(redisTemplate).keys(anyString())
        verify(redisTemplate).opsForValue()
        verify(valueOperations).multiGet(anyList())
    }

    @Test
    @DisplayName("Verify we are able to retrieve a map containing the key/value pairs from Redis using a collection containing the keys to look for.")
    fun givenListOfKeysThenRetrieveAMapWithKeyValueDataFromRedis() {
        val multipleKeys = assignMultipleKeys()
        val multipleValues = assignMultipleValues()
        val expectedKeyValueMap = expectedKeyValueMap()

        `when`(redisTemplate.opsForValue()).thenReturn(valueOperations)
        `when`(valueOperations.multiGet(anyList())).thenReturn(multipleValues)

        val retrievedKeyValueMap = redisRegionCacheService.multiRetrieveMap(multipleKeys)

        assertThat(retrievedKeyValueMap)
            .isNotNull()
            .hasSize(INT_THREE)
            .containsExactlyInAnyOrderEntriesOf(expectedKeyValueMap)

        verify(redisTemplate).opsForValue()
        verify(valueOperations).multiGet(anyList())
    }

    @Test
    @DisplayName("Verify we are able to retrieve a map containing the key/value pairs from Redis using a collection containing the keys to look for, discarding not found keys (which value is null) and discarded by the service.")
    fun givenListOfKeysThenRetrieveAMapWithKeyValueDataFromRedisDiscardingNotFoundKeys() {
        val multipleKeys = assignMultipleKeys()
        val multipleValues = assignMultipleValuesWithNullAsObjectList()
        val expectedKeyValueMap = expectedKeyValueExcludeNullItemMap()

        `when`(redisTemplate.opsForValue()).thenReturn(valueOperations)
        `when`(valueOperations.multiGet(anyList())).thenReturn(multipleValues)

        val retrievedKeyValueMap = redisRegionCacheService.multiRetrieveMap(multipleKeys)

        assertThat(retrievedKeyValueMap)
            .isNotNull()
            .hasSize(INT_TWO)
            .containsExactlyInAnyOrderEntriesOf(expectedKeyValueMap)

        verify(redisTemplate).opsForValue()
        verify(valueOperations).multiGet(anyList())
    }

    @Test
    @DisplayName("Verify we are able to retrieve a map containing all of the key/value pairs from Redis.")
    fun shouldRetrieveAllMapWithKeyValueDataFromRedis() {
        val keyList = assignMultipleKeys()
        val valueList = assignMultipleValues()
        val expectedKeyValueMap = expectedKeyValueMap()

        `when`(redisTemplate.keys(anyString())).thenReturn(keyList)
        `when`(redisTemplate.opsForValue()).thenReturn(valueOperations)
        `when`(valueOperations.multiGet(anyList())).thenReturn(valueList)

        val retrievedStringMap = redisRegionCacheService.multiRetrieveMap()

        assertThat(retrievedStringMap)
            .isNotNull()
            .hasSize(INT_THREE)
            .containsExactlyInAnyOrderEntriesOf(expectedKeyValueMap)

        verify(redisTemplate).keys(anyString())
        verify(redisTemplate).opsForValue()
        verify(valueOperations).multiGet(anyList())
    }

    @Test
    @DisplayName("Verify we can remove a key from Redis")
    fun givenAKeyThenRemove() {
        `when`(redisTemplate.delete(anyString())).thenReturn(true)

        val isRemoved = redisRegionCacheService.delete(REDIS_THE_KEY)

        assertThat(isRemoved)
            .isNotNull()
            .isTrue()

        verify(redisTemplate).delete(anyString())
    }

    @Test
    @DisplayName("Verify we can remove a key from Redis")
    fun givenAListOfKeysThenBulkRemove() {
        val multipleKeys = assignMultipleKeys()

        `when`(redisTemplate.delete(anyList())).thenReturn(LONG_THREE)

        val numKeysRemoved = redisRegionCacheService.delete(multipleKeys)

        assertThat(numKeysRemoved)
            .isNotNull()
            .isEqualTo(LONG_THREE)

        verify(redisTemplate).delete(anyList())
    }

    @Test
    @DisplayName("Verify we retrieve all the items in the Cache Region")
    fun shouldCountAllItemsInCacheRegion() {
        val multipleKeys = assignMultipleKeys()

        `when`(redisTemplate.keys(anyString())).thenReturn(multipleKeys)

        val numItems = redisRegionCacheService.count()

        assertThat(numItems)
            .isNotNull()
            .isEqualTo(LONG_THREE)

        verify(redisTemplate).keys(anyString())
    }

    @Test
    @DisplayName("Verify we retrieve selected items in the Cache Region")
    fun shouldCountSelectedItemsInCacheRegion() {
        val multipleKeys = assignMultipleItemWKeys()

        `when`(redisTemplate.keys(anyString())).thenReturn(multipleKeys)

        val numItems = redisRegionCacheService.count(KEY_PATTERN_AA)

        assertThat(numItems)
            .isNotNull()
            .isEqualTo(LONG_TWO)

        verify(redisTemplate).keys(anyString())
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
        return listOf(REDIS_VALUE_ITEM_1234, REDIS_VALUE_ITEM_4567, REDIS_VALUE_ITEM_8901)
    }

    private fun assignMultipleValuesWithNullAsObjectList(): List<String?> {
        val valueList = ArrayList<String?>()

        valueList.add(REDIS_VALUE_ITEM_1234)
        valueList.add(null)
        valueList.add(REDIS_VALUE_ITEM_8901)

        return valueList
    }

    private fun assignMultipleItemWKeys(): Set<String> {
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

        expectedMap[REDIS_KEY_ITEM_1234] = REDIS_VALUE_ITEM_1234
        expectedMap[REDIS_KEY_ITEM_8901] = REDIS_VALUE_ITEM_8901

        return expectedMap
    }

    private fun createKeyValueMap(): LinkedHashMap<String, String> {
        // We need the key map to be in the order of insertion.
        val expectedMap = LinkedHashMap<String, String>()

        expectedMap[REDIS_KEY_ITEM_1234] = REDIS_VALUE_ITEM_1234
        expectedMap[REDIS_KEY_ITEM_4567] = REDIS_VALUE_ITEM_4567
        expectedMap[REDIS_KEY_ITEM_8901] = REDIS_VALUE_ITEM_8901

        return expectedMap
    }
}
