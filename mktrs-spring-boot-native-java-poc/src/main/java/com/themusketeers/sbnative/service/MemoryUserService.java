/*----------------------------------------------------------------------------*/
/* Source File:   MEMORYUSERSERVICE.JAVA                                      */
/* Copyright (c), 2023 The Musketeers                                         */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Jun.15/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.themusketeers.sbnative.service;

import com.themusketeers.sbnative.domain.User;
import com.themusketeers.sbnative.service.intr.UserService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class MemoryUserService implements UserService {
    private List<User> userList = new ArrayList<>();

    @Override
    public Boolean add(User user) {
        return true;
    }

    @Override
    public Boolean remove(User user) {
        return true;
    }

    @Override
    public Boolean update(User user) {
        return true;
    }

    @Override
    public List<User> retrieveAll() {
        return userList;
    }

    @Override
    public Long size() {
        return Long.valueOf(userList.size());
    }
}
