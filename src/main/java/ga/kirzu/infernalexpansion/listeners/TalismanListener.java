package ga.kirzu.infernalexpansion.listeners;

import ga.kirzu.infernalexpansion.InfernalItems;
import ga.kirzu.infernalexpansion.items.talismans.PiglinTalisman;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PigZombieAngerEvent;

public class TalismanListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPiglinTarget(PigZombieAngerEvent event) {
        Entity target = event.getTarget();
        if (!(target instanceof Player)) {
            return;
        }

        if (PiglinTalisman.trigger(event, InfernalItems.PIGLIN_TALISMAN.getItem())) {
            event.setCancelled(true);
        }
    }

}
