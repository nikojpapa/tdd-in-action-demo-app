package com.nvoulgaris.taskmanager.infrastructure

import com.nvoulgaris.taskmanager.domain.user.User
import com.nvoulgaris.taskmanager.domain.user.UserRepository
import spock.lang.Specification

class UserRepositoryImplSpec extends Specification {

  UserRepository userRepository

  def setup() {
    userRepository = new UserRepositoryImpl()
  }

  def "Should fetch all existing users"() {
    given:
      User alice = new User(UUID.randomUUID(), "Alice", "123")
      User bob = new User(UUID.randomUUID(), "Bob", "456")
      List<User> expectedUsers = [alice, bob]

    and:
      userRepository.save(alice)
      userRepository.save(bob)

    when:
      List<User> actualUsers = userRepository.findAll()

    then:
      actualUsers == expectedUsers
  }

  def "Should fetch existing users by their ID"() {
    given:
      UUID anId = UUID.randomUUID()
      User expectedUser = new User(anId, "Alice", "123")

    and:
      userRepository.save(expectedUser)

    when:
      User actualUser = userRepository.findById(anId)

    then:
      actualUser == expectedUser
  }

  def "Should return null when fetching a user by their ID and the user does not exist"() {
    given:
      UUID nonExistentUserId = UUID.randomUUID()

    when:
      User user = userRepository.findById(nonExistentUserId)

    then:
      user == null
  }
}
