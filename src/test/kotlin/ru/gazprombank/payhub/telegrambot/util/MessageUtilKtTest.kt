package ru.gazprombank.payhub.telegrambot.util

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.telegram.telegrambots.meta.api.methods.ParseMode
import org.junit.jupiter.api.DisplayName
import ru.gazprombank.payhub.telegrambot.util.*

class MessageUtilKtTest {
    @Test
    fun `testCreateMessage should create SendMessage with correct parameters`(){
        val chatId = "12345"
        val text = "Test message"

        val message = createMessage(chatId, text)

        assertNotNull(message)
        assertEquals(chatId, message.chatId)
        assertEquals(text, message.text)
        assertEquals(ParseMode.MARKDOWN, message.parseMode)
        assertTrue(message.disableWebPagePreview)
    }
}

