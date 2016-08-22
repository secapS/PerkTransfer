package xyz.whynospaces.perktransfer;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadCommand implements CommandExecutor {

    public boolean onCommand(CommandSender commandSender, Command command, String commandLabel, String[] args) {
        if(command.getName().equalsIgnoreCase("perkreload")) {
            PerkTransfer.instance.reloadConfig();
            commandSender.sendMessage("[PerkTransfer] " + "reload complete.");
            return true;
        }
        return false;
    }
}
