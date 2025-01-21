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
import org.bukkit.event.Event;
import org.geysermc.cumulus.form.util.FormType;

@Name(value="Forms - Form types")
@Description(value={"All types of form"})
@Examples(value={"custom form", "modal form", "simple form"})
@RequiredPlugins(value={"Floodgate"})
@Since(value="1.0")
public class ExprFormTypes
        extends SimpleExpression<FormType> {
    int pattern;

    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        this.pattern = matchedPattern;
        return true;
    }

    protected FormType[] get(Event event) {
        return new FormType[]{FormType.values()[this.pattern]};
    }

    public boolean isSingle() {
        return true;
    }

    public Class<? extends FormType> getReturnType() {
        return FormType.class;
    }

    public String toString(Event event, boolean debug) {
        return "Form type " + FormType.values()[this.pattern].toString();
    }

    static {
        Skript.registerExpression(ExprFormTypes.class, FormType.class, (ExpressionType)ExpressionType.SIMPLE, (String[])new String[]{"custom form", "modal form", "simple form"});
    }
}

