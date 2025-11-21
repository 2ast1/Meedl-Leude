package de.basti.commands;

import de.basti.util.DataManager;
import de.basti.util.TablistPrefixManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class RemoveStatusCMD implements CommandExecutor {

    @Override
    public boolean onCommand(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String label,
            @NotNull String[] args) {

        if (!(sender instanceof Player p)) {
            sender.sendMessage("Dieser Command kann nur von Spielern ausgeführt werden.");
            return true;
        }

        DataManager.delete("","playerdata.yml",p.getUniqueId() + ".status");
        TablistPrefixManager.clearTabPrefix(p);

        p.sendMessage("§aDu hast deinen alten Status gelöscht.");

        return true;
    }
}
