package com.nvoulgaris.taskmanager.domain.task

interface TaskRepository {

  fun save(task: Task): Task
}
