package com.nvoulgaris.taskmanager.api

import com.nvoulgaris.taskmanager.domain.user.User
import com.nvoulgaris.taskmanager.domain.user.UserService
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody

class UserApi(
    val userService: UserService
) {

    companion object {
        private val logger = LoggerFactory.getLogger(UserApi::class.java)
    }

    fun register(@RequestBody request: UserRegistrationRequestDto): ResponseEntity<User> {
        try {
            userService.create(request.username, request.password)
            return ResponseEntity.ok().body(null)
        } catch (e: Exception) {
            logger.warn("Creating a new user failed", e)
            return ResponseEntity.badRequest().body(null)
        }
    }
}