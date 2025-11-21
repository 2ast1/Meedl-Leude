package de.basti.util;

import de.basti.main.MeedlLeude;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.logging.Level;

public class AutoMod {
    public static void checkMessage(Player player, String message) {
        if(DataManager.getBoolean("automod","settings.yml", "settings.chat.scan")) {

            List<String> badwords = DataManager.getStringList("automod", "badwords.yml", "chat.badwords");
            for (String word : badwords) {
                if (message.toLowerCase().contains(word.toLowerCase())) {


                    MeedlLeude.getInstance().getLogger().log(Level.WARNING, "AutoMod hat eine Nachricht makiert: [" + player.getName() + "] " + message);
                    break;
                }
            }

        }
    }
}