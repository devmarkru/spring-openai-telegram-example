package ru.devmark.openai.bot

import mu.KLogging
import org.springframework.ai.chat.messages.AssistantMessage
import org.springframework.ai.chat.messages.UserMessage
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand
import org.telegram.telegrambots.meta.api.objects.Update
import ru.devmark.openai.service.MessageCache
import ru.devmark.openai.service.OpenAiService
import ru.devmark.openai.util.createMessage

@Component
class TelegramBot(
    private val openAiService: OpenAiService,
    private val messageCache: MessageCache,
    commands: Set<BotCommand>,
    @Value("\${telegram.token}")
    token: String,
) : TelegramLongPollingCommandBot(token) {

    @Value("\${telegram.botName}")
    private val botName: String = ""

    init {
        registerAll(*commands.toTypedArray())
    }

    override fun getBotUsername(): String = botName

override fun processNonCommandUpdate(update: Update) {
    if (update.hasMessage()) {
        val chatId = update.message.chatId
        if (update.message.hasText()) {
            val messageText = update.message.text
            logger.info { "Message from chatId=$chatId received: $messageText." }
            val messages = messageCache.getOrInitMessages(chatId).toMutableList()
            messages += UserMessage(messageText)
            val assistantMessages = openAiService.sendMessages(messages)
            messages += assistantMessages.map { AssistantMessage(it) }
            messageCache.saveMessages(chatId, messages)
            assistantMessages.forEach { message ->
                execute(createMessage(chatId.toString(), message))
            }
        } else {
            execute(createMessage(chatId.toString(), "Я понимаю только текст!"))
        }
    }
}

    private companion object: KLogging()
}