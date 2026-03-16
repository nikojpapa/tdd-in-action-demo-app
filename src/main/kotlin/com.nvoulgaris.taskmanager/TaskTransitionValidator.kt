package com.nvoulgaris.taskmanager

class TaskTransitionValidator(
    private val transitionRules: List<TaskTransitionRule>
) {
    fun validateFor(task: Task, targetStatus: TaskStatus): Boolean {
        return transitionRules
            .filter { it.targetStatus() == targetStatus }
            .all { it.isSatisfiedBy(task) }
    }
}
