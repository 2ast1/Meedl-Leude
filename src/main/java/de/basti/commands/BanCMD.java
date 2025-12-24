package de.basti.commands;

import de.basti.util.AutoMod;
import de.basti.util.DataManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;

public class BanCMD implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(!sender.hasPermission("meedlleude.ban")) {
            sender.sendMessage("§cDu hast keine Berechtigung dazu.");
            return true;
        }

        if(args.length < 3) {
            sender.sendMessage("§cVerwendung: /ban (playername) (DD-MM-YYYY oder perma)");
            return true;
        }

        String uuidfromfiles = DataManager.getString("","playerdata.yml",args[0] + ".uuid");

        if(uuidfromfiles == null) {
            sender.sendMessage("§cDieser Spieler war noch nie auf diesem Server.\n\n§aFalls du ihn troztdem bannen willst\n-> Automod manuell überschreiben via Datein");
            return true;
        }

        String timeArg = args[1];
        String reason = String.join(" ", Arrays.copyOfRange(args,2, args.length));

        if(timeArg.equalsIgnoreCase("perma")) {
            AutoMod.permaBanByUUID(sender, uuidfromfiles, reason);
            sender.sendMessage("§aDu hast die UUID " + uuidfromfiles + " (" + args[0] + ") erfolgreich gebannt!");
        } else {

            if(!isValidDate(timeArg)) {
                sender.sendMessage("§cUngültiges Datum! Format: DD-MM-YYYY (z.B. 24-12-2025)");
                return true;
            }

            if(isTodayOrPast(timeArg)) {
                sender.sendMessage("§cDas Datum muss in der Zukunft liegen!");
                return true;
            }

            AutoMod.tempBanByUUID(sender, uuidfromfiles, reason, timeArg);
            sender.sendMessage("§aDu hast erfolgreich " + args[0] + " temporär gebannt.");

        }

        return true;
    }

    public boolean isValidDate(String input) {
        try {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate.parse(input, fmt);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public boolean isTodayOrPast(String dateString) {
        try {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate input = LocalDate.parse(dateString, fmt);
            LocalDate today = LocalDate.now();
            return !input.isAfter(today);
        } catch (DateTimeParseException e) {
            return true; // Im Zweifel als "ungültig/vergangen" behandeln
        }
    }
}