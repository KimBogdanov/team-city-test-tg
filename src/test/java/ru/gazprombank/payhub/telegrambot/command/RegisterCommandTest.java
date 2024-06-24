package ru.gazprombank.payhub.telegrambot.command;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.gazprombank.payhub.telegrambot.client.UserClient;
import ru.gazprombank.payhub.telegrambot.dto.CreateUserRequestDto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RegisterCommandTest {
    private final UserClient userClient = mock(UserClient.class);
    private final RegisterCommand command = new RegisterCommand(userClient);
    private final AbsSender absSender = spy(AbsSender.class);
    private final User user = new User();
    private final Chat chat = new Chat();

    @Test
    @DisplayName("Проверяем передачу UserDto в клиент")
    void testSendUserDto() {
        prepareUserData(false, "Регистрация успешна!");

        command.execute(absSender, user, chat, new String[]{});

        ArgumentCaptor<CreateUserRequestDto> requestCaptor = ArgumentCaptor.forClass(CreateUserRequestDto.class);
        verify(userClient, times(1)).create(requestCaptor.capture());
        CreateUserRequestDto actualRequest = requestCaptor.getValue();
        assertEquals(user.getId(), actualRequest.getUserId());
        assertEquals(user.getUserName(), actualRequest.getUserName());
    }

    @Test
    @DisplayName("Проверяем возвращаемое сообщение")
    void testResponseMessage() throws Exception {
        String expectedResponse = "Регистрация успешна!";
        prepareUserData(false, expectedResponse);

        command.execute(absSender, user, chat, new String[]{});

        ArgumentCaptor<SendMessage> messageCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(absSender).execute(messageCaptor.capture());
        SendMessage actualMessage = messageCaptor.getValue();
        assertEquals(chat.getId().toString(), actualMessage.getChatId());
        assertEquals(expectedResponse, actualMessage.getText());
    }

    @Test
    @DisplayName("Регистрация бота ")
    void testBotRegistration() throws Exception {
        String expectedResponse = "Вы не можете зарегистрировать бота";
        prepareUserData(true, expectedResponse);

        command.execute(absSender, user, chat, new String[]{});

        ArgumentCaptor<SendMessage> messageCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(absSender).execute(messageCaptor.capture());
        SendMessage actualMessage = messageCaptor.getValue();
        assertEquals(chat.getId().toString(), actualMessage.getChatId());
        assertEquals(expectedResponse, actualMessage.getText());
    }

    private void prepareUserData(boolean isBot, String expectedResponse) {
        user.setId(12345L);
        user.setUserName("testUserName");
        user.setIsBot(isBot);
        chat.setId(54321L);
        when(userClient.create(any())).thenReturn(expectedResponse);
    }
}

