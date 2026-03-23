package com.nvoulgaris.taskmanager.domain.user

import org.springframework.stereotype.Service
import java.util.UUID

@Service
class UserServiceImpl(
    private val userRepository: UserRepository
) : UserService {
    override fun create(username: String, password: String): User {
        val existingUsernames = userRepository.findAll().map { it.username }
        if (existingUsernames.contains(username)) {
            throw UsernameExistsException()
        }
        val savedUser = userRepository.save(User(UUID.randomUUID(), username, password))
        return savedUser
    }
}
