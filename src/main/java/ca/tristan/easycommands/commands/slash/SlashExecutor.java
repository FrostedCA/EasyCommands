package ca.tristan.easycommands.commands.slash;

import ca.tristan.easycommands.commands.EventData;
import ca.tristan.easycommands.commands.IExecutor;
import ca.tristan.easycommands.database.MySQL;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;

public abstract class SlashExecutor implements IExecutor {

    public List<OptionData> options = new ArrayList<>();
    public List<String> aliases = new ArrayList<>();
    public List<Role> authorizedRoles = new ArrayList<>();
    public List<Channel> authorizedChannels = new ArrayList<>();

    /**
     * Override this function to add command aliases;
     */
    @Override
    public void updateAliases() {

    }

    /**
     * Override this function to add your specific authorized roles ex:
     * authorizedRoles.add(jda.getGuildById().getRoleById("myRoleID"));
     *
     * @param jda
     */
    @Override
    public void updateAuthorizedRoles(JDA jda) {

    }

    /**
     * Override this function to add your specific authorized channels ex:
     * authorizedChannels.add(jda.getChannelByID("myChannelID"));
     *
     * @param jda
     */
    @Override
    public void updateAuthorizedChannels(JDA jda) {

    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public List<String> getAliases() {
        return aliases;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public boolean isOwnerOnly() {
        return false;
    }

    /**
     * Use this function to add options to your commands.
     */
    public void updateOptions() {

    }

    public List<OptionData> getOptions() {
        return options;
    }

    /**
     * You do not need to override this function anymore.
     */
    @Override
    public List<Channel> getAuthorizedChannels() {
        return authorizedChannels;
    }

    /**
     * You do not need to override this function anymore.
     */
    @Override
    public List<Role> getAuthorizedRoles() {
        return authorizedRoles;
    }

    /**
     * You do not need to override this function anymore. Please remove it if so, it will get removed soon.
     * Replaced by: getAuthorizedChannels();
     */
    @Override
    public List<Channel> getAuthorizedChannels(JDA jda) {
        return getAuthorizedChannels();
    }

    /**
     * You do not need to override this function anymore. Please remove it if so, it will get removed soon to.
     * Replaced by: getAuthorizedRoles();
     */
    @Override
    public List<Role> getAuthorizedRoles(JDA jda) {
        return getAuthorizedRoles();
    }

    public void execute(EventData event, MySQL mySQL) {

    }

    public void execute(EventData event) {

    }

}
