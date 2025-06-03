package com.nvoulgaris.taskmanager.domain.task

import com.nvoulgaris.taskmanager.domain.task.validation.TaskTransitionValidator
import com.nvoulgaris.taskmanager.domain.user.User
import com.nvoulgaris.taskmanager.domain.user.UserRepository
import spock.lang.Specification

import static com.nvoulgaris.taskmanager.domain.task.TaskStatus.DONE
import static com.nvoulgaris.taskmanager.domain.task.TaskStatus.IN_PROGRESS
import static com.nvoulgaris.taskmanager.domain.task.TaskStatus.TODO

class TaskServiceImplSpec extends Specification {

  UserRepository userRepository = Mock()
  TaskRepository taskRepository = Mock()
  TaskTransitionValidator taskTransitionValidator = Mock()
  TaskService taskService

  def setup() {
    taskService = new TaskServiceImpl(userRepository, taskRepository, taskTransitionValidator)
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

  def "Should throw an InvalidTaskStatusTransitionException when an invalid task status update is requested"() {
    given:
      TaskStatus targetStatus = DONE
      UUID taskId = UUID.randomUUID()
      Task task = new Task(taskId, "Clean apartment", IN_PROGRESS, UUID.randomUUID(), false)

    and:
      taskRepository.findById(taskId) >> task
      taskTransitionValidator.validateFor(task, targetStatus)

    when:
      taskService.updateStatus(taskId, targetStatus)

    then:
      thrown(InvalidTaskStatusTransitionException)
  }

  def "Should throw a TaskNotFoundException when requested to update the status of a non-existent task"() {
    given:
      UUID nonExistentTaskId = UUID.randomUUID()

    and:
      taskRepository.findById(nonExistentTaskId) >> null

    when:
      taskService.updateStatus(nonExistentTaskId, DONE)

    then:
      thrown(TaskNotFoundException)
  }

  def "Should update the status of an existing task"() {
    given:
      TaskStatus targetStatus = DONE
      UUID taskId = UUID.randomUUID()
      Task task = new Task(taskId, "Clean apartment", IN_PROGRESS, UUID.randomUUID(), false)

    and:
      taskRepository.findById(taskId) >> task
      taskTransitionValidator.validateFor(task, targetStatus) >> true

    when:
      taskService.updateStatus(taskId, targetStatus)

    then:
      1 * taskRepository.save(*_) >> { args ->
        with(args[0] as Task) {
          id == task.id
          title == task.title
          status == targetStatus
          assigneeId == task.assigneeId
          blocked == task.blocked
        }
      }
  }
}
