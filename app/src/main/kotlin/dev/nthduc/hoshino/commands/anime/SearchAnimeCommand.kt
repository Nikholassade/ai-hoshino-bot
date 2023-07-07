package dev.nthduc.hoshino.commands.anime

import dev.kord.common.Color
import dev.kord.core.entity.Message
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.rest.builder.message.create.embed
import dev.nthduc.hoshino.commands.Command
import dev.nthduc.hoshino.utils.client
import dev.nthduc.hoshino.utils.getOwnerInfo
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.datetime.Clock
import kotlinx.serialization.Serializable
@Serializable
data class TraceMoeResponse(
    val frameCount: Int,
    val error: String,
    val result: List<TraceMoeResult>
)

@Serializable
data class TraceMoeResult(
    val anilist: Anilist,
    val filename: String,
    val episode: Int?,
    val from: Double,
    val to: Double,
    val similarity: Double,
    val video: String,
    val image: String
)

@Serializable
data class Anilist(
    val id: Int,
    val idMal: Int?,
    val title: AnilistTitle,
    val synonyms: List<String>,
    val isAdult: Boolean
)
@Serializable
data class AnilistTitle(
    val native: String?,
    val romaji: String?,
    val english: String?
)
class SearchAnimeCommand() : Command {
    override suspend fun execute(event: MessageCreateEvent) {
        val message = event.message
        val (userOwner, avatarOwner) = getOwnerInfo(event)
        val imageUrl = message.getImageUrl()
        if (imageUrl != null) {
            val response: HttpResponse = client.get("https://api.trace.moe/search?anilistInfo") {
                parameter("url", imageUrl)
            }
            val traceMoeResponse = response.body<TraceMoeResponse>()
            if (traceMoeResponse.result.isNotEmpty()) {
                val result = traceMoeResponse.result[0]
                println(result.anilist)

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
                            value = result.anilist.id.toString()
                        }
                        field {
                            name = "Tên phim"
                            value = result.anilist.title.english ?: result.anilist.title.romaji.toString()
                        }
                        field {
                            name = "Tập"
                            value = result.episode?.toString() ?: "Unknown"
                        }
                        field {
                            name = "Từ (giây)"
                            value = result.from.toString()
                        }
                        field {
                            name = "Đến (giây)"
                            value = result.to.toString()
                        }
                        field {
                            name = "Độ tương đồng"
                            value = "%.2f%%".format(result.similarity * 100)
                        }
                        field {
                            name = "Video URL"
                            value = "[Nhấn vào đây để xem Video](${result.video})"
                        }
                        image = result.image
                        color = Color(49, 14, 76)
                        thumbnail {
                            url = result.image
                        }
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

    private fun Message.getImageUrl(): String? {
        return this.attachments.firstOrNull()?.url ?: this.embeds.firstOrNull()?.image?.url
    }

    override val description: String
        get() = "Tìm kiếm thông tin anime dựa trên hình ảnh"
}