package com.nvoulgaris.taskmanager.domain.task

import java.util.UUID

interface TaskRepository {

  fun save(task: Task): Task

  fun findById(taskId: UUID): Task?

  fun findByAssigneeId(assigneeId: UUID): List<Task>
}
