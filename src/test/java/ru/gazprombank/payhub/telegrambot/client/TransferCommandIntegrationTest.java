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
import ru.gazprombank.payhub.telegrambot.command.TransferCommand;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@SpringBootTest
@AutoConfigureWireMock(port = 9090)
public class TransferCommandIntegrationTest {
    @Autowired
    private TransferCommand command;
    private final User user = new User();
    private final Chat chat = new Chat();
    private final AbsSender absSender = spy(AbsSender.class);

    @Test
    void testSuccessfulCreateAccount() throws TelegramApiException {
        final String responseMessage = "Successful";
        final String receiverName = "Акционный";
        final String amount = "100";
        final String userName = "testUserName";
        final Long userId = 12345L;
        WireMock.stubFor(
                WireMock.post(WireMock.urlEqualTo("/api/v1/transfers"))
                        .willReturn(WireMock.aResponse().withStatus(200).withBody(responseMessage))
        );
        prepareUserData(userId, userName, false);
        ArgumentCaptor<SendMessage> messageCaptor = ArgumentCaptor.forClass(SendMessage.class);

        command.execute(absSender, user, chat, new String[]{receiverName, amount});

        WireMock.verify(
                WireMock.postRequestedFor(WireMock.urlEqualTo("/api/v1/transfers"))
                        .withRequestBody(WireMock.equalToJson(String.format(
                                """
                                        {
                                          "from": "%s",
                                          "to": "%s",
                                          "amount": "%s"
                                        }
                                        """,
                                user.getUserName(),
                                receiverName,
                                amount)))
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
