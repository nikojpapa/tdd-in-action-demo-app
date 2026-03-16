package com.nvoulgaris.taskmanager

class TaskMustNotBeBlockedRule : TaskTransitionRule {
    override fun isSatisfiedBy(task: Task) = !task.blocked

    override fun targetStatus() = TaskStatus.DONE
}
