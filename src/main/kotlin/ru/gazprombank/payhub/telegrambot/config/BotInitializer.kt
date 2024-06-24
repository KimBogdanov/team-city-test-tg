package ru.gazprombank.payhub.telegrambot.config

import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession
import ru.gazprombank.payhub.telegrambot.controller.TelegramBot

@Component
class BotInitializer(private val bot: TelegramBot) : CommandLineRunner {
    override fun run(vararg args: String?) {
        val telegramBotsApi = TelegramBotsApi(DefaultBotSession::class.java)
        try {
            telegramBotsApi.registerBot(bot)
        } catch (e: TelegramApiException) {
            e.printStackTrace()
        }
    }
}
