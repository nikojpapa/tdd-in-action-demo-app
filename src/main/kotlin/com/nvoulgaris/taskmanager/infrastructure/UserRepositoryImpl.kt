package com.nvoulgaris.taskmanager.infrastructure

import com.nvoulgaris.taskmanager.domain.user.User
import com.nvoulgaris.taskmanager.domain.user.UserRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class UserRepositoryImpl : UserRepository {

  private val users: MutableList<User> = mutableListOf()

  override fun findAll(): List<User> =
    users.toList()

  override fun findById(userId: UUID): User? =
    users.firstOrNull { it.id == userId }

  override fun save(user: User): User {
    users.add(user)
    return user
  }
}