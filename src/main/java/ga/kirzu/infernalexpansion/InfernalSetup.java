package ga.kirzu.infernalexpansion;

import ga.kirzu.infernalexpansion.items.blocks.PiglinAttractor;
import ga.kirzu.infernalexpansion.items.runes.BedRune;
import ga.kirzu.infernalexpansion.items.runes.ThermalWalkRune;
import ga.kirzu.infernalexpansion.items.talismans.PiglinTalisman;
import ga.kirzu.infernalexpansion.items.tools.FireballLauncher;
import ga.kirzu.infernalexpansion.items.tools.PortableNetherTeleporter;
import ga.kirzu.infernalexpansion.items.tools.PyromaniacBlazeRod;
import ga.kirzu.infernalexpansion.listeners.RuneListener;
import ga.kirzu.infernalexpansion.listeners.TalismanListener;
import ga.kirzu.infernalexpansion.tasks.ThermalWalkTask;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.utils.itemstack.ColoredFireworkStar;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Server;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;

public class InfernalSetup {

    private final static InfernalExpansion INSTANCE
            = InfernalExpansion.getInstance();

    private final static ItemGroup ITEM_GROUP
            = new ItemGroup(
            new NamespacedKey(INSTANCE, "infernal_runes"),
            new ColoredFireworkStar(Color.fromRGB(0, 0, 0), "&c下界工艺")
    );

    private static boolean initialized = false;

    public InfernalSetup() {
        if (initialized) {
            return;
        }

        initialized = true;

        Server server = INSTANCE.getServer();
        PluginManager pm = server.getPluginManager();

        pm.registerEvents(new TalismanListener(), INSTANCE);
        pm.registerEvents(new RuneListener(), INSTANCE);

        new ThermalWalkRune(ITEM_GROUP, InfernalItems.THERMAL_WALK_RUNE, RecipeType.ANCIENT_ALTAR, new ItemStack[]{
                SlimefunItems.MAGIC_LUMP_3, new ItemStack(Material.LAVA_BUCKET), SlimefunItems.MAGIC_LUMP_3,
                new ItemStack(Material.OBSIDIAN), SlimefunItems.FIRE_RUNE, new ItemStack(Material.OBSIDIAN),
                SlimefunItems.MAGIC_LUMP_3, new ItemStack(Material.LAVA_BUCKET), SlimefunItems.MAGIC_LUMP_3
        }).register(INSTANCE);
        new BedRune(ITEM_GROUP, InfernalItems.BED_RUNE, RecipeType.ANCIENT_ALTAR, new ItemStack[] {
                SlimefunItems.MAGIC_LUMP_3, new ItemStack(Material.NETHERRACK), SlimefunItems.MAGIC_LUMP_3,
                new ItemStack(Material.ORANGE_BED), SlimefunItems.LIGHTNING_RUNE, new ItemStack(Material.ORANGE_BED),
                SlimefunItems.MAGIC_LUMP_3, new ItemStack(Material.NETHERRACK), SlimefunItems.MAGIC_LUMP_3
        }).register(INSTANCE);

        server.getScheduler().runTaskTimer(INSTANCE, new ThermalWalkTask(), 0L, 20L);

        new PortableNetherTeleporter(ITEM_GROUP, InfernalItems.PORTABLE_NETHER_TELEPORTER, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{
                new ItemStack(Material.NETHER_STAR), SlimefunItems.REINFORCED_PLATE, new ItemStack(Material.NETHER_STAR),
                new ItemStack(Material.OBSIDIAN), SlimefunItems.PORTABLE_TELEPORTER, new ItemStack(Material.OBSIDIAN),
                new ItemStack(Material.NETHER_STAR), SlimefunItems.REINFORCED_PLATE, new ItemStack(Material.NETHER_STAR),
        }).register(INSTANCE);
        new PyromaniacBlazeRod(ITEM_GROUP, InfernalItems.PYROMANIAC_BLAZE_ROD, RecipeType.MAGIC_WORKBENCH, new ItemStack[]{
                null, SlimefunItems.LAVA_CRYSTAL, null,
                null, new ItemStack(Material.BLAZE_ROD), null,
                null, new ItemStack(Material.FLINT_AND_STEEL), null
        }).register(INSTANCE);
        new FireballLauncher(ITEM_GROUP, InfernalItems.FIREBALL_LAUNCHER, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                SlimefunItems.REINFORCED_PLATE, null, SlimefunItems.REINFORCED_PLATE,
                SlimefunItems.COPPER_WIRE, new ItemStack(Material.FIRE_CHARGE), SlimefunItems.COPPER_WIRE,
                SlimefunItems.REINFORCED_PLATE, null, SlimefunItems.REINFORCED_PLATE
        }).register(INSTANCE);

        new PiglinTalisman(InfernalItems.PIGLIN_TALISMAN, new ItemStack[]{
                SlimefunItems.MAGIC_LUMP_3, null, SlimefunItems.MAGIC_LUMP_3,
                new ItemStack(Material.GOLD_INGOT), SlimefunItems.COMMON_TALISMAN, new ItemStack(Material.GOLD_NUGGET),
                SlimefunItems.MAGIC_LUMP_3, null, SlimefunItems.MAGIC_LUMP_3
        }, false, false, null)
                .register(INSTANCE);

        new PiglinAttractor(ITEM_GROUP, InfernalItems.PIGLIN_ATTRACTOR, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                new ItemStack(Material.GOLD_NUGGET), new ItemStack(Material.NETHER_STAR), new ItemStack(Material.GOLD_NUGGET),
                SlimefunItems.BLISTERING_INGOT_3, SlimefunItems.HEATING_COIL, SlimefunItems.BLISTERING_INGOT_3,
                SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.LAVA_CRYSTAL, SlimefunItems.ELECTRIC_MOTOR
        }).register(INSTANCE);

    }
}
