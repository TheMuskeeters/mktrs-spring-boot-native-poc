/*----------------------------------------------------------------------------*/ /* Source File:   LOCALEKEYVALIDATORSERVICEIMPL.JAVA                          */ /* Copyright (c), 2023 The Musketeers                                         */ /*----------------------------------------------------------------------------*/ /*-----------------------------------------------------------------------------
 History
 Sep.07/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.themusketeers.sbnative.service

import com.themusketeers.sbnative.common.consts.GlobalConstants.DOT
import com.themusketeers.sbnative.common.consts.GlobalConstants.EMPTY_DEFINITION
import com.themusketeers.sbnative.common.consts.GlobalConstants.EN_KEY_MANDATORY
import com.themusketeers.sbnative.common.consts.GlobalConstants.INT_ONE
import com.themusketeers.sbnative.common.consts.GlobalConstants.INT_ZERO
import com.themusketeers.sbnative.common.consts.GlobalConstants.KEY_CONTAINS_MORE_THAN_ONE_TOKEN
import com.themusketeers.sbnative.common.consts.GlobalConstants.KEY_LEFT_BRACKET
import com.themusketeers.sbnative.common.consts.GlobalConstants.KEY_MULTIPLE_DEFINED
import com.themusketeers.sbnative.common.consts.GlobalConstants.KEY_MUST_NOT_CONTAIN_SPACES
import com.themusketeers.sbnative.common.consts.GlobalConstants.KEY_RIGHT_BRACKET_SUPPORTED_LIST
import com.themusketeers.sbnative.common.consts.GlobalConstants.LOCALE_EN
import com.themusketeers.sbnative.common.consts.GlobalConstants.NULL_STR
import com.themusketeers.sbnative.common.consts.GlobalConstants.SPACE_STR
import com.themusketeers.sbnative.service.intr.LocaleKeyValidatorService
import java.util.Locale
import java.util.StringTokenizer
import java.util.function.Function
import java.util.stream.Collectors
import java.util.stream.Stream
import org.apache.commons.lang3.StringUtils
import org.springframework.stereotype.Service

/**
 * Helps validate the keys contained in the map about the languages supported
 * in the Marketing Card. (Implementation).
 *
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
@Service
class LocaleKeyValidatorServiceImpl : LocaleKeyValidatorService {
    private val validLanguageKeys: List<String>

    /**
     * Default initialization.
     */
    init {
        validLanguageKeys = Stream.of("en, es, fr")
            .map { s: String -> s.trim { it <= ' ' } }
            .collect(Collectors.toList())
    }

    override fun validateErrorsOnKeys(keys: List<String>): List<String> {
        val errorList = ArrayList<String>()
        val keyCountMap = keys.stream()
            .map { key: String -> key.trim { it <= ' ' } }
            .map { key: String -> key.lowercase(Locale.getDefault()) }
            .collect(Collectors.groupingByConcurrent<String, String, Any, Long>(Function.identity(), Collectors.counting()))
        val defaultLanguage: String = LOCALE_EN

        if (!keyCountMap.keys.contains(defaultLanguage)) {
            errorList.add(EN_KEY_MANDATORY)
        }

        keys.stream()
            .map<String> { key: String? -> key!!.lowercase(Locale.getDefault()) }
            .forEach { key: String ->
                val tokens = extractTokens(key)
                val countMatch: Int = StringUtils.countMatches(key, SPACE_STR)
                val keyTrimmed = key.trim { it <= ' ' }
                if (countMatch > INT_ZERO) {
                    errorList.add(
                        KEY_LEFT_BRACKET
                            + (if (StringUtils.EMPTY == keyTrimmed) EMPTY_DEFINITION else keyTrimmed)
                            + KEY_MUST_NOT_CONTAIN_SPACES
                    )
                }
                if (tokens.size > INT_ONE) {
                    errorList.add(KEY_LEFT_BRACKET + keyTrimmed + KEY_CONTAINS_MORE_THAN_ONE_TOKEN)
                }
            }

        keyCountMap.forEach { (k: String, v: Long) ->
            if (!validLanguageKeys.contains(k)) {
                val isNullOrEmptyKey = StringUtils.EMPTY == k || NULL_STR == k
                errorList.add(
                    KEY_LEFT_BRACKET
                        + (if (StringUtils.EMPTY == k) EMPTY_DEFINITION else k)
                        + KEY_RIGHT_BRACKET_SUPPORTED_LIST
                        + validLanguageKeys
                        + DOT
                )
                if (isNullOrEmptyKey) {
                    if (v > INT_ONE) {
                        errorList.add(
                            KEY_LEFT_BRACKET
                                + (if (StringUtils.EMPTY == k) EMPTY_DEFINITION else k)
                                + KEY_MULTIPLE_DEFINED
                        )
                    }
                } else {
                    if (v > INT_ONE) {
                        errorList.add(KEY_LEFT_BRACKET + k + KEY_MULTIPLE_DEFINED)
                    }
                }
            } else {
                if (v > INT_ONE) {
                    errorList.add(KEY_LEFT_BRACKET + k + KEY_MULTIPLE_DEFINED)
                }
            }
        }
        return errorList
    }

    private fun extractTokens(str: String): List<String> {
        val tokens = ArrayList<String>()
        val tokenizer = StringTokenizer(str, SPACE_STR)
        while (tokenizer.hasMoreElements()) {
            tokens.add(tokenizer.nextToken())
        }
        return tokens
    }
}
