package com.github.cedricrev.skriptbedrock.forms;

import ch.njol.skript.SkriptEventHandler;
import com.github.cedricrev.skriptbedrock.elements.events.FormCloseEvent;
import com.github.cedricrev.skriptbedrock.elements.events.FormOpenEvent;
import com.github.cedricrev.skriptbedrock.elements.events.FormSubmitEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class FormEvents
        implements Listener {
    public FormEvents() {
        SkriptEventHandler.listenCancelled.add(FormSubmitEvent.class);
        SkriptEventHandler.listenCancelled.add(FormOpenEvent.class);
        SkriptEventHandler.listenCancelled.add(FormCloseEvent.class);
    }

    @EventHandler(priority=EventPriority.LOWEST, ignoreCancelled=true)
    public void onFormSubmit(FormSubmitEvent event) {
        event.getForm().getEventHandler().onSubmit(event);
    }

    @EventHandler(priority=EventPriority.LOWEST, ignoreCancelled=true)
    public void onFormOpen(FormOpenEvent event) {
        event.getForm().getEventHandler().onOpen(event);
    }

    @EventHandler(priority=EventPriority.LOWEST)
    public void onFormClose(FormCloseEvent event) {
        event.getForm().getEventHandler().onClose(event);
    }
}

