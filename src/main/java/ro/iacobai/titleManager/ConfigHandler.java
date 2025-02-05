package ro.iacobai.titleManager;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.List;

public class ConfigHandler {
    public static void loadTittles() {
        // we get the plugins instance
        TitleManager titleManager = TitleManager.getInstance();

        // we save the default config if it doesn't exist
        titleManager.saveDefaultConfig();

        // get the tittles section
        ConfigurationSection tittlesSection = titleManager.getConfig().getConfigurationSection("titles");

        // if the section is null, we return
        if (tittlesSection == null) {
            return;
        }

        // get loop through keys of the tittles section, and add the specified permission to the plugin
        for (String key : tittlesSection.getKeys(false)) {
            String permission = String.format("titlemanager.titles.%s", key);

            // after we make the permission, we register it, log it and add the title to the TitleHandler
            Permission perm = new Permission(permission, PermissionDefault.FALSE);
            titleManager.getServer().getPluginManager().addPermission(perm);
            titleManager.getLogger().info(String.format("Registered permission %s", permission));

            // get the title and the description from the config
            String title = tittlesSection.getString(key + ".title");
            List<String> description = tittlesSection.getStringList(key + ".description");

            // create a new title and register it
            TitleHandler.getInstance().registerTitle(new Title(key, perm, description, title));
        }
    }
}
