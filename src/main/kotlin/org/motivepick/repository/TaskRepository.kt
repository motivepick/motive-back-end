package org.motivepick.repository

import org.motivepick.domain.document.Task
import org.springframework.data.mongodb.repository.MongoRepository

interface TaskRepository : MongoRepository<Task, String>
