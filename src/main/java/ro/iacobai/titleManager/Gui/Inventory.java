package ro.iacobai.titleManager.Gui;

import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import ro.iacobai.titleManager.Title;
import ro.iacobai.titleManager.TitleHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Inventory implements InventoryHolder {
    private final org.bukkit.inventory.Inventory titleGui;

    public Inventory(Player player) {
        // create a new inventory with 64 slots and the title "Title Manager"
        titleGui = Bukkit.createInventory(this, 54, "Title Manager");

        // add the items to the inventory, so all the tittles that the players has access to
        Set<Title> titles = TitleHandler.getInstance().getTitles();

        // loop through all the titles and only add the ones that the player has access to
        for (Title title : titles) {
            if (player.hasPermission(title.getPermission())) {
                ItemStack item = new ItemStack(Material.NAME_TAG);
                ItemMeta meta = item.getItemMeta();
                List<Component> lore = new ArrayList<>();

                // convert "&cBOSS" into an Adventure Component with colors
                String titleRaw = title.getTitle();
                Component coloredTitle = LegacyComponentSerializer.legacyAmpersand().deserialize(titleRaw);
                meta.displayName(coloredTitle);

                // convert "&7This is a boss title" into an Adventure Component with colors
                List<String> descriptionRaw = title.getDescription();

                // if the description is not null or empty, we convert it into an Adventure Component
                if (descriptionRaw != null && !descriptionRaw.isEmpty()) {
                    // Convert each line of the description into an Adventure Component
                    List<Component> coloredDescription = descriptionRaw.stream().map(line -> LegacyComponentSerializer.legacyAmpersand().deserialize(line)).collect(Collectors.toList());

                    // add the description to the lore
                    lore.addAll(coloredDescription);
                }

                // finally, check if the tittle is set as the suffix or prefix of the player, if is highlight it
                String prefix = PlaceholderAPI.setPlaceholders(player, "%uperms_prefix%");

                if (prefix.toLowerCase().contains(title.getName().toLowerCase())) {
                    // add enchantment and lore to the item so the player knows that this is the current title
                    meta.addEnchant(Enchantment.MENDING, 1, true);
                    meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);

                    lore.add(MiniMessage.miniMessage().deserialize("<green>(Currently Selected)</green>"));
                }

                // set the lore and the meta of the item and add it to the inventory
                meta.lore(lore);
                item.setItemMeta(meta);
                titleGui.addItem(item);
            }
        }
    }

    @Override
    public @NotNull org.bukkit.inventory.Inventory getInventory() {
        return titleGui;
    }
}
