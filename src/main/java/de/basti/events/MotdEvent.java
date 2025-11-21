package de.basti.events;

import de.basti.util.DataManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import java.time.LocalTime;

public class MotdEvent implements Listener {

    @EventHandler
    public void onServerPing(ServerListPingEvent event) {

        if (DataManager.getBoolean("","config.yml","settings.server.motd.change")) {
            LocalTime now = LocalTime.now();
            String hh = String.format("%02d", now.getHour());
            String mm = String.format("%02d", now.getMinute());
            String ss = String.format("%02d", now.getSecond());

            Runtime runtime = Runtime.getRuntime();

            long maxMemory = runtime.maxMemory();         // max möglicher Heap
            long totalMemory = runtime.totalMemory();     // aktuell reserviert
            long freeMemory = runtime.freeMemory();       // frei innerhalb total
            long usedMemory = totalMemory - freeMemory;   // benutzt

            // In MB umrechnen
            long maxMB = maxMemory / 1024 / 1024;
            long freeMB = (maxMemory - usedMemory) / 1024 / 1024; // frei relativ zu max
            long usedMB = usedMemory / 1024 / 1024;


            String motd = DataManager.getString("","config.yml","settings.server.motd.value")
                    .replace("{hh}", hh + "&r")
                    .replace("{mm}", mm + "&r")
                    .replace("{ss}", ss + "&r")
                    .replace("{version}", Bukkit.getServer().getVersion() + "&r")
                    .replace("{mcversion}", Bukkit.getServer().getMinecraftVersion() + "&r")
                    .replace("{bukkitversion}", Bukkit.getServer().getBukkitVersion() + "&r")
                    .replace("{maxmb}", maxMB + "&r")
                    .replace("{freemb}", freeMB + "&r")
                    .replace("{usedmb}", usedMB + "&r")
                    .replace("{slots}", Bukkit.getServer().getMaxPlayers() + "&r")
                    .replace("{onlineplayers}", Bukkit.getServer().getOnlinePlayers().size() + "&r")
                    .replace("{servername}", Bukkit.getServer().getName() + "&r");

            Component comp = LegacyComponentSerializer.legacyAmpersand().deserialize(motd);
            event.motd(comp);
        }

    }

}
