package com.nvoulgaris.taskmanager.domain.task

import java.util.UUID

data class Task(
  val id: UUID,
  val title: String,
  var status: TaskStatus,
  val assigneeId: UUID?,
  val blocked: Boolean
)