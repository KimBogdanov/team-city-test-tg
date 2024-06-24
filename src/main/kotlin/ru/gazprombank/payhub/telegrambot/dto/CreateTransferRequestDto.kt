package ru.gazprombank.payhub.telegrambot.dto

data class CreateTransferRequestDto(val from: String, val to: String, val amount: String)
