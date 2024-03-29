package org.motivepick.service

import org.motivepick.domain.entity.TaskEntity
import org.motivepick.domain.entity.TaskListType
import org.motivepick.domain.entity.TaskListType.CLOSED
import org.motivepick.domain.entity.TaskListType.INBOX
import org.motivepick.domain.entity.UserEntity
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.LocalDateTime.now
import java.time.ZoneOffset.UTC

@Component
internal class InitialTasksFactory {

    fun createInitialTasks(tasksOwner: UserEntity, language: String): Map<TaskListType, List<TaskEntity>> {
        val now = now(UTC)
        val yesterday = now.minusDays(1)
        val tomorrow = now.plusDays(1)
        val dayAfterTomorrow = now.plusDays(2)
        val inTwoMonths = now.plusMonths(2)
        return if ("ru".equals(language, true)) {
            tasksInRussian(tasksOwner, tomorrow, dayAfterTomorrow, yesterday, inTwoMonths)
        } else {
            defaultTasks(tasksOwner, tomorrow, dayAfterTomorrow, yesterday, inTwoMonths)
        }
    }

    private fun tasksInRussian(tasksOwner: UserEntity, tomorrow: LocalDateTime, dayAfterTomorrow: LocalDateTime, yesterday: LocalDateTime, inTwoMonths: LocalDateTime): Map<TaskListType, List<TaskEntity>> {
        return mapOf(INBOX to listOf(task(tasksOwner, "Купить подарок Лене на день рождения", tomorrow, false),
                task(tasksOwner, "Закончить курс о микросервисах", false),
                task(tasksOwner, "Дописать статью для блога", dayAfterTomorrow, false),
                task(tasksOwner, "Убраться на кухне", yesterday, false),
                task(tasksOwner, "Перевести деньги Евгению за новую иллюстрацию", "840 ₽ за каждую иллюстрацию. Всего 2520 ₽.", inTwoMonths)),
                CLOSED to listOf(task(tasksOwner, "Найти отель в Софии", true),
                        task(tasksOwner, "Написать отзыв об учителе по эстонскому", yesterday, true)))
    }

    private fun defaultTasks(tasksOwner: UserEntity, tomorrow: LocalDateTime, dayAfterTomorrow: LocalDateTime, yesterday: LocalDateTime, inTwoMonths: LocalDateTime): Map<TaskListType, List<TaskEntity>> {
        return mapOf(INBOX to listOf(task(tasksOwner, "Buy a birthday present for Steve", tomorrow, false),
                task(tasksOwner, "Finish the course about microservices", false),
                task(tasksOwner, "Finalize the blog post", dayAfterTomorrow, false),
                task(tasksOwner, "Tidy up the kitchen", yesterday, false),
                task(tasksOwner, "Transfer money for the new illustration to Ann", "12$ for each illustration should be transferred. 36$ in total.", inTwoMonths)),
                CLOSED to listOf(task(tasksOwner, "Find a hotel in Sofia", true),
                        task(tasksOwner, "Write a review for the Estonian teacher", yesterday, true)))
    }

    private fun task(tasksOwner: UserEntity, name: String, closed: Boolean): TaskEntity {
        val task = TaskEntity(tasksOwner, name)
        task.closed = closed
        return task
    }

    private fun task(tasksOwner: UserEntity, name: String, dueDate: LocalDateTime, closed: Boolean): TaskEntity {
        val task = task(tasksOwner, name, closed)
        task.dueDate = dueDate
        return task
    }

    private fun task(tasksOwner: UserEntity, name: String, description: String, dueDate: LocalDateTime): TaskEntity {
        val task = task(tasksOwner, name, dueDate, false)
        task.description = description
        return task
    }
}
