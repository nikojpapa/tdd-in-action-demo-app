package com.nvoulgaris.taskmanager

import com.nvoulgaris.taskmanager.domain.task.Task
import com.nvoulgaris.taskmanager.domain.task.TaskStatus
import com.nvoulgaris.taskmanager.domain.task.TaskStatus.IN_PROGRESS

class TitleMustNotBeBlankRule : TaskTransitionRule {

  override fun isSatisfiedBy(task: Task): Boolean =
    task.title.isNotBlank()

  override fun targetStatus(): TaskStatus = IN_PROGRESS
}