package dev.nthduc.hoshino.extensions

import com.kotlindiscord.kord.extensions.components.components
import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.chatCommand
import com.kotlindiscord.kord.extensions.extensions.publicSlashCommand
import com.kotlindiscord.kord.extensions.types.respond
import com.kotlindiscord.kord.extensions.utils.respond
import dev.kord.common.Color
import dev.kord.rest.builder.message.create.actionRow
import dev.kord.rest.builder.message.create.embed
import dev.nthduc.hoshino.utils.getOwnerInfo
import io.ktor.client.request.forms.*
import io.ktor.utils.io.jvm.javaio.*
import kotlinx.datetime.Clock

class AboutExtension : Extension() {
    override val name = "about"

    override suspend fun setup() {
        publicSlashCommand {
            name = "about"
            description = "Hiển thị thông tin về bot"

            action {
                val (userOwner, avatarOwner) = getOwnerInfo(event)

                respond {
                    content = "Thông tin về AI Hoshino"
                    embed {
                        title = "AI Hoshino"
                        color = Color(49, 14, 76)
                        description = """
                            Ai Hoshino là một nhân vật chính trong bộ truyện tranh Oshi no Ko. Cô là người mẫu ảnh của loạt phim và từng là thần tượng hàng đầu của [B-Komachi](https://oshinoko.fandom.com/wiki/B-Komachi). 
                            
                            Bot sử dụng [Java](https://www.java.com/en/) và [Kotlin](https://kotlinlang.org/) để tương tác với Discord API.
                        """.trimIndent()
                        thumbnail {
                            url = "attachment://ai-hoshino.gif"
                        }
                        url = "https://www.youtube.com/watch?v=pk3qOPMXzDk"
                        footer {
                            text = "Bot được phát triển bởi $userOwner"
                            icon = "$avatarOwner"
                        }
                        timestamp = Clock.System.now()
                    }
                    components {
                        actionRow {
                            linkButton("https://discord.com/api/oauth2/authorize?client_id=1110045503548313610&permissions=31335812955735&scope=bot%20applications.commands") {
                                label = "Mời bot vào máy chủ của bạn"
                            }
                        }
                    }
                    val inputStream = this::class.java.getResourceAsStream("/images/ai-hoshino.gif")
                    if (inputStream != null) {
                        val byteReadChannel = inputStream.toByteReadChannel()
                        val channelProvider = ChannelProvider(size = inputStream.available().toLong()) { byteReadChannel }
                        addFile("ai-hoshino.gif", channelProvider)
                    } else {
                        respond {
                            content = "Lệnh about đang gặp vấn đề , hãy chờ fix nhé ^_^"
                        }
                    }
                }
            }
        }
        chatCommand {
            name = "about"
            description = "Hiển thị thông tin về bot"
            aliasKey = "abt"

            action {
                val (userOwner, avatarOwner) = getOwnerInfo(event)

                message.respond {
                    content = "Thông tin về AI Hoshino"
                    embed {
                        title = "AI Hoshino"
                        color = Color(49, 14, 76)
                        description = """
                            Ai Hoshino là một nhân vật chính trong bộ truyện tranh Oshi no Ko. Cô là người mẫu ảnh của loạt phim và từng là thần tượng hàng đầu của [B-Komachi](https://oshinoko.fandom.com/wiki/B-Komachi). 
                            
                            Bot sử dụng [Java](https://www.java.com/en/) và [Kotlin](https://kotlinlang.org/) để tương tác với Discord API.
                        """.trimIndent()
                        thumbnail {
                            url = "attachment://ai-hoshino.gif"
                        }
                        url = "https://www.youtube.com/watch?v=pk3qOPMXzDk"
                        footer {
                            text = "Bot được phát triển bởi $userOwner"
                            icon = "$avatarOwner"
                        }
                        timestamp = Clock.System.now()
                    }
                    components {
                        actionRow {
                            linkButton("https://discord.com/api/oauth2/authorize?client_id=1110045503548313610&permissions=31335812955735&scope=bot%20applications.commands") {
                                label = "Mời bot vào máy chủ của bạn"
                            }
                        }
                    }
                    val inputStream = this::class.java.getResourceAsStream("/images/ai-hoshino.gif")
                    if (inputStream != null) {
                        val byteReadChannel = inputStream.toByteReadChannel()
                        val channelProvider = ChannelProvider(size = inputStream.available().toLong()) { byteReadChannel }
                        addFile("ai-hoshino.gif", channelProvider)
                    } else {
                        message.respond {
                            content = "Lệnh about đang gặp vấn đề , hãy chờ fix nhé ^_^"
                        }
                    }

                }
            }
        }
    }
}
