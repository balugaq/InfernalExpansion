package ga.kirzu.infernalexpansion.items.tools;

import ga.kirzu.infernalexpansion.InfernalExpansion;
import ga.kirzu.infernalexpansion.InfernalUtils;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.Rechargeable;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.libraries.dough.config.Config;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PortableNetherTeleporter extends SlimefunItem implements Rechargeable {

    private final static float COST = 125;
    private final static Config CONFIG
            = InfernalExpansion.getCfg();
    private final static InfernalExpansion INSTANCE
            = InfernalExpansion.getInstance();

    private final List<UUID> beingTeleported = new ArrayList<>();

    public PortableNetherTeleporter(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Override
    public float getMaxItemCharge(ItemStack itemStack) {
        return 250;
    }

    @Override
    public void preRegister() {
        addItemHandler(handleItemUse());
    }

    private ItemUseHandler handleItemUse() {
        return (event) -> {
            Player player = event.getPlayer();
            ItemStack item = event.getItem();
            event.cancel();

            Server server = player.getServer();

            if (!server.getAllowNether()) {
                InfernalUtils.sendMessage(player, "portable-nether-teleporter.nether-disabled");
                return;
            }

            if (!CONFIG.contains("default-nether-world")) {
                InfernalUtils.sendMessage(player, "portable-nether-teleporter.nether-world-not-set");
                return;
            }

            if (CONFIG.contains("travel-to-nether-from-end")) {
                if (!CONFIG.getBoolean("travel-to-nether-from-end") && player.getWorld().getEnvironment() == World.Environment.THE_END) {
                    InfernalUtils.sendMessage(player, "portable-nether-teleporter.cant-use-in-end");
                    return;
                }
            }

            World world = server.getWorld(CONFIG.getString("default-nether-world"));

            if (world == null) {
                InfernalUtils.sendMessage(player, "portable-nether-teleporter.nether-world-not-found");
                return;
            }

            if (world.getUID() == player.getWorld().getUID()) {
                InfernalUtils.sendMessage(player, "portable-nether-teleporter.already-on-world");
                return;
            }

            if (beingTeleported.contains(player.getUniqueId())) {
                InfernalUtils.sendMessage(player, "portable-nether-teleporter.already-being-teleported");
                return;
            }

            if (removeItemCharge(item, COST)) {
                InfernalUtils.sendMessage(player, "portable-nether-teleporter.teleporting");
                beingTeleported.add(player.getUniqueId());

                Location loc = player.getLocation();
                player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 160, 1, false, false, false));
                new BukkitRunnable() {
                    int count = 3;

                    @Override
                    public void run() {
                        if (count > 0) {
                            spawnParticles(loc, count);
                            player.playSound(loc, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                            --count;
                        } else {
                            player.playSound(loc, Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                            player.teleport(world.getSpawnLocation());
                            beingTeleported.remove(player.getUniqueId());
                            this.cancel();
                        }
                    }
                }.runTaskTimer(INSTANCE, 0L, 20L);
            }
        };
    }

    public void spawnParticles(Location loc, int radius) {
        for (int degree = 0; degree < 360; degree++) {
            double radians = Math.toRadians(degree);
            double x = Math.cos(radians) * radius;
            double z = Math.sin(radians) * radius;
            loc.add(x, 0, z);
            loc.getWorld().spawnParticle(degree % 2 == 0 ? Particle.SOUL_FIRE_FLAME : Particle.FLAME, loc, 1);
            loc.subtract(x, 0, z);
        }
    }
}
