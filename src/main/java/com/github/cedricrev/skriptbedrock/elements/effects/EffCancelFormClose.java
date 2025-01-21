package com.github.cedricrev.skriptbedrock.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.RequiredPlugins;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SectionSkriptEvent;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.github.cedricrev.skriptbedrock.elements.sections.*;
import com.github.cedricrev.skriptbedrock.forms.Form;
import com.github.cedricrev.skriptbedrock.forms.FormManager;
import org.bukkit.event.Event;

@Name(value="Forms - Cancel close")
@Description(value={"Cancel or uncancel form close", "Its simply reopen form if cancelled"})
@RequiredPlugins(value={"Floodgate"})
@Since(value="1.0")
public class EffCancelFormClose
        extends Effect {
    private boolean cancel;

    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        SkriptEvent skriptEvent;
        if (!(this.getParser().isCurrentSection(new Class[]{SecCreateCustomForm.class, SecCreateSimpleForm.class, SecCreateModalForm.class, SecCreateModalForm.class}) || (skriptEvent = this.getParser().getCurrentSkriptEvent()) instanceof SectionSkriptEvent && (((SectionSkriptEvent)skriptEvent).isSection(SecFormResult.class) || ((SectionSkriptEvent)skriptEvent).isSection(SecFormOpenClose.class) || ((SectionSkriptEvent)skriptEvent).isSection(SecFormButton.class)))) {
            Skript.error((String)"Cancelling or uncancelling the closing of a Form can only be done inside form creation section.");
            return false;
        }
        this.cancel = matchedPattern == 0;
        return true;
    }

    protected void execute(Event event) {
        Form form = FormManager.getFormManager().getForm(event);
        if (form != null) {
            form.setCloseCancelled(this.cancel);
        }
    }

    public String toString(Event e, boolean debug) {
        return (this.cancel ? "cancel" : "uncancel") + " the form closing";
    }

    static {
        Skript.registerEffect(EffCancelFormClose.class, (String[])new String[]{"cancel [the] form clos(e|ing)", "uncancel [the] form clos(e|ing)"});
    }
}

