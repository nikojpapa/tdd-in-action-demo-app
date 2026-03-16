package com.nvoulgaris.taskmanager

class TaskMustBeAssignedRule : TaskTransitionRule {
    override fun isSatisfiedBy(task: Task) = task.assigneeId != null

    override fun targetStatus() = TaskStatus.DONE
}
