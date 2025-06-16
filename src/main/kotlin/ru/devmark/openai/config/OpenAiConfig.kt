package ru.devmark.openai.config

import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor
import org.springframework.ai.chat.memory.ChatMemory
import org.springframework.ai.chat.memory.ChatMemoryRepository
import org.springframework.ai.chat.memory.MessageWindowChatMemory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenAiConfig {
    @Bean
    fun chatMemory(chatMemoryRepository: ChatMemoryRepository): ChatMemory =
        MessageWindowChatMemory.builder()
            .chatMemoryRepository(chatMemoryRepository) // InMemoryChatMemoryRepository
            .maxMessages(100) // сохраняем последние 100 сообщений
            .build()

    @Bean
    fun chatClient(builder: ChatClient.Builder, chatMemory: ChatMemory): ChatClient =
        builder
            .defaultAdvisors(
                // логирование запросов и ответов к LLM
                SimpleLoggerAdvisor(),

                // накапливаем всю историю переписки с LLM в system prompt
                PromptChatMemoryAdvisor
                    .builder(chatMemory)
                    .build(),
            )
            .build()
}
