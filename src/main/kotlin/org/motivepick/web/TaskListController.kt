package org.motivepick.web

import org.motivepick.domain.entity.TaskEntity
import org.motivepick.domain.entity.TaskListType
import org.motivepick.domain.ui.task.MoveTaskRequest
import org.motivepick.service.TaskListService
import org.motivepick.service.TaskService
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.*

@RestController
internal class TaskListController(private val taskService: TaskService, private val taskListService: TaskListService) {

    @GetMapping("/task-lists/{type}")
    fun read(@PathVariable("type") listType: TaskListType, offset: Int, limit: Int): ResponseEntity<Page<TaskEntity>> =
            ok(taskService.findForCurrentUser(listType, offset, limit))

    @PostMapping("/orders")
    fun moveTask(@RequestBody request: MoveTaskRequest): ResponseEntity<Void> {
        taskListService.moveTask(request.sourceListType!!, request.sourceIndex!!, request.destinationListType!!, request.destinationIndex!!)
        return ok().build()
    }
}
