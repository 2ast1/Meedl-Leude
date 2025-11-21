package de.basti.util;

public class ServerHealthUtil {

    /**
     * Berechnet einen Server-Health-Wert von 0% (sehr entspannt)
     * bis 175% (stark überlastet).
     *
     * @param maxMB       maximaler Heap in MB
     * @param freeMB      freie MB relativ zu max (optional, hier nicht zwingend genutzt)
     * @param usedMB      genutzter Heap in MB
     * @param cpuPercent  CPU-Usage des MC-Prozesses in %
     * @param tps         TPS (Paper: Bukkit.getTPS()[1] für 5s)
     * @return int von 0 bis 175
     */
    public static int calculateServerHealth(long maxMB, long freeMB, long usedMB,
                                            double cpuPercent, double tps) {

        // --- 1. Einzelne Lastfaktoren normalisieren (0.0 = entspannt, 1.0 = voll am Limit) ---

        // RAM-Auslastung
        double memLoad = 0.0;
        if (maxMB > 0) {
            memLoad = (double) usedMB / (double) maxMB;  // z.B. 0.75 = 75% RAM
        }
        memLoad = clamp(memLoad, 0.0, 1.0);

        // CPU-Auslastung
        double cpuLoad = cpuPercent / 100.0;            // 0–1
        cpuLoad = clamp(cpuLoad, 0.0, 1.0);

        // TPS-Last: 20 TPS = 0 (alles gut), 0 TPS = 1 (komplett am Limit)
        double tpsClamped = Math.min(tps, 20.0);
        double tpsLoad = 1.0 - (tpsClamped / 20.0);     // 20 TPS -> 0, 10 TPS -> 0.5, 0 TPS -> 1
        tpsLoad = clamp(tpsLoad, 0.0, 1.0);

        // --- 2. Grundlast berechnen (0.0–1.0) ---
        // Gewichtung: RAM 40%, CPU 40%, TPS 20%
        double baseLoad = memLoad * 0.4 + cpuLoad * 0.4 + tpsLoad * 0.2;

        // --- 3. Overload-Faktor (für >100% bis 175%) ---

        double overloadMem = memLoad > 0.9 ? (memLoad - 0.9) / 0.1 : 0.0;   // 90–100% RAM => 0–1
        double overloadCpu = cpuLoad > 0.9 ? (cpuLoad - 0.9) / 0.1 : 0.0;   // 90–100% CPU => 0–1
        double overloadTps = tps < 18.0 ? (18.0 - tps) / 18.0 : 0.0;        // <18 TPS => 0–1

        // Mittelwert der Overloads, auf 0–1 begrenzt
        double overload = (overloadMem + overloadCpu + overloadTps) / 3.0;
        overload = clamp(overload, 0.0, 1.0);

        // --- 4. Health-Wert berechnen ---
        // 0–100% aus der Grundlast, bis zu +75% aus Overload
        double health = baseLoad * 100.0 + overload * 75.0;

        // auf 0–175 begrenzen und runden
        int healthInt = (int) Math.round(clamp(health, 0.0, 175.0));

        return healthInt;
    }

    private static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    public static String getIntensity(int value) {
        if (value >= 0 && value <= 25) {
            return "sehr wenig";
        } else if (value <= 50) {
            return "leicht";
        } else if (value <= 75) {
            return "moderat";
        } else if (value <= 100) {
            return "stärker";
        } else if (value <= 125) {
            return "sehr stark";
        } else if (value <= 150) {
            return "unfassbar stark";
        } else {
            return "außerhalb des Bereichs";
        }
    }

    public static String getTpsStatus(double tps) {
        if (tps >= 19.5) {
            return "perfekt";
        } else if (tps >= 15) {
            return "normal";
        } else if (tps >= 10) {
            return "spürbar laggend";
        } else if (tps > 0) {
            return "stark verzögert";
        } else {
            return "ungültig";
        }
    }


}
