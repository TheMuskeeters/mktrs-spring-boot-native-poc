package com.themusketeers.sbnative.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class LocaleKeyValidatorServiceImpl implements LocaleKeyValidatorService {
    private static final String EMPTY_DEFINITION = "\"\"";
    private static final String DOT = ".";
    private static final String COMMA = ",";
    private static final String SPACE_STR = " ";
    private static final String NULL_STR = "null";
    private static final String KEY_LEFT_BRACKET = "Key [";
    private static final String KEY_MULTIPLE_DEFINED = "] in 'localeData' has more than one value defined.";
    private static final String KEY_RIGHT_BRACKET_SUPPORTED_LIST = "] in 'localeData' is not supported as valid locale key. List of supported language keys are ";
    private static final String KEY_MUST_NOT_CONTAIN_SPACES = "] must not contain spaces.";
    private static final String KEY_CONTAINS_MORE_THAN_ONE_TOKEN = "] contains more than one token.";
    private static final String EN_KEY_MANDATORY = "EN key is mandatory to be present in 'localeData'.";

    private static final int INT_ZERO = 0;
    private static final int INT_ONE = 1;

    private String defaultLanguage = "en";

    private List<String> validLanguageKeys;

    public LocaleKeyValidatorServiceImpl() {

        validLanguageKeys =
            List.of("en, fr, es")
                .stream()
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
