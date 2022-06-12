package ga.kirzu.infernalexpansion.items.tools;

import ga.kirzu.infernalexpansion.InfernalExpansion;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.Rechargeable;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class FireballLauncher extends SlimefunItem implements Rechargeable {

    private final static InfernalExpansion INSTANCE
            = InfernalExpansion.getInstance();
    private final static float COST = 50;

    public FireballLauncher(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
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
            int radius = 2;
            event.cancel();

            if (removeItemCharge(item, COST)) {
                Location loc = player.getLocation();

                new BukkitRunnable() {
                    double y = 0;

                    @Override
                    public void run() {
                        if (y <= 1) {
                            y += .2;
                            Location newLoc = loc.clone();
                            newLoc.setY(loc.getY() + y);
                            drawCircle(player.getWorld(), newLoc, radius - (y * radius));
                        } else {
                            Location eyeLoc = player.getEyeLocation();
                            Vector direction = eyeLoc.getDirection().multiply(2);
                            Fireball fireball = player.getWorld().spawn(eyeLoc, Fireball.class);
                            fireball.setShooter(player);
                            fireball.setVelocity(direction);
                            this.cancel();
                        }
                    }
                }.runTaskTimer(INSTANCE, 0L, 2L);
            }
        };
    }

    private void drawCircle(World world, Location loc, double radius) {
        for (int i = 0; i < 90; i++) {
            double x = radius * Math.sin(i);
            double z = radius * Math.cos(i);

            double centerX = loc.getX();
            double centerZ = loc.getZ();
            world.spawnParticle(Particle.FLAME, centerX + x, loc.getY(), centerZ + z, 1, 0, 0, 0, 0);
        }
    }
}
