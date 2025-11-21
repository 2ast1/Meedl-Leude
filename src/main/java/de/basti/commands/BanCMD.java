package de.basti.commands;

import de.basti.util.DataManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import javax.xml.crypto.Data;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class BanCMD implements CommandExecutor {

    @Override
    public boolean onCommand(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String label,
            @NotNull String[] args) {

        if (!sender.hasPermission("meedlleude.ban")) {
            sender.sendMessage("§cDu hast keine Berechtigung dazu");
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[0]);
        if(target == null) {
            sender.sendMessage("§cDas ist kein Spieler der Online ist.");
            return true;
        }

        if (args.length == 2) {
            //PERMABANN
            permaBan(sender, target, args[1]);
            sender.sendMessage("§aDu hast gerade " + target.getName() + " für immer vom Server gebannt!");
            Bukkit.broadcast(Component.text("§a" + target.getName() + " wurde gerade für immer vom Server gebannt."));
        } else {
            //Temorärer Bann
            if (args.length == 3) {
                // /ban (playername) (reason) (time)

                if(!isValidDate(args[2])) {
                    sender.sendMessage("§cDas ist kein valides Datum, Benötigtes Format: DD-MM-YYYY\n" +
                            "Beispiel: 22-12-2025, 04-02-2009");
                    return true;
                }

                if(isTodayOrPast(args[2])) {
                    sender.sendMessage("§cMit diesem Datum währe der Spieler schon wieder entbannt.");
                    return true;
                }

                tempBan(sender, target, args[1], args[2]);
                sender.sendMessage("§aDu hast gerade " + target.getName() + " bis zum " + args[2] + " gebannt.");
            } else {
                sender.sendMessage("§cBitte benutze den Command wie folgt:\n" +
                        "§aPermabann: §9/ban (playername) (reason)\n" +
                        "§aTemp. Bann: §9/ban (playername) (reason) (date, dd-mm-yyyy)");
                return true;
            }
        }

        return true;
    }

    public void permaBan(CommandSender issuer, Player p, String reason) {
        DataManager.set("automod","playerdata.yml",p.getUniqueId() + ".ban.isbanned", true);
        DataManager.set("automod", "playerdata.yml", p.getUniqueId() + ".ban.end", "permanent");
        DataManager.set("automod", "playerdata.yml",p.getUniqueId() + ".ban.reason", reason);
        List<String> history = DataManager.getStringList("automod","playerdata.yml",p.getUniqueId() + ".history");
        String permatime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        history.add(permatime + ", Ban, for infinity (by " + issuer.getName() + "), Reason: " + reason);
        DataManager.set("automod","playerdata.yml",p.getUniqueId() + ".history",history);
        p.kick(Component.text("§cDu wurdest gebannt.\n\n\nGrund:\n\n" + reason));
    }

    public void tempBan(CommandSender issuer, Player p, String reason, String time) {
        DataManager.set("automod","playerdata.yml", p.getUniqueId() + ".ban.isbanned", true);
        DataManager.set("automod", "playerdata.yml", p.getUniqueId() + ".ban.end", time);
        DataManager.set("automod", "playerdata.yml", p.getUniqueId() + ".ban.reason", reason);
        List<String> history = DataManager.getStringList("automod","playerdata.yml",p.getUniqueId() + ".history");
        String temptime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        history.add(temptime + ", Ban, until " + time + " (by " + issuer.getName() + "), Reason: " + reason);
        DataManager.set("automod", "playerdata.yml", p.getUniqueId() + ".history", history);
        p.kick(Component.text("§cDu wurdest temporär gebannt.\nEnde: " + time + "\n\nGrund:\n\n" + reason));
    }

    public boolean isValidDate(String input) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        try {
            LocalDate.parse(input, fmt); // versucht das Datum zu parsen
            return true;                 // gültig
        } catch (DateTimeParseException e) {
            return false;                // ungültig
        }
    }

    public boolean isTodayOrPast(String dateString) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        LocalDate input = LocalDate.parse(dateString, fmt);
        LocalDate today = LocalDate.now();

        return !input.isAfter(today); // true wenn heute oder früher
    }

}
