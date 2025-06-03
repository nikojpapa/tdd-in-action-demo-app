package com.nvoulgaris.taskmanager.domain.task.validation

import com.nvoulgaris.taskmanager.domain.task.Task
import com.nvoulgaris.taskmanager.domain.task.TaskStatus
import org.springframework.stereotype.Service

@Service
class TaskTransitionValidator(
  private val transitionRules: List<TaskTransitionRule>
) {

  fun validateFor(task: Task, targetStatus: TaskStatus): Boolean =
    transitionRules
      .filter { it.targetStatus() == targetStatus }
      .all { it.isSatisfiedBy(task) }
}