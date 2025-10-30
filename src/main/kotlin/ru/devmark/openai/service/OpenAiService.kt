package ru.devmark.openai.service

import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.memory.ChatMemory
import org.springframework.ai.chat.messages.SystemMessage
import org.springframework.ai.chat.prompt.Prompt
import org.springframework.ai.openai.OpenAiChatOptions
import org.springframework.ai.openai.api.OpenAiApi
import org.springframework.ai.openai.api.ResponseFormat
import org.springframework.stereotype.Service

@Service
class OpenAiService(private val chatClient: ChatClient) {
    fun processUserMessage(chatId: Long, userMessage: String): String {
        val responseFormat = ResponseFormat.builder()
            .type(ResponseFormat.Type.TEXT)
            .build()

        val chatOptions = OpenAiChatOptions.builder()
            .model(OpenAiApi.ChatModel.GPT_5)
            .temperature(1.0) // для GPT 5 температура всегда 1.0
            .responseFormat(responseFormat)
            .build()

        return chatClient.prompt(Prompt(SystemMessage(SYSTEM_PROMPT), chatOptions))
            .advisors { advisor -> advisor.param(ChatMemory.CONVERSATION_ID, chatId.toString()) }
            .user(userMessage)
            .call()
            .content()
            ?: "Не удалось получить ответ"
    }

    private companion object {
        val SYSTEM_PROMPT = """
            Ты — умный помощник. Отвечай лаконично и по существу.
            Без лишних слов, деталей и рассуждений. Только суть ответа.
        """.trimIndent()
    }
}
