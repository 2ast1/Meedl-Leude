package de.basti.commands;

import de.basti.util.DataManager;
import de.basti.util.TablistPrefixManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public class InventoryCMD implements CommandExecutor {

    @Override
    public boolean onCommand(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String label,
            @NotNull String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("Dieser Command kann nur von Spielern ausgeführt werden.");
            return true;
        }

        Player p = (Player) sender;

        if(p.hasPermission("meedlleude.inventory")) {
            if (args.length == 0) {
                p.sendMessage("§cBitte benutze den Command wie folgt: §9/inventory (player)");
            } else {
                Player target = Bukkit.getPlayerExact(args[0]);

                if (target == null) {
                    p.sendMessage("§cDiesen Spieler gibt es nicht!");
                }  else {
                    Inventory inv = target.getInventory();
                    p.openInventory(inv);
                    p.sendMessage("§aDu hast das Inventar von " + target.getName() + " geöffnet.");
                }
            }
        } else {
            p.sendMessage("§cDu hast keine Berechtigung dazu.");
        }

        return true;
    }
}