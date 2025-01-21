package com.github.cedricrev.skriptbedrock.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.RequiredPlugins;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.github.cedricrev.skriptbedrock.elements.events.FormCloseEvent;
import org.bukkit.event.Event;

@Name(value="Forms - Close reasons")
@Description(value={"All reasons of form close"})
@Examples(value={"close", "invalid", "submit"})
@RequiredPlugins(value={"Floodgate"})
@Since(value="3.0")
public class ExprCloseReasons
        extends SimpleExpression<FormCloseEvent.CloseReason> {
    int pattern;

    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        this.pattern = matchedPattern;
        return true;
    }

    protected FormCloseEvent.CloseReason[] get(Event event) {
        return new FormCloseEvent.CloseReason[]{FormCloseEvent.CloseReason.values()[this.pattern]};
    }

    public boolean isSingle() {
        return true;
    }

    public Class<? extends FormCloseEvent.CloseReason> getReturnType() {
        return FormCloseEvent.CloseReason.class;
    }

    public String toString(Event event, boolean debug) {
        return "Close reason " + FormCloseEvent.CloseReason.values()[this.pattern].toString();
    }

    static {
        Skript.registerExpression(ExprCloseReasons.class, FormCloseEvent.CloseReason.class, (ExpressionType)ExpressionType.SIMPLE, (String[])new String[]{"close", "invalid[ response]", "(submit|success)"});
    }
}

