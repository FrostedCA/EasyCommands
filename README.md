# EasyCommands
Public library to make slash commands creation for JDA API easier.

## Summary

 - <a href="#example-main-class-setup">Example Main Class</a>
 - <a href="#commandexecutor">CommandExecutor</a>
 - <a href="#download">Download</a>
https://github.com/FrostedCA/EasyCommands#download
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
