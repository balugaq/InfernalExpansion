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

import java.util.concurrent.ThreadLocalRandom;

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
                int centerX = loc.getBlockX();
                int centerZ = loc.getBlockZ();
                int radius = 5;

                for (int x = centerX - radius; x < centerX + radius; x++) {
                    for (int z = centerZ - radius; z < centerZ + radius; z++) {
                        if (ThreadLocalRandom.current().nextFloat() > 0.9) {
                            Block block = getHighestBlock(player, x, z, radius);
                            if (block != null && block.getY() != loc.getBlockY() && x != centerX && z != centerZ) {
                                block.setType(Material.FIRE);
                            }
                        }
                    }
                }
            }
        };
    }

    private Block getHighestBlock(Player player, int x, int z, int radius) {
        Location playerLoc = player.getLocation();
        World world = player.getWorld();
        int centerY = playerLoc.getBlockY();
        Block block = null;
        for (int currentY = centerY - radius; currentY <= centerY + radius; currentY++) {
            Block actualBlock = world.getBlockAt(x, currentY, z);
            Block lastBlock = world.getBlockAt(x, currentY - 1, z);

            if (lastBlock.isSolid() && actualBlock.getType() == Material.AIR) {
                block = actualBlock;
                break;
            }
        }

        return block;
    }
}
