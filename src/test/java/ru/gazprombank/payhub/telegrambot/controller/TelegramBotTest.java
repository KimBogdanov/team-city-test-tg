package ru.gazprombank.payhub.telegrambot.controller;

import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TelegramBotTest {

    private SendMessage createSendMessage(long chatId, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        sendMessage.disableWebPagePreview();
        sendMessage.enableMarkdown(true);
        return sendMessage;
    }

    @Test
    void testConstructorAndGetBotUsername() {
        Set<BotCommand> commands = new HashSet<>();
        String testToken = "test_token";
        String testBotName = "test_bot_name";

        TelegramBot telegramBot = new TelegramBot(commands, testToken, testBotName);

        assertEquals(testBotName, telegramBot.getBotUsername());
    }

    @Test
    void testProcessNonCommandUpdateWithText() throws Exception {
        TelegramBot telegramBot = mock(TelegramBot.class);
        doCallRealMethod().when(telegramBot).processNonCommandUpdate(any(Update.class));
        when(telegramBot.execute(any(SendMessage.class))).thenReturn(mock(Message.class));

        long chatId = 12345L;
        String expectedText = "Текст пока не обрабатывается";
        SendMessage expectedSendMessage = createSendMessage(chatId, expectedText);

        Update update = new Update();
        Message message = mock(Message.class);
        when(message.getChatId()).thenReturn(chatId);
        when(message.hasText()).thenReturn(true);
        update.setMessage(message);

        telegramBot.processNonCommandUpdate(update);

        verify(telegramBot).execute(eq(expectedSendMessage));
    }

    @Test
    void testProcessNonCommandUpdateNullMessage() throws Exception {
        TelegramBot telegramBot = mock(TelegramBot.class);
        doCallRealMethod().when(telegramBot).processNonCommandUpdate(any(Update.class));
        Update update = new Update();

        telegramBot.processNonCommandUpdate(update);

        verify(telegramBot, never()).execute(any(SendMessage.class));
    }

    @Test
    void testProcessNonCommandUpdateNoText() throws Exception {
        TelegramBot telegramBot = mock(TelegramBot.class);
        doCallRealMethod().when(telegramBot).processNonCommandUpdate(any(Update.class));
        when(telegramBot.execute(any(SendMessage.class))).thenReturn(mock(Message.class));

        long chatId = 12345L;
        String expectedText = "Я понимаю только команды!";
        SendMessage expectedSendMessage = createSendMessage(chatId, expectedText);

        Update update = new Update();
        Message message = mock(Message.class);
        when(message.getChatId()).thenReturn(chatId);
        when(message.hasText()).thenReturn(false);
        update.setMessage(message);

        telegramBot.processNonCommandUpdate(update);

        verify(telegramBot).execute(eq(expectedSendMessage));
    }
}
