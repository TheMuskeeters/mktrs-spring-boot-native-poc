/**
 * User API Controller Test.
 * <p><b>Path:</b>{@code api/v1/users}</p>
 *
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
package com.themusketeers.sbnative.controller.api.v1;

import static com.themusketeers.sbnative.common.consts.GlobalConstants.LONG_ZERO;
import static org.assertj.core.api.Assertions.assertThat;

import com.themusketeers.sbnative.domain.response.UsersDataResponse;
import com.themusketeers.sbnative.service.intr.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.client.MockMvcWebTestClient;

//https://rieckpil.de/test-your-spring-mvc-controller-with-webtestclient-against-mockmvc/
//https://www.callicoder.com/spring-5-reactive-webclient-webtestclient-examples/

/**
 * Unit test for checking {@link UserController} api.
 *
 * @author COQ- Carlos Adolfo Ortiz Q.
 */
@WebMvcTest(UserController.class)
class UserControllerTest {
    public static final String USER_CONTROLLER_BASE_PATH = "/api/v1/users";

    private WebTestClient client;

    @MockBean
    private UserService userService;

    @BeforeEach
    void beforeEach(@Autowired MockMvc mockMvc) {
        this.client = MockMvcWebTestClient.bindTo(mockMvc).build();
    }

    @Test
    @DisplayName("should Retrieve an empty list of users")
    void shouldRetrieveAnEmtpyUserList() {
        client.get()
            .uri(USER_CONTROLLER_BASE_PATH)
            .exchange()
            .expectStatus().isOk()
            .expectBody(UsersDataResponse.class)
            //.returnResult().getResponseBody();
            //.returnResult()
            .consumeWith(response -> {
                var res = response.getResponseBody();

                assertThat(res).isNotNull();
                assertThat(res.count()).isNotNull().isEqualTo(LONG_ZERO);
                assertThat(res.users()).isNotNull().isEmpty();
            });
    }
}
