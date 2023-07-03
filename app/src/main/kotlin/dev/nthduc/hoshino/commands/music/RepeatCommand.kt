package dev.nthduc.hoshino.commands.music

import dev.kord.core.event.message.MessageCreateEvent
import dev.nthduc.hoshino.commands.Command
import dev.schlaubi.lavakord.LavaKord

class RepeatCommand(private val lavalink: LavaKord) : Command {
    var repeat = false

    override suspend fun execute(event: MessageCreateEvent) {
        val args = event.message.content.split(" ")
        if (args.size < 2) {
            event.message.channel.createMessage("Vui lòng nhập on hoặc off để bật hoặc tắt chức năng lặp lại")
            return
        }
        when (args[1]) {
            "on" -> {
                repeat = true
                event.message.channel.createMessage("Đã bật chức năng lặp lại")
            }
            "off" -> {
                repeat = false
                event.message.channel.createMessage("Đã tắt chức năng lặp lại")
            }
            else -> event.message.channel.createMessage("Vui lòng nhập on hoặc off để bật hoặc tắt chức năng lặp lại")
        }
    }

    override val description: String
        get() = "Lặp lại bài hát đang phát"
}
