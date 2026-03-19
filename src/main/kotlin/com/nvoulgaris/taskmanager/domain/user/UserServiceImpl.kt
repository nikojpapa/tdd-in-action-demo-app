package com.nvoulgaris.taskmanager.domain.user

import org.springframework.stereotype.Service

@Service
class UserServiceImpl(
    private val userRepository: UserRepository
) : UserService {
    override fun create(username: String, password: String): User {
        val existingUsernames = userRepository.findAll().map { it.username }
        if (existingUsernames.contains(username)) {
            throw UsernameExistsException()
        }
        throw UnsupportedOperationException()
    }
}
