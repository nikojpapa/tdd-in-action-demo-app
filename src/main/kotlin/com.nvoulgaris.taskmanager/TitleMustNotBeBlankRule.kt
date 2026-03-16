package com.nvoulgaris.taskmanager

class TitleMustNotBeBlankRule : TaskTransitionRule {
    override fun isSatisfiedBy(task: Task) = task.title.isNotBlank()

    override fun targetStatus() = TaskStatus.IN_PROGRESS
}
