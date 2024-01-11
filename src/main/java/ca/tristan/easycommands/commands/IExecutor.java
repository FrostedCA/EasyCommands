package ca.tristan.easycommands.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.Channel;

import java.util.List;

public interface IExecutor {

    /*
    Override this function the set the command name ex: return "play"; This command will be executed as "/play" if using SlashExecutor for example.
     */
    String getName();

    /**
     * Override this function to add command aliases;
     */
    void updateAliases();
    /**
     * Override this function to add your specific authorized roles ex:
     * authorizedRoles.add(jda.getGuildById().getRoleById("myRoleID"));
     */
    void updateAuthorizedRoles(JDA jda);
    /**
     * Override this function to add your specific authorized channels ex:
     * authorizedChannels.add(jda.getChannelByID("myChannelID"));
     */
    void updateAuthorizedChannels(JDA jda);

    List<String> getAliases();

    String getDescription();

    /*
    Override this function if you want the command to only be executable by the owner of the guild.
     */
    boolean isOwnerOnly();

    /*
    You do not need to override that anymore.
     */
    List<Channel> getAuthorizedChannels();
    /*
    You do not need to override that anymore.
     */
    List<Role> getAuthorizedRoles();

    /*
    You do not need to override that anymore.
     */
    @Deprecated
    List<Channel> getAuthorizedChannels(JDA jda);
    /*
    You do not need to override that anymore.
     */
    @Deprecated
    List<Role> getAuthorizedRoles(JDA jda);

}
