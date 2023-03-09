package ca.tristan.easycommands.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.Channel;

import java.util.List;

public interface IECCommand {

    String getName();

    List<String> getAliases();

    String getDescription();

    boolean isOwnerOnly();

    List<Channel> getAuthorizedChannels(JDA jda);

    List<Role> getAuthorizedRoles(JDA jda);

}
