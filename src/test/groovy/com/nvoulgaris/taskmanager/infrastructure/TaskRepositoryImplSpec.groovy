package com.nvoulgaris.taskmanager.infrastructure

import com.nvoulgaris.taskmanager.domain.task.Task
import com.nvoulgaris.taskmanager.domain.task.TaskRepository
import spock.lang.Specification

import static com.nvoulgaris.taskmanager.domain.task.TaskStatus.TODO

class TaskRepositoryImplSpec extends Specification {

  TaskRepository taskRepository

  def setup() {
    taskRepository = new TaskRepositoryImpl()
  }

  def "Should save a new task"() {
    given:
      Task task = new Task(UUID.randomUUID(), "Clean apartment", TODO, UUID.randomUUID(), false)

    when:
      Task savedTask = taskRepository.save(task)

    then:
      savedTask == task
  }
}
