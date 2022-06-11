package ga.kirzu.infernalexpansion.tasks;

import ga.kirzu.infernalexpansion.InfernalExpansion;
import ga.kirzu.infernalexpansion.InfernalUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.Levelled;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class ThermalWalkTask implements Runnable {
    private final static InfernalExpansion INSTANCE
            = InfernalExpansion.getInstance();

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!player.isValid() || player.isDead()) {
                return;
            }

            ItemStack boots = player.getInventory().getBoots();
            if (!InfernalUtils.hasThermalWalk(boots)) {
                continue;
            }

            player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 40, 0, false, false, false));
            createObsidianFloor(player);
        }
    }

    private void createObsidianFloor(Player player) {
        Location playerLoc = player.getLocation();
        int cx = playerLoc.getBlockX();
        int cy = playerLoc.getBlockY() - 1;
        int cz = playerLoc.getBlockZ();
        World w = playerLoc.getWorld();
        int radius = 3;
        List<Location> blocks = new ArrayList<>();
        for (int x = cx - radius; x <= cx + radius; x++) {
            for (int z = cz - radius; z <= cz + radius; z++) {
                if ((cx - x) * (cx - x) + (cz - z) * (cz - z) <= radius * radius) {
                    Block block = w.getBlockAt(x, cy, z);
                    if (block.getType() == Material.LAVA && ((Levelled) block.getBlockData()).getLevel() == 0) {
                        block.setType(Material.OBSIDIAN);
                        blocks.add(block.getLocation());
                    }
                }
            }
        }
        cleanupFloor(player, blocks);
    }

    private void cleanupFloor(Player player, List<Location> blocks) {
        new BukkitRunnable() {
            int count = 0;

            @Override
            public void run() {
                if (count <= 5) {
                    for (Location loc : blocks) {
                        player.sendBlockDamage(loc, ((float) count / 5));
                    }
                    ++count;
                } else {
                    for (Location loc : blocks) {
                        Block block = loc.getBlock();
                        if (block.getType() == Material.OBSIDIAN) {
                            block.setType(Material.LAVA);
                        }
                    }
                    this.cancel();
                }
            }
        }.runTaskTimer(INSTANCE, 20L, 20L);
    }
}