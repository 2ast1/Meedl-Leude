package de.basti.commands;

import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BastardCMD implements CommandExecutor {

    @Override
    public boolean onCommand(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String label,
            @NotNull String[] args) {

        sender.sendMessage("Hurensohn");

        if ((sender instanceof Player)) {
            Player p = (Player) sender;
            p.kick(Component.text("§cDu wurdest gebannt!"));
        }

        return true;
    }
}
