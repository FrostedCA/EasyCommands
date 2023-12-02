package ca.tristan.easycommands.commands.slash;

import ca.tristan.easycommands.EasyCommands;
import ca.tristan.easycommands.commands.EventData;
import ca.tristan.easycommands.commands.IExecutor;
import ca.tristan.easycommands.database.MySQL;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;

public abstract class SlashExecutor implements IExecutor {

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

    public void execute(EventData event, MySQL mySQL) {

    }

    public void execute(EventData event) {

    }

}
