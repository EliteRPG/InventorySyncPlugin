package com.hoanght.data;

import com.hoanght.InventorySync;
import com.hoanght.data.config.GlobalConfig;
import com.hoanght.data.sql.MySQLConnect;
import com.hoanght.utils.ItemStackSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerData {
    public static ItemStackSerializer itemStackSerializer;
    private static final Map<UUID, String> cache = new HashMap<>();
    private final GlobalConfig config;
    private Connection connection;

    public PlayerData(InventorySync plugin) {
        PlayerData.itemStackSerializer = new ItemStackSerializer();
        this.config = GlobalConfig.getInstance();

        new BukkitRunnable() {
            @Override
            public void run() {
                if (Bukkit.getOnlinePlayers().isEmpty()) return;
                connection = MySQLConnect.getConnection();
                assert connection != null;

                long startTime = System.currentTimeMillis();
                plugin.getLogger().info("Syncing player data...");

                for (Player player : Bukkit.getOnlinePlayers()) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            syncInventory(player);
                            Bukkit.getLogger().info("[Debug] Synced player data for " + player.getName());
                        }
                    }.runTaskTimerAsynchronously(plugin, 0, 20L);
                }

                long endTime = System.currentTimeMillis();
                long duration = endTime - startTime;
                plugin.getLogger().info("Player data synced! Took " + duration + "ms");
            }
        }.runTaskTimerAsynchronously(plugin, 0, config.getSyncInterval());
    }

    public void addCache(Player player, String data) {
        cache.put(player.getUniqueId(), data);
    }

    public String getCache(Player player) {
        return cache.get(player.getUniqueId());
    }

    public boolean hasCache(Player player) {
        return cache.containsKey(player.getUniqueId());
    }

    public static void removeCache(Player player) {
        cache.remove(player.getUniqueId());
    }

    public static void clearCache() {
        cache.clear();
    }

    private void syncInventory(Player player) {
        String encode = itemStackSerializer.encode(player.getInventory().getContents());
        if (hasCache(player)) {
            String data = getCache(player);
            if (data.equals(encode)) return;
            saveToSQL(player, encode);
            addCache(player, encode);
            return;
        }
        saveToSQL(player, encode);
        addCache(player, encode);
    }

    private void saveToSQL(Player player, String data) {
        UUID uuid = player.getUniqueId();
        String playerName = player.getName();

        String table = config.getMysqlTable();
        String query =
                "INSERT INTO " + table + " (uuid, player_name, data) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE data = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, uuid.toString());
            statement.setString(2, playerName);
            statement.setString(3, data);
            statement.setString(4, data);
            statement.executeUpdate();
        } catch (Exception e) {
            Bukkit.getLogger().warning("[Error] Failed to save player data to MySQL database!");
        }
    }
}
