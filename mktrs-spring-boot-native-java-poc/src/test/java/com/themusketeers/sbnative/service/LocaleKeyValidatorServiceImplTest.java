/*----------------------------------------------------------------------------*/
/* Source File:   LOCALEKEYVALIDATORSERVICEIMPLTEST.JAVA                      */
/* Copyright (c), 2023 The Musketeers                                         */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Sep.07/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.themusketeers.sbnative.service;

import static com.themusketeers.sbnative.common.consts.GlobalConstants.INT_ZERO;
import static org.assertj.core.api.Assertions.assertThat;

import com.themusketeers.sbnative.service.intr.LocaleKeyValidatorService;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit Tests for LocaleKeyValidatorService.
 *
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
class LocaleKeyValidatorServiceImplTest {
    private static final String CRLF = "\r\n";
    private static final String LOCALE_EMPTY_KEY = "";
    private static final String LOCALE_EMPTY_KEY_DUPLICATE = " ";
    private static final String LOCALE_EN_KEY = "en";
    private static final String LOCALE_ES_KEY = "es";
    private static final String LOCALE_MM_KEY = "mm";
    private static final String LOCALE_MM_SPACES_KEY = " mm ";
    private static final String LOCALE_ES_UPPERCASE = "ES";
    private static final String LOCALE_ES_SPACES_KEY = "    es    ";
    private static final String LOCALE_KEY_ES_ES_SPACES_DUPLICATE = " es es ";
    private static final String LOCALE_KEY_MM_MM_SPACES_DUPLICATE = " mm mm ";
    private static final String LOCALE_SPACES_KEY = "   ";
    private static final String LOCALE_NULL_KEY = "null";
    private static final String LOCALE_NULL_SPACES_KEY = " null ";
    private static final String LOCALE_KEY_ES_MUST_NOT_CONTAIN_SPACES = "Key [es] must not contain spaces.";
    private static final String LOCALE_KEY_ES_ES_MUST_NOT_CONTAIN_SPACES = "Key [es es] must not contain spaces.";
    private static final String LOCALE_MM_MUST_NOT_CONTAIN_SPACES = "Key [mm] must not contain spaces.";
    private static final String LOCALE_MM_MM_MUST_NOT_CONTAIN_SPACES = "Key [mm mm] must not contain spaces.";
    private static final String LOCALE_KEY_EMPTY_MUST_NOT_CONTAIN_SPACES = "Key [\"\"] must not contain spaces.";
    private static final String LOCALE_NULL_MUST_NOT_CONTAIN_SPACES = "Key [null] must not contain spaces.";
    private static final String KEY_MM_IN_LOCALE_DATA_HAS_MORE_THAN_ONE_VALUE_DEFINED = "Key [mm] in 'localeData' has more than one value defined.";
    private static final String LOCALE_KEY_ES_ES_CONTAINS_MORE_THAN_ONE_TOKEN = "Key [es es] contains more than one token.";
    private static final String LOCALE_KEY_MM_MM_CONTAINS_MORE_THAN_ONE_TOKEN = "Key [mm mm] contains more than one token.";
    private static final String LOCALE_KEY_ES_ES_NOT_SUPPORTED_AS_VALID_LOCALE = "Key [es es] in 'localeData' is not supported as valid locale key. List of supported language keys are [en, es, fr].";
    private static final String LOCALE_KEY_MM_MM_NOT_SUPPORTED_AS_VALID_LOCALE = "Key [mm mm] in 'localeData' is not supported as valid locale key. List of supported language keys are [en, es, fr].";
    private static final String LOCALE_KEY_MM_NOT_SUPPORTED_AS_VALID_LOCALE = "Key [mm] in 'localeData' is not supported as valid locale key. List of supported language keys are [en, es, fr].";
    private static final String LOCALE_KEY_ES_MORE_THAN_ONE_VALUE_DEFINED = "Key [es] in 'localeData' has more than one value defined.";
    private static final String LOCALE_KEY_EMPTY_MORE_THAN_ONE_VALUE_DEFINED = "Key [\"\"] in 'localeData' has more than one value defined.";
    private static final String LOCALE_KEY_EMPTY_NOT_SUPPORTED_AS_VALID_LOCALE = "Key [\"\"] in 'localeData' is not supported as valid locale key. List of supported language keys are [en, es, fr].";
    private static final String LOCALE_KEY_EMPTY_CONTAINS_MORE_THAN_ONE_VALUE_DEFINED = "Key [\"\"] in 'localeData' has more than one value defined.";
    private static final String LOCALE_KEY_NULL_NOT_SUPPORTED_AS_VALID_LOCALE = "Key [null] in 'localeData' is not supported as valid locale key. List of supported language keys are [en, es, fr].";
    private static final String LOCALE_KEY_NULL_CONTAINS_MORE_THAN_ONE_VALUE_DEFINED = "Key [null] in 'localeData' has more than one value defined.";
    private static final String EN_KEY_MANDATORY_IN_LOCALE_DATA = "EN key is mandatory to be present in 'localeData'.";

    private LocaleKeyValidatorService localeKeyValidatorService;

    @BeforeEach
    public void setUp() {
        /*
        var errorsOnKeys =
        localeKeyValidatorService.validateErrorsOnKeys(
            localeData.keySet().stream().collect(Collectors.toList()));

    if (errorsOnKeys.size() > INT_ZERO) {
      throw new IllegalArgumentException(StringUtils.join(errorsOnKeys, CRLF));
    }
         */


        localeKeyValidatorService = new LocaleKeyValidatorServiceImpl();
    }

    @Test
    void whenLocaleENIsNotPresentReturnErrorMessages() {
        var errorsOnKeys = localeKeyValidatorService.validateErrorsOnKeys(List.of(LOCALE_ES_KEY));

        assertThat(errorsOnKeys)
            .isNotNull()
            .isNotEmpty()
            .size()
            .isGreaterThan(INT_ZERO);

        var messages = extractMessages(errorsOnKeys);

        assertThat(messages)
            .isEqualTo(EN_KEY_MANDATORY_IN_LOCALE_DATA);
    }

    @Test
    void givenLocaleESWithSpacesReturnErrorMessages() {
        var errorsOnKeys = localeKeyValidatorService.validateErrorsOnKeys(List.of(LOCALE_EN_KEY, LOCALE_ES_SPACES_KEY));

        assertThat(errorsOnKeys)
            .isNotNull()
            .isNotEmpty()
            .size()
            .isGreaterThan(INT_ZERO);

        var messages = extractMessages(errorsOnKeys);

        assertThat(messages)
            .isEqualTo(LOCALE_KEY_ES_MUST_NOT_CONTAIN_SPACES);
    }

    @Test
    void givenLocaleESWithSpacesAndDuplicatedThenReturnErrorMessages() {
        var expectedErrorMessages = buildLocaleESWithSpacesAndDuplicateErrorMessages();
        var errorsOnKeys = localeKeyValidatorService.validateErrorsOnKeys(List.of(LOCALE_EN_KEY, LOCALE_KEY_ES_ES_SPACES_DUPLICATE));

        assertThat(errorsOnKeys)
            .isNotNull()
            .isNotEmpty()
            .size()
            .isGreaterThan(INT_ZERO);

        var messages = extractMessages(errorsOnKeys);

        assertThat(messages).isEqualTo(expectedErrorMessages);
    }

    @Test
    void givenLocaleESLowerAndUpperThenReturnErrorMessages() {
        var errorsOnKeys = localeKeyValidatorService.validateErrorsOnKeys(List.of(LOCALE_EN_KEY, LOCALE_ES_KEY, LOCALE_ES_UPPERCASE));

        assertThat(errorsOnKeys)
            .isNotNull()
            .isNotEmpty()
            .size()
            .isGreaterThan(INT_ZERO);

        var messages = extractMessages(errorsOnKeys);

        assertThat(messages)
            .isEqualTo(LOCALE_KEY_ES_MORE_THAN_ONE_VALUE_DEFINED);
    }

    @Test
    void givenLocaleNotDefinedISupportedLowerAndUpperThenReturnErrorMessages() {
        var errorsOnKeys = localeKeyValidatorService.validateErrorsOnKeys(List.of(LOCALE_EN_KEY, LOCALE_MM_KEY));

        assertThat(errorsOnKeys)
            .isNotNull()
            .isNotEmpty()
            .size()
            .isGreaterThan(INT_ZERO);

        var messages = extractMessages(errorsOnKeys);

        assertThat(messages)
            .isEqualTo(LOCALE_KEY_MM_NOT_SUPPORTED_AS_VALID_LOCALE);
    }

    @Test
    void givenLocaleEmptyAndNullKeysSetNotDefinedISupportedLowerAndUpperThenReturnErrorMessages() {
        var expectedErrorMessages = buildLocaleEMPTYAndNullErrorMessages();
        var errorsOnKeys = localeKeyValidatorService.validateErrorsOnKeys(List.of(LOCALE_EN_KEY, LOCALE_EMPTY_KEY, LOCALE_NULL_KEY));

        assertThat(errorsOnKeys)
            .isNotNull()
            .isNotEmpty()
            .size()
            .isGreaterThan(INT_ZERO);

        var messages = extractMessages(errorsOnKeys);

        assertThat(messages)
            .isEqualTo(expectedErrorMessages);
    }

    @Test
    void givenLocaleEmptyDuplicateThenReturnErrorMessages() {
        var expectedErrorMessages = buildLocaleEMPTYDuplicateErrorMessages();
        var errorsOnKeys = localeKeyValidatorService.validateErrorsOnKeys(List.of(LOCALE_EN_KEY, LOCALE_EMPTY_KEY, LOCALE_EMPTY_KEY_DUPLICATE));

        assertThat(errorsOnKeys)
            .isNotNull()
            .isNotEmpty()
            .size()
            .isGreaterThan(INT_ZERO);

        var messages = extractMessages(errorsOnKeys);

        assertThat(messages)
            .isEqualTo(expectedErrorMessages);
    }

    @Test
    void givenEmptyAndNullWithDuplicateKeysThenReturnErrorMessages() {
        var expectedErrorMessages = buildLocaleEMPTYAndNullErrorMessagesDuplicates();
        var errorsOnKeys = localeKeyValidatorService.validateErrorsOnKeys(List.of(LOCALE_EN_KEY, LOCALE_EMPTY_KEY, LOCALE_SPACES_KEY, LOCALE_NULL_KEY, LOCALE_NULL_SPACES_KEY));

        assertThat(errorsOnKeys)
            .isNotNull()
            .isNotEmpty()
            .size()
            .isGreaterThan(INT_ZERO);

        var messages = extractMessages(errorsOnKeys);

        assertThat(messages)
            .isEqualTo(expectedErrorMessages);
    }

    @Test
    void given2EmptyAndNullWithDuplicateKeysThenReturnErrorMessages() {
        var expectedErrorMessages = buildLocaleMMSpacesErrorMessages();
        var errorsOnKeys = localeKeyValidatorService.validateErrorsOnKeys(List.of(LOCALE_EN_KEY, LOCALE_MM_KEY, LOCALE_MM_SPACES_KEY, LOCALE_KEY_MM_MM_SPACES_DUPLICATE));

        assertThat(errorsOnKeys)
            .isNotNull()
            .isNotEmpty()
            .size()
            .isGreaterThan(INT_ZERO);

        var messages = extractMessages(errorsOnKeys);

        assertThat(messages)
            .isEqualTo(expectedErrorMessages);
    }

    @Test
    void givenSupportedLocaleKeysThenNoErrorMessagesReturned() {
        var errorsOnKeys =
            localeKeyValidatorService.validateErrorsOnKeys(List.of(LOCALE_EN_KEY, LOCALE_ES_KEY));

        assertThat(errorsOnKeys)
            .isNotNull()
            .isEmpty();
    }

    private String buildLocaleMMSpacesErrorMessages() {
        return extractMessages(
            List.of(
                LOCALE_MM_MUST_NOT_CONTAIN_SPACES,
                LOCALE_MM_MM_MUST_NOT_CONTAIN_SPACES,
                LOCALE_KEY_MM_MM_CONTAINS_MORE_THAN_ONE_TOKEN,
                LOCALE_KEY_MM_NOT_SUPPORTED_AS_VALID_LOCALE,
                KEY_MM_IN_LOCALE_DATA_HAS_MORE_THAN_ONE_VALUE_DEFINED,
                LOCALE_KEY_MM_MM_NOT_SUPPORTED_AS_VALID_LOCALE));
    }

    private String buildLocaleEMPTYAndNullErrorMessagesDuplicates() {
        return extractMessages(
            List.of(
                LOCALE_KEY_EMPTY_MUST_NOT_CONTAIN_SPACES,
                LOCALE_NULL_MUST_NOT_CONTAIN_SPACES,
                LOCALE_KEY_EMPTY_NOT_SUPPORTED_AS_VALID_LOCALE,
                LOCALE_KEY_EMPTY_CONTAINS_MORE_THAN_ONE_VALUE_DEFINED,
                LOCALE_KEY_NULL_NOT_SUPPORTED_AS_VALID_LOCALE,
                LOCALE_KEY_NULL_CONTAINS_MORE_THAN_ONE_VALUE_DEFINED));
    }

    private String buildLocaleEMPTYDuplicateErrorMessages() {
        return extractMessages(
            List.of(
                LOCALE_KEY_EMPTY_MUST_NOT_CONTAIN_SPACES,
                LOCALE_KEY_EMPTY_NOT_SUPPORTED_AS_VALID_LOCALE,
                LOCALE_KEY_EMPTY_MORE_THAN_ONE_VALUE_DEFINED));
    }

    private String buildLocaleEMPTYAndNullErrorMessages() {
        return extractMessages(
            List.of(
                LOCALE_KEY_EMPTY_NOT_SUPPORTED_AS_VALID_LOCALE,
                LOCALE_KEY_NULL_NOT_SUPPORTED_AS_VALID_LOCALE));
    }

    private String buildLocaleESWithSpacesAndDuplicateErrorMessages() {
        return extractMessages(
            List.of(
                LOCALE_KEY_ES_ES_MUST_NOT_CONTAIN_SPACES,
                LOCALE_KEY_ES_ES_CONTAINS_MORE_THAN_ONE_TOKEN,
                LOCALE_KEY_ES_ES_NOT_SUPPORTED_AS_VALID_LOCALE));
    }

    private String extractMessages(List<String> messages) {
        return StringUtils.join(messages, CRLF);
    }
}
