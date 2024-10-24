package com.hoanght.data.config;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GlobalConfig extends AbstractConfig {
    private static GlobalConfig instance;

    private Long syncInterval;
    private String mysqlHost;
    private String mysqlPort;
    private String mysqlDatabase;
    private String mysqlTable;
    private String mysqlUsername;
    private String mysqlPassword;

    public static GlobalConfig getInstance() {
        if (instance == null) {
            instance = new GlobalConfig();
        }
        return instance;
    }

    public void init() {
        super.init("", "config.yml");

        if (getConfig().get("sync_interval") == null) getConfig().set("sync_interval", 6000L);
        if (getConfig().get("mysql.host") == null) getConfig().set("mysql.host", "127.0.0.1");
        if (getConfig().get("mysql.port") == null) getConfig().set("mysql.port", "3306");
        if (getConfig().get("mysql.database") == null) getConfig().set("mysql.database", "database");
        if (getConfig().get("mysql.table") == null) getConfig().set("mysql.table", "table");
        if (getConfig().get("mysql.username") == null) getConfig().set("mysql.username", "username");
        if (getConfig().get("mysql.password") == null) getConfig().set("mysql.password", "password");

        save();
        reload();
    }

    @Override
    public void save() {
        super.save();
    }

    @Override
    public void reload() {
        loadConfig();
        syncInterval = getConfig().getLong("sync_interval");
        mysqlHost = getConfig().getString("mysql.host");
        mysqlPort = getConfig().getString("mysql.port");
        mysqlDatabase = getConfig().getString("mysql.database");
        mysqlTable = getConfig().getString("mysql.table");
        mysqlUsername = getConfig().getString("mysql.username");
        mysqlPassword = getConfig().getString("mysql.password");
    }
}
