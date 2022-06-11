package ga.kirzu.infernalexpansion.listeners;

import ga.kirzu.infernalexpansion.InfernalExpansion;
import ga.kirzu.infernalexpansion.InfernalItems;
import ga.kirzu.infernalexpansion.items.runes.BedRune;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.libraries.dough.config.Config;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

public class RuneListener implements Listener {

    private final static Config CONFIG
            = InfernalExpansion.getCfg();

    @EventHandler
    public void onBedRune(PlayerBedEnterEvent event) {
        Player player = event.getPlayer();
        World world = player.getWorld();

        if (world.getEnvironment() != World.Environment.NETHER || event.getBedEnterResult() != PlayerBedEnterEvent.BedEnterResult.NOT_POSSIBLE_HERE) {
            return;
        }

        Block bed = event.getBed();
        Location bedLoc = bed.getLocation();
        Collection<Item> entities = bed.getLocation().getNearbyEntitiesByType(Item.class, 2, item -> {
            ItemStack itemStack = item.getItemStack();
            return itemStack.getType() == Material.FIREWORK_STAR;
        });

        Item runeItem = null;
        for (Item entity : entities) {
            ItemStack item = entity.getItemStack();
            SlimefunItem sfItem = SlimefunItem.getByItem(item);
            if (sfItem != null && sfItem.getId().equals(InfernalItems.BED_RUNE.getItemId())) {
                runeItem = entity;
                break;
            }
        }

        if (runeItem != null) {
            runeItem.remove();
            int radius = CONFIG.getInt("bed-rune-radius");
            world.createExplosion(bedLoc, radius, true);
            ItemStack rune = runeItem.getItemStack();
            BedRune.damageItem(player, rune);
            if (!player.getInventory().addItem(rune).isEmpty()) {
                world.dropItemNaturally(bedLoc, rune);
            }
        }
    }

}
