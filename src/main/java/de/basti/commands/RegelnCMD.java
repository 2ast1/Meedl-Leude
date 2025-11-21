package de.basti.commands;

import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RegelnCMD implements CommandExecutor {

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

        // Titel und Autor
        Component title = Component.text("Server-Regeln")
                .color(NamedTextColor.GOLD)
                .decorate(TextDecoration.BOLD);

        Component author = Component.text("DeinServer");

        // Seite 1 – hier kannst du deine Zahlen/Buchstaben/Regeln reinballern
        Component page1 = Component.text()
                .append(Component.text("§5§l§nRegelwerk§r\n\n")) // oder Adventure-Style formatieren
                .append(Component.text("§0§l1. Aktion & Reaktion§r\n\n"))
                .append(Component.text("-Alles fällt unter\n"))
                .append(Component.text("-> Aktion = Reaktion\n\n"))
                .append(Component.text("- Wer austeilt, muss einstecken können.\n\n"))
                .append(Component.text("- Wer fair spielt, wird fair behandelt."))
                .build();

        // Seite 2 – Beispiel mit Codes
        Component page2 = Component.text()
                .append(Component.text("§0§l2. Safe-Zones§r\n\n"))
                .append(Component.text("- Safe-Zones werden vom Spieler festgelegt.\n\n"))
                .append(Component.text("- Safe-Zones werden in der Whatsapp-Gruppe bekannt gegeben.\n\n"))
                .append(Component.text("- PvP, Trolling, Griefing ist untersagt in den Zonen."))
                .build();

        Component page3 = Component.text()
                .append(Component.text("§0§l3. Grundstücke§r\n\n"))
                .append(Component.text("- Ein Grundstück gilt als geclaimt, sobald es mit einem Schild makiert ist.\n\n"))
                .append(Component.text("- Du darfst an der Grenze von einem Grundstück bauen.\n\n"))
                .append(Component.text("- Verändere das Grund anderer nicht."))
                .build();

        Component page4 = Component.text()
                .append(Component.text("§0§l4. Fairplay§r\n\n"))
                .append(Component.text("- Spaß ist erlaubt, Respekt ist Pflicht.\n\n"))
                .append(Component.text("- Wenn jemand dich bittet zu Stoppen, dann stoppe.\n\n"))
                .build();

        Component page5 = Component.text()
                .append(Component.text("§0§l5. PvP, Crystal-PvP§r\n\n"))
                .append(Component.text("- PvP ist erlaubt, solang keine Safe-Zones betroffen sind.\n\n"))
                .append(Component.text("- Crystal-PvP ist in Maßen erlaubt.\n\n"))
                .append(Component.text("- Kämpfe fair"))
                .build();

        Component page6 = Component.text()
                .append(Component.text("§0§l6. Dupen, Lags§r\n\n"))
                .append(Component.text("- Server-TPS darf nicht dauerhaft unter 16 fallen.\n\n"))
                .append(Component.text("- Nur Dupen, wenn niemand zu schaden kommt.\n\n"))
                .append(Component.text("- Farmen, Redstone sollte abschaltbar sein"))
                .build();

        Component page7 = Component.text()
                .append(Component.text("§0§l7. Verhalten§r\n\n"))
                .append(Component.text("- Andere Spieler mit Respekt behandeln.\n\n"))
                .append(Component.text("- Kein Spam, keine Beleidigungen, keine Drohungen\n\n"))
                .append(Component.text("- Keine Fremd-Werbung"))
                .build();

        Component page8_1 = Component.text()
                .append(Component.text("§0§l8.1 Konsequenzen§r\n\n"))
                .append(Component.text("§0§lLeichter Verstoß§r\n\n"))
                .append(Component.text("Beleidigen, Provokation\n\n"))
                .append(Component.text("Strafen:\n"))
                .append(Component.text("Mute, Verwahnung, Mündlicher Hinweis"))
                .build();

        Component page8_2 = Component.text()
                .append(Component.text("§0§l8.2 Konsequenzen§r\n\n"))
                .append(Component.text("§0§lMittlerer Verstoß§r\n\n"))
                .append(Component.text("Töten von Mitspielern\n\n"))
                .append(Component.text("Strafe:\n"))
                .append(Component.text("Zeitweilige Sperre"))
                .build();

        Component page8_3 = Component.text()
                .append(Component.text("§0§l8.3 Konsequenzen§r\n\n"))
                .append(Component.text("§0§lSchwerer Verstoß§r\n\n"))
                .append(Component.text("Massives Laggen, Safe-Zones Griefing, dauerhafte Provokation\n\n"))
                .append(Component.text("Strafe:\n"))
                .append(Component.text("Dauerhafte Sperre"))
                .build();

        Component page9 = Component.text()
                .append(Component.text("§0§lSchlusswort§r\n\n"))
                .append(Component.text("Dieser Server lebt von Aktion, Reaktion und Respekt. Wenn du dich an die Regeln hälst, wirst du Spaß, Chaos und legendäre Momente erleben.\n\nBleibe fair und kreativ."))
                .build();

        // Virtuelles Buch erstellen (Adventure Book)
        Book book = Book.book(
                title,
                author,
                List.of(page1, page2, page3, page4, page5, page6, page7, page8_1, page8_2, page8_3, page9)
        );

        // Buch-GUI öffnen – KEIN Item im Inventar!
        p.openBook(book);

        return true;
    }
}
