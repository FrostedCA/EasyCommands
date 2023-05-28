package ca.tristan.easycommands.utils;

enum ConfigOptions {

    TOKEN(""),
    USE_MYSQL(""),
    DB_HOST(""),
    DB_PORT(""),
    DB_NAME(""),
    DB_USER(""),
    DB_PASSWORD(""),
    USE_MUSIC_BOT(""),
    USE_PREFIXCOMMANDS(""),
    MEMBER_ROLE_ID(""),
    BOT_ROLE_ID(""),
    LOG_CHANNEL_ID(""),
    MUSIC_EMBED_COLOR("");

    private String value;

    ConfigOptions(String value){
        this.value = value;
    }

    public void setValue(String value){
        this.value = value;
    }

    public String getValue(){
        return value;
    }

}
