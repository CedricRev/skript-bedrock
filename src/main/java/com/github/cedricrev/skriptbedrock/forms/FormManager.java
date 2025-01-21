package com.github.cedricrev.skriptbedrock.forms;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;
import org.bukkit.event.Event;

public class FormManager {
    private static FormManager manager;
    private final List<Form> forms = new ArrayList<Form>();
    private final WeakHashMap<Event, Form> eventForms = new WeakHashMap();

    public FormManager() {
        manager = this;
    }

    public static FormManager getFormManager() {
        return manager;
    }

    public void register(Form form) {
        this.forms.add(form);
    }

    public void unregister(Form form) {
        this.forms.remove(form);
        this.eventForms.values().removeIf(eventForm -> eventForm == form);
    }

    public List<Form> getTrackedForms() {
        return this.forms;
    }

    public Form getForm(Event event) {
        return this.eventForms.get(event);
    }

    public void setForm(Event event, Form form) {
        if (form != null) {
            this.eventForms.put(event, form);
        } else {
            this.eventForms.remove(event);
        }
    }
}

