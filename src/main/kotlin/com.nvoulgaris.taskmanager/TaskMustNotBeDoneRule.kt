package com.nvoulgaris.taskmanager

class TaskMustNotBeDoneRule : TaskTransitionRule {
    override fun isSatisfiedBy(task: Task) = task.status != TaskStatus.DONE

    override fun targetStatus() = TaskStatus.DONE
}
