package com.nvoulgaris.taskmanager

import com.nvoulgaris.taskmanager.TaskStatus.DONE

class TaskMustNotBeDoneRule : TaskTransitionRule {

  override fun isSatisfiedBy(task: Task): Boolean =
    task.status != DONE

  override fun targetStatus(): TaskStatus = DONE
}