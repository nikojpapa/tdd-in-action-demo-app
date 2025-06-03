package com.nvoulgaris.taskmanager

import com.nvoulgaris.taskmanager.domain.task.Task
import com.nvoulgaris.taskmanager.domain.task.TaskStatus
import com.nvoulgaris.taskmanager.domain.task.TaskStatus.IN_PROGRESS

class TitleMustBeAtLeastThreeCharactersLongRule : TaskTransitionRule {

  override fun isSatisfiedBy(task: Task): Boolean =
    task.title.length > 2

  override fun targetStatus(): TaskStatus = IN_PROGRESS
}