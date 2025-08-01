package ru.devmark.openai.bot

import mu.KLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.telegram.telegrambots.extensions.bots.commandbot.CommandLongPollingTelegramBot
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.generics.TelegramClient
import ru.devmark.openai.service.OpenAiService
import ru.devmark.openai.util.createMessage

@Component
class TelegramBot(
    private val openAiService: OpenAiService,
    @Value("\${telegram.token}")
    private val tgBotToken: String,
    telegramClient: TelegramClient,
    commands: Set<BotCommand>,
) : SpringLongPollingBot, CommandLongPollingTelegramBot(telegramClient, true, { tgBotToken }) {

    init {
        registerAll(*commands.toTypedArray())
    }

    override fun getBotToken(): String = tgBotToken

    override fun getUpdatesConsumer(): LongPollingUpdateConsumer = this

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

    private companion object : KLogging()
}
