package org.motivepick.web

import com.github.springtestdbunit.annotation.DatabaseOperation.DELETE_ALL
import com.github.springtestdbunit.annotation.DatabaseSetup
import com.github.springtestdbunit.annotation.DatabaseTearDown
import com.github.springtestdbunit.annotation.DbUnitConfiguration
import jakarta.servlet.http.Cookie
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.motivepick.IntegrationTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@ExtendWith(SpringExtension::class)
@IntegrationTest(1234567890L)
@DatabaseSetup("/dbunit/tasks.xml")
@DatabaseTearDown("/dbunit/tasks.xml", type = DELETE_ALL)
@DbUnitConfiguration(databaseConnection = ["dbUnitDatabaseConnection"])
@AutoConfigureMockMvc
class TaskListControllerIntegrationTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `should create a custom task list if the user exists`() {
        val token = readTextFromResource("token.aae47dd3-32f1-415d-8bd8-4dc1086a6d10.txt")
        mockMvc
            .perform(post("/task-lists").cookie(Cookie("Authorization", token)))
            .andExpect(status().isOk())
            .andExpect(content().string(equalTo("{\"id\":1,\"type\":\"CUSTOM\"}")))
    }

    @Test
    fun `should respond with 404 if the user does not exist`() {
        val token = readTextFromResource("token.c03354bb-0c31-4010-90a1-65582f4c35cf.txt")
        mockMvc
            .perform(post("/task-lists").cookie(Cookie("Authorization", token)))
            .andExpect(status().isNotFound())
    }

    @Test
    fun `should read tasks by task list ID`() {
        val token = readTextFromResource("token.aae47dd3-32f1-415d-8bd8-4dc1086a6d10.txt")
        mockMvc
            .perform(get("/task-lists/4").param("offset", "0").param("limit", "1").cookie(Cookie("Authorization", token)))
            .andExpect(status().isOk())
            .andExpect(content().string(equalTo("{\"content\":[{\"id\":4,\"name\":\"Test task 3\",\"description\":\"\",\"dueDate\":null,\"closed\":false}],\"page\":{\"size\":1,\"number\":0,\"totalElements\":1,\"totalPages\":1}}")))
    }

    @Test
    fun `should read tasks by a predefined task list type`() {
        val token = readTextFromResource("token.aae47dd3-32f1-415d-8bd8-4dc1086a6d10.txt")
        mockMvc
            .perform(get("/task-lists/INBOX").param("offset", "0").param("limit", "1").cookie(Cookie("Authorization", token)))
            .andExpect(status().isOk())
            .andExpect(content().string(equalTo("{\"content\":[{\"id\":4,\"name\":\"Test task 3\",\"description\":\"\",\"dueDate\":null,\"closed\":false}],\"page\":{\"size\":1,\"number\":0,\"totalElements\":1,\"totalPages\":1}}")))
    }

    @Test
    fun `should return 400 if the task list type is not predefined`() {
        val token = readTextFromResource("token.aae47dd3-32f1-415d-8bd8-4dc1086a6d10.txt")
        mockMvc
            .perform(get("/task-lists/CUSTOM").param("offset", "0").param("limit", "1").cookie(Cookie("Authorization", token)))
            .andExpect(status().is4xxClientError())
    }

    @Test
    fun `should return 404 if task list does not exist`() {
        val token = readTextFromResource("token.aae47dd3-32f1-415d-8bd8-4dc1086a6d10.txt")
        mockMvc
            .perform(get("/task-lists/1000000").param("offset", "0").param("limit", "1").cookie(Cookie("Authorization", token)))
            .andExpect(status().isNotFound())
    }

    private fun readTextFromResource(path: String) = this::class.java.classLoader.getResource(path)?.readText() ?: error("Resource not found: $path")
}
