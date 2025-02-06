package ro.iacobai.titleManager.gui;

import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import ro.iacobai.titleManager.models.Title;
import ro.iacobai.titleManager.models.TitleHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainMenu implements InventoryHolder {
    private final org.bukkit.inventory.Inventory titleGui;
    private final Player player;
    private final boolean changingPrefix;
    private static final NamespacedKey PREFIX_KEY = new NamespacedKey("titlemanager", "changing_prefix");

    public MainMenu(Player player) {
        this.player = player;
        this.titleGui = Bukkit.createInventory(this, 54, "Title Manager");

        // Retrieve changingPrefix state from PersistentDataContainer
        PersistentDataContainer data = player.getPersistentDataContainer();
        this.changingPrefix = data.getOrDefault(PREFIX_KEY, PersistentDataType.BYTE, (byte) 1) == 1;

        updateMenu();
    }

    public void updateMenu() {
        titleGui.clear();
        addFillerGlass();
        addTitlesToMenu();
        addPlayerHead();
        addCloseButton();
        addTitleSettings();
        addDeleteButton();
    }

    private void addTitlesToMenu() {
        List<Title> titles = new ArrayList<>(TitleHandler.getInstance().getTitles());

        // Loop through all the titles and only add the ones that the player has access to
        for (int i = 0; i < titles.size() && i < 45; i++) {
            Title title = titles.get(i);

            if (player.hasPermission(title.getPermission())) {
                ItemStack item = createTitleItem(title);
                titleGui.setItem(i, item);
            }
        }
    }

    private ItemStack createTitleItem(Title title) {
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
        String currentTitle = PlaceholderAPI.setPlaceholders(player, "%uperms_prefix%");
        if (!changingPrefix) {
            currentTitle = PlaceholderAPI.setPlaceholders(player, "%uperms_suffix%");
        }

        if (currentTitle.toLowerCase().contains(title.getName().toLowerCase())) {
            meta.addEnchant(org.bukkit.enchantments.Enchantment.MENDING, 1, true);
            meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);
            lore.add(Component.text("(Currently Selected)", NamedTextColor.GREEN));
        }

        meta.lore(lore);
        item.setItemMeta(meta);
        return item;
    }

    private void addPlayerHead() {
        // Add the player head as a placeholder for the current title
        ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta playerHeadMeta = (SkullMeta) playerHead.getItemMeta();

        // Set the player head to the player's head skin
        playerHeadMeta.setOwningPlayer(player);

        // Add the prefix, suffix, and the name of the player to the player head
        String displayName = buildPlayerDisplayName();
        playerHeadMeta.displayName(Component.text(displayName));
        playerHead.setItemMeta(playerHeadMeta);

        titleGui.setItem(49, playerHead);
    }

    private String buildPlayerDisplayName() {
        StringBuilder displayName = new StringBuilder();

        // Add the prefix to the display name if it exists
        String prefix = PlaceholderAPI.setPlaceholders(player, "%uperms_prefix%");
        if (!prefix.isEmpty()) {
            displayName.append(prefix).append(" ");
        }

        // Add the player name to the display name
        displayName.append("§7").append(player.getName());

        // Add the suffix to the display name if it exists
        String suffix = PlaceholderAPI.setPlaceholders(player, "%uperms_suffix%");
        if (!suffix.isEmpty()) {
            displayName.append(" ").append(suffix);
        }

        return displayName.toString();
    }

    private void addCloseButton() {
        // Add the close button as a barrier
        ItemStack close = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = close.getItemMeta();

        closeMeta.displayName(Component.text("Close", NamedTextColor.RED));
        close.setItemMeta(closeMeta);

        titleGui.setItem(45, close);
    }

    private void addFillerGlass() {
        // Add filler glass panes to the menu
        ItemStack filler = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta fillerMeta = filler.getItemMeta();

        fillerMeta.displayName(Component.text(" ", NamedTextColor.GRAY));
        filler.setItemMeta(fillerMeta);

        for (int i = 0; i < 54; i++) {
            titleGui.setItem(i, filler);
        }
    }

    private void addTitleSettings() {
        // Add the title settings button
        ItemStack settings = new ItemStack(Material.COMPARATOR);
        ItemMeta settingsMeta = settings.getItemMeta();

        settingsMeta.displayName(Component.text("Changing:", NamedTextColor.GRAY));
        List<Component> settingsLore = new ArrayList<>();

        // Check if the player is changing the prefix or suffix
        if (changingPrefix) {
            settingsLore.add(Component.text("● prefix", NamedTextColor.GRAY));
            settingsLore.add(Component.text("○ suffix", NamedTextColor.GRAY));
        } else {
            settingsLore.add(Component.text("○ prefix", NamedTextColor.GRAY));
            settingsLore.add(Component.text("● suffix", NamedTextColor.GRAY));
        }

        settingsMeta.lore(settingsLore);
        settings.setItemMeta(settingsMeta);

        titleGui.setItem(53, settings);
    }

    private void addDeleteButton() {
        // Add the delete button as a redstone block
        ItemStack delete = new ItemStack(Material.REDSTONE_BLOCK);
        ItemMeta deleteMeta = delete.getItemMeta();

        deleteMeta.displayName(Component.text("Delete", NamedTextColor.RED));
        delete.setItemMeta(deleteMeta);

        titleGui.setItem(52, delete);
    }

    public void open() {
        player.openInventory(titleGui);
    }

    @Override
    public @NotNull org.bukkit.inventory.Inventory getInventory() {
        return titleGui;
    }
}