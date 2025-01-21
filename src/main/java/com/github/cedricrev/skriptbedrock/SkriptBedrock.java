package com.github.cedricrev.skriptbedrock;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import com.github.cedricrev.skriptbedrock.elements.types.TypeCloseReason;
import com.github.cedricrev.skriptbedrock.elements.types.TypeForm;
import com.github.cedricrev.skriptbedrock.elements.types.TypeFormType;
import com.github.cedricrev.skriptbedrock.forms.FormEvents;
import com.github.cedricrev.skriptbedrock.forms.FormManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public enum SkriptBedrock {
    PLUGIN;

    private JavaPlugin plugin;
    private SkriptAddon addon;
    private FormManager formManager;

    public void onStart(JavaPlugin plugin) {
        this.plugin = plugin;
        this.addon = Skript.registerAddon(plugin);
        try {
            TypeCloseReason.register();
            TypeForm.register();
            TypeFormType.register();
            Bukkit.getServer().getPluginManager().registerEvents(new FormEvents(), plugin);
            this.formManager = new FormManager();
            this.addon.loadClasses("com.github.cedricrev.skriptbedrock.elements");
            plugin.getLogger().info("SkriptBedrock elements loaded!");
        } catch (IOException e) {
            Bukkit.getPluginManager().disablePlugin(plugin);
            throw new RuntimeException(e);
        }

    }

    public JavaPlugin getPlugin() {
        return plugin;
    }

    public SkriptAddon getAddon() {
        return addon;
    }

    public FormManager getFormManager() {
        return formManager;
    }
}
