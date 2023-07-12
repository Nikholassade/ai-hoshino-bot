package dev.nthduc.hoshino.commands

import dev.kord.common.Color
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.rest.builder.message.create.embed
import dev.nthduc.hoshino.utils.getOwnerInfo
import io.ktor.client.request.forms.*
import io.ktor.utils.io.jvm.javaio.*
import kotlinx.datetime.Clock

@Deprecated("AboutCommand Replace AboutExtension")
class AboutCommand() : Command {
    override val description: String
        get() = "Hiển thị thông tin về bot"

    override suspend fun execute(event: MessageCreateEvent) {
        val message = event.message
        val (userOwner, avatarOwner) = getOwnerInfo(event)
        val inputStream = this::class.java.getResourceAsStream("/images/ai-hoshino.gif")
        if (inputStream != null) {
            val byteReadChannel = inputStream.toByteReadChannel()
            val channelProvider = ChannelProvider(size = inputStream.available().toLong()) { byteReadChannel }
            event.kord.rest.channel.createMessage(event.message.channelId) {
                addFile("ai-hoshino.gif", channelProvider)
                embed {
                    title = "AI Hoshino"
                    color = Color(49, 14, 76)
                    url = "https://www.youtube.com/watch?v=pk3qOPMXzDk"
                    description = """
                    Ai Hoshino là một nhân vật chính trong bộ truyện tranh Oshi no Ko. Cô là người mẫu ảnh của loạt phim và từng là thần tượng hàng đầu của [B-Komachi](https://oshinoko.fandom.com/wiki/B-Komachi). 
                    
                    Bot sử dụng [Java](https://www.java.com/en/) và [Kotlin](https://kotlinlang.org/) để tương tác với Discord API.
                """.trimIndent()
                    thumbnail {
                        url = "attachment://ai-hoshino.gif"
                    }
                    footer {
                        text = "Bot được phát triển bởi $userOwner"
                        icon = "$avatarOwner"
                    }
                    timestamp = Clock.System.now()
                }
            }
        } else {
            message.channel.createMessage("Lệnh about đang gặp vấn đề , hãy chờ fix nhé ^_^")
        }
    }

}
