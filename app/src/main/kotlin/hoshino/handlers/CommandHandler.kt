package hoshino.handlers

import dev.kord.core.event.message.MessageCreateEvent
import hoshino.commands.Command

class CommandHandler {
     val commands = mutableMapOf<String, Command>()

    fun registerCommand(commandName: String, command: Command) {
        commands[commandName] = command
    }

    suspend fun handleCommand(event: MessageCreateEvent) {
        val message = event.message
        val content = message.content

        if (content.startsWith("!")) {
            val args = content.split(" ")
            val commandName = args[0].substring(1)

            val command = commands[commandName]
            command?.execute(event)
        }
    }
}
