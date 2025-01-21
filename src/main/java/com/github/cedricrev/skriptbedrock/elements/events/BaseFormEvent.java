package com.github.cedricrev.skriptbedrock.elements.events;

import com.github.cedricrev.skriptbedrock.forms.Form;
import org.bukkit.entity.Player;

public interface BaseFormEvent {
    public Player getPlayer();
    public Form getForm();
}

