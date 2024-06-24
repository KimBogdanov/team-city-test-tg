package ru.gazprombank.payhub.telegrambot.command

import feign.RetryableException
import org.springframework.stereotype.Component
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender
import ru.gazprombank.payhub.telegrambot.client.AccountClient
import ru.gazprombank.payhub.telegrambot.dto.CreateAccountRequestDto
import ru.gazprombank.payhub.telegrambot.util.createMessage

@Component
class CreateAccountCommand(
    private val accountClient: AccountClient
) : BotCommand("createaccount", "") {
    override fun execute(absSender: AbsSender, user: User, chat: Chat, arguments: Array<out String>) {
        if (user.isBot) {
            absSender.execute(createMessage(chat.id.toString(), "Вы не можете зарегистрировать бота"))
            return
        }
        
        val response: String = try {
            accountClient.create(user.id, CreateAccountRequestDto(arguments.joinToString(" ")))
        } catch (e: RetryableException) {
            e.printStackTrace()
            "Произошла ошибка. Попробуйте позже."
        }

        absSender.execute(createMessage(chat.id.toString(), response))
    }
}