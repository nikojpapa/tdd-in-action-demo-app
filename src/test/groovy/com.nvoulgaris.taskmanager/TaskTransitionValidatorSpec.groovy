package com.nvoulgaris.taskmanager

import spock.lang.Specification
import static com.nvoulgaris.taskmanager.TaskStatus.*

class TaskTransitionValidatorSpec extends Specification {

    TaskTransitionValidator validator

    def setup() {
        validator = new TaskTransitionValidator([
            new TitleMustNotBeBlankRule(),
            new TitleMustBeAtLeastThreeCharactersLongRule(),
            new TaskMustBeAssignedRule(),
            new TaskMustNotBeBlockedRule(),
            new TaskMustNotBeDoneRule()
        ])
    }

    def "should not allow task transition to IN_PROGRESS when the task title is blank"() {
        given:
            Task task = new Task(
                UUID.randomUUID(),
                "",
                TODO,
                UUID.randomUUID(),
                false
            )

        when:
            boolean validTransaction = validator.validateFor(task, IN_PROGRESS)

        then:
            !validTransaction
    }

    def "should allow task transition to IN_PROGRESS when the task title is not blank"() {
        given:
            Task task = new Task(
                UUID.randomUUID(),
                "Task Title",
                TODO,
                UUID.randomUUID(),
                false
            )

        when:
            boolean validTransaction = validator.validateFor(task, IN_PROGRESS)

        then:
            validTransaction
    }

    def "should not allow task transition to IN_PROGRESS when the title is less than 3 characters long"() {
        given:
            Task task = new Task(
                UUID.randomUUID(),
                "ab",
                TODO,
                UUID.randomUUID(),
                false
            )

        when:
            boolean validTransaction = validator.validateFor(task, IN_PROGRESS)

        then:
            !validTransaction
    }

    def "should not allow task transition to DONE when the task is not assigned"() {
        given:
            Task task = new Task(
                UUID.randomUUID(),
                "Task Title",
                TODO,
                null,
                false
            )

        when:
            boolean validTransaction = validator.validateFor(task, DONE)

        then:
            !validTransaction
    }

    def "should allow task transition to DONE when the task is assigned"() {
        given:
            Task task = new Task(
                UUID.randomUUID(),
                "Task Title",
                TODO,
                UUID.randomUUID(),
                false
            )

        when:
            boolean validTransaction = validator.validateFor(task, DONE)

        then:
            validTransaction
    }

    def "should not allow task transition to DONE when the task is blocked"() {
        given:
            Task task = new Task(
                UUID.randomUUID(),
                "Task Title",
                TODO,
                UUID.randomUUID(),
                true
            )

        when:
            boolean validTransaction = validator.validateFor(task, DONE)

        then:
            !validTransaction
    }

    def "should not allow task transition to DONE when the task is already in DONE"() {
        given:
            Task task = new Task(
                UUID.randomUUID(),
                "Task Title",
                DONE,
                UUID.randomUUID(),
                false
            )

        when:
            boolean validTransaction = validator.validateFor(task, DONE)

        then:
            !validTransaction
    }

}
