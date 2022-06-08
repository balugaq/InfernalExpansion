package ga.kirzu.infernalexpansion.items.runes;

import ga.kirzu.infernalexpansion.InfernalUtils;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemDropHandler;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collection;

public class ThermalWalkRune extends SimpleSlimefunItem<ItemDropHandler> {

    private final static double RANGE
            = 1.5;

    @ParametersAreNonnullByDefault
    public ThermalWalkRune(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Override
    public @NotNull ItemDropHandler getItemHandler() {
        return (event, player, item) -> {
            if (isItem(item.getItemStack())) {
                if (!canUse(player, true)) {
                    return true;
                }

                Slimefun.runSync(() -> use(player, item), 20L);

                return true;
            }
            return false;
        };
    }

    private void use(Player player, Item rune) {
        if (!rune.isValid()) {
            return;
        }

        Location loc = rune.getLocation();
        Collection<Entity> entities = loc.getNearbyEntitiesByType(Item.class, RANGE, RANGE, RANGE, (entity) -> {
            ItemStack item = ((Item) entity).getItemStack();

            return InfernalUtils.validBoots(item) && !isItem(item);
        });

        Item item = (Item) entities.stream().findFirst().orElse(null);
        if (item != null) {
            ItemStack itemStack = item.getItemStack();
            if (itemStack.getAmount() >= 1) {
                loc.getWorld().strikeLightningEffect(loc);

                Slimefun.runSync(() -> {
                    if (rune.isValid() && item.isValid() && itemStack.getAmount() == 1) {
                        loc.getWorld().playSound(loc, Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1, 1);
                        loc.getWorld().playEffect(loc, Effect.MOBSPAWNER_FLAMES, 1);

                        rune.remove();
                        item.remove();

                        InfernalUtils.setThermalWalk(itemStack, true);
                        loc.getWorld().dropItemNaturally(loc, itemStack);

                        InfernalUtils.sendMessage(player, "thermal-walk-rune.success");
                    } else {
                        InfernalUtils.sendMessage(player, "thermal-walk-rune.fail");
                    }
                }, 10L);
            }
        }
    }
}
