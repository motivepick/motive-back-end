package org.motivepick.web

import org.motivepick.domain.entity.TaskEntity
import org.motivepick.domain.ui.task.CreateTaskRequest
import org.motivepick.domain.ui.task.UpdateTaskRequest
import org.motivepick.service.TaskListService
import org.motivepick.service.TaskService
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.notFound
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.*

@RestController
internal class TaskController(private val taskService: TaskService, private val taskListService: TaskListService) {

    @PostMapping("/tasks")
    fun create(@RequestBody request: CreateTaskRequest): ResponseEntity<TaskEntity> =
        ResponseEntity(taskService.createTask(request), CREATED)

    @GetMapping("/tasks/{id}")
    fun read(@PathVariable("id") taskId: Long): ResponseEntity<TaskEntity> {
        val task = taskService.findTaskById(taskId)
        return if (task == null) notFound().build() else ok(task)
    }

    @PutMapping("/tasks/{id}")
    fun update(@PathVariable("id") taskId: Long, @RequestBody request: UpdateTaskRequest): ResponseEntity<TaskEntity> {
        val updatedTask = taskService.updateTaskById(taskId, request)
        return if (updatedTask == null) notFound().build() else ok(updatedTask)
    }

    @PutMapping("/tasks/{id}/closing")
    fun close(@PathVariable("id") taskId: Long): ResponseEntity<TaskEntity> =
        taskListService.closeTask(taskId)
            .map { ok(it) }
            .orElse(notFound().build())

    @PutMapping("/tasks/{id}/undo-closing")
    fun undoClose(@PathVariable("id") taskId: Long): ResponseEntity<TaskEntity> =
        taskListService.undoCloseTask(taskId)
            .map { ok(it) }
            .orElse(notFound().build())

    @DeleteMapping("/tasks/{id}")
    fun delete(@PathVariable("id") taskId: Long): ResponseEntity<TaskEntity> {
        val deletedTask = taskService.softDeleteTaskById(taskId)
        return if (deletedTask == null) notFound().build() else ok(deletedTask)
    }
}
