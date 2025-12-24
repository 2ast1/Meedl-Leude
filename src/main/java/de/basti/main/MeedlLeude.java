package de.basti.main;

import de.basti.commands.*;
import de.basti.events.MotdEvent;
import de.basti.events.NewChatEvent;
import de.basti.events.OnJoinEvent;
import de.basti.util.DataManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.logging.Level;

public final class MeedlLeude extends JavaPlugin {

    // optional, falls du später von anderen Klassen aus darauf zugreifen willst:
    private static MeedlLeude instance;

    @Override
    public void onEnable() {
        instance = this; // Plugin-Instanz merken (wenn du willst)

        getLogger().log(Level.INFO, getName() + " startet jetzt");

        DataManager.setup(this);

        getLogger().log(Level.INFO,"Dateisystem geladen.");
        getLogger().log(Level.INFO,"Ordner löschen und Server Neustarten bei Problemen.");

        Objects.requireNonNull(getCommand("status")).setExecutor(new StatusCMD());
        Objects.requireNonNull(getCommand("bastard")).setExecutor(new BastardCMD());
        Objects.requireNonNull(getCommand("setstatus")).setExecutor(new SetStatusCMD());
        Objects.requireNonNull(getCommand("removestatus")).setExecutor(new RemoveStatusCMD());
        Objects.requireNonNull(getCommand("serverhealth")).setExecutor(new ServerHealthCMD());
        Objects.requireNonNull(getCommand("inventory")).setExecutor(new InventoryCMD());
        Objects.requireNonNull(getCommand("enderchest")).setExecutor(new EnderchestCMD());
        Objects.requireNonNull(getCommand("regeln")).setExecutor(new RegelnCMD());
        Objects.requireNonNull(getCommand("mute")).setExecutor(new MuteCMD());
        Objects.requireNonNull(getCommand("unmute")).setExecutor(new UnmuteCMD());
        Objects.requireNonNull(getCommand("ban")).setExecutor(new BanCMD());
        Objects.requireNonNull(getCommand("unban")).setExecutor(new UnbanCMD());
        Objects.requireNonNull(getCommand("automod")).setExecutor(new AutoModCMD());

        getLogger().log(Level.INFO,"Befehle geladen.");

        getServer().getPluginManager().registerEvents(new NewChatEvent(), this);
        getServer().getPluginManager().registerEvents(new OnJoinEvent(), this);
        getServer().getPluginManager().registerEvents(new MotdEvent(), this);

        getLogger().log(Level.INFO,"Events geladen.");
    }

    @Override
    public void onDisable() {
        getLogger().info(getName() + " wird gestoppt");
    }

    // optionaler Getter
    public static MeedlLeude getInstance() {
        return instance;
    }

}
