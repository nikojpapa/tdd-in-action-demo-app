package com.nvoulgaris.taskmanager.api

import com.nvoulgaris.taskmanager.domain.user.User
import com.nvoulgaris.taskmanager.domain.user.UserService
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
@RequestMapping("/api/users")
class UserApi(
  val userService: UserService
) {

  companion object {
    private val logger = LoggerFactory.getLogger(UserApi::class.java)
  }

  @PostMapping(
    consumes = [APPLICATION_JSON_VALUE],
    produces = [APPLICATION_JSON_VALUE]
  )
  fun register(@RequestBody request: UserRegistrationRequestDto): ResponseEntity<User> {
    try {
      val user = userService.create(request.username, request.password)
      return createdResponseWith(user)
    } catch (e: Exception) {
      logger.warn("Creating a new user failed", e)
      return ResponseEntity.badRequest().body(null)
    }
  }

  private fun createdResponseWith(user: User): ResponseEntity<User> =
    ResponseEntity
      .created(URI.create("/users/${user.id}"))
      .body(user)
}
