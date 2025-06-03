package com.nvoulgaris.taskmanager.api

import com.nvoulgaris.taskmanager.domain.task.InvalidTaskStatusTransitionException
import com.nvoulgaris.taskmanager.domain.task.Task
import com.nvoulgaris.taskmanager.domain.task.TaskNotFoundException
import com.nvoulgaris.taskmanager.domain.task.TaskService
import com.nvoulgaris.taskmanager.domain.task.TaskStatus
import com.nvoulgaris.taskmanager.domain.task.UserNotExistsException
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import spock.lang.Specification

import static com.nvoulgaris.taskmanager.domain.task.TaskStatus.IN_PROGRESS
import static com.nvoulgaris.taskmanager.domain.task.TaskStatus.TODO

class TaskApiSpec extends Specification {

  TaskService taskService = Mock()
  TaskApi taskApi

  def setup() {
    taskApi = new TaskApi(taskService)
  }

  def "Should return a 400 when creating a task that is assigned to a non-existent user"() {
    given:
      String title = "Clean apartment"
      TaskStatus status = TODO
      UUID assigneeId = UUID.randomUUID()
      boolean blocked = false
      CreateTaskRequestDto request = new CreateTaskRequestDto(title, status, assigneeId, blocked)

    and:
      taskService.create(title, status, assigneeId, blocked) >> { throw new UserNotExistsException() }

    when:
      ResponseEntity<Task> response = taskApi.create(request)

    then:
      response.statusCode == HttpStatusCode.valueOf(400)
  }

  def "Should create a new task"() {
    given:
      UUID taskId = UUID.randomUUID()
      String title = "Clean apartment"
      TaskStatus status = TODO
      UUID assigneeId = UUID.randomUUID()
      boolean blocked = false
      CreateTaskRequestDto request = new CreateTaskRequestDto(title, status, assigneeId, blocked)
      Task task = new Task(taskId, title, status, assigneeId, blocked)

    and:
      taskService.create(title, status, assigneeId, blocked) >> task

    when:
      ResponseEntity<Task> response = taskApi.create(request)

    then:
      response.statusCode == HttpStatusCode.valueOf(201)
      response.body == task
  }

  def "Should return a 404 when updating the status of a non-existent task"() {
    given:
      UUID taskId = UUID.randomUUID()
      TaskStatus status = IN_PROGRESS
      TaskStatusUpdateRequestDto request = new TaskStatusUpdateRequestDto(status)

    and:
      taskService.updateStatus(taskId, status) >> { throw new TaskNotFoundException() }

    when:
      ResponseEntity<Task> response = taskApi.updateTask(taskId, request)

    then:
      response.statusCode == HttpStatusCode.valueOf(404)
  }

  def "Should return a 400 when updating the status of a task in an invalid way"() {
    given:
      UUID taskId = UUID.randomUUID()
      TaskStatus status = IN_PROGRESS
      TaskStatusUpdateRequestDto request = new TaskStatusUpdateRequestDto(status)

    and:
      taskService.updateStatus(taskId, status) >> { throw new InvalidTaskStatusTransitionException() }

    when:
      ResponseEntity<Task> response = taskApi.updateTask(taskId, request)

    then:
      response.statusCode == HttpStatusCode.valueOf(400)
  }

  def "Should update the status of a task"() {
    given:
      UUID taskId = UUID.randomUUID()
      TaskStatus status = IN_PROGRESS
      TaskStatusUpdateRequestDto request = new TaskStatusUpdateRequestDto(status)
      Task updatedTask = new Task(UUID.randomUUID(), "Clean apartment", IN_PROGRESS, UUID.randomUUID(), false)

    and:
      taskService.updateStatus(taskId, status) >> updatedTask

    when:
      ResponseEntity<Task> response = taskApi.updateTask(taskId, request)

    then:
      response.statusCode == HttpStatusCode.valueOf(200)
      response.body == updatedTask
  }
}
