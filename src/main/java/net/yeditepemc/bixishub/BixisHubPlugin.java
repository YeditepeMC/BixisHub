package net.yeditepemc.bixishub;

import net.yeditepemc.bixishub.listener.HubListener;
import net.yeditepemc.bixishub.manager.CrateManager;
import net.yeditepemc.bixishub.manager.SpawnManager;
import net.yeditepemc.bixishub.manager.VoidProtectionTask;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

/**
 * BixisHub — YeditepeMC lobi sunucusu temel mekanik plugini.
 */
public class BixisHubPlugin extends JavaPlugin {

    private SpawnManager spawnManager;
    private CrateManager crateManager;

    @Override
    public void onEnable() {
        // Varsayilan config.yml'i olustur ve verileri yukle
        saveDefaultConfig();

        this.spawnManager = new SpawnManager(this);
        this.spawnManager.loadSpawn();

        this.crateManager = new CrateManager(this);
        this.crateManager.loadCrate();

        // Listener kaydi
        getServer().getPluginManager().registerEvents(new HubListener(this), this);

        // Void korumasi tarama gorevi
        if (VoidProtectionTask.isEnabled(this)) {
            long interval = VoidProtectionTask.getCheckIntervalTicks(this);
            new VoidProtectionTask(this).runTaskTimer(this, interval, interval);
        }

        getLogger().info("BixisHub etkinlestirildi.");
    }

    @Override
    public void onDisable() {
        getLogger().info("BixisHub devre disi birakildi.");
    }

    public SpawnManager getSpawnManager() {
        return spawnManager;
    }

    public CrateManager getCrateManager() {
        return crateManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {
        String name = command.getName().toLowerCase();

        if (name.equals("setspawn")) {
            return handleSetSpawn(sender);
        }

        if (name.equals("spawn")) {
            return handleSpawn(sender);
        }

        if (name.equals("setkasa")) {
            return handleSetKasa(sender);
        }

        if (name.equals("kasa")) {
            return handleKasa(sender);
        }

        return false;
    }

    private boolean handleSetSpawn(CommandSender sender) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Bu komut sadece oyuncular tarafindan kullanilabilir.");
            return true;
        }

        if (!player.hasPermission("bixishub.admin")) {
            player.sendMessage("Bu komutu kullanmak icin yetkiniz yok.");
            return true;
        }

        spawnManager.saveSpawn(player.getLocation());
        player.sendMessage("Spawn noktasi mevcut konumunuza ayarlandi.");
        return true;
    }

    private boolean handleSpawn(CommandSender sender) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Bu komut sadece oyuncular tarafindan kullanilabilir.");
            return true;
        }

        if (!spawnManager.hasSpawn()) {
            player.sendMessage("Spawn noktasi henuz ayarlanmamis.");
            return true;
        }

        Location spawn = spawnManager.getSpawn();
        player.teleport(spawn);
        player.sendMessage("Spawn noktasina isinlandiniz.");
        return true;
    }

    private boolean handleSetKasa(CommandSender sender) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Bu komut sadece oyuncular tarafindan kullanilabilir.");
            return true;
        }

        if (!player.hasPermission("bixishub.admin")) {
            player.sendMessage("Bu komutu kullanmak icin yetkiniz yok.");
            return true;
        }

        crateManager.saveCrate(player.getLocation());
        player.sendMessage("Kasa alani mevcut konumunuza ayarlandi.");
        return true;
    }

    private boolean handleKasa(CommandSender sender) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Bu komut sadece oyuncular tarafindan kullanilabilir.");
            return true;
        }

        if (!crateManager.hasCrate()) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cKasa alani henuz ayarlanmamis."));
            return true;
        }

        Location crate = crateManager.getCrate();
        player.teleport(crate);
        player.sendMessage("Kasa alanina isinlandiniz.");
        return true;
    }
}
