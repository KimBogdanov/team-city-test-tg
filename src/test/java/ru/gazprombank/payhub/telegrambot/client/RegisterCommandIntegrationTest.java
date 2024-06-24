package ru.gazprombank.payhub.telegrambot.client;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.gazprombank.payhub.telegrambot.command.RegisterCommand;

import static org.mockito.Mockito.spy;

@SpringBootTest
@AutoConfigureWireMock(port = 9090)
class RegisterCommandIntegrationTest {
    @Autowired
    private RegisterCommand registerCommand;
    private final User user = new User();
    private final Chat chat = new Chat();
    private final AbsSender absSender = spy(AbsSender.class);

    @Test
    void testSuccessfulRegistration() {
        WireMock.stubFor(
                WireMock.post(WireMock.urlEqualTo("/api/v1/users"))
                        .willReturn(WireMock.aResponse().withStatus(200).withBody("Регистрация успешна!"))
        );
        prepareUserData(false, "Регистрация успешна!");

        registerCommand.execute(absSender, user, chat, new String[]{});

        WireMock.verify(
                WireMock.postRequestedFor(WireMock.urlEqualTo("/api/v1/users"))
                        .withRequestBody(WireMock.equalToJson("{\"userId\":12345,\"userName\":\"testUserName\"}"))
        );
    }

    private void prepareUserData(boolean isBot, String expectedResponse) {
        user.setId(12345L);
        user.setUserName("testUserName");
        user.setIsBot(isBot);
        chat.setId(54321L);
    }
}

