package com.nvoulgaris.taskmanager

import com.nvoulgaris.taskmanager.TaskStatus.DONE

class TaskMustNotBeBlockedRule : TaskTransitionRule {

  override fun isSatisfiedBy(task: Task): Boolean =
    task.blocked.not()

  override fun targetStatus(): TaskStatus = DONE
}