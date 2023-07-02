package ca.tristan.testbot;


import ca.tristan.easycommands.EasyCommands;
import ca.tristan.easycommands.commands.defaults.HelpCmd;
import ca.tristan.easycommands.commands.defaults.ReloadConfigCmd;
import net.dv8tion.jda.api.JDA;

import java.io.IOException;

public class BotMain {

    private static JDA jda;

    public static void main(String[] args) throws IOException, InterruptedException {
        EasyCommands easyCommands = new EasyCommands();
        BotMain botMain = new BotMain();
        jda = botMain.setupJDA(easyCommands);
    }

    public static JDA getJDA() {
        return jda;
    }

    private JDA setupJDA(EasyCommands easyCommands) throws InterruptedException {
        return easyCommands.registerListeners(

        ).addExecutor(
                new HelpCmd(easyCommands),
                new ReloadConfigCmd(easyCommands),
                new ExampleCommand()
        ).addEnabledCacheFlags().addGatewayIntents().buildJDA();
    }

}
