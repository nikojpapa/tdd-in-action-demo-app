package com.nvoulgaris.taskmanager

import com.nvoulgaris.taskmanager.domain.task.Task
import com.nvoulgaris.taskmanager.domain.task.TaskStatus
import com.nvoulgaris.taskmanager.domain.task.TaskStatus.DONE

class TaskMustNotBeDoneRule : TaskTransitionRule {

  override fun isSatisfiedBy(task: Task): Boolean =
    task.status != DONE

  override fun targetStatus(): TaskStatus = DONE
}