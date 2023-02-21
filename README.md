# EasyCommands
Public library to make slash commands creation for JDA API easier.

## Example Main class setup.

    private static EasyCommands easyCommands;

    /* Example Main class */

    public static void main(String[] args) throws InterruptedException {

        JDABuilder jdaBuilder = JDABuilder.create("---", Arrays.asList(EasyCommands.gatewayIntents));
        
        jdaBuilder.enableCache(Arrays.asList(EasyCommands.cacheFlags));

        JDA jda = jdaBuilder.build().awaitReady();

        easyCommands = new EasyCommands(jda);
        easyCommands.mysqlConnect("localhost:3306/support", "root", "Test");

        easyCommands.addExecutor(new HelpCmd(), new OtherCmdEx(), etc);
        easyCommands.enableMusicBot();
        easyCommands.updateCommands();
        jda.addEventListener(easyCommands);
        easyCommands.logCurrentExecutors();
    }

    public static EasyCommands getEasyCommands() {
        return easyCommands;
    }
