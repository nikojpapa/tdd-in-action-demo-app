package com.nvoulgaris.taskmanager

import com.nvoulgaris.taskmanager.domain.task.Task
import com.nvoulgaris.taskmanager.domain.task.validation.TaskMustBeAssignedRule
import com.nvoulgaris.taskmanager.domain.task.validation.TaskMustNotBeBlockedRule
import com.nvoulgaris.taskmanager.domain.task.validation.TaskMustNotBeDoneRule
import com.nvoulgaris.taskmanager.domain.task.validation.TaskTransitionValidator
import com.nvoulgaris.taskmanager.domain.task.validation.TitleMustBeAtLeastThreeCharactersLongRule
import com.nvoulgaris.taskmanager.domain.task.validation.TitleMustNotBeBlankRule
import spock.lang.Specification

import static com.nvoulgaris.taskmanager.domain.task.TaskStatus.DONE
import static com.nvoulgaris.taskmanager.domain.task.TaskStatus.IN_PROGRESS
import static com.nvoulgaris.taskmanager.domain.task.TaskStatus.TODO

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

  def "Should not allow task transition to IN_PROGRESS when the task title is blank"() {
    given:
      Task task = new Task(
        UUID.randomUUID(),
        "   ",
        TODO,
        UUID.randomUUID(),
        false
      )

    when:
      boolean validTransition = validator.validateFor(task, IN_PROGRESS)

    then:
      !validTransition
  }

  def "Should allow task transition to IN_PROGRESS when the task title is not blank"() {
    given:
      Task task = new Task(
        UUID.randomUUID(),
        "Clean apartment",
        TODO,
        UUID.randomUUID(),
        false
      )

    when:
      boolean validTransition = validator.validateFor(task, IN_PROGRESS)

    then:
      validTransition
  }

  def "Should not allow task transition to IN_PROGRESS when the title is less than 3 characters long"() {
    given:
      Task task = new Task(
        UUID.randomUUID(),
        "Cl",
        TODO,
        UUID.randomUUID(),
        false
      )

    when:
      boolean validTransition = validator.validateFor(task, IN_PROGRESS)

    then:
      !validTransition
  }

  def "Should not allow task transition to DONE when the task is not assigned"() {
    given:
      Task task = new Task(
        UUID.randomUUID(),
        "Clean apartment",
        TODO,
        null,
        false
      )

    when:
      boolean validTransition = validator.validateFor(task, DONE)

    then:
      !validTransition
  }

  def "Should allow task transition to DONE when the task is assigned"() {
    given:
      Task task = new Task(
        UUID.randomUUID(),
        "Clean apartment",
        TODO,
        UUID.randomUUID(),
        false
      )

    when:
      boolean validTransition = validator.validateFor(task, DONE)

    then:
      validTransition
  }

  def "Should not allow task transition to DONE when the task is blocked"() {
    given:
      Task task = new Task(
        UUID.randomUUID(),
        "Clean apartment",
        TODO,
        UUID.randomUUID(),
        true
      )

    when:
      boolean validTransition = validator.validateFor(task, DONE)

    then:
      !validTransition
  }

  def "Should not allow task transition to DONE when the task is already in DONE"() {
    given:
      Task task = new Task(
        UUID.randomUUID(),
        "Clean apartment",
        DONE,
        UUID.randomUUID(),
        false
      )

    when:
      boolean validTransition = validator.validateFor(task, DONE)

    then:
      !validTransition
  }
}
