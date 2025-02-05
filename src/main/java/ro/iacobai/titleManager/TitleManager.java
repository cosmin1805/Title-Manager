package ro.iacobai.titleManager;

import org.bukkit.plugin.java.JavaPlugin;

public final class TitleManager extends JavaPlugin {
    // We need to create a static instance of the plugin
    // so we can access it from other classes
    private static TitleManager instance;

    public static TitleManager getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        // set the instance to this plugin
        instance = this;

        // load the titles permissions from the config
        ConfigHandler.loadTittles();

        // register the command
        getCommand("titlemanager").setExecutor(new TitleCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
