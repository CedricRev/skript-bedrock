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
import com.github.cedricrev.skriptbedrock.forms.Form;
import com.github.cedricrev.skriptbedrock.forms.FormManager;
import org.bukkit.event.Event;

@Name(value="Forms - Last form")
@Description(value={"Get last created form"})
@Examples(value={"open last created form to player"})
@RequiredPlugins(value={"Floodgate"})
@Since(value="1.0")
public class ExprLastForm
        extends SimpleExpression<Form> {
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        return true;
    }

    protected Form[] get(Event event) {
        return new Form[]{FormManager.getFormManager().getForm(event)};
    }

    public boolean isSingle() {
        return true;
    }

    public Class<? extends Form> getReturnType() {
        return Form.class;
    }

    public String toString(Event e, boolean debug) {
        return "the last created form";
    }

    static {
        Skript.registerExpression(ExprLastForm.class, Form.class, (ExpressionType)ExpressionType.SIMPLE, (String[])new String[]{"[the] (last[ly] [(created|edited)]|(created|edited)) form"});
    }
}

