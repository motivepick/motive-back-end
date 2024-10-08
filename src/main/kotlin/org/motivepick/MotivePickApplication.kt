package org.motivepick

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.web.config.EnableSpringDataWebSupport
import org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO
import org.springframework.web.client.RestTemplate
import java.time.Clock

@SpringBootApplication
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
class MotivePickApplication {

    @Bean
    fun restTemplate(): RestTemplate = RestTemplate()

    @Bean
    fun clock(): Clock = Clock.systemUTC()
}

fun main(args: Array<String>) {
    SpringApplication.run(MotivePickApplication::class.java, *args)
}
