package ca.tristan.easycommands.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;

import java.sql.Connection;

public class EventData {

    private final MessageChannelUnion channel;
    private final GuildMessageChannel guildMessageChannel;
    private final TextChannel textChannel;
    private final Member selfMember;
    private final Member commandSender;
    private final Guild guild;
    private final JDA jda;
    private final CommandData command;

    private final GuildVoiceState selfVoiceState;
    private final GuildVoiceState memberVoiceState;

    private final ReplyCallbackAction deferReply;
    private final InteractionHook hook;

    private final Connection connection;

    private final SlashCommandInteractionEvent event;

    public EventData(SlashCommandInteractionEvent event) {
        this.channel = event.getChannel();
        this.commandSender = event.getMember();
        this.guild = event.getGuild();
        this.jda = event.getJDA();
        this.command = new CommandData(event.getCommandIdLong(), event.getCommandString(), event.getCommandType(), event.getName(), event.getFullCommandName(), event.getOptions());

        this.deferReply = event.deferReply();
        this.hook = event.getHook();

        this.guildMessageChannel = this.channel.asGuildMessageChannel();
        this.textChannel = this.channel.asTextChannel();
        this.selfMember = this.guild != null ? this.guild.getSelfMember() : null;

        this.selfVoiceState = this.selfMember != null ? this.selfMember.getVoiceState() : null;
        this.memberVoiceState = this.commandSender != null ? this.commandSender.getVoiceState() : null;

        this.connection = EasyCommands.getConnection() != null ? EasyCommands.getConnection() : null;

        this.event = event;
    }

    public MessageChannelUnion getChannel() {
        return channel;
    }

    public GuildMessageChannel getGuildMessageChannel() {
        return guildMessageChannel;
    }

    public TextChannel getTextChannel() {
        return textChannel;
    }

    public Member getSelfMember() {
        return selfMember;
    }

    public Member getCommandSender() {
        return commandSender;
    }

    public Guild getGuild() {
        return guild;
    }

    public JDA getJda() {
        return jda;
    }

    public CommandData getCommand() {
        return command;
    }

    public GuildVoiceState getSelfVoiceState() {
        return selfVoiceState;
    }

    public GuildVoiceState getMemberVoiceState() {
        return memberVoiceState;
    }

    public ReplyCallbackAction deferReply() {
        return deferReply;
    }

    public InteractionHook getHook() {
        return hook;
    }

    public Connection getConnection() {
        return connection;
    }

    public SlashCommandInteractionEvent getEvent() {
        return event;
    }

}
