/*----------------------------------------------------------------------------*/
/* Source File:   USERCONTROLLERTEST.JAVA                                     */
/* Copyright (c), 2023 The Musketeers                                         */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Jun.22/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.themusketeers.sbnative.controller.api.v1;

import static com.themusketeers.sbnative.common.consts.ControllerExceptionHandlerConstants.ERROR_CATEGORY_GENERIC;
import static com.themusketeers.sbnative.common.consts.ControllerExceptionHandlerConstants.ERROR_CATEGORY_PARAMETERS;
import static com.themusketeers.sbnative.common.consts.ControllerExceptionHandlerConstants.TITLE_BAD_REQUEST_ON_PAYLOAD;
import static com.themusketeers.sbnative.common.consts.ControllerExceptionHandlerConstants.TITLE_USER_NOT_FOUND;
import static com.themusketeers.sbnative.common.consts.ControllerExceptionHandlerConstants.TITLE_VALIDATION_ERROR_ON_SUPPLIED_PAYLOAD;
import static com.themusketeers.sbnative.common.consts.ExceptionConstants.NOT_FOUND;
import static com.themusketeers.sbnative.common.consts.ExceptionConstants.USER_WITH_ID;
import static com.themusketeers.sbnative.common.consts.GlobalConstants.LONG_TWO;
import static com.themusketeers.sbnative.common.consts.GlobalConstants.LONG_ZERO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.themusketeers.sbnative.domain.User;
import com.themusketeers.sbnative.domain.response.UserDataResponse;
import com.themusketeers.sbnative.domain.response.UsersDataResponse;
import com.themusketeers.sbnative.service.intr.UserService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.client.MockMvcWebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

/**
 * Unit test for checking {@link UserController} api.
 * This does not make the call to the real web server.
 * <p><b>Path:</b>{@code api/v1/users}</p>
 * <p>Some reference links follow:
 * <ul>
 *     <li><a href="https://rieckpil.de/test-your-spring-mvc-controller-with-webtestclient-against-mockmvc/">Test Your Spring MVC Controller with the WebTestClient and MockMvc</a></li>
 *     <li><a href="https://www.callicoder.com/spring-5-reactive-webclient-webtestclient-examples/">Spring 5 WebClient and WebTestClient Tutorial with Examples</a></li>
 * </ul>
 * </p>
 *
 * @author COQ- Carlos Adolfo Ortiz Q.
 */
@WebMvcTest(UserController.class)
class UserControllerTest {
    public static final String USER_CONTROLLER_BASE_PATH = "/api/v1/users";
    public static final String EXPECTED_ERROR_ADDRESS_IS_MANDATORY = "address: User Address is mandatory";
    public static final String EXPECTED_ERROR_NAME_USER_IS_MANDATORY = "name: User Name is mandatory";
    public static final String JSONPATH_TITLE = "$.title";
    public static final String JSONPATH_DETAIL = "$.detail";
    public static final String JSONPATH_ERROR_CATEGORY = "$.errorCategory";
    public static final String JSONPATH_BODY_TITLE = "$.body.title";
    public static final String JSONPATH_BODY_DETAIL = "$.body.detail";
    public static final String JSONPATH_BODY_ERROR_CATEGORY = "$.body.errorCategory";
    public static final String JSONPATH_BODY_ERRORS = "$.body.errors";
    public static final String JSONPATH_BODY_ERRORS_0 = "$.body.errors[0]";
    public static final String JSONPATH_BODY_ERRORS_1 = "$.body.errors[1]";
    public static final String USER_NAME = "USER_NAME";
    public static final String USER_ADDRESS = "USER_ADDRESS";
    public static final String USER_ID_UUID = "53eb385f-582d-4a13-8275-c26a5de6655c";
    public static final String USER_ID_NULL = "null";
    public static final String USER_ID_EMPTY = "";
    public static final String USER_ID_ONE = "c56b2741-028e-4ff5-9e15-be4f96b4ea35";
    public static final String USER_ID_TWO = "b94f6ae6-e1d2-4fdf-8c6b-eb471da1d4d1";
    public static final String USER_NAME_ONE = "Name One";
    public static final String USER_NAME_ONE_UPDATED = "Name One Updated";
    public static final String USER_NAME_TWO = "Name Two";
    public static final String USER_ADDRESS_ONE = "Address One";
    public static final String USER_ADDRESS_ONE_UPDATED = "Address One Updated";
    public static final String USER_ADDRESS_TWO = "Address Two";
    public static final String USER_ID_PATH_VARIABLE = "/{userId}";
    public static final String HTTP_400_BAD_REQUEST_RESPONSE = """
        {"type":"about:blank","title":"Bad Request","status":400,"detail":"Failed to read request","instance":"/api/v1/users"}""";

    private WebTestClient client;

    @MockBean
    private UserService userService;

    @BeforeEach
    void beforeEach(@Autowired MockMvc mockMvc) {
        this.client = MockMvcWebTestClient
            .bindTo(mockMvc)
            .build();
    }

    @Test
    @DisplayName("Should Retrieve an empty list of users")
    void shouldRetrieveAnEmptyUserList() {
        client.get()
            .uri(USER_CONTROLLER_BASE_PATH)
            .accept(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .exchange()
            .expectStatus().isOk()
            .expectBody(UsersDataResponse.class)
            .consumeWith(response -> {
                var resBody = response.getResponseBody();

                assertThat(resBody).isNotNull();
                assertThat(resBody.count()).isNotNull().isEqualTo(LONG_ZERO);
                assertThat(resBody.users()).isNotNull().isEmpty();
            });
    }

    @Test
    @DisplayName("Should Retrieve a list of users")
    void shouldRetrieveListWithUsers() {
        var userList = buildUserList();

        when(userService.count()).thenReturn(LONG_TWO);
        when(userService.retrieveAll()).thenReturn(userList);

        client.get()
            .uri(USER_CONTROLLER_BASE_PATH)
            .accept(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .exchange()
            .expectStatus().isOk()
            .expectBody(UsersDataResponse.class)
            .consumeWith(response -> {
                var resBody = response.getResponseBody();

                assertThat(resBody).isNotNull();
                assertThat(resBody.count()).isNotNull().isEqualTo(LONG_TWO);
                assertThat(resBody.users()).isNotNull().isNotEmpty().hasSameElementsAs(userList);
            });

        verify(userService).count();
        verify(userService).retrieveAll();
    }

    @Test
    @DisplayName("When no payload is sent for creating a new record, then a BAD request is given.")
    void whenUserCreateNoPayloadSentShouldReturnBadRequest() {

        client.post()
            .uri(USER_CONTROLLER_BASE_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody(String.class)
            .consumeWith(response -> assertThat(response.getResponseBody()).isEqualTo(HTTP_400_BAD_REQUEST_RESPONSE));
    }

    @Test
    @DisplayName("When payload is empty for creating a new record, then BAD request is given.")
    void whenUserCreatePayloadEmptyShouldReturnBadRequest() {
        var jsonPayload = "{}";

        client.post()
            .uri(USER_CONTROLLER_BASE_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .bodyValue(jsonPayload)
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody()
            .jsonPath(JSONPATH_BODY_TITLE).isEqualTo(TITLE_BAD_REQUEST_ON_PAYLOAD)
            .jsonPath(JSONPATH_BODY_DETAIL).isEqualTo(TITLE_VALIDATION_ERROR_ON_SUPPLIED_PAYLOAD)
            .jsonPath(JSONPATH_BODY_ERROR_CATEGORY).isEqualTo(ERROR_CATEGORY_PARAMETERS)
            .jsonPath(JSONPATH_BODY_ERRORS).isArray()
            .jsonPath(JSONPATH_BODY_ERRORS_0).isEqualTo(EXPECTED_ERROR_ADDRESS_IS_MANDATORY)
            .jsonPath(JSONPATH_BODY_ERRORS_1).isEqualTo(EXPECTED_ERROR_NAME_USER_IS_MANDATORY);
    }

    @Test
    @DisplayName("When payload fields are null for creating a new record, then BAD request is given.")
    void whenUserCreatePayloadFieldsNullShouldReturnBadRequest() {
        var jsonPayload = """
            {
              "name": null,
              "address": null
            }
            """;

        client.post()
            .uri(USER_CONTROLLER_BASE_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .bodyValue(jsonPayload)
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody()
            .jsonPath(JSONPATH_BODY_TITLE).isEqualTo(TITLE_BAD_REQUEST_ON_PAYLOAD)
            .jsonPath(JSONPATH_BODY_DETAIL).isEqualTo(TITLE_VALIDATION_ERROR_ON_SUPPLIED_PAYLOAD)
            .jsonPath(JSONPATH_BODY_ERROR_CATEGORY).isEqualTo(ERROR_CATEGORY_PARAMETERS)
            .jsonPath(JSONPATH_BODY_ERRORS).isArray()
            .jsonPath(JSONPATH_BODY_ERRORS_0).isEqualTo(EXPECTED_ERROR_ADDRESS_IS_MANDATORY)
            .jsonPath(JSONPATH_BODY_ERRORS_1).isEqualTo(EXPECTED_ERROR_NAME_USER_IS_MANDATORY);
    }

    @Test
    @DisplayName("When payload fields are empty for creating a new record, then BAD request is given.")
    void whenUserCreatePayloadFieldsEmptyShouldReturnBadRequest() {
        var jsonPayload = """
            {
              "name": "",
              "address": ""
            }
            """;

        client.post()
            .uri(USER_CONTROLLER_BASE_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .bodyValue(jsonPayload)
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody()
            .jsonPath(JSONPATH_BODY_TITLE).isEqualTo(TITLE_BAD_REQUEST_ON_PAYLOAD)
            .jsonPath(JSONPATH_BODY_DETAIL).isEqualTo(TITLE_VALIDATION_ERROR_ON_SUPPLIED_PAYLOAD)
            .jsonPath(JSONPATH_BODY_ERROR_CATEGORY).isEqualTo(ERROR_CATEGORY_PARAMETERS)
            .jsonPath(JSONPATH_BODY_ERRORS).isArray()
            .jsonPath(JSONPATH_BODY_ERRORS_0).isEqualTo(EXPECTED_ERROR_ADDRESS_IS_MANDATORY)
            .jsonPath(JSONPATH_BODY_ERRORS_1).isEqualTo(EXPECTED_ERROR_NAME_USER_IS_MANDATORY);
    }

    @Test
    @DisplayName("When payload field address is not empty, name is null/empty, for creating a new record then BAD request is given.")
    void whenUserCreatePayloadFieldAddressIsNotEmptyNameNullOrEmptyShouldReturnBadRequest() {
        var jsonPayload = """
            {
              "name": "",
              "address": "address"
            }
            """;

        client.post()
            .uri(USER_CONTROLLER_BASE_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .bodyValue(jsonPayload)
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody()
            .jsonPath(JSONPATH_BODY_TITLE).isEqualTo(TITLE_BAD_REQUEST_ON_PAYLOAD)
            .jsonPath(JSONPATH_BODY_DETAIL).isEqualTo(TITLE_VALIDATION_ERROR_ON_SUPPLIED_PAYLOAD)
            .jsonPath(JSONPATH_BODY_ERROR_CATEGORY).isEqualTo(ERROR_CATEGORY_PARAMETERS)
            .jsonPath(JSONPATH_BODY_ERRORS).isArray()
            .jsonPath(JSONPATH_BODY_ERRORS_0).isEqualTo(EXPECTED_ERROR_NAME_USER_IS_MANDATORY);
    }

    @Test
    @DisplayName("When payload field name is not empty, address is null/empty, for creating a new record then BAD request is given.")
    void whenUserCreatePayloadFieldAddressIsNotEmptyShouldReturnBadRequest() {
        var jsonPayload = """
            {
              "name": "user",
              "address": ""
            }
            """;

        client.post()
            .uri(USER_CONTROLLER_BASE_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .bodyValue(jsonPayload)
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody()
            .jsonPath(JSONPATH_BODY_TITLE).isEqualTo(TITLE_BAD_REQUEST_ON_PAYLOAD)
            .jsonPath(JSONPATH_BODY_DETAIL).isEqualTo(TITLE_VALIDATION_ERROR_ON_SUPPLIED_PAYLOAD)
            .jsonPath(JSONPATH_BODY_ERROR_CATEGORY).isEqualTo(ERROR_CATEGORY_PARAMETERS)
            .jsonPath(JSONPATH_BODY_ERRORS).isArray()
            .jsonPath(JSONPATH_BODY_ERRORS_0).isEqualTo(EXPECTED_ERROR_ADDRESS_IS_MANDATORY);
    }

    @Test
    @DisplayName("Verify we can create a new record.")
    void shouldCreateNewRecord() {
        var user = buildUserWithIDSet();

        when(userService.insert(any(User.class))).thenReturn(user);

        client.post()
            .uri(USER_CONTROLLER_BASE_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .body(BodyInserters.fromValue(user))
            .exchange()
            .expectStatus().isOk()
            .expectBody(User.class)
            .consumeWith(response -> assertThat(response.getResponseBody()).isEqualTo(user));

        verify(userService).insert(any());
    }

    @Test
    @DisplayName("Verify when we look for an user Id and it is not found it gives 404 error.")
    void shouldFindByIdNotFoundThenReturnError404() {
        client.get()
            .uri(USER_CONTROLLER_BASE_PATH + USER_ID_PATH_VARIABLE, USER_ID_UUID)
            .accept(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .exchange()
            .expectStatus().is4xxClientError()
            .expectBody()
            .jsonPath(JSONPATH_TITLE).isEqualTo(TITLE_USER_NOT_FOUND)
            .jsonPath(JSONPATH_DETAIL).isEqualTo(USER_WITH_ID + USER_ID_UUID + NOT_FOUND)
            .jsonPath(JSONPATH_ERROR_CATEGORY).isEqualTo(ERROR_CATEGORY_GENERIC);
    }

    @Test
    @DisplayName("Verify we can find an existing user with its 'Id'.")
    void shouldFindByIdUsingExistingUserId() {
        var user = buildUserWithUUIDForInsert();
        var expectedUserDataResponse = buildUserDataResponseWith(user);

        when(userService.retrieve(anyString())).thenReturn(user);

        client.get()
            .uri(USER_CONTROLLER_BASE_PATH + USER_ID_PATH_VARIABLE, USER_ID_UUID)
            .accept(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .exchange()
            .expectStatus().isOk()
            .expectBody(UserDataResponse.class)
            .consumeWith(response -> {
                var resBody = response.getResponseBody();

                assertThat(resBody).isEqualTo(expectedUserDataResponse);
            });

        verify(userService).retrieve(anyString());
    }

    @Test
    @DisplayName("Verify when no payload is sent for updating a record, then 400 error is returned as BAD REQUEST.")
    void givenNoPayloadIsSentToUpdateRecordThenReturn400ErrorCode() {
        client.patch()
            .uri(USER_CONTROLLER_BASE_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody(String.class)
            .consumeWith(response -> assertThat(response.getResponseBody()).isEqualTo(HTTP_400_BAD_REQUEST_RESPONSE));
    }

    @Test
    @DisplayName("When payload is empty for updating a record, then 400 error is returned as BAD REQUEST.")
    void whenUserUpdatePayloadEmptyShouldReturnBadRequest() {
        var jsonPayload = "{}";

        client.patch()
            .uri(USER_CONTROLLER_BASE_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .bodyValue(jsonPayload)
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody()
            .jsonPath(JSONPATH_BODY_TITLE).isEqualTo(TITLE_BAD_REQUEST_ON_PAYLOAD)
            .jsonPath(JSONPATH_BODY_DETAIL).isEqualTo(TITLE_VALIDATION_ERROR_ON_SUPPLIED_PAYLOAD)
            .jsonPath(JSONPATH_BODY_ERROR_CATEGORY).isEqualTo(ERROR_CATEGORY_PARAMETERS)
            .jsonPath(JSONPATH_BODY_ERRORS).isArray()
            .jsonPath(JSONPATH_BODY_ERRORS_0).isEqualTo(EXPECTED_ERROR_ADDRESS_IS_MANDATORY)
            .jsonPath(JSONPATH_BODY_ERRORS_1).isEqualTo(EXPECTED_ERROR_NAME_USER_IS_MANDATORY);
    }

    @Test
    @DisplayName("When payload fields are null for updating a record, then 400 error is returned as BAD REQUEST.")
    void whenUserUpdatePayloadFieldsNullShouldReturnBadRequest() {
        var jsonPayload = """
            {
              "name": null,
              "address": null
            }
            """;

        client.patch()
            .uri(USER_CONTROLLER_BASE_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .bodyValue(jsonPayload)
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody()
            .jsonPath(JSONPATH_BODY_TITLE).isEqualTo(TITLE_BAD_REQUEST_ON_PAYLOAD)
            .jsonPath(JSONPATH_BODY_DETAIL).isEqualTo(TITLE_VALIDATION_ERROR_ON_SUPPLIED_PAYLOAD)
            .jsonPath(JSONPATH_BODY_ERROR_CATEGORY).isEqualTo(ERROR_CATEGORY_PARAMETERS)
            .jsonPath(JSONPATH_BODY_ERRORS).isArray()
            .jsonPath(JSONPATH_BODY_ERRORS_0).isEqualTo(EXPECTED_ERROR_ADDRESS_IS_MANDATORY)
            .jsonPath(JSONPATH_BODY_ERRORS_1).isEqualTo(EXPECTED_ERROR_NAME_USER_IS_MANDATORY);
    }

    @Test
    @DisplayName("When payload fields are empty for creating a record, then 400 error is returned as BAD REQUEST.")
    void whenUserUpdatePayloadFieldsEmptyShouldReturnBadRequest() {
        var jsonPayload = """
            {
              "name": "",
              "address": ""
            }
            """;

        client.patch()
            .uri(USER_CONTROLLER_BASE_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .bodyValue(jsonPayload)
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody()
            .jsonPath(JSONPATH_BODY_TITLE).isEqualTo(TITLE_BAD_REQUEST_ON_PAYLOAD)
            .jsonPath(JSONPATH_BODY_DETAIL).isEqualTo(TITLE_VALIDATION_ERROR_ON_SUPPLIED_PAYLOAD)
            .jsonPath(JSONPATH_BODY_ERROR_CATEGORY).isEqualTo(ERROR_CATEGORY_PARAMETERS)
            .jsonPath(JSONPATH_BODY_ERRORS).isArray()
            .jsonPath(JSONPATH_BODY_ERRORS_0).isEqualTo(EXPECTED_ERROR_ADDRESS_IS_MANDATORY)
            .jsonPath(JSONPATH_BODY_ERRORS_1).isEqualTo(EXPECTED_ERROR_NAME_USER_IS_MANDATORY);
    }

    @Test
    @DisplayName("When payload field address is not empty, name is null/empty, then 400 is returned as BAD request.")
    void whenUserUpdateFieldAddressIsNotEmptyNameNullOrEmptyShouldReturnBadRequest() {
        var jsonPayload = """
            {
              "name": "",
              "address": "address"
            }
            """;

        client.patch()
            .uri(USER_CONTROLLER_BASE_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .bodyValue(jsonPayload)
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody()
            .jsonPath(JSONPATH_BODY_TITLE).isEqualTo(TITLE_BAD_REQUEST_ON_PAYLOAD)
            .jsonPath(JSONPATH_BODY_DETAIL).isEqualTo(TITLE_VALIDATION_ERROR_ON_SUPPLIED_PAYLOAD)
            .jsonPath(JSONPATH_BODY_ERROR_CATEGORY).isEqualTo(ERROR_CATEGORY_PARAMETERS)
            .jsonPath(JSONPATH_BODY_ERRORS).isArray()
            .jsonPath(JSONPATH_BODY_ERRORS_0).isEqualTo(EXPECTED_ERROR_NAME_USER_IS_MANDATORY);
    }

    @Test
    @DisplayName("When payload field name is not empty, address is null/empty then 400 is returned as BAD request.")
    void whenUserUpdatePayloadFieldAddressIsNotEmptyShouldReturnBadRequest() {
        var jsonPayload = """
            {
              "name": "user",
              "address": ""
            }
            """;

        client.patch()
            .uri(USER_CONTROLLER_BASE_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .bodyValue(jsonPayload)
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody()
            .jsonPath(JSONPATH_BODY_TITLE).isEqualTo(TITLE_BAD_REQUEST_ON_PAYLOAD)
            .jsonPath(JSONPATH_BODY_DETAIL).isEqualTo(TITLE_VALIDATION_ERROR_ON_SUPPLIED_PAYLOAD)
            .jsonPath(JSONPATH_BODY_ERROR_CATEGORY).isEqualTo(ERROR_CATEGORY_PARAMETERS)
            .jsonPath(JSONPATH_BODY_ERRORS).isArray()
            .jsonPath(JSONPATH_BODY_ERRORS_0).isEqualTo(EXPECTED_ERROR_ADDRESS_IS_MANDATORY);
    }

    @Test
    @DisplayName("When required payload fields are set but 'id' field is not set, then 404 error is return as Record not found.")
    void whenUserUpdatePayloadRequiredFieldsSetButNoIdSetReturn404Error() {
        var jsonPayload = """
            {
              "name": "user",
              "address": "address"
            }
            """;

        client.patch()
            .uri(USER_CONTROLLER_BASE_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .bodyValue(jsonPayload)
            .exchange()
            .expectStatus().is4xxClientError()
            .expectBody()
            .jsonPath(JSONPATH_TITLE).isEqualTo(TITLE_USER_NOT_FOUND)
            .jsonPath(JSONPATH_DETAIL).isEqualTo(USER_WITH_ID + USER_ID_NULL + NOT_FOUND)
            .jsonPath(JSONPATH_ERROR_CATEGORY).isEqualTo(ERROR_CATEGORY_GENERIC);
    }

    @Test
    @DisplayName("When required payload fields are set but 'id' field is empty, then 404 error is return as Record not found.")
    void whenUserUpdatePayloadRequiredFieldsSetButIdIsEmptyReturn404Error() {
        var jsonPayload = """
            {
              "id" : "",
              "name": "user",
              "address": "address"
            }
            """;

        client.patch()
            .uri(USER_CONTROLLER_BASE_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .bodyValue(jsonPayload)
            .exchange()
            .expectStatus().is4xxClientError()
            .expectBody()
            .jsonPath(JSONPATH_TITLE).isEqualTo(TITLE_USER_NOT_FOUND)
            .jsonPath(JSONPATH_DETAIL).isEqualTo(USER_WITH_ID + USER_ID_EMPTY + NOT_FOUND)
            .jsonPath(JSONPATH_ERROR_CATEGORY).isEqualTo(ERROR_CATEGORY_GENERIC);
    }

    @Test
    @DisplayName("When all fields in payload are not empty, but 'id' is not found then 404 not found is returned.")
    void whenAllPayloadIsSetButIdNotFoundThenReturn404ErrorCode() {
        var user = buildUserWithIDSet();

        when(userService.update(any())).thenReturn(Boolean.FALSE);

        client.patch()
            .uri(USER_CONTROLLER_BASE_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .body(BodyInserters.fromValue(user))
            .exchange()
            .expectStatus().is4xxClientError()
            .expectBody()
            .jsonPath(JSONPATH_TITLE).isEqualTo(TITLE_USER_NOT_FOUND)
            .jsonPath(JSONPATH_DETAIL).isEqualTo(USER_WITH_ID + USER_ID_UUID + NOT_FOUND)
            .jsonPath(JSONPATH_ERROR_CATEGORY).isEqualTo(ERROR_CATEGORY_GENERIC);

        verify(userService).update(any());
    }

    @Test
    @DisplayName("When all fields in payload are not empty, and 'id' is found then record is updated and response is the updated record.")
    void whenAllPayloadIsSetButIdFoundThenReturnUserUpdatedRecord() {
        var user = buildUserWithIDSet();

        when(userService.update(any())).thenReturn(Boolean.TRUE);

        client.patch()
            .uri(USER_CONTROLLER_BASE_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .body(BodyInserters.fromValue(user))
            .exchange()
            .expectStatus().isOk()
            .expectBody(User.class)
            .consumeWith(response -> assertThat(response.getResponseBody()).isEqualTo(user));

        verify(userService).update(any());
    }

    @Test
    @DisplayName("Verify when we delete a non existing record, it returns 404 error code User Not Found.")
    void whenUserDeleteWithKnownIdAndNotFoundThen404Returned() {

        when(userService.delete(anyString())).thenReturn(Boolean.FALSE);

        client.delete()
            .uri(USER_CONTROLLER_BASE_PATH + USER_ID_PATH_VARIABLE, USER_ID_UUID)
            .accept(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .exchange()
            .expectStatus().is4xxClientError()
            .expectBody()
            .jsonPath(JSONPATH_TITLE).isEqualTo(TITLE_USER_NOT_FOUND)
            .jsonPath(JSONPATH_DETAIL).isEqualTo(USER_WITH_ID + USER_ID_UUID + NOT_FOUND)
            .jsonPath(JSONPATH_ERROR_CATEGORY).isEqualTo(ERROR_CATEGORY_GENERIC);

        verify(userService).delete(anyString());
    }

    @Test
    @DisplayName("Verify when we delete a existing record, it returns 'true' indicating it could remove the requested record.")
    void whenUserDeleteWithKnownIdAndRecordFoundThen404Returned() {

        when(userService.delete(anyString())).thenReturn(Boolean.TRUE);

        client.delete()
            .uri(USER_CONTROLLER_BASE_PATH + USER_ID_PATH_VARIABLE, USER_ID_UUID)
            .accept(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .exchange()
            .expectStatus().isOk()
            .expectBody(Boolean.class)
            .consumeWith(response -> assertThat(response.getResponseBody()).isNotNull().isTrue());

        verify(userService).delete(anyString());
    }

    private User buildUserWithIDSet() {
        return new User(USER_ID_UUID, USER_NAME, USER_ADDRESS);
    }

    private List<User> buildUserList() {
        return List.of(buildUserWithIDOne(), buildUserWithIDTwo());
    }

    private User buildUserWithIDOne() {
        return new User(USER_ID_ONE, USER_NAME_ONE, USER_ADDRESS_ONE);
    }

    private User buildUserWithIDTwo() {
        return new User(USER_ID_TWO, USER_NAME_TWO, USER_ADDRESS_TWO);
    }

    private User buildUserWithUUIDForInsert() {
        return new User(USER_ID_UUID, USER_NAME_ONE_UPDATED, USER_ADDRESS_ONE_UPDATED);
    }

    private UserDataResponse buildUserDataResponseWith(User user) {
        return new UserDataResponse(user);
    }
}
