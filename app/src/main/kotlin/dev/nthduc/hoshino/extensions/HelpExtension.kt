package dev.nthduc.hoshino.extensions

import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.chatCommand
import com.kotlindiscord.kord.extensions.utils.respond
import dev.kord.common.Color
import dev.kord.rest.builder.message.create.embed
import dev.nthduc.hoshino.config.DEFAULT_PREFIX

class HelpExtension : Extension() {
    override val name = "help"

    override suspend fun setup() {
        chatCommand {
            name = "help"
            description = "Hiển thị thông tin trợ giúp cho các lệnh của bot"

            action {
                val animeCommands = listOf("kiss", "hug", "feed","pat","slap","smug","tickle","timnguon","sauce")
                val musicCommands = listOf("play","stop","skip","pause","resume","join","leave")
                val infoCommands = listOf("aihoshino", "about", "avatar" ,"coinflip", "serverinfo","userinfo")

                message.respond {
                    content = "Danh sách các lệnh của bot:"
                    embed {
                        title = "Trợ giúp"
                        description = "Cách sử dụng: `$DEFAULT_PREFIX` + <lệnh>"
                        color = Color(49, 14, 76)

                        field {
                            name = "Anime"
                            value = animeCommands.joinToString(" ,") { "`$it`" }
                        }

                        field {
                            name = "Music"
                            value = musicCommands.joinToString(", ") { "`$it`" }
                        }

                        field {
                            name = "Information"
                            value = infoCommands.joinToString(", ") { "`$it`" }
                        }
                    }
                }
            }
        }
    }
}


