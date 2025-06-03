package com.nvoulgaris.taskmanager.infrastructure

import com.nvoulgaris.taskmanager.domain.task.Task
import com.nvoulgaris.taskmanager.domain.task.TaskRepository
import org.springframework.stereotype.Repository

@Repository
class TaskRepositoryImpl : TaskRepository {

  private val tasks: MutableList<Task> = mutableListOf()

  override fun save(task: Task): Task {
    tasks.add(task)
    return task
  }
}