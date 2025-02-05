package ro.iacobai.titleManager.events;

import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import ro.iacobai.titleManager.models.Title;
import ro.iacobai.titleManager.gui.MainMenu;

import java.util.Objects;

public class MenuHandler implements Listener {

    @EventHandler
    public void onMenuClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        final String MAIN_MENU = "Title Manager";

        // Get the title of the inventory
        Component titleComponent = e.getView().title();
        String inventoryTitle = LegacyComponentSerializer.legacySection().serialize(titleComponent);

        // Check if the clicked inventory is the main menu
        if (inventoryTitle.equalsIgnoreCase(MAIN_MENU)) {
            e.setCancelled(true);

            if (e.getClickedInventory() == null) return;

            // Check which item is clicked and if it's air or the title is already selected, return
            ItemStack clickedItem = e.getCurrentItem();
            if (clickedItem == null || clickedItem.getType().isAir()) return;

            // Get the title from the item
            Title title = new Title(clickedItem);

            // Check if the title is already selected
            String prefix = PlaceholderAPI.setPlaceholders(player, "%uperms_prefix%");
            if (prefix.toLowerCase().contains(title.getName().toLowerCase())) {
                return;
            }

            // Set the title as the prefix
            String command = "upc SetPlayerPrefix " + player.getName() + " " + title.getTitle();
            Bukkit.getScheduler().runTask(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("TitleManager")), () -> {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
            });

            // Refresh and update the menu
            Bukkit.getScheduler().runTaskLater(Bukkit.getPluginManager().getPlugin("TitleManager"), () -> {
                MainMenu menu = new MainMenu(player);
                menu.updateMenu();
                menu.open();
            }, 5L);
        }
    }
}
