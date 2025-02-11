package ro.iacobai.titleManager;

import org.bukkit.plugin.java.JavaPlugin;
import ro.iacobai.titleManager.commands.TitleCommand;
import ro.iacobai.titleManager.commands.TitleCommandCompleter;
import ro.iacobai.titleManager.events.MenuHandler;
import ro.iacobai.titleManager.handler.ConfigHandler;

public final class TitleManager extends JavaPlugin {
    // Static instance of the plugin for easy access
    private static TitleManager instance;

    // Getter for the plugin instance
    public static TitleManager getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        // Set the instance to this plugin
        instance = this;

        // Load titles and their permissions from the config
        ConfigHandler.loadTittles();

        // Register the command executor for "titlemanager"
        getCommand("titlemanager").setExecutor(new TitleCommand());
        getCommand("titlemanager").setTabCompleter(new TitleCommandCompleter());

        // Register event listeners
        getServer().getPluginManager().registerEvents(new MenuHandler(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}