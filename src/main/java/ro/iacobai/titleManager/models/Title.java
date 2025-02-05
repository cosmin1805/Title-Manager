package ro.iacobai.titleManager.models;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.Permission;

import java.util.ArrayList;
import java.util.List;

public class Title {
    private final String name;
    private final Permission permission;
    private final List<String> description;
    private final String title;

    // Constructor from config
    public Title(String name, Permission permission, List<String> description, String title) {
        this.name = name;
        this.permission = permission;
        this.description = description;
        this.title = title;
    }

    // Constructor to create a Title from an ItemStack
    public Title(ItemStack item) {
        if (item == null || !item.hasItemMeta()) throw new IllegalArgumentException("Invalid item!");

        ItemMeta meta = item.getItemMeta();

        // Extract title (display name)
        Component displayName = meta.displayName();
        this.title = displayName != null ? LegacyComponentSerializer.legacyAmpersand().serialize(displayName) : "";

        // Remove color codes from title and convert to lowercase for `name`
        this.name = stripColors(this.title).toLowerCase().replace(" ", "_");

        // Extract lore (description lines)
        List<Component> loreComponents = meta.lore();
        List<String> descriptionList = new ArrayList<>();
        if (loreComponents != null) {
            for (Component loreLine : loreComponents) {
                descriptionList.add(stripColors(LegacyComponentSerializer.legacyAmpersand().serialize(loreLine)));
            }
        }
        this.description = descriptionList;

        // Automatically generate permission
        this.permission = new Permission("titlemanager.titles." + this.name);
    }

    public String getName() {
        return name;
    }

    public Permission getPermission() {
        return permission;
    }

    public List<String> getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    // Utility Method to Strip Color Codes Using MiniMessage
    private static String stripColors(String input) {
        return input.replaceAll("(?i)&[0-9A-FK-OR]", "");
    }
}