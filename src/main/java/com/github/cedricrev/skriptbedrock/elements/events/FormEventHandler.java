package com.github.cedricrev.skriptbedrock.elements.events;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.entity.Player;

public abstract class FormEventHandler {
    private boolean paused;
    private final List<Player> pausedFor = new ArrayList<Player>();

    public void resume() {
        this.paused = false;
    }

    public void resume(Player player) {
        this.pausedFor.remove(player);
    }

    public void pause() {
        this.paused = true;
    }

    public void pause(Player player) {
        this.pausedFor.add(player);
    }

    public boolean isPaused() {
        return this.paused;
    }

    public boolean isPaused(Player player) {
        return this.isPaused() || this.pausedFor.contains(player);
    }

    public abstract void onSubmit(FormSubmitEvent var1);

    public abstract void onOpen(FormOpenEvent var1);

    public abstract void onClose(FormCloseEvent var1);
}

