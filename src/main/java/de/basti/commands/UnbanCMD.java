package de.basti.commands;

import de.basti.util.AutoMod;
import de.basti.util.DataManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class UnbanCMD implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {

        if(!sender.hasPermission("meedlleude.unban")) {
            sender.sendMessage("§cDu hast keine Berechtigung dazu!");
            return true;
        }

        if(!(args.length == 1)) {
           sender.sendMessage("§cBitte benutze §9/unban (playername)");
           return true;
        }

        String uuidfromfiles = DataManager.getString("","playerdata.yml",args[0] + ".uuid");

        if(uuidfromfiles == null) {
            sender.sendMessage("§cDieser Spieler war noch nie auf diesem Server.\n\n§aFalls du ihn troztdem entbannen willst\n-> Automod manuell überschreiben via Datein");
            return true;
        }

        if(!DataManager.getBoolean("automod", "playerdata.yml",uuidfromfiles + ".ban.isbanned")) {
            sender.sendMessage("§cDieser Spieler ist aktuell nicht gebannt.");
            return true;
        }

        AutoMod.unban(sender, uuidfromfiles, args[0]);

        return true;
    }

}
