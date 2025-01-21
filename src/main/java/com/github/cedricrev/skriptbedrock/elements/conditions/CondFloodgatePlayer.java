package com.github.cedricrev.skriptbedrock.elements.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.RequiredPlugins;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.geysermc.floodgate.api.FloodgateApi;

import java.util.UUID;

@Name("Is floodgate player")
@Description({"Check if player from bedrock edition"})
@RequiredPlugins({"Floodgate"})
@Examples({"if player is from floodgate:", "\tbroadcast \"%player% from floodgate\""})
@Since("1.0")
public class CondFloodgatePlayer extends Condition {

    private Expression<Player> player;

    static {
        Skript.registerCondition(CondFloodgatePlayer.class, new String[] { "%player% is from floodgate", "%player% is(n't| not) from floodgate" });
    }

    public String toString(Event e, boolean debug) {
        return "is player from Floodgate: " + this.player.toString(e, debug);
    }

    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        this.player = (Expression<Player>)exprs[0];
        setNegated((matchedPattern == 1));
        return true;
    }

    public boolean check(Event e) {
        UUID uuid = player.getSingle(e).getUniqueId();
        return (isNegated() != FloodgateApi.getInstance().isFloodgatePlayer(uuid));
    }
}