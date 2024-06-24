package ru.gazprombank.payhub.telegrambot.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import ru.gazprombank.payhub.telegrambot.dto.CreateTransferRequestDto

@FeignClient(name = "transferClient", url = "\${middle-server.url}")
interface TransferClient {
    @PostMapping("/api/v1/transfers")
    fun create(
        @RequestBody createTransferRequestDto: CreateTransferRequestDto
    ): String
}