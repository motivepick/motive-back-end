package org.motivepick.web

import org.motivepick.domain.entity.TaskEntity
import org.motivepick.domain.ui.task.CreateTaskRequest
import org.motivepick.domain.ui.task.UpdateTaskRequest
import org.motivepick.repository.TaskRepository
import org.motivepick.service.TaskListService
import org.motivepick.service.TaskService
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.notFound
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.*

@RestController
internal class TaskController(private val taskRepo: TaskRepository, private val taskService: TaskService,
        private val taskListService: TaskListService) {

    @PostMapping("/tasks")
    fun create(@RequestBody request: CreateTaskRequest): ResponseEntity<TaskEntity> =
            ResponseEntity(taskService.createTask(request), CREATED)

    @GetMapping("/tasks/{id}")
    fun read(@PathVariable("id") taskId: Long): ResponseEntity<TaskEntity> =
            taskRepo.findByIdAndVisibleTrue(taskId)
                    .map { ok(it) }
                    .orElse(notFound().build())

    @PutMapping("/tasks/{id}")
    fun update(@PathVariable("id") taskId: Long, @RequestBody request: UpdateTaskRequest): ResponseEntity<TaskEntity> =
            taskRepo.findByIdAndVisibleTrue(taskId)
                    .map { task ->
                        request.name?.let { task.name = it.trim() }
                        request.description?.let { task.description = it.trim() }
                        request.created?.let { task.created = it }
                        request.dueDate?.let { task.dueDate = it }
                        request.closingDate?.let { task.closingDate = it }
                        request.closed?.let { task.closed = it }
                        if (request.deleteDueDate) {
                            task.dueDate = null
                        }
                        ok(taskRepo.save(task))
                    }
                    .orElse(notFound().build())

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
    fun delete(@PathVariable("id") taskId: Long): ResponseEntity<TaskEntity> =
            taskRepo.findByIdAndVisibleTrue(taskId)
                    .map { task ->
                        task.visible = false
                        ok(taskRepo.save(task))
                    }
                    .orElse(ResponseEntity(NOT_FOUND))
}
