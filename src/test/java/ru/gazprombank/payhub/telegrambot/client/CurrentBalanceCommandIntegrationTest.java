package ru.gazprombank.payhub.telegrambot.client;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.gazprombank.payhub.telegrambot.command.CurrentBalanceCommand;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@SpringBootTest
@AutoConfigureWireMock(port = 7070)
public class CurrentBalanceCommandIntegrationTest {
    @Autowired
    private CurrentBalanceCommand command;
    private final User user = new User();
    private final Chat chat = new Chat();
    private final AbsSender absSender = spy(AbsSender.class);

    @Test
    void testSuccessfulFindBalance() throws TelegramApiException {
        final String responseMessage = "Successful";
        final String accountName = "Акционный";
        final String userName = "testUserName";
        final Long userId = 12345L;
        WireMock.stubFor(
                WireMock.get(WireMock.urlEqualTo(String.format("/api/v1/users/%d/accounts", userId)))
                        .willReturn(WireMock.aResponse().withStatus(200).withBody(responseMessage))
        );
        prepareUserData(userId, userName, false);
        ArgumentCaptor<SendMessage> messageCaptor = ArgumentCaptor.forClass(SendMessage.class);

        command.execute(absSender, user, chat, new String[]{accountName});

        WireMock.verify(
                WireMock.getRequestedFor(WireMock.urlEqualTo(String.format("/api/v1/users/%d/accounts", userId)))
        );

        verify(absSender).execute(messageCaptor.capture());
        SendMessage capturedMessage = messageCaptor.getValue();
        assertEquals(responseMessage, capturedMessage.getText());
    }

    private void prepareUserData(Long userId, String userName, boolean isBot) {
        user.setId(userId);
        user.setUserName(userName);
        user.setIsBot(isBot);
        chat.setId(54321L);
    }

}
