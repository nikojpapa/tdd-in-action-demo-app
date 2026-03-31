package com.nvoulgaris.taskmanager.api

import com.nvoulgaris.taskmanager.domain.task.InvalidTaskStatusTransitionException
import com.nvoulgaris.taskmanager.domain.task.Task
import com.nvoulgaris.taskmanager.domain.task.TaskNotFoundException
import com.nvoulgaris.taskmanager.domain.task.TaskService
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.net.URI
import java.util.UUID

@RestController
@RequestMapping("/api/tasks")
class TaskApi(
  private val taskService: TaskService
) {

  companion object {
    private val logger = LoggerFactory.getLogger(TaskApi::class.java)
  }

  @PostMapping(
    consumes = [APPLICATION_JSON_VALUE],
    produces = [APPLICATION_JSON_VALUE],
  )
  fun create(@RequestBody request: CreateTaskRequestDto): ResponseEntity<Task> {
    try {
      val task = taskService.create(request.title, request.status, request.assigneeId, request.blocked)
      return createdTaskResponse(task)
    } catch (e: Exception) {
      logger.warn("Creating a new task failed", e)
      return ResponseEntity.badRequest().body(null)
    }
  }

  @PatchMapping("/{taskId}")
  fun updateTask(@PathVariable taskId: UUID, @RequestBody request: TaskStatusUpdateRequestDto): ResponseEntity<Task> {
    try {
      val updatedTask = taskService.updateStatus(taskId, request.status)
      return ResponseEntity.ok().body(updatedTask)
    } catch (e: Exception) {
      logger.warn("Updating task status failed", e)
      return when (e) {
        is TaskNotFoundException -> ResponseEntity.notFound().build()
        is InvalidTaskStatusTransitionException -> ResponseEntity.badRequest().body(null)
        else -> ResponseEntity.badRequest().body(null)
      }
    }
  }

  private fun createdTaskResponse(task: Task): ResponseEntity<Task> =
    ResponseEntity
      .created(URI.create("/tasks/${task.id}"))
      .body(task)

  @GetMapping
  fun getTasks(@RequestParam(required = false) assigneeId: UUID?): ResponseEntity<List<Task>> {
    throw UnsupportedOperationException("Not yet implemented")
  }
}