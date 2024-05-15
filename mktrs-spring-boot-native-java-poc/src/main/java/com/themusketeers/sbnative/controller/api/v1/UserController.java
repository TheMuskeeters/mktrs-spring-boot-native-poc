/*----------------------------------------------------------------------------*/
/* Source File:   USERCONTROLLER.JAVA                                         */
/* Copyright (c), 2023 The Musketeers                                         */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 May.14/2024  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.themusketeers.sbnative.controller.api.v1;

import static com.themusketeers.sbnative.common.consts.GlobalConstants.USER_CONTROLLER_DELETE_USER_INFO;
import static com.themusketeers.sbnative.common.consts.GlobalConstants.USER_CONTROLLER_GET_RETRIEVE_USERS_INFO;
import static com.themusketeers.sbnative.common.consts.GlobalConstants.USER_CONTROLLER_GET_RETRIEVE_USER_INFO;
import static com.themusketeers.sbnative.common.consts.GlobalConstants.USER_CONTROLLER_PATCH_USER_INFO;
import static com.themusketeers.sbnative.common.consts.GlobalConstants.USER_CONTROLLER_POST_INSERT_USER_INFO;

import com.themusketeers.sbnative.common.exception.UserNotFoundException;
import com.themusketeers.sbnative.domain.User;
import com.themusketeers.sbnative.domain.response.UserDataResponse;
import com.themusketeers.sbnative.domain.response.UsersDataResponse;
import com.themusketeers.sbnative.service.intr.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * User API Controller.
 * <p><b>Path:</b>{@code api/v1/users}</p>
 *
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
@RestController
@RequestMapping("api/v1/users")
public record UserController(UserService userService) {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    /**
     * Retrieves all users registered in the system.
     * <p>{@code GET: api/v1/users}</p>
     *
     * @return Registered information.
     */
    @GetMapping
    public UsersDataResponse retrieveUsers() {
        log.info(USER_CONTROLLER_GET_RETRIEVE_USERS_INFO);

        return new UsersDataResponse(userService.count(), userService.retrieveAll());
    }

    /**
     * Retrieve one user registered in the system.
     * <p>{@code GET: api/v1/users/{userId} }</p>
     *
     * @param userId Indicates the user unique identifier to search. If it is empty or NULL an exception is thrown.
     * @return If it is not found an HTTP 404 is returned, otherwise an HTTP 200 is returned with the proper information.
     */
    @GetMapping("{userId}")
    public UserDataResponse retrieveUser(@PathVariable String userId) {
        log.info(USER_CONTROLLER_GET_RETRIEVE_USER_INFO);
        log.info("==> User Id=[" + userId + "]");

        var userRetrieved = userService.retrieve(userId);

        if (userRetrieved == null) {
            throw new UserNotFoundException(userId);
        }

        return new UserDataResponse(userRetrieved);
    }

    /**
     * Add new record to the User List system.
     * <p>{@code POST: api/v1/users}</p>
     * <p>For the User to be inserted there are validations for required fields, {@code name} and {@code address}.
     * A BAD REQUEST 400 error code is returned when {@code payload} is mal formed.</p>
     *
     * @param user Includes the user information to insert.
     * @return Record with 'Id' inserted.
     */
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public User insertUser(@Valid @RequestBody User user) {
        log.info(USER_CONTROLLER_POST_INSERT_USER_INFO);
        log.info("==> Payload user=[" + user + "]");

        return userService.insert(user);
    }

    /**
     * Modifies the data for the user.
     * <p>{@code PATCH: api/v1/users}</p>
     * <p>For the User to be inserted there are validations for required fields, {@code name} and {@code address}.
     * A BAD REQUEST 400 error code is returned when {@code payload} is mal formed.</p>
     *
     * @param user Includes the user information to update.
     * @return If record is not found, then an HTTP 404 is returned, otherwise an HTTP 200 is returned.
     */
    @PatchMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.info(USER_CONTROLLER_PATCH_USER_INFO);
        log.info("==> Payload user=[" + user + "]");

        if (!userService.update(user)) {
            throw new UserNotFoundException(user.id());
        }

        return user;
    }

    /**
     * Removes an User from the system.
     * <p>{@code DELETE api/v1/users/{userId} }</p>
     *
     * @param userId Indicates the user unique identifier to search. If it is empty or NULL an exception is thrown.
     * @return HTTP 200 if removed, HTTP 404 if user record not found.
     */
    @DeleteMapping("{userId}")
    public Boolean deleteUser(@PathVariable String userId) {
        log.info(USER_CONTROLLER_DELETE_USER_INFO);
        log.info("==> User Id=[" + userId + "]");

        if (!userService.delete(userId)) {
            throw new UserNotFoundException(userId);
        }

        return true;
    }
}
