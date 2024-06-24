package ru.gazprombank.payhub.telegrambot.command

import feign.RetryableException
import org.springframework.stereotype.Component
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender
import ru.gazprombank.payhub.telegrambot.client.TransferClient
import ru.gazprombank.payhub.telegrambot.dto.CreateTransferRequestDto
import ru.gazprombank.payhub.telegrambot.util.createMessage

@Component
class TransferCommand(
    private val transferClient: TransferClient
) : BotCommand("transfer", "") {
    override fun execute(absSender: AbsSender, user: User, chat: Chat, arguments: Array<out String>) {
        if (user.isBot) {
            absSender.execute(createMessage(chat.id.toString(), "Боты не поддерживаются"))
            return
        }
        if (arguments.size != 2) {
            absSender.execute(
                createMessage(
                    chat.id.toString(),
                    "Команда должна быть вида " +
                            "```Help" +
                            " /transfer [Имя получателя в телеграмме] [Сумма перевода]\n" +
                            "   Пример:\n" +
                            " /transfer Popov 100```"
                )
            )
            return
        }
        val response: String = try {
            transferClient.create(buildCreateTransferRequestDto(user, arguments))
        } catch (e: RetryableException) {
            e.printStackTrace()
            "Произошла ошибка. Попробуйте позже."
        }

        absSender.execute(createMessage(chat.id.toString(), response))
    }

    private fun buildCreateTransferRequestDto(user: User, arguments: Array<out String>): CreateTransferRequestDto {
        return CreateTransferRequestDto(user.userName, arguments[0], arguments[1])
    }
}