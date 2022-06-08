package ga.kirzu.infernalexpansion;

import ga.kirzu.infernalexpansion.items.runes.ThermalWalkRune;
import ga.kirzu.infernalexpansion.items.tools.PortableNetherTeleporter;
import ga.kirzu.infernalexpansion.items.tools.PyromaniacBlazeRod;
import ga.kirzu.infernalexpansion.tasks.ThermalWalkTask;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.utils.itemstack.ColoredFireworkStar;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

public class InfernalSetup {

    private final static InfernalExpansion INSTANCE
            = InfernalExpansion.getInstance();

    private final static ItemGroup ITEM_GROUP
            = new ItemGroup(
            new NamespacedKey(INSTANCE, "infernal_runes"),
            new ColoredFireworkStar(Color.fromRGB(0, 0, 0), "&cInfernalExpansion")
    );

    private static boolean initialized = false;

    public InfernalSetup() {
        if (initialized) {
            return;
        }

        initialized = true;

        new ThermalWalkRune(ITEM_GROUP, InfernalItems.THERMAL_WALK_RUNE, RecipeType.ANCIENT_ALTAR, new ItemStack[]{
                SlimefunItems.MAGIC_LUMP_3, new ItemStack(Material.LAVA_BUCKET), SlimefunItems.MAGIC_LUMP_3,
                new ItemStack(Material.OBSIDIAN), SlimefunItems.FIRE_RUNE, new ItemStack(Material.OBSIDIAN),
                SlimefunItems.MAGIC_LUMP_3, new ItemStack(Material.LAVA_BUCKET), SlimefunItems.MAGIC_LUMP_3
        }).register(INSTANCE);
        INSTANCE.getServer().getScheduler().runTaskTimer(INSTANCE, new ThermalWalkTask(), 0L, 20L);

        new PortableNetherTeleporter(ITEM_GROUP, InfernalItems.PORTABLE_NETHER_TELEPORTER, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                new ItemStack(Material.NETHER_STAR), SlimefunItems.REINFORCED_PLATE, new ItemStack(Material.NETHER_STAR),
                new ItemStack(Material.OBSIDIAN), SlimefunItems.PORTABLE_TELEPORTER, new ItemStack(Material.OBSIDIAN),
                new ItemStack(Material.NETHER_STAR), SlimefunItems.REINFORCED_PLATE, new ItemStack(Material.NETHER_STAR),
        }).register(INSTANCE);

        new PyromaniacBlazeRod(ITEM_GROUP, InfernalItems.PYROMANIAC_BLAZE_ROD, RecipeType.MAGIC_WORKBENCH, new ItemStack[] {
                null, SlimefunItems.LAVA_CRYSTAL, null,
                null, new ItemStack(Material.BLAZE_ROD), null,
                null, new ItemStack(Material.FLINT_AND_STEEL), null
        }).register(INSTANCE);

    }
}
