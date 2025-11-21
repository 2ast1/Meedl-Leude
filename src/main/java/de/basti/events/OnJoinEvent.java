package de.basti.events;

import de.basti.util.DataManager;
import de.basti.util.TablistPrefixManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class OnJoinEvent implements Listener {

    @EventHandler
    public void onJoinEvent(PlayerJoinEvent event) {
        Player p = event.getPlayer();

        DataManager.set("","playerdata.yml",p.getName() + ".uuid", p.getUniqueId().toString());

        boolean isbanned = DataManager.getBoolean("automod", "playerdata.yml",p.getUniqueId() + ".ban.isbanned");
        String banDate = DataManager.getString("automod", "playerdata.yml",p.getUniqueId() + ".ban.end");

        if(isbanned) {
            if (Objects.equals(DataManager.getString("automod", "playerdata.yml", p.getUniqueId() + ".ban.end"), "permanent")) {
                p.kick(Component.text("§cDu bist Permanent gebannt!\n\n\nGrund:\n\n" + DataManager.getString("automod","playerdata.yml",p.getUniqueId() + ".ban.reason")));
            } else {
                if(isTodayOrPast(banDate)) {
                    DataManager.delete("automod", "playerdata.yml",p.getUniqueId() + ".ban.isbanned");
                    DataManager.delete("automod", "playerdata.yml",p.getUniqueId() + ".ban.end");

                    p.sendMessage("§aWillkommen zurück, du bist wieder entbannt.");

                } else {
                    p.kick(Component.text("§cDu bist bis zum " + banDate + " gebannt!\n\n\nGrund:\n\n" + DataManager.getString("automod","playerdata.yml",p.getUniqueId() + ".ban.reason")));
                }
            }
        }

        TablistPrefixManager.setTabStatus(p);

    }

    public boolean isTodayOrPast(String dateString) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        LocalDate input = LocalDate.parse(dateString, fmt);
        LocalDate today = LocalDate.now();

        return !input.isAfter(today); // true wenn heute oder früher
    }

}
