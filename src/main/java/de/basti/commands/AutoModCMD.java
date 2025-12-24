package de.basti.commands;

import de.basti.util.DataManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AutoModCMD implements CommandExecutor {

    @Override
    public boolean onCommand(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String label,
            @NotNull String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("Dieser Befehl kann aus Verwaltungsgründen nur von Spieler ausgeführt werden.");
            return true;
        }

        if (args.length == 0) {
            ownStats((Player) sender);
            return true;
        }

        if (args.length == 1) {
            if(!sender.hasPermission("meedlleude.automod.stats")) {
                sender.sendMessage("§cDu hast keine Berechtigung dazu.");
                return true;
            }

            Player target = Bukkit.getPlayerExact(args[0]);

            if(target == null) {
                sender.sendMessage("§cDieser Spieler existierd nicht.");
                return true;
            }

            elseStats((Player) sender, target);
            return true;
        }

        return true;
    }

    private void ownStats(Player sender) {
        List<String> historycontent = DataManager.getStringList("automod","playerdata.yml",sender.getUniqueId() + ".history");
        int strikes = DataManager.getInt("automod","playerdata.yml", sender.getUniqueId() + ".chat.strikes");
        boolean ismuted = DataManager.getBoolean("automod", "playerdata.yml", sender.getUniqueId() + ".mute.ismuted");
        String muteend = DataManager.getString("automod", "playerdata.yml", sender.getUniqueId() + ".mute.end");
        String[] s = getLastFive(historycontent);

        String mutedGetter;
        if(ismuted) {
            mutedGetter = "§2Momentan bist du für den Chat gesperrt (" + muteend + ").";
        } else {
            mutedGetter = "§2Momentan hast du keine Chat sperre";
        }

        sender.sendMessage( "§aAutomod -> §2Du hast insgesamt §6" + historycontent.size() + " §2Einträge.\n" +
                            "§aAutomod -> §2Davon Strikes die auf den Chat bezogen sind -> §6" + strikes + "\n" +
                            "§aAutomod -> " + mutedGetter + "\n" +
                            "§aAutomod -> §2Deine letzten fünf Einträge:");

        for (int i = 0; i < s.length; i++) {
            sender.sendMessage("§aAutomod -> §2" + (i + 1) + ": " + s[i]);
        }


    }

    private void elseStats(Player sender, Player target) {

    }

    public static String[] getLastFive(List<String> list) {
        int size = Math.min(list.size(), 5); // max. 5 oder weniger, wenn Liste kleiner
        return list.subList(list.size() - size, list.size())
                .toArray(new String[0]);
    }

}
