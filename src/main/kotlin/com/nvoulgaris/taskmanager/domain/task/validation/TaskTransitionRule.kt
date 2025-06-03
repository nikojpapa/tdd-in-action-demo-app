package com.nvoulgaris.taskmanager.domain.task.validation

import com.nvoulgaris.taskmanager.domain.task.Task
import com.nvoulgaris.taskmanager.domain.task.TaskStatus

interface TaskTransitionRule {

  fun isSatisfiedBy(task: Task): Boolean

  fun targetStatus(): TaskStatus
}
