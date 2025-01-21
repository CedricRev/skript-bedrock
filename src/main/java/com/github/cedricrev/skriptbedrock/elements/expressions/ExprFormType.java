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
import org.bukkit.event.Event;
import org.geysermc.cumulus.form.util.FormType;

@Name(value="Forms - Type")
@Description(value={"Get type of form"})
@Examples(value={"broadcast \"%form-type of last created form%\""})
@RequiredPlugins(value={"Floodgate"})
@Since(value="1.0")
public class ExprFormType
        extends SimpleExpression<FormType> {
    Expression<Form> form;

    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        this.form = (Expression<Form>) exprs[0];
        return true;
    }

    protected FormType[] get(Event event) {
        return new FormType[]{((Form)this.form.getSingle(event)).getType()};
    }

    public boolean isSingle() {
        return true;
    }

    public Class<? extends FormType> getReturnType() {
        return FormType.class;
    }

    public String toString(Event event, boolean debug) {
        return "Type of form " + this.form.toString(event, debug);
    }

    static {
        Skript.registerExpression(ExprFormType.class, FormType.class, (ExpressionType)ExpressionType.COMBINED, (String[])new String[]{"form[(-| )]type of %form%", "%form%'s form[(-| )]type"});
    }
}

