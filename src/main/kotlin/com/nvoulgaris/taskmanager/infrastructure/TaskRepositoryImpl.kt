package com.nvoulgaris.taskmanager.infrastructure

import com.nvoulgaris.taskmanager.domain.task.Task
import com.nvoulgaris.taskmanager.domain.task.TaskRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class TaskRepositoryImpl : TaskRepository {

  private val tasks: MutableList<Task> = mutableListOf()

  override fun save(task: Task): Task {
    tasks.add(task)
    return task
  }

  override fun findById(taskId: UUID): Task? =
    tasks.firstOrNull { it.id == taskId }

  override fun findByAssigneeId(assigneeId: UUID): List<Task> =
    tasks.filter { it.assigneeId == assigneeId }
}