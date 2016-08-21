package xyz.whynospaces.perktransfer;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TransferCommand implements CommandExecutor {

    // /perktransfer <perk> <player>
    public boolean onCommand(CommandSender commandSender, Command command, String commandLabel, String[] args) {
        if(!(commandSender instanceof Player)) {
            commandSender.sendMessage(ChatColor.RED + "You must be a player to use this command.");
            return false;
        }

        Player player = (Player)commandSender;

        if(command.getName().equalsIgnoreCase("perktransfer")) {
            if(args.length <= 1) {
                player.sendMessage(ChatColor.RED + "Usage: /perktransfer <perk> <player>");
                ComponentBuilder message = new ComponentBuilder("Transferable Perks: ").color(ChatColor.GRAY);
                int i = 0;
                for(String perks : PerkTransfer.instance.getConfig().getConfigurationSection("perks").getKeys(false)) {
                    i++;
                    message.append(perks).color(PerkTransfer.instance.transferAPI.playerHasPerk(player, perks) ? ChatColor.GREEN : ChatColor.RED)
                        .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/perktransfer " + perks + " "))
                            .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(PerkTransfer.instance.getConfig().getString("perks." + perks + ".description")).create()));
                    if(i == PerkTransfer.instance.getConfig().getConfigurationSection("perks").getKeys(false).size()) {
                        message.append(".").color(ChatColor.GRAY);
                    } else {
                        message.append(", ").color(ChatColor.GRAY);
                    }
                }
                player.spigot().sendMessage(message.create());
            }
            else if(args.length == 2) {
                if(Bukkit.getPlayer(args[1]) != null) {
                    PerkTransfer.instance.transferAPI.transferPerk(args[0], player, Bukkit.getPlayer(args[1]));
                } else {
                    player.sendMessage(ChatColor.RED + args[1] + " is not online!");
                }
            }
            return true;
        }
        return false;
    }
}
