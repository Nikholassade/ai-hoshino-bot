package dev.nthduc.hoshino.commands.anime
import dev.inmo.saucenaoapi.SauceNaoAPI
import dev.inmo.saucenaoapi.models.urls
import dev.kord.common.Color
import dev.kord.core.entity.Message
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.rest.builder.message.create.embed
import dev.nthduc.hoshino.commands.Command
import dev.nthduc.hoshino.config.SAUCE_API
import dev.nthduc.hoshino.utils.getOwnerInfo
import kotlinx.datetime.Clock

class SearchAnimeSauceNaoCommand() : Command {
    override suspend fun execute(event: MessageCreateEvent) {
        val message = event.message
        val (userOwner, avatarOwner) = getOwnerInfo(event)
        val imageUrl = message.getImageUrl()
        if (imageUrl != null) {
            // Replace this with your own SauceNao API key
            val saucenao = SauceNaoAPI(apiToken = SAUCE_API)
            val response = saucenao.request(imageUrl)

            if (response.results.isNotEmpty()) {
                val item = response.results[0]

                event.kord.rest.channel.createMessage(event.message.channelId) {
                    content = "Tôi đã tìm thấy một kết quả phù hợp!"
                    embed {
                        title = "Thông tin Anime"
                        author {
                            name = "${event.message.author?.username}"
                            icon = event.message.author?.avatar?.cdnUrl?.toUrl()
                        }
                        field {
                            name = "ID"
                            value = item.data.anidbAId ?: "Không có"
                        }
                        field {
                            name = "Tên"
                            value = item.data.source ?: "Không có"
                        }
                        field {
                            name = "Tập"
                            value = item.data.part.toString()
                        }
                        field {
                            name = "Link"
                            value = "[Nhấn vào đây](${item.data.urls.firstOrNull() ?: "Không có"})"
                        }
                        field {
                            name = "Độ tương đồng"
                            value = "%.2f%%".format(item.header.similarity)
                        }
                        field {
                            name = "Năm"
                            value = item.data.year.toString()
                        }
                        field {
                            name = "Thời gian ước tính"
                            value = item.data.estTime.toString()
                        }
                        color = Color(49, 14, 76)
                        image = item.header.thumbnail
                        footer {
                            text = "Bot được phát triển bởi $userOwner"
                            icon = "$avatarOwner"
                        }
                        timestamp = Clock.System.now()
                    }
                }
            } else {
                message.channel.createMessage("Xin lỗi, tôi không thể tìm thấy kết quả phù hợp nào.")
            }
        } else {
            message.channel.createMessage("Vui lòng cung cấp một hình ảnh để tìm kiếm.")
        }
    }

    override val description: String
        get() = "Tìm kiếm thông tin anime dựa trên hình ảnh nâng cao"

    private fun Message.getImageUrl(): String? {
        return this.attachments.firstOrNull()?.url ?: this.embeds.firstOrNull()?.image?.url
    }
}


