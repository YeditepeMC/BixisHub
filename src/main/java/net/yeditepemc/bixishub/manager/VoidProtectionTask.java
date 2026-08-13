package net.yeditepemc.bixishub.manager;

import net.yeditepemc.bixishub.BixisHubPlugin;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

/**
 * Void korumasi: belirli bir Y seviyesinin altina inen oyuncular
 * olmeden once spawn noktasina isinlanir.
 *
 * Move event yerine periyodik tarama kullanilir; move event oyuncu
 * basina tick'te birden fazla tetiklendigi icin bu kontrolde israftir.
 */
public class VoidProtectionTask extends BukkitRunnable {

    private static final long DEFAULT_INTERVAL_TICKS = 10L;

    private final BixisHubPlugin plugin;
    private final double yLevel;
    private final String worldName;

    public VoidProtectionTask(BixisHubPlugin plugin) {
        this.plugin = plugin;

        FileConfiguration config = plugin.getConfig();
        this.yLevel = config.getDouble("void-protection.y-level", 0.0);
        this.worldName = config.getString("void-protection.world", "");
    }

    /**
     * Void korumasi yapilandirmada acik mi?
     */
    public static boolean isEnabled(BixisHubPlugin plugin) {
        return plugin.getConfig().getBoolean("void-protection.enabled", true);
    }

    /**
     * Tarama araligi (tick). Gecersiz deger verilirse varsayilana doner.
     */
    public static long getCheckIntervalTicks(BixisHubPlugin plugin) {
        long interval = plugin.getConfig().getLong("void-protection.check-interval-ticks",
                DEFAULT_INTERVAL_TICKS);

        if (interval <= 0L) {
            plugin.getLogger().warning("Gecersiz void-protection.check-interval-ticks degeri: "
                    + interval + ". Varsayilan " + DEFAULT_INTERVAL_TICKS + " kullanilacak.");
            return DEFAULT_INTERVAL_TICKS;
        }

        return interval;
    }

    @Override
    public void run() {
        String targetWorld = resolveWorldName();
        if (targetWorld == null || targetWorld.isEmpty()) {
            return;
        }

        for (Player player : plugin.getServer().getOnlinePlayers()) {
            if (!player.getWorld().getName().equals(targetWorld)) {
                continue;
            }

            if (player.getLocation().getY() >= yLevel) {
                continue;
            }

            rescue(player);
        }
    }

    /**
     * Yapilandirilan dunya adi; bos birakilmissa kayitli spawn'in dunyasi.
     */
    private String resolveWorldName() {
        if (worldName != null && !worldName.isEmpty()) {
            return worldName;
        }

        Location spawn = plugin.getSpawnManager().getSpawn();
        return spawn == null ? null : spawn.getWorld().getName();
    }

    /**
     * Oyuncuyu spawn'a isinlar. Isinlamadan once dusme mesafesi ve hiz
     * sifirlanir; aksi halde oyuncu spawn'da dusme hasari alir ve
     * isinlanma sonrasi segirme olusur.
     */
    private void rescue(Player player) {
        Location target = plugin.getSpawnManager().hasSpawn()
                ? plugin.getSpawnManager().getSpawn()
                : fallbackSpawn(player);

        player.setFallDistance(0.0f);
        player.setVelocity(new Vector(0, 0, 0));
        player.teleport(target);
    }

    /**
     * Spawn kaydedilmemisse dunyanin kendi spawn konumu kullanilir.
     */
    private Location fallbackSpawn(Player player) {
        World world = player.getWorld();
        return world.getSpawnLocation();
    }
}
