/*----------------------------------------------------------------------------*/
/* Source File:   MEMORYUSERSERVICETEST.JAVA                                  */
/* Copyright (c), 2023 The Musketeers                                         */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Jun.15/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.themusketeers.sbnative.service;

import static com.themusketeers.sbnative.common.consts.GlobalConstants.INT_ONE;
import static com.themusketeers.sbnative.common.consts.GlobalConstants.INT_TWO;
import static com.themusketeers.sbnative.common.consts.GlobalConstants.INT_ZERO;
import static org.assertj.core.api.Assertions.assertThat;

import com.themusketeers.sbnative.domain.User;
import com.themusketeers.sbnative.service.intr.UserService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemoryUserServiceTest {

    public static final String USER_ID_ONE = "c56b2741-028e-4ff5-9e15-be4f96b4ea35";
    public static final String USER_ID_TWO = "b94f6ae6-e1d2-4fdf-8c6b-eb471da1d4d1";

    public static final String USER_NAME_ONE = "Name One";
    public static final String USER_NAME_ONE_UPDATED = "Name One Updated";
    public static final String USER_NAME_TWO = "Name Two";
    public static final String USER_ADDRESS_ONE = "Address One";
    public static final String USER_ADDRESS_ONE_UPDATED = "Address One Updated";
    public static final String USER_ADDRESS_TWO = "Address Two";

    private UserService userService;

    @BeforeEach
    void beforeEach() {
        userService = new MemoryUserService();
    }

    @Test
    @DisplayName("When we insert a new user with no 'id' set, it generates one for us.")
    void shouldCreateNewIdWhenInsertingNewUser() {
        var user = buildUserWithIDNULL();
        var insertedUser = userService.insert(user);

        assertThat(insertedUser).isNotNull();
        assertThat(insertedUser.id()).isNotNull();
        assertThat(insertedUser.name()).isEqualTo(user.name());
        assertThat(insertedUser.address()).isEqualTo(user.address());
    }

    @Test
    @DisplayName("Given User Data set Then It is added to User list")
    void givenUserInfoThenAddToUserList() {
        var user = buildUserWithIDOne();
        var insertedUser = userService.insert(user);

        assertThat(insertedUser)
            .isNotNull()
            .isEqualTo(user);
    }

    @Test
    @DisplayName("Verify User List is empty.")
    void verifyUserListIsEmpty() {
        var userListSize = userService.count();

        assertThat(userListSize)
            .isEqualTo(INT_ZERO);
    }

    @Test
    @DisplayName("Verify it should hold more than one item in user list")
    void shouldHoldAsManyUsersInList() {
        userService.insert(buildUserWithIDOne());
        userService.insert(buildUserWithIDTwo());

        var userListSize = userService.count();

        assertThat(userListSize).isEqualTo(INT_TWO);
    }

    @Test
    @DisplayName("When adding an user, verify the list has one element.")
    void whenAddingUserThenListHasOneElement() {
        var user = buildUserWithIDOne();

        userService.insert(user);

        var userListSize = userService.count();

        assertThat(userListSize)
            .isEqualTo(INT_ONE);
    }

    @Test
    @DisplayName("Verify that user with 'id' 1 exists in list of users.")
    void givenUserWithIdOneThenExistsInUserList() {
        var user = buildUserWithIDOne();

        userService.insert(user);

        var userExists = userService.exists(USER_ID_ONE);

        assertThat(userExists)
            .isTrue();
    }

    @Test
    @DisplayName("Verify that user with 'id' 2 does not exist in list of users.")
    void givenUserWithIdTwoThenDoesNotExistsInUserList() {
        var user = buildUserWithIDOne();

        userService.insert(user);

        var userExists = userService.exists(USER_ID_TWO);

        assertThat(userExists)
            .isFalse();
    }

    @Test
    @DisplayName("Verify we can retrieve a user with 'id' 1 from the User List")
    void shouldRetrieveUserWithIDOneFromUserList() {
        var expectedUser = buildUserWithIDOne();

        userService.insert(expectedUser);

        var actualUser = userService.retrieve(USER_ID_ONE);

        assertThat(actualUser)
            .isNotNull()
            .isEqualTo(expectedUser);
    }

    @Test
    @DisplayName("Verify when we ask for an user 'id' of 2 that's not in the User List a NULL is given")
    void whenUserIdIsTwoAndNotInUserListThenReturnNull() {
        userService.insert(buildUserWithIDOne());

        var actualUser = userService.retrieve(USER_ID_TWO);

        assertThat(actualUser).isNull();
    }

    @Test
    @DisplayName("Verify we can successfully delete an user from list.")
    void shouldRemoveUserWithIdTwoFromList() {
        userService.insert(buildUserWithIDOne());
        userService.insert(buildUserWithIDTwo());

        var deletedUser = userService.delete(USER_ID_TWO);

        assertThat(deletedUser)
            .isNotNull()
            .isTrue();
    }

    @Test
    @DisplayName("Verify when we remove user 'id' 2 and not present in user list it returns false.")
    void shouldRemoveUserWthIdTwoFromList() {
        var deletedUser = userService.delete(USER_ID_TWO);

        assertThat(deletedUser)
            .isNotNull()
            .isFalse();
    }

    @Test
    @DisplayName("Verify we can update an existing user in the list.")
    void shouldUpdateExistingUserInList() {
        var updateInfo = buildUserWithIDOneForUpdate();

        userService.insert(buildUserWithIDOne());

        var userUpdated = userService.update(updateInfo);

        assertThat(userUpdated)
            .isNotNull()
            .isTrue();

        assertThat(userService.retrieve(USER_ID_ONE))
            .isNotNull()
            .isEqualTo(updateInfo);
    }

    @Test
    @DisplayName("Verify we cannot update an non-existing user in the list.")
    void shouldNotUpdateExistingUserInList() {
        var updateInfo = buildUserWithIDOneForUpdate();
        var userUpdated = userService.update(updateInfo);

        assertThat(userUpdated)
            .isNotNull()
            .isFalse();
    }

    @Test
    @DisplayName("Verify it can retrieve full list of users.")
    void shouldRetrieveAllUserList() {
        var expectedList = buildUserList();

        userService.insert(buildUserWithIDOne());
        userService.insert(buildUserWithIDTwo());

        var actualUserList = userService.retrieveAll();

        assertThat(actualUserList)
            .isNotNull()
            .hasSize(INT_TWO)
            .isEqualTo(expectedList);
    }

    @Test
    @DisplayName("Verify it retrieves empty list as user list does not  elements yet.")
    void shouldRetrieveAllUserListAsEmptyList() {
        var actualUserList = userService.retrieveAll();

        assertThat(actualUserList)
            .isNotNull()
            .hasSize(INT_ZERO);
    }

    private List<User> buildUserList() {
        return List.of(buildUserWithIDOne(), buildUserWithIDTwo());
    }

    private User buildUserWithIDOneForUpdate() {
        return new User(USER_ID_ONE, USER_NAME_ONE_UPDATED, USER_ADDRESS_ONE_UPDATED);
    }

    private User buildUserWithIDNULL() {
        return new User(null, USER_NAME_ONE, USER_ADDRESS_ONE);
    }

    private User buildUserWithIDOne() {
        return new User(USER_ID_ONE, USER_NAME_ONE, USER_ADDRESS_ONE);
    }

    private User buildUserWithIDTwo() {
        return new User(USER_ID_TWO, USER_NAME_TWO, USER_ADDRESS_TWO);
    }
}