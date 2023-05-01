package ca.tristan.easycommands.utils;

import ca.tristan.easycommands.EasyCommands;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

import java.util.Calendar;

public class Logger {

    private EasyCommands easyCommands;

    public Logger(EasyCommands easyCommands) {
        this.easyCommands = easyCommands;
    }

    /**
     * This function will log to the console.
     * @param type OK, WARNING, ERROR.
     * @param log String to be logged to the console.
     * @param stacktrace If you don't have a stacktrace make it "null", or use the other function without stacktrace.
     * @deprecated Use logBoth(LogType, String log)! This will make sure to send the logs inside the log channel if enabled.
     */
    private static void log(LogType type, String log, String stacktrace) {
        String hour = String.format("%02d", Calendar.getInstance().getTime().getHours());
        String minute = String.format("%02d", Calendar.getInstance().getTime().getMinutes());
        String second = String.format("%02d", Calendar.getInstance().getTime().getSeconds());;
        String time = "[" + hour + ":" + minute + ":" + second + " INFO]: ";
        String line = null;



        if(type == null) {
            System.out.println(time + ConsoleColors.RED + "Log type can't be null. Here's the log anyways: " + log + ConsoleColors.RESET);
            return;
        }

        switch (type) {
            case PREFIXCMD:
            case SLASHCMD:
            case EXECUTORS:
            case LISTENERS:
            case OK:
                line = time + ConsoleColors.GREEN_BOLD + type + ": " + ConsoleColors.GREEN + log + ConsoleColors.RESET + (stacktrace != null ? " Stacktrace: " + stacktrace : "");
                break;
            case WARNING:
                line = time + ConsoleColors.YELLOW_BOLD + type + ": " + ConsoleColors.YELLOW + log + ConsoleColors.RESET + (stacktrace != null ? " Stacktrace: " + stacktrace : "");
                break;
            case ERROR:
                line = time + ConsoleColors.RED_BOLD + type + ": " + ConsoleColors.RED + log + ConsoleColors.RESET + (stacktrace != null ? " Stacktrace: " + stacktrace : "");
                break;
        }

        System.out.println(line);
    }

    public static void log(LogType type, String log) {
        log(type, log, null);
    }

    public static void logNoType(String log) {
        String hour = String.format("%02d", Calendar.getInstance().getTime().getHours());
        String minute = String.format("%02d", Calendar.getInstance().getTime().getMinutes());
        String second = String.format("%02d", Calendar.getInstance().getTime().getSeconds());;
        String time = "[" + hour + ":" + minute + ":" + second + " INFO]: ";
        System.out.println(time + ConsoleColors.PURPLE + log + ConsoleColors.RESET);
    }

    public void logBoth(LogType type, String log, Member commandSender) {
        log(type, log);

        if(easyCommands.getLogChannel() == null) {
            return;
        }

        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("Logs");
        if(commandSender != null) {
            builder.addField("Triggered by:", commandSender.getAsMention(), false);
        }
        builder.addField("LogType: " + type.toString(), "Log: " + log, false);
        builder.setFooter(Calendar.getInstance().getTime().toString());
        easyCommands.getLogChannel().sendMessageEmbeds(builder.build()).queue();
    }

}
