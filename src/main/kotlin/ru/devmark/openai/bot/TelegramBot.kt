package ru.devmark.openai.bot

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Component
import org.telegram.telegrambots.extensions.bots.commandbot.CommandLongPollingTelegramBot
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.generics.TelegramClient
import ru.devmark.openai.config.TelegramProperties
import ru.devmark.openai.service.OpenAiService
import ru.devmark.openai.util.createMessage

private val logger = KotlinLogging.logger {}

@Component
class TelegramBot(
    private val openAiService: OpenAiService,
    private val telegramProperties: TelegramProperties,
    telegramClient: TelegramClient,
    commands: Set<BotCommand>,
) : CommandLongPollingTelegramBot(telegramClient, true, { telegramProperties.botName }) {

    init {
        registerAll(*commands.toTypedArray())
    }

    override fun processNonCommandUpdate(update: Update) {
        if (update.hasMessage()) {
            val chatId = update.message.chatId
            if (update.message.hasText()) {
                val userMessage = update.message.text
                logger.info { "Message from chatId=$chatId received: $userMessage." }
                val assistantMessage = openAiService.processUserMessage(chatId, userMessage)
                telegramClient.execute(createMessage(chatId.toString(), assistantMessage))
            } else {
                telegramClient.execute(createMessage(chatId.toString(), "Я понимаю только текст!"))
            }
        }
    }
}
