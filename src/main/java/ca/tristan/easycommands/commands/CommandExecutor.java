package ca.tristan.easycommands.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;

public abstract class CommandExecutor implements IECCommand {

    public List<OptionData> options = new ArrayList<>();

    @Override
    public String getName() {
        return null;
    }

    @Override
    public List<String> getAliases() {
        return new ArrayList<>();
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public boolean isOwnerOnly() {
        return false;
    }

    public List<OptionData> getOptions() {
        return options;
    }

    @Override
    public List<Channel> getAuthorizedChannels(JDA jda) {
        return new ArrayList<>();
    }

    @Override
    public List<Role> getAuthorizedRoles(JDA jda) {
        return new ArrayList<>();
    }

    public void execute(EventData data) {

    }

}
