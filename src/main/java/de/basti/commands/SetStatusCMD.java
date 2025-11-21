package de.basti.commands;

import de.basti.main.MeedlLeude;
import de.basti.util.DataManager;
import de.basti.util.TablistPrefixManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import javax.xml.crypto.Data;

public class SetStatusCMD implements CommandExecutor {

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
        int maxStatusLenght = DataManager.getInt("","config.yml","settings.status.maxlength");

        if (args.length == 0) {
            // Command hat KEINE Argumente
            p.sendMessage("§cGebe Argumente an! §9Beispiel: /setstatus (Status)");
        } else {
            // Command hat Argumente
            String playerorstatus = args[0];

            Player target = Bukkit.getPlayerExact(playerorstatus);

            if (target != null) {
                if (p.hasPermission("meedlleude.setstatus")) {

                    if (args.length != 1) {

                        if(args[1].length() > maxStatusLenght) {
                            p.sendMessage("§aDer Status ist zu lang! Maximal erlaubt: " + maxStatusLenght + " Zeichen.");
                            return true;
                        } else {
                            DataManager.set("", "playerdata.yml", target.getUniqueId() + ".name", target.getName());
                            DataManager.set("", "playerdata.yml", target.getUniqueId() + ".status", args[1]);
                            TablistPrefixManager.setTabStatus(target);
                            p.sendMessage("§aDu hast den Status von " + target.getName() + " zu '§r" + args[1].replace("&", "§") + "§r§a' geändert.");

                        }

                        return true;
                    } else {
                        p.sendMessage("§cGebe Argumente an! §9Beispiel: /setstatus (Spieler) (Status)");
                    }

                    return true;
                } else {
                    p.sendMessage("§cDu hast keine Berechtigung dazu.");
                }
            } else {

                if(args[0].length() > maxStatusLenght) {
                    p.sendMessage("§cDer Status ist zu lang! Maximal erlaubt: " + maxStatusLenght + " Zeichen.");
                    return true;
                } else {
                    DataManager.set("", "playerdata.yml", p.getUniqueId() + ".name", p.getName());
                    DataManager.set("", "playerdata.yml", p.getUniqueId() + ".status", args[0]);
                    TablistPrefixManager.setTabStatus(p);
                    p.sendMessage("§aDu hast deinen Status zu '§r" + args[0].replace("&", "§") + "§r§a' geändert.");
                }

            }
        }
        return true;
    }
}
