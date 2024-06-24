package ru.gazprombank.payhub.telegrambot.command;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;

import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.Mockito.*;

public class StartCommandTest {
    private final User user = new User();
    private final Chat chat = new Chat();
    private final StartCommand startCommand = new StartCommand();
    private final AbsSender absSender = Mockito.spy(AbsSender.class);

    @Test
    void testResponseMessage() throws TelegramApiException {
        chat.setId(54321L);
        String expectedResponse = "Добро пожаловать!";

        startCommand.execute(absSender, user, chat, new String[]{});

        ArgumentCaptor<SendMessage> messageCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(absSender).execute(messageCaptor.capture());
        SendMessage actualMessage = messageCaptor.getValue();
        assertEquals(expectedResponse, actualMessage.getText());
    }
}
