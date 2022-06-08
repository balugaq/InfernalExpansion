package ga.kirzu.infernalexpansion;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.libraries.dough.config.Config;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class InfernalExpansion extends JavaPlugin implements SlimefunAddon {

    private static InfernalExpansion instance;
    private static Config config;

    @Override
    public void onEnable() {
        instance = this;
        config = new Config(this);

        new InfernalSetup();

        InfernalUtils.log("Addon enabled.", null);
    }

    @Override
    public void onDisable() {
        instance = null;

        InfernalUtils.log("Addon disabled.", null);
    }

    public static Config getCfg() {
        return config;
    }

    public static InfernalExpansion getInstance() {
        return instance;
    }

    @Override
    public @NotNull JavaPlugin getJavaPlugin() {
        return this;
    }

    @Override
    public String getBugTrackerURL() {
        return "https://github.com/NotKirzu/InfernalExpansion/issues";
    }
}
