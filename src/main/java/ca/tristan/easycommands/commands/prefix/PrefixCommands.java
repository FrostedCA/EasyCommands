package ca.tristan.easycommands.commands.prefix;

import ca.tristan.easycommands.commands.EasyCommands;
import ca.tristan.easycommands.utils.LogType;
import ca.tristan.easycommands.utils.Logger;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class PrefixCommands extends ListenerAdapter {

    private final EasyCommands easyCommands;
    private String prefix = "!";

    public PrefixCommands(EasyCommands easyCommands) {
        this.easyCommands = easyCommands;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split(" ");

        if(!args[0].contains(prefix) || Objects.requireNonNull(event.getMember()).getUser().isBot()) {
            return;
        }

        String cmdName = args[0].replace(prefix, "");
        if(easyCommands.getExecutors().containsKey(cmdName) && easyCommands.getExecutors().get(cmdName) instanceof PrefixExecutor executor) {
            Logger.log(LogType.PREFIXCMD, "'" + cmdName + "' has been triggered.");
            if(!executor.getAuthorizedChannels(easyCommands.jda).isEmpty() && !executor.getAuthorizedChannels(easyCommands.jda).contains(event.getChannel())) {
                return;
            }

            if(executor.getAuthorizedRoles(easyCommands.jda) != null && !executor.getAuthorizedRoles(easyCommands.jda).isEmpty()) {
                for (Role authorizedRole : executor.getAuthorizedRoles(easyCommands.jda)) {
                    if(Objects.requireNonNull(event.getMember()).getRoles().contains(authorizedRole)) {
                        executor.execute(event);
                        event.getMessage().delete().queue();
                        break;
                    }
                }
                return;
            }

            executor.execute(event);
            event.getMessage().delete().queue();
        }
    }

}
