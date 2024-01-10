[![Version](https://jitpack.io/v/FrostedCA/EasyCommands.svg)](https://jitpack.io/#FrostedCA/EasyCommands)
[![Release](https://img.shields.io/github/release/FrostedCA/EasyCommands.svg)](https://github.com/FrostedCA/EasyCommands/releases/latest)
[![JDA](https://img.shields.io/badge/JDA%20Version-5.0.0--beta.18+-important)](https://github.com/DV8FromTheWorld/JDA/releases)
[![Discord](https://discord.com/api/guilds/944538207868059669/embed.png?style=shield)](https://discord.gg/2rfSEGNgrv)

<img align="right" src="https://raw.githubusercontent.com/FrostedCA/EasyCommands/master/ECLogo_new.png" width="200" height="200" />

# EasyCommands (JDA Slash & Prefix Command handler)
Public library to make slash & prefix commands creation for JDA API easier.
#### *Contains a Music Bot*
#### Join the [Discord](https://discord.com/invite/2rfSEGNgrv) server for support.
### If you want to bring any changes to the library you can create a fork of this branch, contact me on Discord, give good explanation of your changes and they might get added to this official branch.

## Summary
- <a href="#example-main-class-setup">Main Class Example</a>
- <a href="#slashexecutor">Create a slash command</a>
- <a href="#prefixexecutor">Create a prefix command</a>
- <a href="#config">Config File Setup</a>
- <a href="#mysql-database">MySQL Database</a>
- <a href="#download">Download</a>

## Example Main class setup.
#### Latest Version: [GitHub Release](https://github.com/FrostedCA/EasyCommands/releases/latest)
```java
    public static void main(String[] args) throws InterruptedException, IOException {
       	EasyCommands easyCommands = new EasyCommands();
	
	JDA jda = easyCommands.addExecutor( // Add your custom commands/executors here!
		new HelpCmd(easyCommands),
		new ExampleCmd1()...
	).registerListeners( // Add your custom listeners/events here!
		new ExampleListener1(),
		new ExampleListener2()...
	).addGatewayIntents(/*leave empty if any*/)
	.addEnabledCacheFlags(/*leave empty if any*/)
	.buildJDA(); // Starts the bot!
    }
```

## SlashExecutor
#### How to create a custom slash command with EasyCommands
Example slash executor class (Implement needed functions)
**Includes more functions than shown in the example**

```java
public class ExampleCmd extends SlashExecutor {

    @Override
    public String getName() {
        return "hello";
    }

    @Override
    public String getDescription() {
        return "Say Hello world!";
    }
    
    @Override
    public void execute(EventData data, MySQL mySQL) {
    	data.reply("Hello world!", false).queue();
    }

}
```
or check out this simple command class: [NowPlayingCmd.java](https://github.com/FrostedCA/EasyCommands/blob/master/src/main/java/ca/tristan/easycommands/commands/music/NowPlayingCmd.java)

## PrefixExecutor
#### How to create a custom prefix command with EasyCommands
#### You can edit the default prefix by adding one line of code to your main class. <a href="#example-main-class-setup">See Main class example</a>
- Create a new Java class. Ex: PHelloCmd.java
- Extend the class with CommandExecutor. Ex: `public class HelloCmd extends PrefixExecutor`
- You can now override all the necessesary functions. Ex: `getName(), getDescription(), execute()`
- When you are done creating your command class, you can register it inside of your Main class. Ex: `JDA jda = easyCommands.addExecutor(new PHelloCmd()).buildJDA();`
#### How to change the prefix for commands?
`easyCommands.getPrefixCommands().setPrefix("prefix");`
Simply add this line inside your Main class under `EasyCommands easyCommands = new EasyCommands();`

## Register an Executor
In your main class when building the JDA ->

`.addExecutor(new ExampleCmd())`

`.addExecutor(new ExampleCmd1(), new ExampleCmd2(), new ExampleCmd3()...)` (You can also add multiple commands inside that function)

## Config
#### How to use the Config class?
The Config file settings get generated automaticaly on creation.
If you want to update the config file when a new version releases take a look at [ConfigSettings](https://github.com/FrostedCA/EasyCommands/blob/master/src/main/java/ca/tristan/easycommands/utils/ConfigSettings.java)

To access the config settings inside of your code you need to use a `EasyCommands` instance can get the function `getConfig()`
#### Main class with Config
```java
    public static void main(String[] args) throws InterruptedException, IOException {
       	EasyCommands easyCommands = new EasyCommands();
	
	easyCommands.getConfig().get...; <------ !!!
	
	JDA jda = easyCommands.addExecutor( // Add your custom commands/executors here!
		new HelpCmd(easyCommands),
		new ExampleCmd1()...
	).registerListeners( // Add your custom listeners/events here!
		new ExampleListener1(),
		new ExampleListener2()...
	).buildJDA(); // Starts the bot!
    }
```

## MySQL Database
#### How to use a MySQL database with the library.
You need to configure the Config file correctly. See --> <a href="#config">Config File Setup</a>

## Download
[![Version](https://jitpack.io/v/FrostedCA/EasyCommands.svg)](https://jitpack.io/#FrostedCA/EasyCommands)

#### Latest Version: [GitHub Release](https://github.com/FrostedCA/EasyCommands/releases/latest)

Make sure to replace **VERSION** with the latest available version above! Check [JitPack](https://jitpack.io/#FrostedCA/EasyCommands) for more information. 

### Maven
```pom.xml
<repositories>
    <repository>
	<id>jitpack.io</id>
	<url>https://jitpack.io</url>
    </repository>
</repositories>

<dependency>
    <groupId>com.github.FrostedCA</groupId>
    <artifactId>EasyCommands</artifactId>
    <version>VERSION</version>
</dependency>
```
### Gradle
```gradle
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.FrostedCA:EasyCommands:VERSION'
}
```
