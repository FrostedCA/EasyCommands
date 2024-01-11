package ca.tristan.easycommands.commands.prefix;

import ca.tristan.easycommands.commands.IExecutor;
import ca.tristan.easycommands.database.MySQL;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;

public abstract class PrefixExecutor implements IExecutor {

    public List<PrefixOption> options = new ArrayList<>();
    public List<String> aliases = new ArrayList<>();
    public List<Role> authorizedRoles = new ArrayList<>();
    public List<Channel> authorizedChannels = new ArrayList<>();

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

    protected final List<PrefixOption> getOptions(){
        return options;
    }

    public String usage(){
        StringBuilder usage = new StringBuilder("Usage: " + PrefixCommands.prefix + getName());
        for(PrefixOption option : options){
            usage.append(" <").append(option.getName().replace("<", "").replace(">", "")).append(">");
        }
        return usage.toString();
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

    @Override
    public boolean isOwnerOnly() {
        return false;
    }

    abstract public void execute(MessageReceivedEvent event, MySQL mySQL);

}
