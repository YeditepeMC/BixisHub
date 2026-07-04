package net.yeditepemc.bixishub.manager;

import net.yeditepemc.bixishub.BixisHubPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * Kasa alaninin config.yml uzerinden yonetimi.
 */
public class CrateManager {

    private final BixisHubPlugin plugin;
    private Location crate;

    public CrateManager(BixisHubPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * config.yml'den kasa konumunu okur.
     */
    public void loadCrate() {
        FileConfiguration config = plugin.getConfig();
        String worldName = config.getString("crate.world", "");

        if (worldName == null || worldName.isEmpty()) {
            this.crate = null;
            return;
        }

        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            plugin.getLogger().warning("Kasa dunyasi bulunamadi: " + worldName);
            this.crate = null;
            return;
        }

        double x = config.getDouble("crate.x");
        double y = config.getDouble("crate.y");
        double z = config.getDouble("crate.z");
        float yaw = (float) config.getDouble("crate.yaw");
        float pitch = (float) config.getDouble("crate.pitch");

        this.crate = new Location(world, x, y, z, yaw, pitch);
    }

    /**
     * Verilen konumu kasa alani olarak config.yml'e yazar.
     */
    public void saveCrate(Location location) {
        this.crate = location.clone();

        FileConfiguration config = plugin.getConfig();
        config.set("crate.world", location.getWorld().getName());
        config.set("crate.x", location.getX());
        config.set("crate.y", location.getY());
        config.set("crate.z", location.getZ());
        config.set("crate.yaw", location.getYaw());
        config.set("crate.pitch", location.getPitch());
        plugin.saveConfig();
    }

    /**
     * Kayitli kasa konumunu dondurur (yoksa null).
     */
    public Location getCrate() {
        return crate == null ? null : crate.clone();
    }

    /**
     * Kasa alani ayarlanmis mi?
     */
    public boolean hasCrate() {
        return crate != null;
    }
}
