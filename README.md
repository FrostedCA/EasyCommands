# EasyCommands
Public library to make slash commands creation for JDA API easier.

## Example Main class setup.
```java
    public static void main(String[] args) throws InterruptedException {

        JDABuilder jdaBuilder = JDABuilder.create("---", Arrays.asList(EasyCommands.gatewayIntents));
        
        jdaBuilder.enableCache(Arrays.asList(EasyCommands.cacheFlags));

        JDA jda = jdaBuilder.build().awaitReady();

        EasyCommands easyCommands = new EasyCommands(jda, useDevCommands?); // If you don't know how to use devCommands you can set it to 'false'

        easyCommands.addExecutor(new HelpCmd(easyCommands), new OtherCmdEx(), etc);
        easyCommands.enableMusicBot();
        easyCommands.registerListeners(
            //Your Listeners in here (ListenerAdpaters)
            new JoinEventEx(), // **Replace with your own events.**
            new LeaveEventEx()
        );
    }
```
## Download
[![](https://jitpack.io/v/FrostedCA/EasyCommands.svg)](https://jitpack.io/#FrostedCA/EasyCommands)

Latest Release: [GitHub Release](https://github.com/FrostedCA/EasyCommands/releases/tag/v0.3.5)

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
