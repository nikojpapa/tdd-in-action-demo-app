package com.nvoulgaris.taskmanager.domain.user

import java.util.UUID

interface UserRepository {

  fun findAll(): List<User>

  fun findById(userId: UUID): User?

  fun save(user: User): User
}