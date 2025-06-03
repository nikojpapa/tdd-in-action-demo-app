package com.nvoulgaris.taskmanager.domain.task

import com.nvoulgaris.taskmanager.domain.user.User
import com.nvoulgaris.taskmanager.domain.user.UserRepository
import spock.lang.Specification

import static com.nvoulgaris.taskmanager.domain.task.TaskStatus.TODO

class TaskServiceImplSpec extends Specification {

  UserRepository userRepository = Mock()
  TaskRepository taskRepository = Mock()
  TaskService taskService

  def setup() {
    taskService = new TaskServiceImpl(userRepository, taskRepository)
  }

  def "Should throw an UserNotExistsException when creating a new task with a non-existent user as an assignee"() {
    given:
      String title = "Clean apartment"
      TaskStatus status = TODO
      UUID nonExistentUserId = UUID.randomUUID()

    and:
      userRepository.findById(nonExistentUserId) >> null

    when:
      taskService.create(title, status, nonExistentUserId, false)

    then:
      thrown(UserNotExistsException)
  }

  def "Should create a new task"() {
    given:
      String aTitle = "Clean apartment"
      TaskStatus aStatus = TODO
      UUID aUserId = UUID.randomUUID()

    and:
      userRepository.findById(aUserId) >> new User(aUserId, "Alice", "123")

    when:
      taskService.create(aTitle, aStatus, aUserId, false)

    then:
      1 * taskRepository.save(*_) >> { args ->
        with(args[0] as Task) {
          id != null
          title == aTitle
          status == aStatus
          assigneeId == aUserId
          !blocked
        }
      }
  }
}
