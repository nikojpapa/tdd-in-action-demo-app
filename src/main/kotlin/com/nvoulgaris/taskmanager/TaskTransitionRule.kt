package com.nvoulgaris.taskmanager

import com.nvoulgaris.taskmanager.domain.task.Task
import com.nvoulgaris.taskmanager.domain.task.TaskStatus

interface TaskTransitionRule {

  fun isSatisfiedBy(task: Task): Boolean

  fun targetStatus(): TaskStatus
}
