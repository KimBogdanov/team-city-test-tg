package ru.gazprombank.payhub.telegrambot.command;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.gazprombank.payhub.telegrambot.client.AccountClient;
import ru.gazprombank.payhub.telegrambot.dto.CreateAccountRequestDto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class CurrentBalanceCommandTest {
    private final AccountClient accountClient = mock(AccountClient.class);
    private final CurrentBalanceCommand command = new CurrentBalanceCommand(accountClient);
    private final AbsSender absSender = spy(AbsSender.class);
    private final User user = new User();
    private final Chat chat = new Chat();

    @Test
    void testBotFindBalance() throws TelegramApiException {
        String expectedMessage = "Боты не поддерживаются";
        prepareUserData(true);

        command.execute(absSender, user, chat, new String[]{});

        ArgumentCaptor<SendMessage> requestCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(absSender).execute(requestCaptor.capture());
        SendMessage actualMessage = requestCaptor.getValue();
        assertEquals(chat.getId().toString(), actualMessage.getChatId());
        assertEquals(expectedMessage, actualMessage.getText());

        verify(accountClient, never()).create(anyLong(), any(CreateAccountRequestDto.class));
    }

    private void prepareUserData(boolean isBot) {
        user.setId(12345L);
        user.setUserName("testUserName");
        user.setIsBot(isBot);
        chat.setId(54321L);
    }
}
