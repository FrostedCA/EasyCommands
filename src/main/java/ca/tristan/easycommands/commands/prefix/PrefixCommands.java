package ca.tristan.easycommands.commands.prefix;

import ca.tristan.easycommands.EasyCommands;
import ca.tristan.easycommands.utils.LogType;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class PrefixCommands extends ListenerAdapter {

    private final EasyCommands easyCommands;
    public static String prefix = "!";

    public PrefixCommands(EasyCommands easyCommands) {
        this.easyCommands = easyCommands;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        PrefixCommands.prefix = prefix;
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split(" ");

        if(!args[0].contains(prefix) || Objects.requireNonNull(event.getMember()).getUser().isBot()) {
            return;
        }

        if(!args[0].startsWith(prefix)) {
            return;
        }

        String cmdName = args[0].replaceFirst(prefix, "");

        if(easyCommands.getExecutors().containsKey(cmdName) && easyCommands.getExecutors().get(cmdName) instanceof PrefixExecutor) {
            PrefixExecutor executor = (PrefixExecutor) easyCommands.getExecutors().get(cmdName);
            String[] options = event.getMessage().getContentRaw().replace(prefix + cmdName + " ", "").split(" ");

            if(options.length != executor.getOptions().size()){
                event.getChannel().sendMessage(executor.usage()).queue(message -> message.delete().queueAfter(10,TimeUnit.SECONDS));
                return;
            }
            else{
                for (int i = 0; i < executor.getOptions().size(); i++) {
                    executor.getOptions().get(i).setStringValue(options[i]);
                }
            }
            easyCommands.getLogger().logBoth(LogType.PREFIXCMD, "'" + cmdName + "' has been triggered.", event.getMember());
            if(!executor.getAuthorizedChannels(easyCommands.jda).isEmpty() && !executor.getAuthorizedChannels(easyCommands.jda).contains(event.getChannel())) {
                easyCommands.getLogger().logBoth(LogType.WARNING, "PrefixCommand: '" + cmdName + "' has been triggered but the channel it was executed in isn't authorized.", event.getMember());
                return;
            }

            if(executor.getAuthorizedRoles(easyCommands.jda) != null && !executor.getAuthorizedRoles(easyCommands.jda).isEmpty()) {
                for (Role authorizedRole : executor.getAuthorizedRoles(easyCommands.jda)) {
                    if(Objects.requireNonNull(event.getMember()).getRoles().contains(authorizedRole)) {
                        executor.execute(event, easyCommands.getMySQL());
                        event.getMessage().delete().queue();
                        break;
                    }
                }
                return;
            }

            executor.execute(event, easyCommands.getMySQL());
            event.getMessage().delete().queue();
        }
    }

}
