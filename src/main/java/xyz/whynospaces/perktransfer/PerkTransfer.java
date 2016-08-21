package xyz.whynospaces.perktransfer;

import net.milkbowl.vault.permission.Permission;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PerkTransfer extends JavaPlugin {

    public static final Logger LOGGER = Logger.getLogger("PerkTransfer");
    public static Permission perms = null;
    public static PerkTransfer instance = null;

    public TransferAPI transferAPI = null;

    @Override
    public void onEnable() {
        instance = this;
        setupPermissions();
        createConfig();
        this.getCommand("perktransfer").setExecutor(new TransferCommand());
        transferAPI = new TransferAPI();
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }

    public void createConfig() {
        if(!this.getDataFolder().exists()) {
            LOGGER.log(Level.CONFIG, "creating plugin folder..");
            this.getDataFolder().mkdirs();
        }

        File configFile = new File(this.getDataFolder(), "config.yml");
        if(!configFile.exists()) {
            LOGGER.log(Level.CONFIG, "creating default config..");
            this.saveDefaultConfig();
        }
    }
}
