package dev.nthduc.hoshino.commands

import dev.kord.core.event.message.MessageCreateEvent

interface Command {
    suspend fun execute(event: MessageCreateEvent)

    val description: String
}
