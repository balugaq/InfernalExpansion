package ga.kirzu.infernalexpansion;

import ga.kirzu.infernalexpansion.items.runes.BedRune;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.utils.LoreBuilder;
import io.github.thebusybiscuit.slimefun4.utils.itemstack.ColoredFireworkStar;
import org.bukkit.Color;
import org.bukkit.Material;

public class InfernalItems {

    public static final SlimefunItemStack THERMAL_WALK_RUNE =
            new SlimefunItemStack("ANCIENT_RUNE_THERMAL_WALK", new ColoredFireworkStar(Color.fromRGB(166, 27, 27), "&7古代符文 &8&l[&c&l岩浆行者&8&l]", "&e将此符文扔在掉落物形式的靴子上", "&e可使其附魔上岩浆行者效果", "", "&e该附魔可使周围的岩浆变为黑曜石", "&e并使佩戴者免疫火焰伤害"));
    public static final SlimefunItemStack BED_RUNE
            = new SlimefunItemStack("ANCIENT_RUNE_BED", new ColoredFireworkStar(Color.fromRGB(255, 123, 0), "&7古代符文 &8&l[&6&l床&8&l]", "&e将此符文扔到床上", "&e以增加它的爆炸半径", "", LoreBuilder.usesLeft(BedRune.USES)));

    public static final SlimefunItemStack PORTABLE_NETHER_TELEPORTER
            = new SlimefunItemStack("PORTABLE_NETHER_TELEPORTER", Material.NETHER_STAR, "&d便携式下界传送器", "", "&f将你从任意位置传送至下界", "", LoreBuilder.powerCharged(0, 250), "", LoreBuilder.RIGHT_CLICK_TO_USE);
    public static final SlimefunItemStack PYROMANIAC_BLAZE_ROD
            = new SlimefunItemStack("PYROMANIAC_BLAZE_ROD", Material.BLAZE_ROD, "&6纵火狂的燃烧棒", "", "&f点燃周围5格半径内的方块", "", LoreBuilder.powerCharged(0, 200), "", LoreBuilder.RIGHT_CLICK_TO_USE);
    public static final SlimefunItemStack FIREBALL_LAUNCHER
            = new SlimefunItemStack("FIREBALL_LAUNCHER", Material.FIRE_CHARGE, "&6火球发射器", "", "&f向你的正前方发射火球", "", LoreBuilder.powerCharged(0, 250), "", LoreBuilder.RIGHT_CLICK_TO_USE);

    public static final SlimefunItemStack PIGLIN_TALISMAN
            = new SlimefunItemStack("PIGLIN_TALISMAN", Material.EMERALD, "&a猪灵护身符", "", "&f当你的背包中有这个护身符时", "&f僵尸猪灵将不再对你有仇恨");

    public static final SlimefunItemStack PIGLIN_ATTRACTOR
            = new SlimefunItemStack("PIGLIN_ATTRACTOR", Material.RESPAWN_ANCHOR, "&e猪灵吸引器", "", "&f充电时", "&f吸引附近的僵尸猪灵", "", LoreBuilder.powerBuffer(2_000), LoreBuilder.powerPerSecond(25));

}