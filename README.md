# EasyCommands
Public library to make slash commands creation for JDA API easier.

## Summary
- <a href="#example-main-class-setup">Example Main Class</a>
- <a href="#commandexecutor">CommandExecutor</a>
- <a href="#download">Download</a>
## Example Main class setup.
### For Latest version: 0.4.0
```java
    public static void main(String[] args) throws InterruptedException {

       	EasyCommands easyCommands = new EasyCommands("token", enableDevCommands?, enableMusicBot?); // If you don't know how to use devCommands leave it to false.
	
	JDA jda = easyCommands.addExecutor( // Add your custom commands/executors here!
		new HelpCmd(easyCommands),
		new ExampleCmd1()...
	).registerListeners( // Add your custom listeners/events here!
		new ExampleListener1(),
		new ExampleListener2()...
	).buildJDA(); // Starts the bot!
	
    }
```

## CommandExecutor
### How to create a custom command with EasyCommands
- Create a new Java class. Ex: HelloCmd.java
- Extend the class with CommandExecutor. Ex: `public class HelloCmd extends CommandExecutor`
- You can now override all the necessesary functions. Ex: `getName(), getDescription(), execute(EventData data)`
- If you still need help you can check out this YouTube video:

<a href="http://www.youtube.com/watch?feature=player_embedded&v=7IUPpeEWM_M
" target="_blank"><img src="http://img.youtube.com/vi/7IUPpeEWM_M/0.jpg" 
alt="youtube thumbnail" width="480" height="340" /></a>

## Download
[![](https://jitpack.io/v/FrostedCA/EasyCommands.svg)](https://jitpack.io/#FrostedCA/EasyCommands)

Latest Release: [GitHub Release](https://github.com/FrostedCA/EasyCommands/releases/tag/v0.4.0)

Make sure to replace **RELEASE** with the latest available release. Check [JitPack](https://jitpack.io/#FrostedCA/EasyCommands) steps for more information. 

```pom.xml
<repositories>
    <repository>
	<id>jitpack.io</id>
	<url>https://jitpack.io</url>
    </repository>
</repositories>
```
```pom.xml
<dependency>
    <groupId>com.github.FrostedCA</groupId>
    <artifactId>EasyCommands</artifactId>
    <version>RELEASE</version>
</dependency>
```
