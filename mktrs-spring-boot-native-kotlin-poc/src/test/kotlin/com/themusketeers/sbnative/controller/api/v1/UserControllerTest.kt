/*----------------------------------------------------------------------------*/
/* Source File:   USERCONTROLLERTEST.JAVA                                     */
/* Copyright (c), 2023 The Musketeers                                         */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Jun.24/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.themusketeers.sbnative.controller.api.v1

import com.themusketeers.sbnative.common.consts.ControllerExceptionHandlerConstants.ERROR_CATEGORY_GENERIC
import com.themusketeers.sbnative.common.consts.ControllerExceptionHandlerConstants.ERROR_CATEGORY_PARAMETERS
import com.themusketeers.sbnative.common.consts.ControllerExceptionHandlerConstants.TITLE_BAD_REQUEST_ON_PAYLOAD
import com.themusketeers.sbnative.common.consts.ControllerExceptionHandlerConstants.TITLE_USER_NOT_FOUND
import com.themusketeers.sbnative.common.consts.ControllerExceptionHandlerConstants.TITLE_VALIDATION_ERROR_ON_SUPPLIED_PAYLOAD
import com.themusketeers.sbnative.common.consts.ExceptionConstants.NOT_FOUND
import com.themusketeers.sbnative.common.consts.ExceptionConstants.USER_WITH_ID
import com.themusketeers.sbnative.common.consts.GlobalConstants.LONG_TWO
import com.themusketeers.sbnative.common.consts.GlobalConstants.LONG_ZERO
import com.themusketeers.sbnative.domain.User
import com.themusketeers.sbnative.domain.response.UserDataResponse
import com.themusketeers.sbnative.domain.response.UsersDataResponse
import com.themusketeers.sbnative.service.intr.UserService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.client.MockMvcWebTestClient
import org.springframework.web.reactive.function.BodyInserters

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
@WebMvcTest(UserController::class)
class UserControllerTest {
    companion object {
        const val USER_CONTROLLER_BASE_PATH = "/api/v1/users"
        const val EXPECTED_ERROR_ADDRESS_IS_MANDATORY = "address: Address is mandatory"
        const val EXPECTED_ERROR_NAME_USER_IS_MANDATORY = "name: Name User is mandatory"
        const val JSONPATH_TITLE = "$.title"
        const val JSONPATH_DETAIL = "$.detail"
        const val JSONPATH_ERROR_CATEGORY = "$.errorCategory"
        const val JSONPATH_BODY_TITLE = "$.body.title"
        const val JSONPATH_BODY_DETAIL = "$.body.detail"
        const val JSONPATH_BODY_ERROR_CATEGORY = "$.body.errorCategory"
        const val JSONPATH_BODY_ERRORS = "$.body.errors"
        const val JSONPATH_BODY_ERRORS_0 = "$.body.errors[0]"
        const val JSONPATH_BODY_ERRORS_1 = "$.body.errors[1]"
        const val USER_NAME = "USER_NAME"
        const val USER_ADDRESS = "USER_ADDRESS"
        const val USER_ID_UUID = "53eb385f-582d-4a13-8275-c26a5de6655c"
        const val USER_ID_NULL = "null"
        const val USER_ID_EMPTY = ""
        const val USER_ID_ONE = "c56b2741-028e-4ff5-9e15-be4f96b4ea35"
        const val USER_ID_TWO = "b94f6ae6-e1d2-4fdf-8c6b-eb471da1d4d1"
        const val USER_NAME_ONE = "Name One"
        const val USER_NAME_ONE_UPDATED = "Name One Updated"
        const val USER_NAME_TWO = "Name Two"
        const val USER_ADDRESS_ONE = "Address One"
        const val USER_ADDRESS_ONE_UPDATED = "Address One Updated"
        const val USER_ADDRESS_TWO = "Address Two"
        const val USER_ID_PATH_VARIABLE = "/{userId}"

        val HTTP_400_BAD_REQUEST_RESPONSE = """
        {"type":"about:blank","title":"Bad Request","status":400,"detail":"Failed to read request","instance":"/api/v1/users"}
        """.trimIndent()
    }

    private lateinit var client: WebTestClient

    @MockBean
    private lateinit var userService: UserService

    @BeforeEach
    fun beforeEach(@Autowired mockMvc: MockMvc) {
        client = MockMvcWebTestClient
            .bindTo(mockMvc)
            .build()
    }

    @Test
    @DisplayName("Should Retrieve an empty list of users")
    fun shouldRetrieveAnEmptyUserList() {
        client.get()
            .uri(USER_CONTROLLER_BASE_PATH)
            .accept(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .exchange()
            .expectStatus().isOk()
            .expectBody(UsersDataResponse::class.java)
            .consumeWith { response ->
                val resBody = response.responseBody

                assertThat(resBody).isNotNull()
                assertThat(resBody!!.count).isNotNull().isEqualTo(LONG_ZERO)
                assertThat(resBody!!.users).isNotNull().isEmpty()
            }
    }

    @Test
    @DisplayName("Should Retrieve a list of users")
    fun shouldRetrieveListWithUsers() {
        val userList = buildUserList()

        `when`(userService.count()).thenReturn(LONG_TWO)
        `when`(userService.retrieveAll()).thenReturn(userList)

        client.get()
            .uri(USER_CONTROLLER_BASE_PATH)
            .accept(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .exchange()
            .expectStatus().isOk()
            .expectBody(UsersDataResponse::class.java)
            .consumeWith { response ->
                val resBody = response.responseBody

                assertThat(resBody).isNotNull()
                assertThat(resBody!!.count).isNotNull().isEqualTo(LONG_TWO)
                assertThat(resBody!!.users).isNotNull().isNotEmpty().hasSameElementsAs(userList)
            }

        verify(userService).count()
        verify(userService).retrieveAll()
    }

    @Test
    @DisplayName("When no payload is sent for creating a new record, then a BAD request is given.")
    fun whenUserCreateNoPayloadSentShouldReturnBadRequest() {
        client.post()
            .uri(USER_CONTROLLER_BASE_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody(String::class.java)
            .consumeWith { response -> assertThat(response.responseBody).isEqualTo(HTTP_400_BAD_REQUEST_RESPONSE) }
    }

    @Test
    @DisplayName("When payload is empty for creating a new record, then BAD request is given.")
    fun whenUserCreatePayloadEmptyShouldReturnBadRequest() {
        val jsonPayload = "{}"

        client.post()
            .uri(USER_CONTROLLER_BASE_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .bodyValue(jsonPayload)
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody()
            .jsonPath(JSONPATH_BODY_TITLE).isEqualTo(TITLE_BAD_REQUEST_ON_PAYLOAD)
            .jsonPath(JSONPATH_BODY_DETAIL).isEqualTo(TITLE_VALIDATION_ERROR_ON_SUPPLIED_PAYLOAD)
            .jsonPath(JSONPATH_BODY_ERROR_CATEGORY).isEqualTo(ERROR_CATEGORY_PARAMETERS)
            .jsonPath(JSONPATH_BODY_ERRORS).isArray()
            .jsonPath(JSONPATH_BODY_ERRORS_0).isEqualTo(EXPECTED_ERROR_ADDRESS_IS_MANDATORY)
            .jsonPath(JSONPATH_BODY_ERRORS_1).isEqualTo(EXPECTED_ERROR_NAME_USER_IS_MANDATORY)
    }

    @Test
    @DisplayName("When payload fields are null for creating a new record, then BAD request is given.")
    fun whenUserCreatePayloadFieldsNullShouldReturnBadRequest() {
        val jsonPayload = """
            {
              "name": null,
              "address": null
            }    
            """.trimIndent()

        client.post()
            .uri(USER_CONTROLLER_BASE_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .bodyValue(jsonPayload)
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody()
            .jsonPath(JSONPATH_BODY_TITLE).isEqualTo(TITLE_BAD_REQUEST_ON_PAYLOAD)
            .jsonPath(JSONPATH_BODY_DETAIL).isEqualTo(TITLE_VALIDATION_ERROR_ON_SUPPLIED_PAYLOAD)
            .jsonPath(JSONPATH_BODY_ERROR_CATEGORY).isEqualTo(ERROR_CATEGORY_PARAMETERS)
            .jsonPath(JSONPATH_BODY_ERRORS).isArray()
            .jsonPath(JSONPATH_BODY_ERRORS_0).isEqualTo(EXPECTED_ERROR_ADDRESS_IS_MANDATORY)
            .jsonPath(JSONPATH_BODY_ERRORS_1).isEqualTo(EXPECTED_ERROR_NAME_USER_IS_MANDATORY)
    }

    @Test
    @DisplayName("When payload fields are empty for creating a new record, then BAD request is given.")
    fun whenUserCreatePayloadFieldsEmptyShouldReturnBadRequest() {
        val jsonPayload = """
            {
              "name": "",
              "address": ""
            }
            """.trimIndent()

        client.post()
            .uri(USER_CONTROLLER_BASE_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .bodyValue(jsonPayload)
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody()
            .jsonPath(JSONPATH_BODY_TITLE).isEqualTo(TITLE_BAD_REQUEST_ON_PAYLOAD)
            .jsonPath(JSONPATH_BODY_DETAIL).isEqualTo(TITLE_VALIDATION_ERROR_ON_SUPPLIED_PAYLOAD)
            .jsonPath(JSONPATH_BODY_ERROR_CATEGORY).isEqualTo(ERROR_CATEGORY_PARAMETERS)
            .jsonPath(JSONPATH_BODY_ERRORS).isArray()
            .jsonPath(JSONPATH_BODY_ERRORS_0).isEqualTo(EXPECTED_ERROR_ADDRESS_IS_MANDATORY)
            .jsonPath(JSONPATH_BODY_ERRORS_1).isEqualTo(EXPECTED_ERROR_NAME_USER_IS_MANDATORY)
    }

    @Test
    @DisplayName("When payload field address is not empty, name is null/empty, for creating a new record then BAD request is given.")
    fun whenUserCreatePayloadFieldAddressIsNotEmptyNameNullOrEmptyShouldReturnBadRequest() {
        val jsonPayload = """
            {
              "name": "",
              "address": "address"
            }
            """.trimIndent()

        client.post()
            .uri(USER_CONTROLLER_BASE_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .bodyValue(jsonPayload)
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody()
            .jsonPath(JSONPATH_BODY_TITLE).isEqualTo(TITLE_BAD_REQUEST_ON_PAYLOAD)
            .jsonPath(JSONPATH_BODY_DETAIL).isEqualTo(TITLE_VALIDATION_ERROR_ON_SUPPLIED_PAYLOAD)
            .jsonPath(JSONPATH_BODY_ERROR_CATEGORY).isEqualTo(ERROR_CATEGORY_PARAMETERS)
            .jsonPath(JSONPATH_BODY_ERRORS).isArray()
            .jsonPath(JSONPATH_BODY_ERRORS_0).isEqualTo(EXPECTED_ERROR_NAME_USER_IS_MANDATORY)
    }

    @Test
    @DisplayName("When payload field name is not empty, address is null/empty, for creating a new record then BAD request is given.")
    fun whenUserCreatePayloadFieldAddressIsNotEmptyShouldReturnBadRequest() {
        val jsonPayload = """
            {
              "name": "user",
              "address": ""
            }    
            """.trimIndent()

        client.post()
            .uri(USER_CONTROLLER_BASE_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .bodyValue(jsonPayload)
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody()
            .jsonPath(JSONPATH_BODY_TITLE).isEqualTo(TITLE_BAD_REQUEST_ON_PAYLOAD)
            .jsonPath(JSONPATH_BODY_DETAIL).isEqualTo(TITLE_VALIDATION_ERROR_ON_SUPPLIED_PAYLOAD)
            .jsonPath(JSONPATH_BODY_ERROR_CATEGORY).isEqualTo(ERROR_CATEGORY_PARAMETERS)
            .jsonPath(JSONPATH_BODY_ERRORS).isArray()
            .jsonPath(JSONPATH_BODY_ERRORS_0).isEqualTo(EXPECTED_ERROR_ADDRESS_IS_MANDATORY)
    }

    @Test
    @DisplayName("Verify we can create a new record.")
    fun shouldCreateNewRecord() {
        val user = buildUserWithIDSet()

        `when`(userService.insert(any())).thenReturn(user)

        client.post()
            .uri(USER_CONTROLLER_BASE_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .body(BodyInserters.fromValue(user))
            .exchange()
            .expectStatus().isOk()
            .expectBody(User::class.java)
            .consumeWith { response -> assertThat(response.responseBody).isEqualTo(user) }

        verify(userService).insert(any())
    }

    @Test
    @DisplayName("Verify when we look for an user Id and it is not found it gives 404 error.")
    fun shouldFindByIdNotFoundThenReturnError404() {
        client.get()
            .uri(USER_CONTROLLER_BASE_PATH + USER_ID_PATH_VARIABLE, USER_ID_UUID)
            .accept(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .exchange()
            .expectStatus().is4xxClientError()
            .expectBody()
            .jsonPath(JSONPATH_TITLE).isEqualTo(TITLE_USER_NOT_FOUND)
            .jsonPath(JSONPATH_DETAIL).isEqualTo(USER_WITH_ID + USER_ID_UUID + NOT_FOUND)
            .jsonPath(JSONPATH_ERROR_CATEGORY).isEqualTo(ERROR_CATEGORY_GENERIC)
    }

    @Test
    @DisplayName("Verify we can find an existing user with its 'Id'.")
    fun shouldFindByIdUsingExistingUserId() {
        val user = buildUserWithUUIDForInsert()
        val expectedUserDataResponse = buildUserDataResponseWith(user)

        `when`(userService.retrieve(anyString())).thenReturn(user)

        client.get()
            .uri(USER_CONTROLLER_BASE_PATH + USER_ID_PATH_VARIABLE, USER_ID_UUID)
            .accept(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .exchange()
            .expectStatus().isOk()
            .expectBody(UserDataResponse::class.java)
            .consumeWith { response ->
                val resBody = response.responseBody

                assertThat(resBody).isEqualTo(expectedUserDataResponse)
            }

        verify(userService).retrieve(anyString())
    }

    @Test
    @DisplayName("Verify when no payload is sent for updating a record, then 400 error is returned as BAD REQUEST.")
    fun givenNoPayloadIsSentToUpdateRecordThenReturn400ErrorCode() {
        client.patch()
            .uri(USER_CONTROLLER_BASE_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody(String::class.java)
            .consumeWith { response -> assertThat(response.responseBody).isEqualTo(HTTP_400_BAD_REQUEST_RESPONSE) }
    }

    @Test
    @DisplayName("When payload is empty for updating a record, then 400 error is returned as BAD REQUEST.")
    fun whenUserUpdatePayloadEmptyShouldReturnBadRequest() {
        val jsonPayload = "{}"

        client.patch()
            .uri(USER_CONTROLLER_BASE_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .bodyValue(jsonPayload)
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody()
            .jsonPath(JSONPATH_BODY_TITLE).isEqualTo(TITLE_BAD_REQUEST_ON_PAYLOAD)
            .jsonPath(JSONPATH_BODY_DETAIL).isEqualTo(TITLE_VALIDATION_ERROR_ON_SUPPLIED_PAYLOAD)
            .jsonPath(JSONPATH_BODY_ERROR_CATEGORY).isEqualTo(ERROR_CATEGORY_PARAMETERS)
            .jsonPath(JSONPATH_BODY_ERRORS).isArray()
            .jsonPath(JSONPATH_BODY_ERRORS_0).isEqualTo(EXPECTED_ERROR_ADDRESS_IS_MANDATORY)
            .jsonPath(JSONPATH_BODY_ERRORS_1).isEqualTo(EXPECTED_ERROR_NAME_USER_IS_MANDATORY)
    }

    @Test
    @DisplayName("When payload fields are null for updating a record, then 400 error is returned as BAD REQUEST.")
    fun whenUserUpdatePayloadFieldsNullShouldReturnBadRequest() {
        val jsonPayload = """
            {
              "name": null,
              "address": null
            }
            """.trimIndent()

        client.patch()
            .uri(USER_CONTROLLER_BASE_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .bodyValue(jsonPayload)
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody()
            .jsonPath(JSONPATH_BODY_TITLE).isEqualTo(TITLE_BAD_REQUEST_ON_PAYLOAD)
            .jsonPath(JSONPATH_BODY_DETAIL).isEqualTo(TITLE_VALIDATION_ERROR_ON_SUPPLIED_PAYLOAD)
            .jsonPath(JSONPATH_BODY_ERROR_CATEGORY).isEqualTo(ERROR_CATEGORY_PARAMETERS)
            .jsonPath(JSONPATH_BODY_ERRORS).isArray()
            .jsonPath(JSONPATH_BODY_ERRORS_0).isEqualTo(EXPECTED_ERROR_ADDRESS_IS_MANDATORY)
            .jsonPath(JSONPATH_BODY_ERRORS_1).isEqualTo(EXPECTED_ERROR_NAME_USER_IS_MANDATORY)
    }

    @Test
    @DisplayName("When payload fields are empty for creating a record, then 400 error is returned as BAD REQUEST.")
    fun whenUserUpdatePayloadFieldsEmptyShouldReturnBadRequest() {
        val jsonPayload = """
            {
              "name": "",
              "address": ""
            }    
            """.trimIndent()

        client.patch()
            .uri(USER_CONTROLLER_BASE_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .bodyValue(jsonPayload)
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody()
            .jsonPath(JSONPATH_BODY_TITLE).isEqualTo(TITLE_BAD_REQUEST_ON_PAYLOAD)
            .jsonPath(JSONPATH_BODY_DETAIL).isEqualTo(TITLE_VALIDATION_ERROR_ON_SUPPLIED_PAYLOAD)
            .jsonPath(JSONPATH_BODY_ERROR_CATEGORY).isEqualTo(ERROR_CATEGORY_PARAMETERS)
            .jsonPath(JSONPATH_BODY_ERRORS).isArray()
            .jsonPath(JSONPATH_BODY_ERRORS_0).isEqualTo(EXPECTED_ERROR_ADDRESS_IS_MANDATORY)
            .jsonPath(JSONPATH_BODY_ERRORS_1).isEqualTo(EXPECTED_ERROR_NAME_USER_IS_MANDATORY)
    }

    @Test
    @DisplayName("When payload field address is not empty, name is null/empty, then 400 is returned as BAD request.")
    fun whenUserUpdateFieldAddressIsNotEmptyNameNullOrEmptyShouldReturnBadRequest() {
        val jsonPayload = """
            {
              "name": "",
              "address": "address"
            }
            """.trimIndent()

        client.patch()
            .uri(USER_CONTROLLER_BASE_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .bodyValue(jsonPayload)
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody()
            .jsonPath(JSONPATH_BODY_TITLE).isEqualTo(TITLE_BAD_REQUEST_ON_PAYLOAD)
            .jsonPath(JSONPATH_BODY_DETAIL).isEqualTo(TITLE_VALIDATION_ERROR_ON_SUPPLIED_PAYLOAD)
            .jsonPath(JSONPATH_BODY_ERROR_CATEGORY).isEqualTo(ERROR_CATEGORY_PARAMETERS)
            .jsonPath(JSONPATH_BODY_ERRORS).isArray()
            .jsonPath(JSONPATH_BODY_ERRORS_0).isEqualTo(EXPECTED_ERROR_NAME_USER_IS_MANDATORY)
    }

    @Test
    @DisplayName("When payload field name is not empty, address is null/empty then 400 is returned as BAD request.")
    fun whenUserUpdatePayloadFieldAddressIsNotEmptyShouldReturnBadRequest() {
        val jsonPayload = """
            {
              "name": "user",
              "address": ""
            }
            """.trimIndent()

        client.patch()
            .uri(USER_CONTROLLER_BASE_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .bodyValue(jsonPayload)
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody()
            .jsonPath(JSONPATH_BODY_TITLE).isEqualTo(TITLE_BAD_REQUEST_ON_PAYLOAD)
            .jsonPath(JSONPATH_BODY_DETAIL).isEqualTo(TITLE_VALIDATION_ERROR_ON_SUPPLIED_PAYLOAD)
            .jsonPath(JSONPATH_BODY_ERROR_CATEGORY).isEqualTo(ERROR_CATEGORY_PARAMETERS)
            .jsonPath(JSONPATH_BODY_ERRORS).isArray()
            .jsonPath(JSONPATH_BODY_ERRORS_0).isEqualTo(EXPECTED_ERROR_ADDRESS_IS_MANDATORY)
    }

    @Test
    @DisplayName("When required payload fields are set but 'id' field is not set, then 404 error is return as Record not found.")
    fun whenUserUpdatePayloadRequiredFieldsSetButNoIdSetReturn404Error() {
        val jsonPayload = """
            {
              "name": "user",
              "address": "address"
            }
            """.trimIndent()

        client.patch()
            .uri(USER_CONTROLLER_BASE_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .bodyValue(jsonPayload)
            .exchange()
            .expectStatus().is4xxClientError()
            .expectBody()
            .jsonPath(JSONPATH_TITLE).isEqualTo(TITLE_USER_NOT_FOUND)
            .jsonPath(JSONPATH_DETAIL).isEqualTo(USER_WITH_ID + USER_ID_NULL + NOT_FOUND)
            .jsonPath(JSONPATH_ERROR_CATEGORY).isEqualTo(ERROR_CATEGORY_GENERIC)
    }

    @Test
    @DisplayName("When required payload fields are set but 'id' field is empty, then 404 error is return as Record not found.")
    fun whenUserUpdatePayloadRequiredFieldsSetButIdIsEmptyReturn404Error() {
        val jsonPayload = """
            {
              "id" : "",
              "name": "user",
              "address": "address"
            }
            """.trimIndent()

        client.patch()
            .uri(USER_CONTROLLER_BASE_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .bodyValue(jsonPayload)
            .exchange()
            .expectStatus().is4xxClientError()
            .expectBody()
            .jsonPath(JSONPATH_TITLE).isEqualTo(TITLE_USER_NOT_FOUND)
            .jsonPath(JSONPATH_DETAIL).isEqualTo(USER_WITH_ID + USER_ID_EMPTY + NOT_FOUND)
            .jsonPath(JSONPATH_ERROR_CATEGORY).isEqualTo(ERROR_CATEGORY_GENERIC)
    }

    @Test
    @DisplayName("When all fields in payload are not empty, but 'id' is not found then 404 not found is returned.")
    fun whenAllPayloadIsSetButIdNotFoundThenReturn404ErrorCode() {
        val user = buildUserWithIDSet()

        `when`(userService.update(any())).thenReturn(java.lang.Boolean.FALSE)

        client.patch()
            .uri(USER_CONTROLLER_BASE_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .body(BodyInserters.fromValue<User>(user))
            .exchange()
            .expectStatus().is4xxClientError()
            .expectBody()
            .jsonPath(JSONPATH_TITLE).isEqualTo(TITLE_USER_NOT_FOUND)
            .jsonPath(JSONPATH_DETAIL).isEqualTo(USER_WITH_ID + USER_ID_UUID + NOT_FOUND)
            .jsonPath(JSONPATH_ERROR_CATEGORY).isEqualTo(ERROR_CATEGORY_GENERIC)

        verify(userService).update(any())
    }

    @Test
    @DisplayName("When all fields in payload are not empty, and 'id' is found then record is updated and response is the updated record.")
    fun whenAllPayloadIsSetButIdFoundThenReturnUserUpdatedRecord() {
        val user = buildUserWithIDSet()

        `when`(userService.update(any())).thenReturn(java.lang.Boolean.TRUE)

        client.patch()
            .uri(USER_CONTROLLER_BASE_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .body(BodyInserters.fromValue(user))
            .exchange()
            .expectStatus().isOk()
            .expectBody(User::class.java)
            .consumeWith { response -> assertThat(response.responseBody).isEqualTo(user) }

        verify(userService).update(any())
    }

    @Test
    @DisplayName("Verify when we delete a non existing record, it returns 404 error code User Not Found.")
    fun whenUserDeleteWithKnownIdAndNotFoundThen404Returned() {
        `when`(userService.delete(anyString())).thenReturn(java.lang.Boolean.FALSE)

        client.delete()
            .uri(USER_CONTROLLER_BASE_PATH + USER_ID_PATH_VARIABLE, USER_ID_UUID)
            .accept(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .exchange()
            .expectStatus().is4xxClientError()
            .expectBody()
            .jsonPath(JSONPATH_TITLE).isEqualTo(TITLE_USER_NOT_FOUND)
            .jsonPath(JSONPATH_DETAIL).isEqualTo(USER_WITH_ID + USER_ID_UUID + NOT_FOUND)
            .jsonPath(JSONPATH_ERROR_CATEGORY).isEqualTo(ERROR_CATEGORY_GENERIC)

        verify(userService).delete(anyString())
    }

    @Test
    @DisplayName("Verify when we delete a existing record, it returns 'true' indicating it could remove the requested record.")
    fun whenUserDeleteWithKnownIdAndRecordFoundThen404Returned() {
        `when`(userService.delete(anyString())).thenReturn(java.lang.Boolean.TRUE)

        client.delete()
            .uri(USER_CONTROLLER_BASE_PATH + USER_ID_PATH_VARIABLE, USER_ID_UUID)
            .accept(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .exchange()
            .expectStatus().isOk()
            .expectBody(Boolean::class.java)
            .consumeWith { response -> assertThat(response.responseBody).isNotNull().isTrue() }

        verify(userService).delete(anyString())
    }

    private fun buildUserWithIDSet() = User(USER_ID_UUID, USER_NAME, USER_ADDRESS)
    private fun buildUserList() = mutableListOf(buildUserWithIDOne(), buildUserWithIDTwo())

    private fun buildUserWithIDOne() = User(USER_ID_ONE, USER_NAME_ONE, USER_ADDRESS_ONE)
    private fun buildUserWithIDTwo() = User(USER_ID_TWO, USER_NAME_TWO, USER_ADDRESS_TWO)
    private fun buildUserWithUUIDForInsert() = User(USER_ID_UUID, USER_NAME_ONE_UPDATED, USER_ADDRESS_ONE_UPDATED)
    private fun buildUserDataResponseWith(user: User) = UserDataResponse(user)
}
