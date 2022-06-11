package ga.kirzu.infernalexpansion.items.runes;

import ga.kirzu.infernalexpansion.InfernalExpansion;
import ga.kirzu.infernalexpansion.InfernalItems;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.libraries.dough.common.ChatColors;
import io.github.thebusybiscuit.slimefun4.utils.LoreBuilder;
import io.github.thebusybiscuit.slimefun4.utils.PatternUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collections;
import java.util.List;

// Methods from https://github.com/Slimefun/Slimefun4/blob/a9c330d6a326f730dc6ceba8a42036b4eeebfc8e/src/main/java/io/github/thebusybiscuit/slimefun4/implementation/items/LimitedUseItem.java
public class BedRune extends SlimefunItem {

    public final static int USES = 5;
    private final static NamespacedKey USES_KEY
            = new NamespacedKey(InfernalExpansion.getInstance(), "bedrune_uses");

    public BedRune(ItemGroup group, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(group, item, recipeType, recipe);
    }

    @NotNull
    public static NamespacedKey getStorageKey() {
        return USES_KEY;
    }

    @ParametersAreNonnullByDefault
    public static void damageItem(Player p, ItemStack item) {
        if (item.getType() != InfernalItems.BED_RUNE.getType()) {
            return;
        }

        if (item.getAmount() > 1) {
            item.setAmount(item.getAmount() - 1);

            ItemStack separateItem = item.clone();
            separateItem.setAmount(1);
            damageItem(p, separateItem);

            if (!p.getInventory().addItem(separateItem).isEmpty()) {
                p.getWorld().dropItemNaturally(p.getLocation(), separateItem);
            }
        } else {
            ItemMeta meta = item.getItemMeta();
            NamespacedKey key = getStorageKey();
            PersistentDataContainer pdc = meta.getPersistentDataContainer();
            int usesLeft = pdc.getOrDefault(key, PersistentDataType.INTEGER, USES);

            if (usesLeft == 1) {
                p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1, 1);
                item.setAmount(0);
                item.setType(Material.AIR);
            } else {
                usesLeft--;
                pdc.set(key, PersistentDataType.INTEGER, usesLeft);

                updateItemLore(item, meta, usesLeft);
            }
        }
    }

    @ParametersAreNonnullByDefault
    private static void updateItemLore(ItemStack item, ItemMeta meta, int usesLeft) {
        List<String> lore = meta.getLore();

        String newLine = ChatColors.color(LoreBuilder.usesLeft(usesLeft));
        if (lore != null && !lore.isEmpty()) {
            for (int i = 0; i < lore.size(); i++) {
                if (PatternUtils.USES_LEFT_LORE.matcher(lore.get(i)).matches()) {
                    lore.set(i, newLine);
                    meta.setLore(lore);
                    item.setItemMeta(meta);
                    return;
                }
            }
        } else {
            meta.setLore(Collections.singletonList(newLine));
            item.setItemMeta(meta);
        }
    }
}
