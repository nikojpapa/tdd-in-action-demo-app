package com.nvoulgaris.taskmanager

import com.nvoulgaris.taskmanager.TaskStatus.IN_PROGRESS

class TitleMustBeAtLeastThreeCharactersLongRule : TaskTransitionRule {

  override fun isSatisfiedBy(task: Task): Boolean =
    task.title.length > 2

  override fun targetStatus(): TaskStatus = IN_PROGRESS
}