package ru.devmark.openai.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication
import org.telegram.telegrambots.meta.generics.TelegramClient
import ru.devmark.openai.bot.TelegramBot

@Configuration
class BotConfig {
    @Bean
    fun telegramClient(telegramProperties: TelegramProperties): TelegramClient {
        return OkHttpTelegramClient(telegramProperties.token)
    }

    @Bean
    fun telegramBotsLongPollingApplication(
        telegramProperties: TelegramProperties,
        bot: TelegramBot,
    ): TelegramBotsLongPollingApplication =
        TelegramBotsLongPollingApplication().apply {
            registerBot(telegramProperties.token, bot)
        }
}
