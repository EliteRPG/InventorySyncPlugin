package com.hoanght.data.sql;

import com.hoanght.data.config.GlobalConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.Statement;

public class MySQLConnect {
    @Getter
    private static HikariDataSource dataSource;
    private GlobalConfig ins;

    public void init() {
        ins = GlobalConfig.getInstance();
        HikariConfig config = getHikariConfig();
        dataSource = new HikariDataSource(config);

        try (Connection connection = dataSource.getConnection(); Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS " + ins.getMysqlTable() +
                                            " (uuid VARCHAR(36) PRIMARY KEY, player_name VARCHAR(16), data TEXT)");
        } catch (Exception e) {
            Bukkit.getLogger().warning("Failed to create table " + ins.getMysqlTable());
        }
    }

    private HikariConfig getHikariConfig() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(
                "jdbc:mysql://" + ins.getMysqlHost() + ":" + ins.getMysqlPort() + "/" + ins.getMysqlDatabase());
        config.setUsername(ins.getMysqlUsername());
        config.setPassword(ins.getMysqlPassword());
        config.setMaximumPoolSize(10);
        config.setConnectionTimeout(30000);
        config.setLeakDetectionThreshold(60000);
        config.setPoolName("HikariCP");
        return config;
    }

    public static Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
