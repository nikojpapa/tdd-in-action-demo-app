package com.nvoulgaris.taskmanager

import com.nvoulgaris.taskmanager.domain.task.Task
import com.nvoulgaris.taskmanager.domain.task.TaskStatus

class TaskTransitionValidator(
  private val transitionRules: List<TaskTransitionRule>
) {

  fun validateFor(task: Task, targetStatus: TaskStatus): Boolean =
    transitionRules
      .filter { it.targetStatus() == targetStatus }
      .all { it.isSatisfiedBy(task) }
}