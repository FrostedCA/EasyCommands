<img src="https://raw.githubusercontent.com/FrostedCA/EasyCommands/master/ECLogo_new.png" width="100" height="100" />

# EasyCommands (JDA Slash & Prefix Command handler)
Public library to make slash & prefix commands creation for JDA API easier.
#### *Contains a Music Bot*
#### Join the [Discord](https://discord.com/invite/2rfSEGNgrv) server for support.
### If you want to bring any changes to the library you can create a fork of this branch, contact me on Discord, give good explanation of your changes and they might get added to this official branch.

## Summary
- <a href="#example-main-class-setup">Main Class Example</a>
- <a href="#slashexecutor">Create a slash command</a>
- <a href="#prefixexecutor">Create a prefix command</a>
- <a href="#config--available-in-version-041">Config File Setup</a>
- <a href="#mysql-database">MySQL Database</a>
- <a href="#download">Download</a>

## Example Main class setup.
#### Latest Version: [GitHub Release](https://github.com/FrostedCA/EasyCommands/releases/latest)
```java
    public static void main(String[] args) throws InterruptedException {

       	EasyCommands easyCommands = new EasyCommands("token", enablePrefixCommands?, enableMusicBot?); // If you don't know how to use devCommands leave it to false.
	
	easyCommands.getPrefixCommands().setPrefix("prefix"); // Only if you want to change the default prefix from '!'
	
	easyCommands.getEnabledCacheFlags() or easyCommands.getDisabledCacheFlags() // Only use if you want to edit the enabled/disabled cacheflags.
	easyCommands.getGatewayIntents() // Only use if you want to edit the GatewayIntents.
	
	JDA jda = easyCommands.addExecutor( // Add your custom commands/executors here!
		new HelpCmd(easyCommands),
		new ExampleCmd1()...
	).registerListeners( // Add your custom listeners/events here!
		new ExampleListener1(),
		new ExampleListener2()...
	).buildJDA(); // Starts the bot!
	
    }
```

## SlashExecutor
#### How to create a custom slash command with EasyCommands
- Create a new Java class. Ex: HelloCmd.java
- Extend the class with SlashExecutor. Ex: `public class HelloCmd extends SlashExecutor`
- You can now override all the necessesary functions. Ex: `getName(), getDescription(), execute(EventData data)`
- When you are done creating your command class, you can register it inside of your Main class. Ex: `JDA jda = easyCommands.addExecutor(new HelloCmd()).buildJDA();`
- If you still need help you can check out this YouTube video:

<a href="http://www.youtube.com/watch?feature=player_embedded&v=7IUPpeEWM_M
" target="_blank"><img src="http://img.youtube.com/vi/7IUPpeEWM_M/0.jpg" 
alt="youtube thumbnail" width="480" height="340" /></a>

or check out this simple command class: [NowPlayingCmd.java](https://github.com/FrostedCA/EasyCommands/blob/master/src/main/java/ca/tristan/easycommands/commands/music/NowPlayingCmd.java)

## PrefixExecutor
#### How to create a custom prefix command with EasyCommands
#### You can edit the default prefix by adding one line of code to your main class. <a href="#example-main-class-setup">See Main class example</a>
- Create a new Java class. Ex: PHelloCmd.java
- Extend the class with CommandExecutor. Ex: `public class HelloCmd extends PrefixExecutor`
- You can now override all the necessesary functions. Ex: `getName(), getDescription(), execute()`
- When you are done creating your command class, you can register it inside of your Main class. Ex: `JDA jda = easyCommands.addExecutor(new PHelloCmd()).buildJDA();`

## Config | Available in version 0.4.1+
#### How to use the Config class?
Config example:
```ini
token=token
db_url=localhost:3306/example
db_username=username
db_password=password
//available in version 0.6+
music_channel=id
```
To access those parameters inside of your code you need to instantiate a new Config variable. Ex: `Config config = new Config();`
Then you will be able to access your token/etc with `config.getToken()` or `config.getDB_URL()` etc.
#### Main class with Config
```java
    public static void main(String[] args) throws InterruptedException, IOException {
	Config config = new Config();
       	EasyCommands easyCommands = new EasyCommands(config.getToken(), enableDevCommands?, enableMusicBot?); // If you don't know how to use devCommands leave it to false.
	
	easyCommands.mysqlConnect(config.getDB_URL(), config.getDB_User(), config.getDB_Password());
	
	JDA jda = easyCommands.addExecutor( // Add your custom commands/executors here!
		new HelpCmd(easyCommands),
		new ExampleCmd1()...
	).registerListeners( // Add your custom listeners/events here!
		new ExampleListener1(),
		new ExampleListener2()...
	).buildJDA(); // Starts the bot!
	
	easyCommands.setMusicChannel(config.getMusicChannelID());
	
    }
```

## MySQL Database
#### How to use a MySQL database with the library.
<a href="http://www.youtube.com/watch?feature=player_embedded&v=3O5csTk7QnI
" target="_blank"><img src="http://img.youtube.com/vi/3O5csTk7QnI/0.jpg" 
alt="youtube thumbnail" width="480" height="340" /></a>

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
