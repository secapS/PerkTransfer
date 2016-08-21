package xyz.whynospaces.perktransfer;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

public class TransferAPI {

    public void transferPerk(String perk, Player tradingPlayer, Player receivingPlayer) {
        if(playerHasPerk(tradingPlayer, perk)) {
            if(!playerHasPerk(receivingPlayer, perk)) {
                switch(getPerkType(perk)) {
                    case PERMISSION:
                        String permission = PerkTransfer.instance.getConfig().getString("perks." + perk + ".permission");
                        PerkTransfer.perms.playerRemove(tradingPlayer, permission);
                        PerkTransfer.perms.playerAdd(receivingPlayer, permission);
                        tradingPlayer.sendMessage(ChatColor.GREEN + "Trade successful!");
                        receivingPlayer.sendMessage(ChatColor.GREEN + "Trade successful!");
                        break;
                    case MULTIPLE_PERMISSIONS:
                        List<String> permissions = PerkTransfer.instance.getConfig().getStringList("perks." + perk + ".permissions");
                        for(String perms : permissions) {
                            PerkTransfer.perms.playerRemove(tradingPlayer, perms);
                            PerkTransfer.perms.playerAdd(receivingPlayer, perms);
                        }
                        tradingPlayer.sendMessage(ChatColor.GREEN + "Trade successful!");
                        receivingPlayer.sendMessage(ChatColor.GREEN + "Trade successful!");
                        break;
                    case GROUP:
                        String group = PerkTransfer.instance.getConfig().getString("perks." + perk + ".group");
                        PerkTransfer.perms.playerRemoveGroup(tradingPlayer, group);
                        PerkTransfer.perms.playerAddGroup(receivingPlayer, group);
                        tradingPlayer.sendMessage(ChatColor.GREEN + "Trade successful!");
                        receivingPlayer.sendMessage(ChatColor.GREEN + "Trade successful!");
                }
            } else {
                tradingPlayer.sendMessage(ChatColor.RED + "Trade failed!" + receivingPlayer.getName() + " already has this perk!");
                return;
            }
        } else {
            tradingPlayer.sendMessage(ChatColor.RED + "Transfer failed! You do not have this perk!");
            return;
        }
    }

    /**
     * Enum to check for different perk types.
     */
    enum PerkType {
        PERMISSION,
        MULTIPLE_PERMISSIONS,
        GROUP;
    }

    public PerkType getPerkType(String perk) {
        if(checkPerk(perk)) {
            if(PerkTransfer.instance.getConfig().getString("perks." + perk + ".permission") != null) {
                return PerkType.PERMISSION;
            }
            else if(PerkTransfer.instance.getConfig().getString("perks." + perk + ".permissions") != null) {
                return PerkType.MULTIPLE_PERMISSIONS;
            }
            else if(PerkTransfer.instance.getConfig().getString("perks." + perk + ".group") != null) {
                return PerkType.GROUP;
            } else {
                return null;
            }
        }
        return null;
    }

    /**
     * Checks if @param perk is real or not
     * @return true if @param perk is real, false if not.
     */
    public boolean checkPerk(String perk) {
        return PerkTransfer.instance.getConfig().getConfigurationSection("perks").getKeys(false).contains(perk);
    }

    /**
     * Checks if player has specified perk or not.
     * @param player
     * @param perk
     * @return
     */
    public boolean playerHasPerk(Player player, String perk) {
        if(checkPerk(perk)) {
            if(PerkTransfer.instance.getConfig().getString("perks." + perk + ".permission") != null) {
                String permission = PerkTransfer.instance.getConfig().getString("perks." + perk + ".permission");
                if(PerkTransfer.perms.playerHas(player, permission)) {
                    player.sendMessage(ChatColor.RED + "You already have " + perk.toUpperCase() + ".");
                    return true;
                } else {
                    return false;
                }
            }
            else if(PerkTransfer.instance.getConfig().getString("perks." + perk + ".permissions") != null) {
                List<String> permissions = PerkTransfer.instance.getConfig().getStringList("perks." + perk + ".permissions");
                if(PerkTransfer.perms.playerHas(player, permissions.get(0))) {
                    player.sendMessage(ChatColor.RED + "You already have " + perk.toUpperCase() + ".");
                    return true;
                } else {
                    return false;
                }
            }
            else if(PerkTransfer.instance.getConfig().getString("perks." + perk + ".group") != null) {
                String group = PerkTransfer.instance.getConfig().getString("perks." + perk + ".group");
                if(PerkTransfer.perms.playerInGroup(player, group)) {
                    player.sendMessage(ChatColor.RED + "You already have " + perk.toUpperCase() + ".");
                    return true;
                } else {
                    return false;
                }
            }
        } else {
            player.sendMessage(ChatColor.RED + "That's not a real perk!");
            return true;
        }
        return true;
    }
}
