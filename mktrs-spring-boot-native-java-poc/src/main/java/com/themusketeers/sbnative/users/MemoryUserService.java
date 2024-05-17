/*----------------------------------------------------------------------------*/
/* Source File:   MEMORYUSERSERVICE.JAVA                                      */
/* Copyright (c), 2023 The Musketeers                                         */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Jun.15/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.themusketeers.sbnative.users;

import com.themusketeers.sbnative.users.domain.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;

/**
 * Handles the list of users in the system. The internal representation is to
 * use a in-memory storage.
 *
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
@Service
public class MemoryUserService implements UserService {
    private List<User> userList = new ArrayList<>();

    @Override
    public Boolean exists(String userId) {
        return findUserInfo(userId).isPresent();
    }

    @Override
    public User insert(User user) {
        var userToInsert = user;

        if (user.id() == null) {
            userToInsert = new User(UUID.randomUUID().toString(), user.name(), user.address());
        }

        userList.add(userToInsert);
        return userToInsert;
    }

    @Override
    public User retrieve(String userId) {
        return findUserInfo(userId).orElseGet(() -> null);
    }

    @Override
    public Boolean delete(String userId) {
        return userList.removeIf(user -> user.id().equals(userId));
    }

    @Override
    public Boolean update(User user) {
        if (exists(user.id())) {
            delete(user.id());
            insert(user);
            return true;
        }

        return false;
    }

    @Override
    public List<User> retrieveAll() {
        return userList;
    }

    @Override
    public Long count() {
        return Long.valueOf(userList.size());
    }

    private Optional<User> findUserInfo(String userId) {
        return userList
            .stream()
            .filter(user -> user.id().equals(userId))
            .findFirst();
    }
}
