package com.github.cedricrev.skriptbedrock.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.RequiredPlugins;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SectionSkriptEvent;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.log.ErrorQuality;
import ch.njol.util.Kleenean;
import com.github.cedricrev.skriptbedrock.elements.events.FormCloseEvent;
import com.github.cedricrev.skriptbedrock.elements.sections.SecCreateCustomForm;
import com.github.cedricrev.skriptbedrock.elements.sections.SecCreateModalForm;
import com.github.cedricrev.skriptbedrock.elements.sections.SecCreateSimpleForm;
import com.github.cedricrev.skriptbedrock.elements.sections.SecFormOpenClose;
import org.bukkit.event.Event;

@Name(value="Forms - Close reason")
@Description(value={"Get reason of form close", "Can be used only in form close section"})
@Examples(value={"run on form close:", "\tbroadcast \"%form close reason%\""})
@RequiredPlugins(value={"Floodgate"})
@Since(value="3.0")
public class ExprCloseReason
        extends SimpleExpression<FormCloseEvent.CloseReason> {
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        SkriptEvent skriptEvent;
        if (!(this.getParser().isCurrentSection(new Class[]{SecCreateCustomForm.class, SecCreateModalForm.class, SecCreateSimpleForm.class}) || (skriptEvent = this.getParser().getCurrentSkriptEvent()) instanceof SectionSkriptEvent && ((SectionSkriptEvent)skriptEvent).isSection(SecFormOpenClose.class))) {
            Skript.error((String)"You can't use a close reason outside of a form close section.", (ErrorQuality)ErrorQuality.SEMANTIC_ERROR);
            return false;
        }
        return true;
    }

    protected FormCloseEvent.CloseReason[] get(Event event) {
        if (event instanceof FormCloseEvent) {
            return new FormCloseEvent.CloseReason[]{((FormCloseEvent)event).getCloseReason()};
        }
        return null;
    }

    public boolean isSingle() {
        return true;
    }

    public Class<? extends FormCloseEvent.CloseReason> getReturnType() {
        return FormCloseEvent.CloseReason.class;
    }

    public String toString(Event event, boolean debug) {
        return "close reason";
    }

    static {
        Skript.registerExpression(ExprCloseReason.class, FormCloseEvent.CloseReason.class, (ExpressionType)ExpressionType.SIMPLE, (String[])new String[]{"[form(-| )]close reason"});
    }
}

