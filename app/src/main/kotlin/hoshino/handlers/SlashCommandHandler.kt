package hoshino.handlers

import dev.kord.common.Locale
import dev.kord.common.annotation.KordPreview
import dev.kord.common.entity.ApplicationCommandType
import dev.kord.common.entity.Permissions
import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.core.entity.interaction.Interaction
import dev.kord.core.event.interaction.InteractionCreateEvent
import dev.kord.core.on
import dev.kord.rest.builder.interaction.GlobalApplicationCommandCreateBuilder
import dev.kord.rest.json.request.ApplicationCommandCreateRequest
import dev.kord.rest.service.RestClient
import dev.kord.common.entity.optional.Optional
import dev.kord.common.entity.optional.OptionalBoolean
import dev.kord.common.entity.optional.optional
import dev.kord.core.entity.interaction.ApplicationCommandInteraction

@KordPreview
class SlashCommandHandler(private val kord: Kord, private val applicationId: Snowflake) {
    private val commands = mutableMapOf<String, suspend (Interaction) -> Unit>()
    private val rest = RestClient(kord.resources.token)

    suspend fun registerCommand(name: String, description: String, handler: suspend (Interaction) -> Unit) {
        val builder = object : GlobalApplicationCommandCreateBuilder {
            override var name: String = name
            override var nameLocalizations: MutableMap<Locale, String>? = null
            val description: String = description
            override var defaultMemberPermissions: Permissions? = null
            @Deprecated("'defaultPermission' is deprecated in favor of 'defaultMemberPermissions' and 'dmPermission'. Setting 'defaultPermission' to false can be replaced by setting 'defaultMemberPermissions' to empty Permissions and 'dmPermission' to false ('dmPermission' is only available for global commands).")
            override var defaultPermission: Boolean? = null
            override val type: ApplicationCommandType = ApplicationCommandType.ChatInput // Change this line
            override var dmPermission: Boolean? = null
            override var nsfw: Boolean? = null
            override fun toRequest(): ApplicationCommandCreateRequest {
                return ApplicationCommandCreateRequest(
                    name = name,
                    description = Optional(description),
                    defaultMemberPermissions = Optional(defaultMemberPermissions),
                    type = type,
                    dmPermission = dmPermission?.optional() ?: OptionalBoolean.Missing,
                    nsfw = nsfw?.optional() ?: OptionalBoolean.Missing
                )
            }
        }
        rest.interaction.createGlobalApplicationCommand(applicationId, builder.toRequest())
        commands[name] = handler
    }

    fun listen() {
        kord.on<InteractionCreateEvent> {
            val interaction = this.interaction
            if (interaction is ApplicationCommandInteraction) {
                val commandName = interaction.invokedCommandName
                val handler = commands[commandName] ?: return@on
                handler(interaction)
            }
        }
        }
}

