package com.nvoulgaris.taskmanager.api

import com.nvoulgaris.taskmanager.domain.task.TaskStatus

data class TaskStatusUpdateRequestDto(
  val status: TaskStatus
)
