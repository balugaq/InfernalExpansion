package ga.kirzu.infernalexpansion.items.talismans;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.implementation.items.magical.talismans.Talisman;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.entity.PigZombieAngerEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ThreadLocalRandom;

public class PiglinTalisman extends Talisman {
    public PiglinTalisman(SlimefunItemStack item, ItemStack[] recipe, boolean consumable, boolean cancelEvent, @Nullable String messageSuffix, PotionEffect... effects) {
        super(item, recipe, consumable, cancelEvent, messageSuffix, effects);
    }

    @ParametersAreNonnullByDefault
    public static boolean trigger(Event e, SlimefunItem item) {
        Player p = getPlayerByEventType(e);
        if (p == null) {
            return false;
        }
        return trigger(p, item);
    }

    @ParametersAreNonnullByDefault
    public static boolean trigger(Player p, SlimefunItem item) {
        if (!(item instanceof Talisman talisman)) {
            return false;
        }

        if (ThreadLocalRandom.current().nextInt(100) > talisman.getChance()) {
            return false;
        }

        Object canEffectsBeApplied = access(talisman.getClass(), "canEffectsBeApplied", p);
        if ((canEffectsBeApplied != null && !((boolean) canEffectsBeApplied))) {
            return false;
        }

        ItemStack talismanItem = talisman.getItem();

        if (SlimefunUtils.containsSimilarItem(p.getInventory(), talismanItem, true)) {
            return talisman.canUse(p, true);
        } else {
            Object enderVariant = access(talisman.getClass(), "getEnderVariant");
            if (!(enderVariant instanceof ItemStack enderTalisman)) {
                return false;
            }

            if (SlimefunUtils.containsSimilarItem(p.getEnderChest(), enderTalisman, true)) {
                return talisman.canUse(p, true);
            } else {
                return false;
            }
        }
    }

    private static Object access(Class<?> clazz, String methodName, Object ...args) {
        try {
            Method method = clazz.getDeclaredMethod(methodName);
            method.setAccessible(true);
            return method.invoke(args);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            return null;
        }
    }

    @Nullable
    private static Player getPlayerByEventType(@Nonnull Event e) {
        if (e instanceof PigZombieAngerEvent) {
            return (Player) ((PigZombieAngerEvent) e).getTarget();
        } else if (e instanceof EntityDeathEvent) {
            return ((EntityDeathEvent) e).getEntity().getKiller();
        } else if (e instanceof BlockBreakEvent) {
            return ((BlockBreakEvent) e).getPlayer();
        } else if (e instanceof BlockDropItemEvent) {
            return ((BlockDropItemEvent) e).getPlayer();
        } else if (e instanceof PlayerEvent) {
            return ((PlayerEvent) e).getPlayer();
        } else if (e instanceof EntityEvent) {
            return (Player) ((EntityEvent) e).getEntity();
        } else if (e instanceof EnchantItemEvent) {
            return ((EnchantItemEvent) e).getEnchanter();
        }

        return null;
    }
}
