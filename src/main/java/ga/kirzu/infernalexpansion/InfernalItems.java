package ga.kirzu.infernalexpansion;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.utils.LoreBuilder;
import io.github.thebusybiscuit.slimefun4.utils.itemstack.ColoredFireworkStar;
import org.bukkit.Color;
import org.bukkit.Material;

public class InfernalItems {

    public static final SlimefunItemStack THERMAL_WALK_RUNE =
            new SlimefunItemStack("ANCIENT_RUNE_THERMAL_WALK", new ColoredFireworkStar(Color.fromRGB(166, 27, 27), "&7Ancient Rune &8&l[&c&lThermal Walk&8&l]", "&eDrop this rune onto a dropped boots to", "&eenchant them with &cThermal Walk I&e.", "", "&eThis enchantment creates obsidian when", "&ewalking over lava, and causes the wearer to", "&ebe immune to fire damage."));

    public static final SlimefunItemStack PORTABLE_NETHER_TELEPORTER
            = new SlimefunItemStack("PORTABLE_NETHER_TELEPORTER", Material.NETHER_STAR, "&dPortable Nether Teleporter", "", "&fTeleports you to the nether from anywhere", "", LoreBuilder.powerCharged(0, 250), "", LoreBuilder.RIGHT_CLICK_TO_USE);

    public static final SlimefunItemStack PYROMANIAC_BLAZE_ROD
            = new SlimefunItemStack("PYROMANIAC_BLAZE_ROD", Material.BLAZE_ROD, "&6Pyromaniac's Blaze Rod", "&fIgnite surrounding blocks in a radius of 5", "", LoreBuilder.powerCharged(0, 200), "", LoreBuilder.RIGHT_CLICK_TO_USE);

}
