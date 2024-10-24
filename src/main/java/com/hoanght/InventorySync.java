package com.hoanght;

import com.hoanght.data.PlayerData;
import com.hoanght.data.config.GlobalConfig;
import com.hoanght.data.sql.MySQLConnect;
import com.hoanght.events.ListenerManagers;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public final class InventorySync extends JavaPlugin {
    @Getter
    private static InventorySync instance;

    @Override
    public void onEnable() {
        instance = this;
        loadConfigs();
        new MySQLConnect().init();
        new ListenerManagers(this);
        new PlayerData(this);
        getLogger().info("InventorySync has been enabled!");
    }

    @Override
    public void onDisable() {
        PlayerData.clearCache();
        getLogger().info("InventorySync has been disabled!");
    }

    private void loadConfigs() {
        GlobalConfig.getInstance().init();
    }

}
