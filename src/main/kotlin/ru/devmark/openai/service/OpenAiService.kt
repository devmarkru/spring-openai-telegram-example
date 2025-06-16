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

    fun sendMessages(chatId: Long, userMessage: String): String {
        val responseFormat = ResponseFormat.builder()
            .type(ResponseFormat.Type.TEXT)
            .build()

        val chatOptions = OpenAiChatOptions.builder()
            .model(OpenAiApi.ChatModel.GPT_4_1)
            .temperature(0.0)
            .responseFormat(responseFormat)
            .build()

        val responseText = chatClient.prompt(Prompt(SystemMessage(SYSTEM_PROMPT), chatOptions))
            .advisors { advisor -> advisor.param(ChatMemory.CONVERSATION_ID, chatId) }
            .user(userMessage)
            .call()
            .content()
            ?: "Не удалось получить ответ"
        return responseText
    }

    private companion object {
        val SYSTEM_PROMPT = """
            Ты - умный помощник. Всегда отвечай на вопросы максимально коротко, без лишних слов и без ненужных деталей.
        """.trimIndent()
    }
}
