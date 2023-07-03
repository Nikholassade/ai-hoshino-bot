package dev.nthduc.hoshino.commands.anime
import dev.kord.core.behavior.channel.createMessage
import dev.kord.core.entity.Message
import dev.kord.core.entity.User
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.x.emoji.Emojis
import dev.nthduc.hoshino.commands.Command
import dev.nthduc.hoshino.embeds.AnimeEmbed
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.flow.firstOrNull
class FeedCommand : Command {
    override suspend fun execute(event: MessageCreateEvent) {
        val message = event.message
        val mentionedUser = message.getUserMention()?.asUser()?.mention ?: message.author?.asUser()?.mention
        if (mentionedUser != null) {
            val response: NekosLifeResponse = client.get("https://nekos.life/api/v2/img/feed").body()
            val imageUrl = response.url
            val animeEmbed = AnimeEmbed(imageUrl, event)

            message.channel.createMessage {
                content = "MĂMMMMMM ${Emojis.hamburger} ${message.author?.mention} đã cho $mentionedUser ăn !"
                embeds.add(animeEmbed.build())
            }
        }
    }

    private suspend fun Message.getUserMention(): User? {
        return this.mentionedUsers.firstOrNull()
    }

    override val description: String
        get() = "Cho một ai đó ăn"
}