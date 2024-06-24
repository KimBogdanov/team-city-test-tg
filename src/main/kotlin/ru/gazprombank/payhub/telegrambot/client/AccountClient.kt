package ru.gazprombank.payhub.telegrambot.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import ru.gazprombank.payhub.telegrambot.dto.CreateAccountRequestDto

@FeignClient(name = "accountClient", url = "\${middle-server.url}")
interface AccountClient {
    @PostMapping("/api/v1/users/{id}/accounts")
    fun create(
        @PathVariable("id") userId: Long,
        @RequestBody createAccountRequestDto: CreateAccountRequestDto
    ): String

    @GetMapping("/api/v1/users/{id}/accounts")
    fun find(
        @PathVariable("id") userId: Long
    ): String
}
