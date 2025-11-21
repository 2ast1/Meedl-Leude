package de.basti.commands;

import com.sun.management.OperatingSystemMXBean;
import de.basti.util.ServerHealthUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.management.ManagementFactory;
import java.time.LocalTime;

public class ServerHealthCMD implements CommandExecutor {

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

        if(p.hasPermission("meedlleude.health")) {
            LocalTime now = LocalTime.now();
            String hh = String.format("%02d", now.getHour());
            String mm = String.format("%02d", now.getMinute());
            String ss = String.format("%02d", now.getSecond());

            Runtime runtime = Runtime.getRuntime();

            long maxMemory = runtime.maxMemory();         // max möglicher Heap
            long totalMemory = runtime.totalMemory();     // aktuell reserviert
            long freeMemory = runtime.freeMemory();       // frei innerhalb total
            long usedMemory = totalMemory - freeMemory;   // benutzt

            // In MB umrechnen
            long maxMB = maxMemory / 1024 / 1024;
            long freeMB = (maxMemory - usedMemory) / 1024 / 1024; // frei relativ zu max
            long usedMB = usedMemory / 1024 / 1024;

            double cpuPercent = getCpuUsagePercent();
            double tps = Bukkit.getTPS()[1]; // stabiler Wert

            int health = ServerHealthUtil.calculateServerHealth(maxMB, freeMB, usedMB, cpuPercent, tps);

            p.sendMessage("§a********** Bericht **********" +
                    "\n§aMomentan errechneter Server-Health-Wert: §9§l" + health
                +   "% \n§r§aDas heißt, dass der Server §9§l" + ServerHealthUtil.getIntensity(health) + "§r§a ausgelastet ist." +
                    "\n*****" +
                    "\n§6§lZusammensetzung:" +
                    "\n\n§r§aMaximaler Ram: §9§l" + maxMB + "§r§a MB" +
                    "\n§r§aFreier Ram: §9§l" + freeMB + "§r§a MB" +
                    "\n§r§aBenutzer Ram: §9§l" + usedMB + "§r§a MB" +
                    "\n§r§aCpu Auslastung: §9§l" + cpuPercent + "§r§a %" +
                    "\n§r§aTps: §9§l" + tps + "§r§a (" + ServerHealthUtil.getTpsStatus(tps) + ")" +
                    "\n*****" +
                    "\nBericht Ende. Die Einschätzung kann ungenau sein." +
                    "\n§a********** Bericht **********");

        } else {
            p.sendMessage("§cDu hast keine Berechtigung dazu.");
        }

        return true;

    }

    public static double getCpuUsagePercent() {
        OperatingSystemMXBean osBean =
                (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

        double load = osBean.getProcessCpuLoad(); // 0.0 – 1.0
        if (load < 0) return 0; // Wenn nicht verfügbar

        return load * 100.0;
    }

}
