package ca.tristan.easycommands.utils;

import net.dv8tion.jda.api.entities.Guild;

import java.util.Calendar;

public class Logger {

    /**
     * This function will log to the console.
     * @param type OK, WARNING, ERROR.
     * @param log String to be logged to the console.
     * @param stacktrace If you don't have a stacktrace make it "null", or use the other function without stacktrace.
     */
    public static void log(LogType type, String log, String stacktrace) {
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

    // TODO
    private static void logToGuildChannel(Guild guild, String log) {

    }

}
