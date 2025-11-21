package de.basti.commands;

import de.basti.util.DataManager;
import de.basti.util.TablistPrefixManager;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class StatusCMD implements CommandExecutor {

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

        // Pfad in der YML
        String path = p.getUniqueId() + ".status";

        // Wert aus der Datei holen
        String rawStatus = DataManager.getString("", "playerdata.yml", path);

        // Wenn kein Status gesetzt ist
        if (rawStatus == null) {
            p.sendMessage("§cDu hast noch keinen Status gesetzt.");
            return true;
        }

        // &-Farbcodes zu § machen (falls du sie so speicherst)
        String coloredStatus = rawStatus.replace("&", "§");

        p.sendMessage("§aDein aktueller Status ist '§r" + coloredStatus + "§a'");
        TablistPrefixManager.setTabStatus(p);

        return true;
    }
}
