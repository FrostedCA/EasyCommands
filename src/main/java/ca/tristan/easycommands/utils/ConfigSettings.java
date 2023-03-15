package ca.tristan.easycommands.utils;

public enum ConfigSettings {

    TOKEN("token="),
    USE_MYSQL("use_mysql="),
    DB_HOST("db_host="),
    DB_PORT("db_port="),
    DB_NAME("db_name="),
    DB_USER("db_user="),
    DB_PASSWORD("db_password="),
    USE_MUSIC_BOT("use_music_bot="),
    USE_PREFIXCOMMANDS("use_prefixcommands="),
    ;

    public String label;

    ConfigSettings(String label) {
        this.label = label;
    }

}
