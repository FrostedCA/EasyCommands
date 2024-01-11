package ca.tristan.easycommands.guild;

public enum GuildOptions {

    MEMBER_ROLE(""),
    BOT_ROLE(""),
    MUSIC_CHANNEL(""),
    LOG_CHANNEL("");

    private String value;

    GuildOptions(String value){
        this.value = value;
    }

    public void setValue(String value){
        this.value = value;
    }

    public String getValue(){
        return value;
    }

}
