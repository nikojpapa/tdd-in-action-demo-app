package com.nvoulgaris.taskmanager.infrastructure

import com.nvoulgaris.taskmanager.domain.user.User
import com.nvoulgaris.taskmanager.domain.user.UserRepository
import org.springframework.stereotype.Repository

@Repository
class UserRepositoryImpl : UserRepository {

    private val users = mutableListOf<User>()

    override fun findAll(): List<User> = users.toList()

    override fun save(user: User): User {
        users.add(user)
        return user
    }
}
