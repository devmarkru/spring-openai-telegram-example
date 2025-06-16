package ru.devmark.openai.command

import org.springframework.ai.chat.memory.ChatMemory
import org.springframework.stereotype.Component
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender
import ru.devmark.openai.util.createMessage

@Component
class StartCommand(
    private val chatMemory: ChatMemory,
) : BotCommand("/start", "") {
    override fun execute(absSender: AbsSender, user: User, chat: Chat, arguments: Array<out String>) {
        chatMemory.clear(chat.id.toString())
        absSender.execute(createMessage(chat.id.toString(), "Начинаем новый диалог!"))
    }
}
