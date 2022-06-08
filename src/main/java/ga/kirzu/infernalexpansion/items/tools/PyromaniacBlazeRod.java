package ga.kirzu.infernalexpansion.items.tools;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.Rechargeable;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class PyromaniacBlazeRod extends SlimefunItem implements Rechargeable {

    private final static float COST = 25;

    public PyromaniacBlazeRod(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Override
    public float getMaxItemCharge(ItemStack itemStack) {
        return 200;
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

            if (removeItemCharge(item, COST)) {
                Location loc = player.getLocation();
                World world = player.getWorld();
                int centerX = loc.getBlockX();
                int centerZ = loc.getBlockZ();
                int playerY = loc.getBlockY();
                int radius = 5;

                for (int x = centerX - radius; x < centerX + radius; x++) {
                    for (int z = centerZ - radius; z < centerZ + radius; z++) {
                        if (new Random().nextFloat() > 0.9) {
                            int y = player.getWorld().getHighestBlockYAt(x, z);
                            Block block1 = new Location(world, x, y, z).getBlock();
                            Block block2 = new Location(world, x, y + 1, z).getBlock();
                            if (Math.abs(y - playerY) <= radius) {
                                if (block1.isSolid() && block2.getType() == Material.AIR) {
                                    block2.setType(Material.FIRE);
                                }
                            }
                        }
                    }
                }
            }
        };
    }
}
