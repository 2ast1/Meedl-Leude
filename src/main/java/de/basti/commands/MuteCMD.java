package de.basti.commands;

import de.basti.util.DataManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class MuteCMD implements CommandExecutor {

    @Override
    public boolean onCommand(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String label,
            @NotNull String[] args) {

        if(sender.hasPermission("meedlleude.mute")) {

            if (args.length == 0) {
                sender.sendMessage("§cBitte benutze §9/mute (playername) (end date)\n§cBeispiel:\n§9/mute PenisHunter33 31-12-2025\n§coder\n§9/mute PenisHunter33");
            } else {
                if(args.length == 1) {
                    Player target = Bukkit.getPlayerExact(args[0]);

                    if (target != null) {
                        DataManager.set("automod","playerdata.yml",target.getUniqueId() + ".name", target.getName());
                        DataManager.set("automod","playerdata.yml",target.getUniqueId() + ".mute.ismuted", true);
                        DataManager.set("automod","playerdata.yml",target.getUniqueId() + ".mute.end", "permanent");
                        List<String> history = DataManager.getStringList("automod","playerdata.yml",target.getUniqueId() + ".history");
                        String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
                        history.add(time + ", Mute, for infinity");
                        DataManager.set("automod","playerdata.yml",target.getUniqueId() + ".history",history);
                        sender.sendMessage("§aDu hast " + target.getName() + " erfolgreich Stummgeschalten.");
                        target.sendMessage("§cDu wurdest gerade von " + sender.getName() + " gemuted.");
                    } else {
                        sender.sendMessage("§cDas ist kein Spieler der Online ist.");
                    }
                } else {
                    Player target = Bukkit.getPlayerExact(args[0]);

                    if (target != null) {

                        if (isValidDate(args[1])) {

                            if (isTodayOrPast(args[1])) {
                                sender.sendMessage("§cMit diesem Datum wäre er schon wieder Entstummt.");
                            } else {
                                DataManager.set("automod","playerdata.yml",target.getUniqueId() + ".name", target.getName());
                                DataManager.set("automod","playerdata.yml",target.getUniqueId() + ".mute.ismuted", true);
                                DataManager.set("automod","playerdata.yml",target.getUniqueId() + ".mute.end", args[1]);
                                List<String> history = DataManager.getStringList("automod","playerdata.yml",target.getUniqueId() + ".history");
                                String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
                                history.add(time + ", Mute, ends at " + args[1]);
                                DataManager.set("automod","playerdata.yml",target.getUniqueId() + ".history",history);
                                sender.sendMessage("§aDu hast " + target.getName() + " erfolgreich Stummgeschalten.");
                                target.sendMessage("§cDu wurdest gerade von " + sender.getName() + " gemuted.");

                            }
                        } else {
                            sender.sendMessage("§cBitte benutze §9/mute (playername) (end date)\n§cBeispiel:\n§9/mute PenisHunter33 31-12-2025 \n§coder\n§9/mute PenisHunter33");
                        }

                    } else {
                        sender.sendMessage("§cDas ist kein Spieler der Online ist.");
                    }
                }

            }
        } else {
            sender.sendMessage("§cDu hast keine Berechtigung dazu");
        }
        return true;
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