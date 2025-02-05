package ro.iacobai.titleManager.handler;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import ro.iacobai.titleManager.models.Title;
import ro.iacobai.titleManager.TitleManager;
import ro.iacobai.titleManager.models.TitleHandler;

import java.util.List;

public class ConfigHandler {
    public static void loadTittles() {
        // Get the plugin instance
        TitleManager titleManager = TitleManager.getInstance();

        // Save the default config if it doesn't exist
        titleManager.saveDefaultConfig();

        // Get the titles section from the config
        ConfigurationSection tittlesSection = titleManager.getConfig().getConfigurationSection("titles");

        // If the section is null, return
        if (tittlesSection == null) {
            return;
        }

        // Loop through keys of the titles section and add the specified permission to the plugin
        for (String key : tittlesSection.getKeys(false)) {
            String permission = String.format("titlemanager.titles.%s", key);

            // Register the permission, log it, and add the title to the TitleHandler
            Permission perm = new Permission(permission, PermissionDefault.FALSE);
            titleManager.getServer().getPluginManager().addPermission(perm);
            titleManager.getLogger().info(String.format("Registered permission %s", permission));

            // Get the title and the description from the config
            String title = tittlesSection.getString(key + ".title");
            List<String> description = tittlesSection.getStringList(key + ".description");

            // Create a new title and register it
            TitleHandler.getInstance().registerTitle(new Title(key, perm, description, title));
        }
    }
}