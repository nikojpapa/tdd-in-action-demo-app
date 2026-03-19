package com.nvoulgaris.taskmanager.domain.user

interface UserRepository {
    fun findAll(): List<User>
}