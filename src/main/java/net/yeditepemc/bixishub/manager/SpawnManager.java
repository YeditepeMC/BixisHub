package net.yeditepemc.bixishub.manager;

import net.yeditepemc.bixishub.BixisHubPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * Spawn noktasinin config.yml uzerinden yonetimi.
 */
public class SpawnManager {

    private final BixisHubPlugin plugin;
    private Location spawn;

    public SpawnManager(BixisHubPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * config.yml'den spawn konumunu okur.
     */
    public void loadSpawn() {
        FileConfiguration config = plugin.getConfig();
        String worldName = config.getString("spawn.world", "");

        if (worldName == null || worldName.isEmpty()) {
            this.spawn = null;
            return;
        }

        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            plugin.getLogger().warning("Spawn dunyasi bulunamadi: " + worldName);
            this.spawn = null;
            return;
        }

        double x = config.getDouble("spawn.x");
        double y = config.getDouble("spawn.y");
        double z = config.getDouble("spawn.z");
        float yaw = (float) config.getDouble("spawn.yaw");
        float pitch = (float) config.getDouble("spawn.pitch");

        this.spawn = new Location(world, x, y, z, yaw, pitch);
    }

    /**
     * Verilen konumu spawn olarak config.yml'e yazar.
     */
    public void saveSpawn(Location location) {
        this.spawn = location.clone();

        FileConfiguration config = plugin.getConfig();
        config.set("spawn.world", location.getWorld().getName());
        config.set("spawn.x", location.getX());
        config.set("spawn.y", location.getY());
        config.set("spawn.z", location.getZ());
        config.set("spawn.yaw", location.getYaw());
        config.set("spawn.pitch", location.getPitch());
        plugin.saveConfig();
    }

    /**
     * Kayitli spawn konumunu dondurur (yoksa null).
     */
    public Location getSpawn() {
        return spawn == null ? null : spawn.clone();
    }

    /**
     * Spawn ayarlanmis mi?
     */
    public boolean hasSpawn() {
        return spawn != null;
    }
}
