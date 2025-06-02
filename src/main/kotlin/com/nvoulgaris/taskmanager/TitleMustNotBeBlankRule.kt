package com.nvoulgaris.taskmanager

import com.nvoulgaris.taskmanager.TaskStatus.IN_PROGRESS

class TitleMustNotBeBlankRule : TaskTransitionRule {

  override fun isSatisfiedBy(task: Task): Boolean =
    task.title.isNotBlank()

  override fun targetStatus(): TaskStatus = IN_PROGRESS
}