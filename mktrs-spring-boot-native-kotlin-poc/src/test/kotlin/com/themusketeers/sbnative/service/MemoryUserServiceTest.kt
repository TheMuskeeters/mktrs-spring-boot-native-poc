/*----------------------------------------------------------------------------*/
/* Source File:   MEMORYUSERSERVICETEST.JAVA                                  */
/* Copyright (c), 2023 The Musketeers                                         */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Jun.16/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.themusketeers.sbnative.service

import com.themusketeers.sbnative.common.consts.GlobalConstants.INT_TWO
import com.themusketeers.sbnative.common.consts.GlobalConstants.INT_ZERO
import com.themusketeers.sbnative.common.consts.GlobalConstants.LONG_ONE
import com.themusketeers.sbnative.common.consts.GlobalConstants.LONG_TWO
import com.themusketeers.sbnative.common.consts.GlobalConstants.LONG_ZERO
import com.themusketeers.sbnative.domain.User
import com.themusketeers.sbnative.service.intr.UserService

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

/**
 * Unit test for checking {@link UserService} interface when implemented as a Memory based storage.
 *
 * @author COQ- Carlos Adolfo Ortiz Q.
 */
class MemoryUserServiceTest {
    companion object {
        const val USER_ID_ONE = "c56b2741-028e-4ff5-9e15-be4f96b4ea35"
        const val USER_ID_TWO = "b94f6ae6-e1d2-4fdf-8c6b-eb471da1d4d1"
        const val USER_NAME_ONE = "Name One"
        const val USER_NAME_ONE_UPDATED = "Name One Updated"
        const val USER_NAME_TWO = "Name Two"
        const val USER_ADDRESS_ONE = "Address One"
        const val USER_ADDRESS_ONE_UPDATED = "Address One Updated"
        const val USER_ADDRESS_TWO = "Address Two"
    }

    private lateinit var userService: UserService

    @BeforeEach
    fun beforeEach() {
        userService = MemoryUserService()
    }

    @Test
    @DisplayName("When we insert a new user with no 'id' set, it generates one for us.")
    fun shouldCreateNewIdWhenInsertingNewUser() {
        val user = buildUserWithIDNULL()
        val insertedUser = userService.insert(user)

        assertThat(insertedUser).isNotNull()
        assertThat(insertedUser.id).isNotNull()
        assertThat(insertedUser.name).isEqualTo(user.name)
        assertThat(insertedUser.address).isEqualTo(user.address)
    }

    @Test
    @DisplayName("Given User Data set Then It is added to User list")
    fun givenUserInfoThenAddToUserList() {
        val user = buildUserWithIDOne()
        val insertedUser = userService.insert(user)

        assertThat(insertedUser)
            .isNotNull()
            .isEqualTo(user)
    }

    @Test
    @DisplayName("Verify User List is empty.")
    fun verifyUserListIsEmpty() {
        val userListSize = userService.count()

        assertThat(userListSize)
            .isEqualTo(LONG_ZERO)
    }

    @Test
    @DisplayName("Verify it should hold more than one item in user list")
    fun shouldHoldAsManyUsersInList() {
        userService.insert(buildUserWithIDOne())
        userService.insert(buildUserWithIDTwo())

        val userListSize = userService.count()

        assertThat(userListSize).isEqualTo(LONG_TWO)
    }

    @Test
    @DisplayName("When adding an user, verify the list has one element.")
    fun whenAddingUserThenListHasOneElement() {
        val user = buildUserWithIDOne()

        userService.insert(user)

        val userListSize = userService.count()

        assertThat(userListSize)
            .isEqualTo(LONG_ONE)
    }

    @Test
    @DisplayName("Verify that user with 'id' 1 exists in list of users.")
    fun givenUserWithIdOneThenExistsInUserList() {
        val user = buildUserWithIDOne()

        userService.insert(user)

        val userExists: Boolean = userService.exists(USER_ID_ONE)

        assertThat(userExists)
            .isTrue()
    }

    @Test
    @DisplayName("Verify that user with 'id' 2 does not exist in list of users.")
    fun givenUserWithIdTwoThenDoesNotExistsInUserList() {
        val user = buildUserWithIDOne()

        userService.insert(user)

        val userExists: Boolean = userService.exists(USER_ID_TWO)

        assertThat(userExists)
            .isFalse()
    }

    @Test
    @DisplayName("Verify we can retrieve a user with 'id' 1 from the User List")
    fun shouldRetrieveUserWithIDOneFromUserList() {
        val expectedUser = buildUserWithIDOne()

        userService.insert(expectedUser)

        val actualUser = userService.retrieve(USER_ID_ONE)

        assertThat(actualUser)
            .isNotNull()
            .isEqualTo(expectedUser)
    }

    @Test
    @DisplayName("Verify when we ask for an user 'id' of 2 that's not in the User List a NULL is given")
    fun whenUserIdIsTwoAndNotInUserListThenReturnNull() {
        userService.insert(buildUserWithIDOne())

        assertThat(userService.retrieve(USER_ID_TWO)).isNull()
    }

    @Test
    @DisplayName("Verify we can successfully delete an user from list.")
    fun shouldRemoveUserWithIdTwoFromList() {
        userService.insert(buildUserWithIDOne())
        userService.insert(buildUserWithIDTwo())

        val deletedUser: Boolean = userService.delete(USER_ID_TWO)

        assertThat(deletedUser)
            .isNotNull()
            .isTrue()
    }

    @Test
    @DisplayName("Verify when we remove user 'id' 2 and not present in user list it returns false.")
    fun shouldRemoveUserWthIdTwoFromList() {
        val deletedUser: Boolean = userService.delete(USER_ID_TWO)

        assertThat(deletedUser)
            .isNotNull()
            .isFalse()
    }

    @Test
    @DisplayName("Verify we can update an existing user in the list.")
    fun shouldUpdateExistingUserInList() {
        val updateInfo = buildUserWithIDOneForUpdate()

        userService.insert(buildUserWithIDOne())

        val userUpdated: Boolean = userService.update(updateInfo)
        assertThat(userUpdated)
            .isNotNull()
            .isTrue()
        assertThat(userService.retrieve(USER_ID_ONE))
            .isNotNull()
            .isEqualTo(updateInfo)
    }

    @Test
    @DisplayName("Verify we cannot update an non-existing user in the list.")
    fun shouldNotUpdateExistingUserInList() {
        val updateInfo = buildUserWithIDOneForUpdate()
        val userUpdated = userService.update(updateInfo)

        assertThat(userUpdated)
            .isNotNull()
            .isFalse()
    }

    @Test
    @DisplayName("Verify it can retrieve full list of users.")
    fun shouldRetrieveAllUserList() {
        val expectedList = buildUserList()

        userService.insert(buildUserWithIDOne())
        userService.insert(buildUserWithIDTwo())

        val actualUserList = userService.retrieveAll()

        assertThat(actualUserList)
            .isNotNull()
            .hasSize(INT_TWO)
            .isEqualTo(expectedList)
    }

    @Test
    @DisplayName("Verify it retrieves empty list as user list does not  elements yet.")
    fun shouldRetrieveAllUserListAsEmptyList() {
        val actualUserList = userService.retrieveAll()

        assertThat(actualUserList)
            .isNotNull()
            .hasSize(INT_ZERO)
    }

    private fun buildUserList() = mutableListOf(buildUserWithIDOne(), buildUserWithIDTwo())
    private fun buildUserWithIDOneForUpdate() = User(USER_ID_ONE, USER_NAME_ONE_UPDATED, USER_ADDRESS_ONE_UPDATED)
    private fun buildUserWithIDNULL() = User(null, USER_NAME_ONE, USER_ADDRESS_ONE)
    private fun buildUserWithIDOne() = User(USER_ID_ONE, USER_NAME_ONE, USER_ADDRESS_ONE)
    private fun buildUserWithIDTwo() = User(USER_ID_TWO, USER_NAME_TWO, USER_ADDRESS_TWO)
}
