package de.basti.events;

import de.basti.main.MeedlLeude;
import de.basti.util.AutoMod;
import de.basti.util.DataManager;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class NewChatEvent implements Listener {

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        event.setCancelled(true);

        Player p = event.getPlayer();

        Boolean isMuted = DataManager.getBoolean("automod", "playerdata.yml",p.getUniqueId() + ".mute.ismuted");

        if (isMuted) {
           if (!Objects.equals(DataManager.getString("automod", "playerdata.yml", p.getUniqueId() + ".mute.end"), "permanent")) {
               if (isTodayOrPast(DataManager.getString("automod", "playerdata.yml", p.getUniqueId() + ".mute.end"))) {
                   sendMSG(p, event);
                   DataManager.set("automod","playerdata.yml",p.getUniqueId() + ".mute.ismuted", false);
                   DataManager.delete("automod","playerdata.yml", p.getUniqueId() + ".mute.ismuted");
               } else {
                   p.sendMessage("§cDu bist gemuted.");
               }
           } else {
                p.sendMessage("§cDu bist gemuted.");
           }
        } else {
            sendMSG(p, event);
        }
    }

    public void sendMSG(Player p, AsyncChatEvent event) {

        String coloredStatus;
        String sandboxms;

        if(DataManager.checkIfExist("","config.yml","settings.status.chatmsg")) {
            sandboxms = DataManager.getString("","config.yml","settings.status.chatmsg");
        } else {
            sandboxms = "&r[{status}&r] {playername} | {msg}";
        }



        // Pfad in der YML
        String path = p.getUniqueId() + ".status";

        // Wert aus der Datei holen
        String rawStatus = DataManager.getString("", "playerdata.yml", path);

        // Wenn Status gesetzt ist
        if (rawStatus != null) {
            // &-Farbcodes zu § machen (falls du sie so speicherst)
            coloredStatus = rawStatus.replace("&", "§");
        } else {
            coloredStatus = "";

            if(DataManager.checkIfExist("","config.yml","settings.status.notset.overwrite")) {
                if(DataManager.getBoolean("","config.yml","settings.status.notset.overwrite")) {
                    if(DataManager.checkIfExist("","config.yml","settings.status.notset.msg")) {
                        sandboxms = DataManager.getString("","config.yml","settings.status.notset.msg");
                    } else {
                        sandboxms = "{playername} | {msg}";
                    }
                }
            }

        }

        World.Environment env = p.getWorld().getEnvironment();
        String worldType;

        switch (env) {
            case NORMAL -> worldType = "Overworld";
            case NETHER -> worldType = "Nether";
            case THE_END -> worldType = "End";
            default -> worldType = "Unknown";
        }

        LocalTime now = LocalTime.now();

        String hh = String.format("%02d", now.getHour());
        String mm = String.format("%02d", now.getMinute());
        String ss = String.format("%02d", now.getSecond());

        String realmsg;

        boolean ischatcolorful = DataManager.getBoolean("","config.yml","settings.chat.colorful");

        if (ischatcolorful) {
            realmsg = PlainTextComponentSerializer.plainText().serialize(event.message()).replace("&","§");
        } else {
            realmsg = PlainTextComponentSerializer.plainText().serialize(event.message());
        }



        sandboxms = sandboxms
                .replace("&","§")
                .replace("{status}",coloredStatus + "§r")
                .replace("{playername}",event.getPlayer().getName() + "§r")
                .replace("{msg}",realmsg + "§r")
                .replace("{world}",event.getPlayer().getLocation().getWorld().getName() + "§r")
                .replace("{worldtype}",worldType + "§r")
                .replace("{hh}", hh + "§r")
                .replace("{mm}", mm + "§r")
                .replace("{ss}", ss + "§r");

        String finalSandboxms = sandboxms;
        AutoMod.checkMessage(p, realmsg);

        Bukkit.getScheduler().runTask(MeedlLeude.getInstance(), () -> {
            Component comp = net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
                    .legacySection()
                    .deserialize(finalSandboxms);

            Bukkit.broadcast(comp);
        });

    }

    public boolean isTodayOrPast(String dateString) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        LocalDate input = LocalDate.parse(dateString, fmt);
        LocalDate today = LocalDate.now();

        return !input.isAfter(today); // true wenn heute oder früher
    }

}
