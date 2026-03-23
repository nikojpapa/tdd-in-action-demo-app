package com.nvoulgaris.taskmanager.domain.user

interface UserRepository {
    fun findAll(): List<User>
    fun save(user: User): User
}