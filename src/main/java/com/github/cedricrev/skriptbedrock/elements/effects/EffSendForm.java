package com.github.cedricrev.skriptbedrock.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.RequiredPlugins;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.github.cedricrev.skriptbedrock.forms.Form;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.geysermc.floodgate.api.FloodgateApi;

@Name(value="Forms - Open")
@Description(value={"Open created form to bedrock players"})
@Examples(value={"open last created form to player"})
@RequiredPlugins(value={"Floodgate"})
@Since(value="1.0")
public class EffSendForm
        extends Effect {
    public static Expression<Form> form;
    public static Expression<Player> players;

    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        form = (Expression<Form>) exprs[0];
        players = (Expression<Player>) exprs[1];
        return true;
    }

    public void execute(Event e) {
        Form form = (Form)EffSendForm.form.getSingle(e);
        for (Player player : (Player[])players.getArray(e)) {
            if (!player.isOnline() || !FloodgateApi.getInstance().isFloodgatePlayer(player.getUniqueId())) continue;
            FloodgateApi.getInstance().getPlayer(player.getUniqueId()).sendForm(form.build(player));
        }
    }

    public String toString(Event e, boolean debug) {
        return "open form " + form.toString(e, debug) + " to" + players.toString(e, debug);
    }

    static {
        Skript.registerEffect(EffSendForm.class, (String[])new String[]{"open %form% (for|to) %players%"});
    }
}

