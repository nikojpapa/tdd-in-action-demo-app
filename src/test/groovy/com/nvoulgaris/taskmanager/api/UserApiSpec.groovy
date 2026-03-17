package com.nvoulgaris.taskmanager.api

import com.nvoulgaris.taskmanager.domain.user.User
import com.nvoulgaris.taskmanager.domain.user.UserService
import com.nvoulgaris.taskmanager.domain.user.UsernameExistsException
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import spock.lang.Specification

class UserApiSpec extends Specification {

    UserService userService = Mock()
    UserApi userApi

    def setup() {
        userApi = new UserApi(userService)
    }
    
    def "should return a 400 HTTP status code when creating a new user with an existing username"() {
        given:
            String username = "Alice"
            String password = "123"
            UserRegistrationRequestDto request = new UserRegistrationRequestDto(username, password)
        and:
            userService.create(username, password) >> { throw new UsernameExistsException() }
        when:
            ResponseEntity<User> response = userApi.register(request)
        then:
            response.statusCode == HttpStatusCode.valueOf(400)
            
    }

    def "should register a new user"() {
        given:
            UUID id = UUID.randomUUID()
            String username = "Alice"
            String password = "123"
            UserRegistrationRequestDto request = new UserRegistrationRequestDto(username, password)
            User user = new User(id, username, password)
        and:
            userService.create(username, password) >> user
        when:
            ResponseEntity<User> response = userApi.register(request)
        then:
            response.statusCode == HttpStatusCode.valueOf(201)
            response.headers.getLocation().toString() == "/users/$id"
            response.body == user
    }
}