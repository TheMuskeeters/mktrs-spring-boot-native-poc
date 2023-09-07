/*----------------------------------------------------------------------------*/
/* Source File:   LOCALEKEYVALIDATORSERVICEIMPL.JAVA                          */
/* Copyright (c), 2023 The Musketeers                                         */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Sep.07/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.themusketeers.sbnative.service;

import static com.themusketeers.sbnative.common.consts.GlobalConstants.DOT;
import static com.themusketeers.sbnative.common.consts.GlobalConstants.EMPTY_DEFINITION;
import static com.themusketeers.sbnative.common.consts.GlobalConstants.EN_KEY_MANDATORY;
import static com.themusketeers.sbnative.common.consts.GlobalConstants.INT_ONE;
import static com.themusketeers.sbnative.common.consts.GlobalConstants.INT_ZERO;
import static com.themusketeers.sbnative.common.consts.GlobalConstants.KEY_CONTAINS_MORE_THAN_ONE_TOKEN;
import static com.themusketeers.sbnative.common.consts.GlobalConstants.KEY_LEFT_BRACKET;
import static com.themusketeers.sbnative.common.consts.GlobalConstants.KEY_MULTIPLE_DEFINED;
import static com.themusketeers.sbnative.common.consts.GlobalConstants.KEY_MUST_NOT_CONTAIN_SPACES;
import static com.themusketeers.sbnative.common.consts.GlobalConstants.KEY_RIGHT_BRACKET_SUPPORTED_LIST;
import static com.themusketeers.sbnative.common.consts.GlobalConstants.LOCALE_EN;
import static com.themusketeers.sbnative.common.consts.GlobalConstants.NULL_STR;
import static com.themusketeers.sbnative.common.consts.GlobalConstants.SPACE_STR;

import com.themusketeers.sbnative.service.intr.LocaleKeyValidatorService;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * Helps validate the keys contained in the map about the languages supported
 * in the Marketing Card. (Implementation).
 *
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
@Service
public class LocaleKeyValidatorServiceImpl implements LocaleKeyValidatorService {
    private List<String> validLanguageKeys;

    /**
     * Default constructor.
     */
    public LocaleKeyValidatorServiceImpl() {

        validLanguageKeys =
            Stream.of("en, es, fr")
                .map(s -> s.trim())
                .collect(Collectors.toList());
    }

    @Override
    public List<String> validateErrorsOnKeys(List<String> keys) {
        var errorList = new ArrayList<String>();
        var keyCountMap =
            keys.stream()
                .map(key -> key.trim())
                .map(key -> key.toLowerCase(Locale.getDefault()))
                .collect(Collectors.groupingByConcurrent(Function.identity(), Collectors.counting()));
        var defaultLanguage = LOCALE_EN;

        if (!keyCountMap.keySet().contains(defaultLanguage)) {
            errorList.add(EN_KEY_MANDATORY);
        }

        keys.stream()
            .map(key -> key.toLowerCase(Locale.getDefault()))
            .forEach(
                key -> {
                    var tokens = extractTokens(key);
                    var countMatch = StringUtils.countMatches(key, SPACE_STR);
                    var keyTrimmed = key.trim();

                    if (countMatch > INT_ZERO) {
                        errorList.add(
                            KEY_LEFT_BRACKET
                                + (StringUtils.EMPTY.equals(keyTrimmed) ? EMPTY_DEFINITION : keyTrimmed)
                                + KEY_MUST_NOT_CONTAIN_SPACES);
                    }

                    if (tokens.size() > INT_ONE) {
                        errorList.add(KEY_LEFT_BRACKET + keyTrimmed + KEY_CONTAINS_MORE_THAN_ONE_TOKEN);
                    }
                });

        keyCountMap.forEach(
            (k, v) -> {
                if (!validLanguageKeys.contains(k)) {
                    var isNullOrEmptyKey = StringUtils.EMPTY.equals(k) || NULL_STR.equals(k);

                    errorList.add(
                        KEY_LEFT_BRACKET
                            + (StringUtils.EMPTY.equals(k) ? EMPTY_DEFINITION : k)
                            + KEY_RIGHT_BRACKET_SUPPORTED_LIST
                            + validLanguageKeys
                            + DOT);

                    if (isNullOrEmptyKey) {
                        if (v > INT_ONE) {
                            errorList.add(
                                KEY_LEFT_BRACKET
                                    + (StringUtils.EMPTY.equals(k) ? EMPTY_DEFINITION : k)
                                    + KEY_MULTIPLE_DEFINED);
                        }
                    } else {
                        if (v > INT_ONE) {
                            errorList.add(KEY_LEFT_BRACKET + k + KEY_MULTIPLE_DEFINED);
                        }
                    }
                } else {
                    if (v > INT_ONE) {
                        errorList.add(KEY_LEFT_BRACKET + k + KEY_MULTIPLE_DEFINED);
                    }
                }
            });

        return errorList;
    }

    private List<String> extractTokens(String str) {
        var tokens = new ArrayList<String>();
        var tokenizer = new StringTokenizer(str, SPACE_STR);

        while (tokenizer.hasMoreElements()) {
            tokens.add(tokenizer.nextToken());
        }

        return tokens;
    }
}
