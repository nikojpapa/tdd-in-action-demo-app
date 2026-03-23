package com.nvoulgaris.taskmanager.domain.user

import spock.lang.Specification

class UserServiceImplSpec extends Specification {

    UserRepository userRepository = Mock()
    UserService userService

    def setup() {
        userService = new UserServiceImpl(userRepository)
    }

    def "should throw a UsernameExistsException when creating a user with an existing username"() {
        given:
            String existingUsername = "Alice"
            String password = "123"
        and:
            userRepository.findAll() >> [new User(UUID.randomUUID(), existingUsername, password)]
        when:
            userService.create(existingUsername, password)
        then:
            thrown UsernameExistsException
    }

    def "should create a new user"() {
        given:
            String aUsername = "Alice"
            String aPassword = "123"
        and:
            userRepository.findAll() >> []
        when:
            userService.create(aUsername, aPassword)
        then:
            1 * userRepository.save(*_) >> { args -> 
                with(args[0] as User) {
                    id != null
                    username == aUsername
                    password == aPassword
                }
            }
    }
}