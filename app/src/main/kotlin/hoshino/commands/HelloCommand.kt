package hoshino.commands

import dev.kord.core.event.message.MessageCreateEvent

class HelloCommand : Command {
    override suspend fun execute(event: MessageCreateEvent) {
        val message = event.message
        message.channel.createMessage("Hello, ${message.author?.asUser()?.mention}!")
    }
}
