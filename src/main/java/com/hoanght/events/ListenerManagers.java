package com.hoanght.events;

import com.hoanght.InventorySync;
import org.bukkit.event.Listener;

import java.util.List;

public class ListenerManagers {
    private final InventorySync plugin;

    public ListenerManagers(InventorySync plugin) {
        this.plugin = plugin;
        List<Listener> events = List.of(
                new PlayerQuit()
                                       );
        events.forEach(this::registerEvents);
    }

    public void registerEvents(Listener event) {
        plugin.getServer().getPluginManager().registerEvents(event, plugin);
    }
}
