package ro.iacobai.titleManager;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ro.iacobai.titleManager.Gui.Inventory;

public class TitleCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // check if the sender is a player
        if (!(sender instanceof Player)) {
            sender.sendMessage(NamedTextColor.RED + "Only players can use this command!");
            return true;
        }

        // cast the sender to a player
        Player player = (Player) sender;

        // open the title gui
        player.openInventory(new Inventory(player).getInventory());

        return true;
    }
}
