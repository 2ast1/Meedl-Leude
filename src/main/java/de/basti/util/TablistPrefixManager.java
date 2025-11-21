package de.basti.util;

import de.basti.main.MeedlLeude;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;

import java.time.LocalTime;

public final class TablistPrefixManager {

    private static Scoreboard pluginScoreboard; // unser eigenes, isoliertes Scoreboard

    private TablistPrefixManager() {
        // Utility-Klasse, kein Konstruktor
    }

    /**
     * Liefert das isolierte Plugin-Scoreboard.
     * Wird beim ersten Zugriff erstellt (lazy init).
     */
    private static Scoreboard getPluginScoreboard() {
        if (pluginScoreboard != null) {
            return pluginScoreboard;
        }

        ScoreboardManager manager = Bukkit.getScoreboardManager();
        if (manager == null) {
            // Sollte in normalen Fällen nicht passieren, außer VIEL zu früh aufgerufen
            throw new IllegalStateException("ScoreboardManager ist noch null – TablistPrefixManager zu früh aufgerufen.");
        }

        pluginScoreboard = manager.getNewScoreboard();
        return pluginScoreboard;
    }

    /**
     * Setzt sicher einen Prefix in der Tabliste für einen Spieler.
     * - Nutzt isoliertes Plugin-Scoreboard
     * - Nutzt Scoreboard-Team
     * - Nutzt Adventure Components
     * - Läuft garantiert im Main-Thread
     */
    public static void setTabPrefix(@NotNull Player player, @NotNull Component prefix) {
        BukkitScheduler scheduler = Bukkit.getScheduler();

        if (!Bukkit.isPrimaryThread()) {
            // Sicherstellen, dass wir im Main-Thread laufen
            scheduler.runTask(MeedlLeude.getInstance(), () -> setTabPrefix(player, prefix));
            return;
        }

        // ✅ isoliertes Scoreboard
        Scoreboard board = getPluginScoreboard();

        // Teamname: max. 16 Zeichen (Minecraft-Limit)
        String teamName = buildSafeTeamName(player);

        Team team = board.getTeam(teamName);
        if (team == null) {
            team = board.registerNewTeam(teamName);
        }

        // Prefix setzen
        team.prefix(prefix);

        // Spieler als "Entry" hinzufügen
        String entry = player.getName();
        if (!team.hasEntry(entry)) {
            team.addEntry(entry);
        }

        // Spieler auf dieses Scoreboard setzen
        if (player.getScoreboard() != board) {
            player.setScoreboard(board);
        }

        // Tablist-Anzeige aktualisieren
        player.playerListName(prefix.append(Component.text(player.getName())));
    }

    /**
     * Entfernt den Prefix / das Team des Spielers wieder sauber.
     */
    public static void clearTabPrefix(@NotNull Player player) {
        if (!Bukkit.isPrimaryThread()) {
            Bukkit.getScheduler().runTask(MeedlLeude.getInstance(), () -> clearTabPrefix(player));
            return;
        }

        Scoreboard board = getPluginScoreboard();
        String teamName = buildSafeTeamName(player);

        Team team = board.getTeam(teamName);
        if (team != null) {
            team.unregister();
        }

        // Tablist wieder auf Standard setzen
        player.playerListName(Component.text(player.getName()));
        // Optional: auf Main-Scoreboard zurücksetzen:
        // player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
    }

    private static String buildSafeTeamName(@NotNull Player player) {
        // Teamname muss <= 16 Zeichen sein
        // "P_" + 14 Zeichen = 16
        return "P_" + player.getUniqueId().toString().replace("-", "").substring(0, 14);
    }

    public static void setTabStatus(Player p) {

        // 1. Status aus playerdata.yml lesen
        String rawStatus = DataManager.getString(
                "",
                "playerdata.yml",
                p.getUniqueId() + ".status"
        );

        // Wenn kein Status gesetzt → GAR NICHTS machen
        if (rawStatus == null || rawStatus.isBlank()) {
            return;
        }

        // 2. Welt-Typ bestimmen
        World.Environment env = p.getWorld().getEnvironment();
        String worldType = switch (env) {
            case NORMAL -> "Overworld";
            case NETHER -> "Nether";
            case THE_END -> "End";
            default -> "Unknown";
        };

        // 3. Uhrzeit holen (zweistellig)
        LocalTime now = LocalTime.now();
        String hh = String.format("%02d", now.getHour());
        String mm = String.format("%02d", now.getMinute());
        String ss = String.format("%02d", now.getSecond());

        // 4. Template aus config.yml laden
        // Passe den Pfad hier an deine Struktur an:
        // z.B. "settings.status.templates.tablist" oder wie du es genannt hast
        String sandboxTab = DataManager.getString(
                "",
                "config.yml",
                "settings.status.tablist" // <--- HIER ggf. anpassen zu settings.status.templates.deinTemplate
        );

        // Wenn in der Config nichts steht → auch nichts machen
        if (sandboxTab == null || sandboxTab.isBlank()) {
            return;
        }

        // 5. Platzhalter ersetzen
        // Wir arbeiten vollständig mit &-Farbcodes und lassen LegacyComponentSerializer das machen.
        sandboxTab = sandboxTab
                .replace("{status}", rawStatus.replace("&","§") + "§r")                  // rawStatus darf auch &-Farben haben
                .replace("{playername}", p.getName() + "§r")
                .replace("{world}", p.getWorld().getName() + "§r")
                .replace("{worldtype}", worldType + "§r")
                .replace("{hh}", hh + "§r")
                .replace("{mm}", mm + "§r")
                .replace("{ss}", ss + "§r");

        // 6. &-Farbcodes zu Component
        Component prefix = LegacyComponentSerializer.legacyAmpersand().deserialize(sandboxTab);

        // 7. Prefix wirklich setzen
        TablistPrefixManager.setTabPrefix(p, prefix);
    }

}
