package net.yeditepemc.bixishub.listener;

import net.yeditepemc.bixishub.BixisHubPlugin;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

/**
 * Lobi mekaniklerine ait event handler'lari.
 */
public class HubListener implements Listener {

    private final BixisHubPlugin plugin;

    public HubListener(BixisHubPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Oyuncu girince 1 tick bekle, sonra adventure mode + spawn'a isinla.
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        // Katilma mesajini gizle
        event.joinMessage(null);

        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            player.setGameMode(GameMode.ADVENTURE);

            if (plugin.getSpawnManager().hasSpawn()) {
                Location spawn = plugin.getSpawnManager().getSpawn();
                player.teleport(spawn);
            }
        }, 1L);
    }

    /**
     * Cikma mesajini gizle.
     */
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.quitMessage(null);
    }

    /**
     * Aclik barini her zaman dolu tut (event'i engellemeden 20'ye sabitle).
     */
    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        event.setFoodLevel(20);
    }

    /**
     * Yeniden dogunca spawn noktasina isinla.
     */
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if (plugin.getSpawnManager().hasSpawn()) {
            event.setRespawnLocation(plugin.getSpawnManager().getSpawn());
        }
    }
}
