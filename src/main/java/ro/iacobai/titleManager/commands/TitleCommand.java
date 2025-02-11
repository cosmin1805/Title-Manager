package ro.iacobai.titleManager.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ro.iacobai.titleManager.gui.MainMenu;
import ro.iacobai.titleManager.handler.ConfigHandler;

public class TitleCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        // Check arguments, if there is a reload argument, reload the config
        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            // Check if the sender has the permission to reload the config
            if (!sender.hasPermission("titlemanager.reload")) {
                sender.sendMessage(Component.text("You don't have permission to reload the config!", NamedTextColor.RED));
                return true;
            }

            // Reload the config
            ConfigHandler.reloadTitles();

            sender.sendMessage(Component.text("Title Manager: Config reloaded!", NamedTextColor.GREEN));
            return true;
        }

        // Check for any other arguments, and if there are any, return
        if (args.length > 0) {
            sender.sendMessage(Component.text("Invalid arguments!", NamedTextColor.RED));
            return false;
        }

        // Check if the sender is a player
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("Only players can use this command!", NamedTextColor.RED));
            return true;
        }

        // Open the title GUI for the player
        MainMenu mainMenu = new MainMenu(player);
        mainMenu.open();
        return true;
    }
}