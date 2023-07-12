package dev.nthduc.hoshino.commands.anime

import dev.kord.common.entity.Snowflake
import dev.kord.core.behavior.channel.createMessage
import dev.kord.core.entity.Message
import dev.kord.core.entity.User
import dev.kord.core.event.interaction.ChatInputCommandInteractionCreateEvent
import dev.kord.core.event.message.MessageCreateEvent
import dev.nthduc.hoshino.commands.Command
import dev.nthduc.hoshino.embeds.AnimeEmbed
import dev.nthduc.hoshino.embeds.AnimeEmbedSlashCommand
import dev.nthduc.hoshino.utils.NekosLifeResponse
import dev.nthduc.hoshino.utils.client
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

/**
 * Abstract class for anime action commands.
 *
 * @property actionType The type of action to perform, used to fetch an image from the nekos.life API.
 *
 * @property actionEmoji The emoji to display in the message.
 *
 * @property actionVerb The verb to display in the message.
 *
 * @author nthduc
 */
abstract class AnimeActionCommand(
    private val actionType: String,
    private val actionEmoji: String,
    private val actionVerb: String
) : Command {
    /**
     * Executes the anime action command.
     *
     * @param event The message create event that triggered the command.
     */
    override suspend fun execute(event: MessageCreateEvent) {
        val message = event.message
        val mentionedUser = message.getUserMention()?.asUser()?.mention ?: message.author?.asUser()?.mention

        if (mentionedUser != null) {
            val response: NekosLifeResponse = client.get("https://nekos.life/api/v2/img/$actionType").body()
            val imageUrl = response.url
            val animeEmbed = AnimeEmbed(imageUrl, event)

            message.channel.createMessage {
                content = "$actionEmoji ${message.author?.mention} đã $actionVerb $mentionedUser !"
                embeds.add(animeEmbed.build())
            }
        }
    }

    suspend fun execute(event: ChatInputCommandInteractionCreateEvent) {
        event.interaction.kord.launch {
            val user = event.interaction.user.asUser()
            val mentionedUserId = event.interaction.command.options["user"]?.value as? Snowflake
            val mentionedUser = mentionedUserId?.let { event.interaction.kord.getUser(it) }?.mention ?: user.mention
            val response: NekosLifeResponse = client.get("https://nekos.life/api/v2/img/$actionType").body()
            val imageUrl = response.url
            val animeEmbed = AnimeEmbedSlashCommand(imageUrl, event)

            event.interaction.kord.rest.channel.createMessage(event.interaction.channelId) {
                content = "$actionEmoji ${user.mention} đã $actionVerb $mentionedUser !"
                embeds.add(animeEmbed.build())
            }
        }
    }




    /**
     * Gets the first mentioned user in the message, if any.
     *
     * @return The first mentioned user, or null if no user was mentioned.
     */
    private suspend fun Message.getUserMention(): User? {
        return this.mentionedUsers.firstOrNull()
    }
}