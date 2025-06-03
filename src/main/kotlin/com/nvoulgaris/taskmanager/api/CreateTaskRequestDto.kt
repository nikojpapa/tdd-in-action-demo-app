package com.nvoulgaris.taskmanager.api

import com.nvoulgaris.taskmanager.domain.task.TaskStatus
import java.util.UUID

data class CreateTaskRequestDto(
  val title: String,
  val status: TaskStatus,
  val assigneeId: UUID?,
  val blocked: Boolean,
)
