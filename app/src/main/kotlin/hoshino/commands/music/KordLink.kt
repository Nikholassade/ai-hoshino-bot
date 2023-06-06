package hoshino.commands.music

import dev.kord.common.entity.Permission
import dev.kord.common.entity.Permissions
import dev.kord.common.entity.Snowflake
import dev.kord.core.entity.channel.TopGuildChannel
import dev.kord.core.entity.channel.VoiceChannel
import dev.kord.gateway.UpdateVoiceStatus
import dev.schlaubi.lavakord.audio.Link
import dev.schlaubi.lavakord.audio.Node
import dev.schlaubi.lavakord.audio.internal.AbstractLink
import kotlinx.coroutines.flow.count
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

class KordLink(
    guildId: ULong,
    node: Node,
    override val lavakord: KordLavaKord
) : AbstractLink(node, guildId) {

    override suspend fun connectAudio(voiceChannelId: ULong) {
        lastChannelId = voiceChannelId
        val channel = lavakord.kord.getChannel(Snowflake(voiceChannelId)) as TopGuildChannel?
        checkChannel(channel)
        val guild = channel.getGuild()

        state = Link.State.CONNECTING
        val gateway = guild.gateway
        checkNotNull(gateway) { "Guild gateway not found" }
        gateway.send(
            UpdateVoiceStatus(
                guild.id,
                channel.id,
                selfMute = false,
                selfDeaf = false
            )
        )
    }

    @OptIn(ExperimentalContracts::class)
    private suspend fun checkChannel(channel: TopGuildChannel?) {
        contract {
            returns() implies (channel != null)
        }

        requireNotNull(channel) { "channelId must be the valid id of a voice channel" }
        require(channel.guildId.value == guildId) {
            "The provided VoiceChannel is not a part of the Guild that this AudioManager handles. Please provide a VoiceChannel from the proper Guild"
        }
        val permissions = channel.getEffectivePermissions(lavakord.kord.selfId)
        if (!permissions.contains(Permission.Connect) && !permissions.contains(Permission.MoveMembers)) {
            throw dev.schlaubi.lavakord.InsufficientPermissionException(
                Permissions(
                    Permission.Connect,
                    Permission.MoveMembers
                ).code.binary
            )
        }

        val voiceState = channel.getGuild().getMember(lavakord.kord.selfId).getVoiceStateOrNull()
        if (channel.id == voiceState?.channelId) return

        if (channel is VoiceChannel && voiceState?.channelId != null) {
            val limit = channel.userLimit
            if (!permissions.contains(Permission.Administrator)) {
                if (limit > 0
                    && limit >= channel.voiceStates.count()
                    && !permissions.contains(Permission.MoveMembers)
                )
                    throw dev.schlaubi.lavakord.InsufficientPermissionException(Permissions(Permission.MoveMembers).code.binary)
            }
        }
    }

    override suspend fun disconnectAudio() {
        state = Link.State.DISCONNECTING
        val guild = lavakord.kord.getGuildOrNull(Snowflake(guildId)) ?: error("Could not find Guild $guildId")
        lastChannelId = null
        val command = UpdateVoiceStatus(
            Snowflake(guildId),
            null,
            selfMute = false,
            selfDeaf = false
        )

        guild.gateway?.send(command)
    }
}
