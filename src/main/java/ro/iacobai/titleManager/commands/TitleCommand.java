package ro.iacobai.titleManager.commands;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ro.iacobai.titleManager.gui.MainMenu;

public class TitleCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Check if the sender is a player
        if (!(sender instanceof Player)) {
            sender.sendMessage(NamedTextColor.RED + "Only players can use this command!");
            return true;
        }

        // Cast the sender to a player
        Player player = (Player) sender;

        // Open the title GUI for the player
        MainMenu mainMenu = new MainMenu(player);
        mainMenu.open();
        return true;
    }
}
