package com.nvoulgaris.taskmanager.domain.user

interface UserService {
    fun create(username: String, password: String): User
}