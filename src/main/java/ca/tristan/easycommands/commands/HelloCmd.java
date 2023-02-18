package ca.tristan.easycommands.commands;

public class HelloCmd extends CommandExecutor {

    @Override
    public String getName() {
        return "hello";
    }

    @Override
    public String getDescription() {
        return "Hello world!";
    }

    @Override
    public String getHelpMessage() {
        return "/hello and receive 'Hello world!' in return.";
    }

    @Override
    public void execute(EventData executor) {
        executor.getDeferReply().queue();
        executor.getHook().sendMessage("Hello world!").queue();
    }

}
