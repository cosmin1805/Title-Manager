package ro.iacobai.titleManager.events;

import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import ro.iacobai.titleManager.models.Title;
import ro.iacobai.titleManager.gui.MainMenu;

import java.util.Objects;

public class MenuHandler implements Listener {

    private static final NamespacedKey PREFIX_KEY = new NamespacedKey("titlemanager", "changing_prefix");

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

            // Check which item is clicked and perform the appropriate action
            ItemStack clickedItem = e.getCurrentItem();
            if (clickedItem == null) return;

            switch (clickedItem.getType()) {
                case BARRIER:
                    player.closeInventory();
                    break;
                case NAME_TAG:
                    changeTitle(player, new Title(clickedItem));
                    refreshMenu(player);
                    break;
                case COMPARATOR:
                    changePrefixSetting(player);
                    refreshMenu(player);
                    break;
                case REDSTONE_BLOCK:
                    changeTitle(player, new Title("none", null, null, "none"));
                    refreshMenu(player);
                    break;
            }
        }
    }

    private void changeTitle(Player player, Title title) {
        PersistentDataContainer data = player.getPersistentDataContainer();
        boolean changingPrefix = data.getOrDefault(PREFIX_KEY, PersistentDataType.BYTE, (byte) 1) == 1;

        String placeholder = changingPrefix ? "%uperms_prefix%" : "%uperms_suffix%";
        String currentTitle = PlaceholderAPI.setPlaceholders(player, placeholder);

        // Check if the player already has the title, if so just set the title to none
        if (currentTitle.toLowerCase().contains(title.getName().toLowerCase())) {
            title = new Title("none", null, null, "none");
        }

        // Choose the correct command based on changingPrefix
        String command = changingPrefix
                ? "upc SetPlayerPrefix " + player.getName() + " " + title.getTitle()
                : "upc SetPlayerSuffix " + player.getName() + " " + title.getTitle();

        Bukkit.getScheduler().runTask(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("TitleManager")), () -> {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
        });
    }

    private void changePrefixSetting(Player player) {
        PersistentDataContainer data = player.getPersistentDataContainer();
        boolean changingPrefix = data.getOrDefault(PREFIX_KEY, PersistentDataType.BYTE, (byte) 1) == 1;

        // Toggle the setting
        data.set(PREFIX_KEY, PersistentDataType.BYTE, (byte) (changingPrefix ? 0 : 1));
    }

    private void refreshMenu(Player player) {
        Bukkit.getScheduler().runTaskLater(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("TitleManager")), () -> {
            MainMenu menu = new MainMenu(player);
            menu.open();
        }, 5L);
    }
}