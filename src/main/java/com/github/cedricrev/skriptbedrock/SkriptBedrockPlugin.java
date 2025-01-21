package com.github.cedricrev.skriptbedrock;

import org.bukkit.plugin.java.JavaPlugin;

public final class SkriptBedrockPlugin extends JavaPlugin {



    @Override
    public void onEnable() {
        SkriptBedrock.PLUGIN.onStart(this);

    }

    @Override
    public void onDisable() {

    }
}
