/*----------------------------------------------------------------------------*/ /* Source File:   MEMORYUSERSERVICE.JAVA                                      */ /* Copyright (c), 2023 The Musketeers                                         */ /*----------------------------------------------------------------------------*/ /*-----------------------------------------------------------------------------
 History
 Jun.15/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.themusketeers.sbnative.service

import com.themusketeers.sbnative.domain.User
import com.themusketeers.sbnative.service.intr.UserService
import org.springframework.stereotype.Service

@Service
class MemoryUserService : UserService {
    private val userList: MutableList<User> = ArrayList()

    override fun add(user: User): Boolean {
        return true
    }

    override fun remove(user: User): Boolean {
        return true
    }

    override fun update(user: User): Boolean {
        return true
    }

    override fun retrieveAll(): List<User> {
        return userList
    }

    override fun size(): Long {
        return userList.size.toLong()
    }
}
