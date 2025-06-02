package com.nvoulgaris.taskmanager

import java.util.UUID

data class Task(
  val id: UUID,
  val title: String,
  val status: TaskStatus,
  val assigneeId: UUID?,
  val blocked: Boolean
)
