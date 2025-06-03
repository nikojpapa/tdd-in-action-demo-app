package com.nvoulgaris.taskmanager

import com.nvoulgaris.taskmanager.domain.task.Task
import com.nvoulgaris.taskmanager.domain.task.TaskStatus
import com.nvoulgaris.taskmanager.domain.task.TaskStatus.DONE

class TaskMustNotBeBlockedRule : TaskTransitionRule {

  override fun isSatisfiedBy(task: Task): Boolean =
    task.blocked.not()

  override fun targetStatus(): TaskStatus = DONE
}