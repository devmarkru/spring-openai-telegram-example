package ru.devmark.openai

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class OpenaiExampleApplication

fun main(args: Array<String>) {
    runApplication<OpenaiExampleApplication>(*args)
}
