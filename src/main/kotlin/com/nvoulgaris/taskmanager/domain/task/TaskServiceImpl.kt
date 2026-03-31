package com.nvoulgaris.taskmanager.domain.task

import com.nvoulgaris.taskmanager.domain.task.validation.TaskTransitionValidator
import com.nvoulgaris.taskmanager.domain.user.UserRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class TaskServiceImpl(
  private val userRepository: UserRepository,
  private val taskRepository: TaskRepository,
  private val taskTransitionValidator: TaskTransitionValidator,
) : TaskService {

  override fun create(
    title: String,
    status: TaskStatus,
    assigneeId: UUID?,
    blocked: Boolean
  ): Task {
    userRepository.findById(assigneeId!!) ?: throw UserNotExistsException()
    val savedTask = taskRepository.save(Task(UUID.randomUUID(), title, status, assigneeId, blocked))
    return savedTask
  }

  override fun updateStatus(taskId: UUID, status: TaskStatus): Task {
    val task = taskRepository.findById(taskId) ?: throw TaskNotFoundException()
    val validTransition = taskTransitionValidator.validateFor(task, status)
    if (!validTransition)
      throw InvalidTaskStatusTransitionException()

    task.status = status
    val updatedTask = taskRepository.save(task)
    return updatedTask
  }

  override fun findByAssigneeId(assigneeId: UUID): List<Task> {
    throw UnsupportedOperationException("Not yet implemented")
  }
}