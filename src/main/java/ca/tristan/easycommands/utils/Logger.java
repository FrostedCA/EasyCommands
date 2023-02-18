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
        int hour = Calendar.getInstance().getTime().getHours();
        int minute = Calendar.getInstance().getTime().getMinutes();
        int second = Calendar.getInstance().getTime().getSeconds();

        String time = "[" + hour + ":" + minute + ":" + second + " INFO]: ";
        String line = null;

        if(type == null) {
            System.out.println(time + ConsoleColors.RED + "Log type can't be null. Here's the log anyways: " + log + ConsoleColors.RESET);
            return;
        }

        switch (type) {
            case OK -> {
                line = time + ConsoleColors.GREEN_BOLD + type + ": " + ConsoleColors.GREEN + log + ConsoleColors.RESET + (stacktrace != null ? " Stacktrace: " + stacktrace : "");
            }
            case WARNING -> {
                line = time + ConsoleColors.YELLOW_BOLD + type + ": " + ConsoleColors.YELLOW + log + ConsoleColors.RESET + (stacktrace != null ? " Stacktrace: " + stacktrace : "");
            }
            case ERROR -> {
                line = time + ConsoleColors.RED_BOLD + type + ": " + ConsoleColors.RED + log + ConsoleColors.RESET + (stacktrace != null ? " Stacktrace: " + stacktrace : "");
            }
        }

        System.out.println(line);
    }

    public static void log(LogType type, String log) {
        log(type, log, null);
    }

    // TODO
    private static void logToGuildChannel(Guild guild, String log) {

    }

}
