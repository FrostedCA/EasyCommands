# EasyCommands
Public library to make slash commands creation for JDA API easier.

## Example Main class setup.

  private static final GatewayIntent[] gatewayIntents = { GatewayIntent.DIRECT_MESSAGES, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MEMBERS };
  private static EasyCommands easyCommands;

  /* Example Main class */

  public static void main(String[] args) {

      JDABuilder jdaBuilder = JDABuilder.create("---", Arrays.asList(gatewayIntents));

      JDA jda = jdaBuilder.build();

      easyCommands = new EasyCommands(jda);
      easyCommands.mysqlConnect("localhost:3306/support", "root", "Test");

      easyCommands.addExecutor(new HelloCmd());
      jda.addEventListener(easyCommands);
      jda.updateCommands().queue();

      easyCommands.logCurrentExecutors();

  }

  public static EasyCommands getEasyCommands() {
      return easyCommands;
  }
