package com.nvoulgaris.taskmanager

class TitleMustBeAtLeastThreeCharactersLongRule : TaskTransitionRule {
    override fun isSatisfiedBy(task: Task) = task.title.length > 2

    override fun targetStatus() = TaskStatus.IN_PROGRESS
}
