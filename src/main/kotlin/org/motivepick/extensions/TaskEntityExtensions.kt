package org.motivepick.extensions

import org.motivepick.domain.entity.TaskEntity
import org.motivepick.domain.view.TaskView

internal object TaskEntityExtensions {

    fun TaskEntity.view(): TaskView = TaskView(this.id, this.name, this.description ?: "", this.dueDate, this.closed)
}