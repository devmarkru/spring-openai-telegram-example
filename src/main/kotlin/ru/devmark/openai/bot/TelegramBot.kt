package ru.devmark.openai.bot

import mu.KLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand
import org.telegram.telegrambots.meta.api.objects.Update
import ru.devmark.openai.service.OpenAiService
import ru.devmark.openai.util.createMessage

@Component
class TelegramBot(
    private val openAiService: OpenAiService,
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
                val userMessage = update.message.text
                logger.info { "Message from chatId=$chatId received: $userMessage." }
                val assistantMessage = openAiService.sendMessages(chatId, userMessage)
                execute(createMessage(chatId.toString(), assistantMessage))
            } else {
                execute(createMessage(chatId.toString(), "Я понимаю только текст!"))
            }
        }
    }

    private companion object : KLogging()
}
