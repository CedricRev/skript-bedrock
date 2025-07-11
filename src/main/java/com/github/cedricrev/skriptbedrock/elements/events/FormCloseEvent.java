package com.github.cedricrev.skriptbedrock.elements.events;

import com.github.cedricrev.skriptbedrock.forms.Form;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class FormCloseEvent
        extends Event
        implements BaseFormEvent {
    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final Form form;
    private final CloseReason reason;

    public FormCloseEvent(Player player, Form form, CloseReason reason) {
        this.player = player;
        this.form = form;
        this.reason = reason;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public Player getPlayer() {
        return this.player;
    }

    @Override
    public Form getForm() {
        return this.form;
    }

    public CloseReason getCloseReason() {
        return this.reason;
    }

    public static enum CloseReason {
        CLOSE,
        INVALID_RESPONSE,
        SUBMIT;

    }
}

