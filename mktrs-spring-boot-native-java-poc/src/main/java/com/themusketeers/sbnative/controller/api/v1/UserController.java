/*----------------------------------------------------------------------------*/
/* Source File:   USERCONTROLLER.JAVA                                         */
/* Copyright (c), 2023 The Musketeers                                         */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 May.30/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.themusketeers.sbnative.controller.api.v1;

import static com.themusketeers.sbnative.common.consts.GlobalConstants.USER_CONTROLLER_GET_RETRIEVE_USERS_INFO;
import static com.themusketeers.sbnative.common.consts.GlobalConstants.USER_CONTROLLER_POST_INSERT_USER_INFO;

import com.themusketeers.sbnative.domain.User;
import com.themusketeers.sbnative.domain.response.UserDataResponse;
import com.themusketeers.sbnative.service.intr.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * User API Controller.
 * <p><b>Path:</b>api/v1/users</p>
 *
 * @author ORTIC140 - Carlos Adolfo Ortiz Q.
 */
@RestController
@RequestMapping("api/v1/users")
public record UserController(UserService userService) {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    /**
     * Retrieves all users registered in the system.
     * <p>GET: api/v1/users/all</p>
     *
     * @return Registered information.
     */
    @GetMapping("all")
    public UserDataResponse retrieveUsers() {
        log.info(USER_CONTROLLER_GET_RETRIEVE_USERS_INFO);
        return new UserDataResponse(userService.size(), userService.retrieveAll());
    }

    /**
     * Add new record to the User List system.
     * <p>POST: api/v1/users</p>
     *
     * @param user Includes the user to insert.
     * @return Record with Id inserted.
     */
    @PostMapping()
    public User insertUser(@RequestBody User user) {
        log.info(USER_CONTROLLER_POST_INSERT_USER_INFO);
        log.info("==> Payload user=[" + user + "]");
        userService.add(user);

        return user;
    }
}
