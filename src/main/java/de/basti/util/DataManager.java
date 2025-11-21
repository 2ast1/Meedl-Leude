package de.basti.util;

import net.kyori.adventure.text.Component;
import org.apache.commons.lang3.ObjectUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;

public class DataManager {

    private static JavaPlugin plugin;

    // Setup muss im onEnable aufgerufen werden
    public static void setup(JavaPlugin pl) {
        plugin = pl;

        // WICHTIG: Plugin-Datenordner sicherstellen
        File dataFolder = plugin.getDataFolder();
        if (!dataFolder.exists()) {
            if (!dataFolder.mkdirs()) {
                plugin.getLogger().log(Level.SEVERE,
                        "Konnte Plugin-Datenordner nicht erstellen: " + dataFolder.getPath());
                return; // Ohne Ordner macht alles andere keinen Sinn
            }
        }

        // Jetzt ist der Ordner sicher da
        createFile("", "config.yml");
        createFile("", "playerdata.yml");

        if (!checkIfExist("","config.yml","settings.chat.colorful")) {
            set("","config.yml", "settings.chat.colorful", true);
        }

        if (!checkIfExist("","config.yml","settings.status.maxlength")) {
            set("","config.yml", "settings.status.maxlength", 25);
        }

        if (!checkIfExist("","config.yml","settings.status.chatmsg")) {
            set("","config.yml", "settings.status.chatmsg", "&r[{status}&r] {playername} | {msg}");
        }

        if (!checkIfExist("","config.yml","settings.status.tablist")) {
            set("","config.yml", "settings.status.tablist", "{status}&r ");
        }

        if (!checkIfExist("","config.yml","settings.status.notset.overwrite")) {
            set("","config.yml", "settings.status.notset.overwrite", true);
        }

        if (!checkIfExist("","config.yml","settings.status.notset.msg")) {
            set("","config.yml", "settings.status.notset.msg", "{playername} | {msg}");
        }

        if (!checkIfExist("","config.yml","settings.server.motd.change")) {
            set("","config.yml", "settings.server.motd.change", false);
        }

        if (!checkIfExist("","config.yml","settings.server.motd.value")) {
            set("","config.yml", "settings.server.motd.value", "A Minecraft Server");
        }

        //AutoMod Dateien schaffen
        createFolder("","automod");
        createFile("automod", "settings.yml");
        createFile("automod", "badwords.yml");
        createFile("automod", "playerdata.yml");

        if (!checkIfExist("automod","settings.yml","settings.chat.scan")) {
            set("automod","settings.yml", "settings.chat.scan", true);
        }

        if (!checkIfExist("automod","badwords.yml","chat.badwords")) {
            java.util.List<String> defaults = java.util.Arrays.asList(
                    "Nigger",
                    "Nigga",
                    "Niggai",
                    "Judensau",
                    "Heil Hitler",
                    "Heil",
                    "Fynn Windheuser",
                    "Hitler",
                    "Hurensohn",
                    "Wixxer",
                    "Bastard",
                    "Törkenschwein",
                    "Törkensau",
                    "Ausländer",
                    "Ausländer raus"
            );
            set("automod","badwords.yml", "chat.badwords", defaults);
        }

    }


    // --------------------------------------------------------
    //                FILE & FOLDER CREATION
    // --------------------------------------------------------

    /**
     * createFolder("", "save")
     * createFolder("data", "player")
     */
    public static File createFolder(String parent, String folderName) {
        // Basis: Plugin-DataFolder
        File base = plugin.getDataFolder();
        if (!base.exists()) {
            base.mkdirs();
        }

        File folder;
        if (parent == null || parent.isEmpty()) {
            folder = new File(base, folderName);
        } else {
            folder = new File(new File(base, parent), folderName);
        }

        if (!folder.exists()) {
            if (!folder.mkdirs()) {
                plugin.getLogger().log(Level.WARNING,
                        "Konnte Ordner nicht erstellen: " + folder.getPath());
            }
        }
        return folder;
    }


    /**
     * createFile("save", "location.yml")
     * createFile("", "config.yml")
     */
    public static File createFile(String folder, String fileName) {
        // Basisordner sicherstellen
        File base = plugin.getDataFolder();
        if (!base.exists()) {
            base.mkdirs();
        }

        File file;

        if (folder == null || folder.isEmpty()) {
            // Direkt im Plugin-Ordner: plugins/DeinPlugin/config.yml
            file = new File(base, fileName);
        } else {
            // Unterordner: plugins/DeinPlugin/save/location.yml
            File dir = createFolder("", folder); // sorgt dafür, dass alles existiert
            file = new File(dir, fileName);
        }

        if (!file.exists()) {
            try {
                if (!file.createNewFile()) {
                    plugin.getLogger().log(Level.WARNING,
                            "Konnte Datei nicht erstellen: " + file.getPath());
                }
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE,
                        "Fehler beim Erstellen der Datei: " + fileName, e);
            }
        }
        return file;
    }


    // --------------------------------------------------------
    //                CONFIG LOAD / SAVE
    // --------------------------------------------------------

    public static FileConfiguration getConfig(String folder, String fileName) {
        File file = createFile(folder, fileName);
        return YamlConfiguration.loadConfiguration(file);
    }

    public static void saveConfig(String folder, String fileName, FileConfiguration cfg) {
        File file = createFile(folder, fileName);
        try {
            cfg.save(file);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Konnte Config nicht speichern: " + fileName, e);
        }
    }

    // --------------------------------------------------------
    //                BASIC GET / SET
    // --------------------------------------------------------

    public static String getString(String folder, String file, String path) {
        return getConfig(folder, file).getString(path);
    }

    public static int getInt(String folder, String file, String path) {
        return getConfig(folder, file).getInt(path);
    }

    public static boolean getBoolean(String folder, String file, String path) {
        return getConfig(folder, file).getBoolean(path);
    }

    public static double getDouble(String folder, String file, String path) {
        return getConfig(folder, file).getDouble(path);
    }

    public static java.util.List<String> getStringList(String folder, String file, String path) {
        return getConfig(folder, file).getStringList(path);
    }


    public static void set(String folder, String file, String path, Object value) {
        FileConfiguration cfg = getConfig(folder, file);
        cfg.set(path, value);
        saveConfig(folder, file, cfg);
    }

    // --------------------------------------------------------
    //                INVENTORY SAVE / LOAD
    // --------------------------------------------------------

    public static void saveInventory(String folder, String file, String path, Inventory inv) {
        FileConfiguration cfg = getConfig(folder, file);
        cfg.set(path, inv.getContents());
        saveConfig(folder, file, cfg);
    }


    @SuppressWarnings("unchecked")
    public static Inventory loadInventory(String folder, String file, String path, int size, String title) {
        FileConfiguration cfg = getConfig(folder, file);

        Inventory inv = Bukkit.createInventory(null, size, Component.text(title));

        Object data = cfg.get(path);
        if (data instanceof List) {
            List<ItemStack> items = (List<ItemStack>) data;
            for (int i = 0; i < items.size() && i < size; i++) {
                inv.setItem(i, items.get(i));
            }
        }

        return inv;
    }

    // --------------------------------------------------------
    // Methoden zur überprüfung und generellen Datenverwaltung
    // --------------------------------------------------------

    public static boolean checkIfExist(String folder, String file, String path) {
        FileConfiguration cfg = getConfig(folder, file);
        return cfg.contains(path);
    }

    public static void delete(String folder, String file, String path) {
        FileConfiguration cfg = getConfig(folder, file);

        if (cfg.contains(path)) {
            cfg.set(path, null); // Datenpunkt löschen
            saveConfig(folder, file, cfg);
        }
    }


}
