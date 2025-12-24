package de.basti.commands;

import de.basti.util.DataManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class UnmuteCMD implements CommandExecutor {

    @Override
    public boolean onCommand(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String label,
            @NotNull String[] args) {

        if(!sender.hasPermission("meedlleude.unmute")) {
            sender.sendMessage("§cDu hast keine Berechtigung dazu");
            return true;
        }

        if(!(args.length == 1)) {
           sender.sendMessage("§cBitte benutze §9/unmute (playername)");
           return true;
        }

        Player target = Bukkit.getPlayerExact(args[0]);

        if(target == null) {
            sender.sendMessage("§cDieser Spieler ist nicht Online.");
            return true;
        }

        if(!DataManager.getBoolean("automod","playerdata.yml",target.getUniqueId() + ".mute.ismuted")) {
            sender.sendMessage("§cDieser Spieler ist nicht gestummt.");
            return true;
        }

        DataManager.set("automod","playerdata.yml",target.getUniqueId() + ".mute.ismuted", false);
        DataManager.set("automod","playerdata.yml",target.getUniqueId() + ".chat.strikes", 0);

        if (sender == target) {
            sender.sendMessage("§aDu hast dich wieder für den Chat freigegeben.");
        } else {
            sender.sendMessage("§aDu hast " + target.getName() + " unmuted.");
            target.sendMessage("§aDu wurdest wieder für den Chat freigegeben.");
        }

        return true;
    }
}
