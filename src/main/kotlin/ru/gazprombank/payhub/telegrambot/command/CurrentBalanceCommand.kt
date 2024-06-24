package ru.gazprombank.payhub.telegrambot.command

import feign.RetryableException
import org.springframework.stereotype.Component
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender
import ru.gazprombank.payhub.telegrambot.client.AccountClient
import ru.gazprombank.payhub.telegrambot.util.createMessage

@Component
class CurrentBalanceCommand(
    private val accountClient: AccountClient
) : BotCommand("currentbalance", "") {
    override fun execute(absSender: AbsSender, user: User, chat: Chat, arguments: Array<out String>) {
        if (user.isBot) {
            absSender.execute(createMessage(chat.id.toString(), "Боты не поддерживаются"))
            return
        }

        val response: String = try {
            accountClient.find(user.id)
        } catch (e: RetryableException) {
            e.printStackTrace()
            "Произошла ошибка. Попробуйте позже."
        }

        absSender.execute(createMessage(chat.id.toString(), response))
    }
}