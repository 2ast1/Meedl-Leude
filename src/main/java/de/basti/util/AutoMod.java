package de.basti.util;

import de.basti.main.MeedlLeude;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Level;

public class AutoMod {
    public static void checkMessage(Player player, String message) {
        if(DataManager.getBoolean("automod","settings.yml", "settings.chat.scan")) {

            List<String> badwords = DataManager.getStringList("automod", "badwords.yml", "chat.badwords");
            for (String word : badwords) {
                if (message.toLowerCase().contains(word.toLowerCase())) {

                    int strikes = DataManager.getInt("automod", "playerdata.yml", player.getUniqueId() + ".chat.strikes");
                    int maxStrikes = DataManager.getInt("automod","settings.yml", "settings.chat.strikes.limit");
                    strikes = strikes + 1;

                    //Wenn zu viele Verstöße vorliegen
                    if (strikes >= maxStrikes) {
                        String punishtype = DataManager.getString("automod","settings.yml","settings.chat.strikes.exceeded.type");
                        int punishdays = DataManager.getInt("automod","settings.yml", "settings.chat.strikes.exceeded.duration");

                        switch (punishtype) {
                            case "mute":
                                tempMute(player, addDaysFromToday(punishdays));
                                player.sendMessage("§aAutomod -> §cDu wurdest gemuted.\n§aAutomod -> §cBenutze /automod um Hinweise über deine Strafe zu bekommen.");
                                break;
                            case "ban":
                                stempBan("Automod", player, "§cZu viele Chat Strikes (Automod)", addDaysFromToday(punishdays));
                                break;
                            default:
                        }

                        DataManager.set("automod","playerdata.yml", player.getUniqueId() + ".chat.strikes", 0);

                    }


                    DataManager.set("automod", "playerdata.yml", player.getUniqueId() + ".chat.strikes", strikes);
                    List<String> history = DataManager.getStringList("automod","playerdata.yml",player.getUniqueId() + ".history");
                    String localtime_var = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
                    history.add(localtime_var + ", Flagged Chat Message (by Automod), Content: " + message);
                    DataManager.set("automod","playerdata.yml",player.getUniqueId() + ".history",history);

                    MeedlLeude.getInstance().getLogger().log(Level.WARNING, "AutoMod hat eine Nachricht makiert: [" + player.getName() + "] " + message);
                    break;
                }
            }

        }
    }

    public static void permaBan(CommandSender issuer, Player p, String reason) {
        DataManager.set("automod","playerdata.yml",p.getUniqueId() + ".ban.isbanned", true);
        DataManager.set("automod", "playerdata.yml", p.getUniqueId() + ".ban.end", "permanent");
        DataManager.set("automod", "playerdata.yml",p.getUniqueId() + ".ban.reason", reason);
        List<String> history = DataManager.getStringList("automod","playerdata.yml",p.getUniqueId() + ".history");
        String permatime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        history.add(permatime + ", Ban, for infinity (by " + issuer.getName() + "), Reason: " + reason);
        DataManager.set("automod","playerdata.yml",p.getUniqueId() + ".history",history);
        p.kick(Component.text("§cDu wurdest gebannt.\n\n\nGrund:\n\n" + reason));
    }

    public static void permaBanByUUID(CommandSender issuer, String uuid, String reason) {
        DataManager.set("automod","playerdata.yml",uuid + ".ban.isbanned", true);
        DataManager.set("automod", "playerdata.yml", uuid + ".ban.end", "permanent");
        DataManager.set("automod", "playerdata.yml",uuid + ".ban.reason", reason);

        List<String> history = DataManager.getStringList("automod","playerdata.yml",uuid + ".history");
        String permatime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        history.add(permatime + ", Ban, for infinity (by " + issuer.getName() + "), Reason: " + reason);
        DataManager.set("automod","playerdata.yml",uuid + ".history",history);

        Player target = Bukkit.getPlayer(UUID.fromString(uuid));
        if (target != null) {
            target.kick(Component.text("§cDu wurdest gebannt.\n\n\nGrund:\n\n" + reason));
        }
    }

    public static void tempBan(CommandSender issuer, Player p, String reason, String time) {
        DataManager.set("automod","playerdata.yml", p.getUniqueId() + ".ban.isbanned", true);
        DataManager.set("automod", "playerdata.yml", p.getUniqueId() + ".ban.end", time);
        DataManager.set("automod", "playerdata.yml", p.getUniqueId() + ".ban.reason", reason);
        List<String> history = DataManager.getStringList("automod","playerdata.yml",p.getUniqueId() + ".history");
        String temptime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        history.add(temptime + ", Ban, until " + time + " (by " + issuer.getName() + "), Reason: " + reason);
        DataManager.set("automod", "playerdata.yml", p.getUniqueId() + ".history", history);
        p.kick(Component.text("§cDu wurdest temporär gebannt.\nEnde: " + time + "\n\nGrund:\n\n" + reason));
    }

    public static void tempBanByUUID(CommandSender sender, String uuid, String reason, String time) {
        DataManager.set("automod","playerdata.yml", uuid + ".ban.isbanned", true);
        DataManager.set("automod", "playerdata.yml", uuid + ".ban.end", time);
        DataManager.set("automod", "playerdata.yml", uuid + ".ban.reason", reason);
        List<String> history = DataManager.getStringList("automod","playerdata.yml",uuid + ".history");
        String temptime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        history.add(temptime + ", Ban, until " + time + " (by " + sender.getName() + "), Reason: " + reason);
        DataManager.set("automod", "playerdata.yml", uuid + ".history", history);

        Player target = Bukkit.getPlayer(UUID.fromString(uuid));
        if (target != null) {
            target.kick(Component.text("§cDu wurdest temporär gebannt.\nEnde: " + time + "\n\nGrund:\n\n" + reason));
        }
    }

    public static void stempBan(String issuer, Player p, String reason, String time) {
        DataManager.set("automod","playerdata.yml", p.getUniqueId() + ".ban.isbanned", true);
        DataManager.set("automod", "playerdata.yml", p.getUniqueId() + ".ban.end", time);
        DataManager.set("automod", "playerdata.yml", p.getUniqueId() + ".ban.reason", reason);
        List<String> history = DataManager.getStringList("automod","playerdata.yml",p.getUniqueId() + ".history");
        String temptime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        history.add(temptime + ", Ban, until " + time + " (by " + issuer + "), Reason: " + reason);
        DataManager.set("automod", "playerdata.yml", p.getUniqueId() + ".history", history);
        p.kick(Component.text("§cDu wurdest temporär gebannt.\nEnde: " + time + "\n\nGrund:\n\n" + reason));
    }

    public static void unban(CommandSender sender, String uuid, String targetname) {
        DataManager.set("automod", "playerdata.yml", uuid + ".ban.isbanned", false);
        DataManager.delete("automod", "playerdata.yml", uuid + ".ban.end");
        DataManager.delete("automod", "playerdata.yml", uuid + ".ban.reason");
        sender.sendMessage("§aDu hast " + targetname + " erfolgreich entbannt.");
    }

    public static void sUnban(String uuid) {
        DataManager.set("automod", "playerdata.yml", uuid + ".ban.isbanned", false);
        DataManager.delete("automod", "playerdata.yml", uuid + ".ban.end");
        DataManager.delete("automod", "playerdata.yml", uuid + ".ban.reason");
    }

    public static void tempMute(Player player, String end) {
        DataManager.set("automod","playerdata.yml",player.getUniqueId() + ".name", player.getName());
        DataManager.set("automod","playerdata.yml",player.getUniqueId() + ".mute.ismuted", true);
        DataManager.set("automod","playerdata.yml",player.getUniqueId() + ".mute.end", end);
        List<String> history = DataManager.getStringList("automod","playerdata.yml",player.getUniqueId() + ".history");
        String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        history.add(time + ", Mute, ends at " + end);
        DataManager.set("automod","playerdata.yml",player.getUniqueId() + ".history",history);
    }

    // Methode: Nimmt Ausgangsdatum als String + Tage als int, gibt das neue Datum zurück
    public static String addDaysToDate(String dateStr, int daysToAdd) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate date = LocalDate.parse(dateStr, formatter);
        LocalDate futureDate = date.plusDays(daysToAdd);
        return futureDate.format(formatter);
    }

    // Methode: Nimmt nur die Anzahl Tage, gibt neues Datum ab heute zurück
    public static String addDaysFromToday(int daysToAdd) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate today = LocalDate.now();           // heutiges Datum
        LocalDate futureDate = today.plusDays(daysToAdd);
        return futureDate.format(formatter);
    }

}