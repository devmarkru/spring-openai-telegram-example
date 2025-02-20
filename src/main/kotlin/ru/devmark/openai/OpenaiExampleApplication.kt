package ru.devmark.openai

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching

@EnableCaching
@SpringBootApplication
class OpenaiExampleApplication

fun main(args: Array<String>) {
    runApplication<OpenaiExampleApplication>(*args)
}
