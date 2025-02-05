package ro.iacobai.titleManager.gui;

import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import ro.iacobai.titleManager.models.Title;
import ro.iacobai.titleManager.models.TitleHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MainMenu implements InventoryHolder {
    private final org.bukkit.inventory.Inventory titleGui;
    private final Player player;

    public MainMenu(Player player) {
        this.player = player;
        this.titleGui = Bukkit.createInventory(this, 54, "Title Manager");
        updateMenu();
    }

    public void updateMenu() {
        titleGui.clear();

        Set<Title> titles = TitleHandler.getInstance().getTitles();

        // Loop through all the titles and only add the ones that the player has access to
        for (Title title : titles) {
            if (player.hasPermission(title.getPermission())) {
                ItemStack item = new ItemStack(Material.NAME_TAG);
                ItemMeta meta = item.getItemMeta();
                List<Component> lore = new ArrayList<>();

                // Convert the title into an Adventure Component with colors
                String titleRaw = title.getTitle();
                Component coloredTitle = LegacyComponentSerializer.legacyAmpersand().deserialize(titleRaw);
                meta.displayName(coloredTitle);

                // Convert descriptions into Adventure Components
                List<String> descriptionRaw = title.getDescription();
                if (descriptionRaw != null && !descriptionRaw.isEmpty()) {
                    List<Component> coloredDescription = descriptionRaw.stream()
                            .map(line -> LegacyComponentSerializer.legacyAmpersand().deserialize(line))
                            .collect(Collectors.toList());
                    lore.addAll(coloredDescription);
                }

                // Check if the current title is selected and highlight it
                String prefix = PlaceholderAPI.setPlaceholders(player, "%uperms_prefix%");
                if (prefix.toLowerCase().contains(title.getName().toLowerCase())) {
                    meta.addEnchant(org.bukkit.enchantments.Enchantment.MENDING, 1, true);
                    meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);
                    lore.add(MiniMessage.miniMessage().deserialize("<green>(Currently Selected)</green>"));
                }

                meta.lore(lore);
                item.setItemMeta(meta);
                titleGui.addItem(item);
            }
        }
    }

    public void open() {
        player.openInventory(titleGui);
    }

    @Override
    public @NotNull org.bukkit.inventory.Inventory getInventory() {
        return titleGui;
    }
}