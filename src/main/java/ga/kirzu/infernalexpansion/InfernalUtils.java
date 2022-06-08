package ga.kirzu.infernalexpansion;

import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.libraries.dough.config.Config;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class InfernalUtils {

    public static String THERMAL_WALK_LORE = ChatColor.GRAY + "Thermal Walk I";

    public static void log(String message, Class<?> clazz) {
        Logger logger = Logger.getLogger("InfernalExpansion");

        if (clazz != null) {
            logger.info(String.format(
                    "[%s] %s",
                    clazz.getSimpleName(),
                    message
            ));
        } else {
            logger.info(message);
        }
    }

    public static void sendMessage (CommandSender recipient, String key) {
        Config config = InfernalExpansion.getCfg();
        String message = config.getString("messages." + key);
        if (message == null) {
            message = key;
        }

        recipient.sendMessage(ChatColor.translateAlternateColorCodes(
                '&', message
        ));
    }
    
    public static boolean validBoots (ItemStack item) {
        if (item == null) {
            return false;
        }

        return item.getType().getEquipmentSlot() == EquipmentSlot.FEET && !hasThermalWalk(item);
    }

    public static void setThermalWalk(ItemStack item, boolean addEnchantment) {
        if (item == null || item.getType().getEquipmentSlot() != EquipmentSlot.FEET) {
            throw new IllegalArgumentException("You cannot apply Thermal Walk to items that are not placeable on feet equipment slot.");
        }

        boolean hasThermalWalk = hasThermalWalk(item);
        ItemMeta meta = item.getItemMeta();

        PersistentDataContainer container = meta.getPersistentDataContainer();
        NamespacedKey key = Slimefun.getRegistry().getSoulboundDataKey();

        if (addEnchantment && !hasThermalWalk) {
            container.set(key, PersistentDataType.BYTE, (byte) 1);
        }

        if (!addEnchantment && hasThermalWalk) {
            container.remove(key);
        }

        List<Component> lore = meta.lore();
        if (lore == null) {
            lore = new ArrayList<>();
        }

        if (addEnchantment && !hasThermalWalk) {
            lore.add(Component.text(THERMAL_WALK_LORE));
        }

        if (!addEnchantment && hasThermalWalk) {
            lore.remove(Component.text(THERMAL_WALK_LORE));
        }

        meta.lore(lore);
        item.setItemMeta(meta);
    }

    public static boolean hasThermalWalk(ItemStack item) {
        if (item != null && item.getType().getEquipmentSlot() == EquipmentSlot.FEET) {
            ItemMeta meta = item.hasItemMeta() ? item.getItemMeta() : null;

            if (hasThermalWalkFlag(meta)) {
                return true;
            }

            if (meta != null) {
                List<Component> lore = meta.lore();
                return lore != null && lore.contains(Component.text(THERMAL_WALK_LORE));
            }

        }
        return false;
    }


    private static boolean hasThermalWalkFlag(ItemMeta meta) {
        if (meta != null) {
            PersistentDataContainer container = meta.getPersistentDataContainer();
            NamespacedKey key = Slimefun.getRegistry().getSoulboundDataKey();

            return container.has(key, PersistentDataType.BYTE);
        }

        return false;
    }


}
