package ru.gazprombank.payhub.telegrambot.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import ru.gazprombank.payhub.telegrambot.dto.CreateUserRequestDto

@FeignClient(name = "userClient", url = "\${middle-server.url}")
interface UserClient {
    @PostMapping("/api/v1/users")
    fun create(@RequestBody userRequestDto: CreateUserRequestDto): String
}
