package ca.tristan.easycommands;

import ca.tristan.easycommands.guild.ECGuild;
import ca.tristan.easycommands.guild.GuildOptions;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public class EC {

    public static boolean updateGuildProperty(GuildOptions key, String value, String guildId) {

        Optional<ECGuild> opt = Arrays.stream(EasyCommands.getEcGuilds()).filter(ecGuild -> Objects.equals(ecGuild.id, guildId)).findAny();

        if (opt.isEmpty()) {
            return false;
        }

        ECGuild guild = opt.get();
        guild.guildProperties.put(key.name(), value);
        guild.saveProperties();
        return true;
    }

    public static boolean canSendMusicCommand(String guildId, String channelId) {

        Optional<ECGuild> opt = Arrays.stream(EasyCommands.getEcGuilds()).filter(ecGuild -> Objects.equals(ecGuild.id, guildId)).findAny();

        if (opt.isEmpty()) {
            return false;
        }

        ECGuild guild = opt.get();

        if (guild.guildProperties.containsKey(GuildOptions.MUSIC_CHANNEL.name())) {
            String id = guild.guildProperties.getProperty(GuildOptions.MUSIC_CHANNEL.name());
            return id.equals(channelId);
        }

        return false;
    }


}
