package ga.kirzu.infernalexpansion.items.blocks;

import ga.kirzu.infernalexpansion.InfernalExpansion;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetComponent;
import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNetComponentType;
import io.github.thebusybiscuit.slimefun4.implementation.handlers.SimpleBlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.interfaces.InventoryBlock;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class PiglinAttractor extends SlimefunItem implements InventoryBlock, EnergyNetComponent {

    private final static int[] BACKGROUND = {0, 1, 2, 3, 5, 6, 7, 8};
    private final static int ENERGY_CONSUMPTION = 25;
    private final static int ENERGY_CAPACITY = 2_000;

    private final static ItemStack LOADING_MENU
            = new CustomItemStack(Material.GRAY_STAINED_GLASS_PANE, "&7Loading...");
    private final static ItemStack INVALID_BIOME_MENU
            = new CustomItemStack(Material.RED_STAINED_GLASS_PANE, "&cInvalid biome.");
    private final static ItemStack NOT_ENERGY_MENU
            = new CustomItemStack(Material.RED_STAINED_GLASS_PANE, "&cNot enough energy.");
    private final static ItemStack WORKING_MENU
            = new CustomItemStack(Material.GREEN_STAINED_GLASS_PANE, "&aWorking!");

    private final static io.github.thebusybiscuit.slimefun4.libraries.dough.config.Config CONFIG
            = InfernalExpansion.getCfg();

    public PiglinAttractor(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);

        createPreset(this, "&5Piglin Attractor", this::createMenu);
    }

    private void createMenu(BlockMenuPreset preset) {
        for (int bg : BACKGROUND) {
            preset.addItem(bg, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
        }

        preset.addItem(4, LOADING_MENU, ChestMenuUtils.getEmptyClickHandler());
    }

    @Override
    public void preRegister() {
        addItemHandler(new BlockTicker() {
            @Override
            public boolean isSynchronized() {
                return true;
            }

            @Override
            public void tick(Block block, SlimefunItem sfItem, Config config) {
                PiglinAttractor.this.tick(block);
            }
        }, new SimpleBlockBreakHandler() {
            @Override
            public void onBlockBreak(@NotNull Block block) {
                killArmorStand(block);
            }
        });
    }

    private void tick(Block block) {
        BlockMenu menu = BlockStorage.getInventory(block);
        if (block.getWorld().getEnvironment() != World.Environment.NETHER) {
            menu.replaceExistingItem(4, INVALID_BIOME_MENU);
            return;
        }

        Location loc = block.getLocation();
        if (isChargeable()) {
            int charge = getCharge(loc);
            if (charge < ENERGY_CONSUMPTION) {
                menu.replaceExistingItem(4, NOT_ENERGY_MENU);
                killArmorStand(block);
                return;
            }

            setCharge(loc, charge - ENERGY_CONSUMPTION);

            int radius = CONFIG.getInt("machines.piglin-attractor.radius");

            Collection<LivingEntity> entities = loc.getNearbyLivingEntities(radius, (entity) -> entity.getType() == EntityType.ZOMBIFIED_PIGLIN);

            ArmorStand target = getArmorStand(block, true);

            menu.replaceExistingItem(4, WORKING_MENU);

            for (LivingEntity entity : entities) {
                if (!(entity instanceof Mob)) {
                    continue;
                }

                ((Mob) entity).setTarget(target);
            }
        }
    }

    @NotNull
    @Override
    public EnergyNetComponentType getEnergyComponentType() {
        return EnergyNetComponentType.CONSUMER;
    }

    @Override
    public int getCapacity() {
        return ENERGY_CAPACITY;
    }

    @Override
    public int[] getInputSlots() {
        return new int[0];
    }

    @Override
    public int[] getOutputSlots() {
        return new int[0];
    }

    private static ArmorStand getArmorStand(Block block, boolean createIfNotExists) {
        Location l = new Location(block.getWorld(), block.getX() + 0.5, block.getY(), block.getZ() + 0.5);
        String nametag = "piglin-attractor";

        for (Entity n : l.getChunk().getEntities()) {
            if (n instanceof ArmorStand && l.distanceSquared(n.getLocation()) < 0.4) {
                String customName = n.getCustomName();

                if (customName != null && customName.equals(nametag)) {
                    return (ArmorStand) n;
                }
            }
        }

        if (!createIfNotExists) {
            return null;
        }

        ArmorStand hologram = spawnArmorStand(l);
        hologram.setCustomName(nametag);
        return hologram;
    }

    private static ArmorStand spawnArmorStand(Location l) {
        ArmorStand armorStand = (ArmorStand) l.getWorld().spawnEntity(l, EntityType.ARMOR_STAND);
        armorStand.setVisible(false);
        armorStand.setSilent(true);
        armorStand.setMarker(true);
        armorStand.setGravity(false);
        armorStand.setBasePlate(false);
        armorStand.setCustomNameVisible(false);
        armorStand.setRemoveWhenFarAway(false);
        return armorStand;
    }

    private static void killArmorStand(Block b) {
        ArmorStand hologram = getArmorStand(b, false);

        if (hologram != null) {
            hologram.remove();
        }
    }
}
