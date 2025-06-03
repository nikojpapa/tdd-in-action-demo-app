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

  def "Should fetch a task by its ID"() {
    given:
      UUID taskId = UUID.randomUUID()
      Task expectedTask = new Task(taskId, "Clean apartment", TODO, UUID.randomUUID(), false)

    and:
      taskRepository.save(expectedTask)

    when:
      Task actualTask = taskRepository.findById(taskId)

    then:
      actualTask == expectedTask
  }

  def "Should return all tasks for a given assignee"() {
    given:
      UUID assigneeId = UUID.randomUUID()
      Task task1 = new Task(UUID.randomUUID(), "Task 1", TODO, assigneeId, false)
      Task task2 = new Task(UUID.randomUUID(), "Task 2", TODO, assigneeId, false)
      Task otherTask = new Task(UUID.randomUUID(), "Other Task", TODO, UUID.randomUUID(), false)

    and:
      taskRepository.save(task1)
      taskRepository.save(task2)
      taskRepository.save(otherTask)

    when:
      def result = taskRepository.findByAssigneeId(assigneeId)

    then:
      result.containsAll([task1, task2])
      result.size() == 2
  }

  def "Should return an empty list if user has no tasks"() {
    given:
      UUID assigneeId = UUID.randomUUID()

    when:
      def result = taskRepository.findByAssigneeId(assigneeId)

    then:
      result.isEmpty()
  }
}
