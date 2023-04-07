package ca.tristan.easycommands.commands.slash;

import ca.tristan.easycommands.EasyCommands;
import ca.tristan.easycommands.commands.EventData;
import ca.tristan.easycommands.utils.LogType;
import ca.tristan.easycommands.utils.Logger;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class SlashCommands extends ListenerAdapter {

    private final EasyCommands easyCommands;

    public SlashCommands(EasyCommands easyCommands) {
        this.easyCommands = easyCommands;
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if(easyCommands.getExecutors().containsKey(event.getName()) && easyCommands.getExecutors().get(event.getName()) instanceof SlashExecutor) {
            SlashExecutor executor = (SlashExecutor) easyCommands.getExecutors().get(event.getName());
            Logger.log(LogType.SLASHCMD, "'" + executor.getName() + "' has been triggered.");
            if(executor.isOwnerOnly() && ! (Objects.requireNonNull(event.getMember())).isOwner()) {
                event.reply("This command can only be used by the server owner.").setEphemeral(true).queue();
                return;
            }

            if(!executor.getAuthorizedChannels(easyCommands.jda).isEmpty() && !executor.getAuthorizedChannels(easyCommands.jda).contains(event.getChannel())) {
                event.reply("This command cannot be used in this channel.").setEphemeral(true).queue();
                return;
            }

            if(executor.getAuthorizedRoles(easyCommands.jda) != null && !executor.getAuthorizedRoles(easyCommands.jda).isEmpty()) {
                for (Role authorizedRole : executor.getAuthorizedRoles(easyCommands.jda)) {
                    if(Objects.requireNonNull(event.getMember()).getRoles().contains(authorizedRole)) {
                        executor.execute(new EventData(event));
                        break;
                    }
                }
                return;
            }

            executor.execute(new EventData(event));
        }
    }

}
