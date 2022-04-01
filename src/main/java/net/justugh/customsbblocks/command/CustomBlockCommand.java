package net.justugh.customsbblocks.command;

import net.justugh.customsbblocks.CustomSBBlocks;
import net.justugh.japi.util.Format;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CustomBlockCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(Format.format("&cUsage: /cstmb give <player> <key>"));
            return true;
        }

        if (args.length >= 3) {
            if (args[0].equalsIgnoreCase("give")) {
                Player player = Bukkit.getPlayer(args[1]);

                if (player == null) {
                    sender.sendMessage(Format.format("&cInvalid player."));
                    return true;
                }

                if (CustomSBBlocks.getInstance().getItems().keySet().stream().noneMatch(key -> key.equalsIgnoreCase(args[2]))) {
                    sender.sendMessage(Format.format("&cInvalid item."));
                    sender.sendMessage(Format.format("&aValid Items:"));
                    CustomSBBlocks.getInstance().getItems().keySet().forEach(key -> sender.sendMessage(Format.format("&8- &a" + key)));
                    return true;
                }

                player.getInventory().addItem(CustomSBBlocks.getInstance().getItems().entrySet().stream().filter(entry -> entry.getKey().equalsIgnoreCase(args[2])).findFirst().orElse(null).getValue());
            }

            sender.sendMessage(Format.format("&cUsage: /cstmb give <player> <key>"));
        }

        return true;
    }

}
