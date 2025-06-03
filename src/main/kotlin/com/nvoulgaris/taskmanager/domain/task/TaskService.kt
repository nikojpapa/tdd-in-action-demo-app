package com.nvoulgaris.taskmanager.domain.task

import java.util.UUID

interface TaskService {

  fun create(title: String, status: TaskStatus, assigneeId: UUID?, blocked: Boolean): Task

  fun updateStatus(taskId: UUID, status: TaskStatus): Task

  fun getTasksByAssignee(assigneeId: UUID): List<Task>
}