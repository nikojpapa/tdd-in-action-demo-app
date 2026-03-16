package com.nvoulgaris.taskmanager

interface TaskTransitionRule {
    fun isSatisfiedBy(task: Task): Boolean
    
    fun targetStatus(): TaskStatus
}
