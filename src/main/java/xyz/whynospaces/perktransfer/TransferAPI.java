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
                        PerkTransfer.perms.playerRemove(null, tradingPlayer, permission);
                        PerkTransfer.perms.playerAdd(null, receivingPlayer, permission);
                        tradingPlayer.sendMessage(ChatColor.GREEN + "Success! " + ChatColor.GRAY + "You transfered " + perk.toUpperCase() + " to " + receivingPlayer.getName());
                        receivingPlayer.sendMessage(ChatColor.GREEN + "Success! " + ChatColor.GRAY + "You received " + perk.toUpperCase() + " from " + tradingPlayer.getName());
                        break;
                    case MULTIPLE_PERMISSIONS:
                        List<String> permissions = PerkTransfer.instance.getConfig().getStringList("perks." + perk + ".permissions");
                        for(String perms : permissions) {
                            PerkTransfer.perms.playerRemove(null, tradingPlayer, perms);
                            PerkTransfer.perms.playerAdd(null, receivingPlayer, perms);
                        }
                        tradingPlayer.sendMessage(ChatColor.GREEN + "Success! " + ChatColor.GRAY + "You transfered " + perk.toUpperCase() + " to " + receivingPlayer.getName());
                        receivingPlayer.sendMessage(ChatColor.GREEN + "Success! " + ChatColor.GRAY + "You received " + perk.toUpperCase() + " from " + tradingPlayer.getName());
                        break;
                    case GROUP:
                        String group = PerkTransfer.instance.getConfig().getString("perks." + perk + ".group");
                        PerkTransfer.perms.playerRemoveGroup(null, tradingPlayer, group);
                        PerkTransfer.perms.playerAddGroup(null, receivingPlayer, group);
                        tradingPlayer.sendMessage(ChatColor.GREEN + "Success! " + ChatColor.GRAY + "You transfered " + perk.toUpperCase() + " to " + receivingPlayer.getName());
                        receivingPlayer.sendMessage(ChatColor.GREEN + "Success! " + ChatColor.GRAY + "You received " + perk.toUpperCase() + " from " + tradingPlayer.getName());
                }
            } else {
                tradingPlayer.sendMessage(ChatColor.RED + "Transfer failed! " + receivingPlayer.getName() + " already has " + perk.toUpperCase());
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
                return (PerkTransfer.perms.playerHas(player, permission));
            }
            else if(PerkTransfer.instance.getConfig().getString("perks." + perk + ".permissions") != null) {
                List<String> permissions = PerkTransfer.instance.getConfig().getStringList("perks." + perk + ".permissions");
                return (PerkTransfer.perms.playerHas(player, permissions.get(0)));
            }
            else if(PerkTransfer.instance.getConfig().getString("perks." + perk + ".group") != null) {
                String group = PerkTransfer.instance.getConfig().getString("perks." + perk + ".group");
                return (PerkTransfer.perms.playerInGroup(player, group));
            }
        } else {
            player.sendMessage(ChatColor.RED +  perk.toUpperCase() + " is not a real perk!");
            return true;
        }
        return true;
    }
}