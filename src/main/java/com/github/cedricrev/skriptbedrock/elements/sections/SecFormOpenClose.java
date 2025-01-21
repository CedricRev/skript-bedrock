package com.github.cedricrev.skriptbedrock.elements.sections;

import ch.njol.skript.Skript;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.RequiredPlugins;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.Section;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.Trigger;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.skript.variables.Variables;
import ch.njol.util.Kleenean;
import java.util.List;

import com.github.cedricrev.skriptbedrock.elements.events.FormCloseEvent;
import com.github.cedricrev.skriptbedrock.elements.events.FormOpenEvent;
import com.github.cedricrev.skriptbedrock.forms.Form;
import com.github.cedricrev.skriptbedrock.forms.FormManager;
import org.bukkit.event.Event;

@Name(value="Forms - Open Close section")
@Description(value={"This executed when form is opened or closed"})
@Examples(value={"create simple form named \"Simple form\":", "\trun on form close:", "\t\tbroadcast \"closed\"", "\trun on form open:", "\t\tbroadcast \"opened\""})
@RequiredPlugins(value={"Floodgate"})
@Since(value="1.0")
public class SecFormOpenClose
        extends Section {
    private Trigger trigger;
    private boolean close;

    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult, SectionNode sectionNode, List<TriggerItem> triggerItems) {
        if (!this.getParser().isCurrentSection(new Class[]{SecCreateModalForm.class, SecCreateSimpleForm.class, SecCreateCustomForm.class})) {
            Skript.error((String)"Form open/close sections can only be put within Form creation or editing sections.");
            return false;
        }
        this.close = parseResult.mark == 1;
        this.trigger = this.close ? this.loadCode(sectionNode, "form close", new Class[]{FormCloseEvent.class}) : this.loadCode(sectionNode, "form open", new Class[]{FormOpenEvent.class});
        return true;
    }

    public TriggerItem walk(Event e) {
        Form form = FormManager.getFormManager().getForm(e);
        if (form != null) {
            Object variables = Variables.copyLocalVariables((Event)e);
            if (this.close) {
                if (variables != null) {
                    form.setOnClose(event -> {
                        Variables.setLocalVariables((Event)event, (Object)variables);
                        this.trigger.execute((Event)event);
                    });
                } else {
                    form.setOnClose(arg_0 -> ((Trigger)this.trigger).execute(arg_0));
                }
            } else if (variables != null) {
                form.setOnOpen(event -> {
                    Variables.setLocalVariables((Event)event, (Object)variables);
                    this.trigger.execute((Event)event);
                });
            } else {
                form.setOnOpen(arg_0 -> ((Trigger)this.trigger).execute(arg_0));
            }
        }
        return this.walk(e, false);
    }

    public String toString(Event e, boolean debug) {
        return "run on form " + (this.close ? "close" : "open");
    }

    static {
        Skript.registerSection(SecFormOpenClose.class, (String[])new String[]{"run (when|while) (open[ing]|1\u00a6clos(e|ing)) [[the] form]", "run (when|while) [the] form (opens|1\u00a6closes)", "run on form (open[ing]|1\u00a6clos(e|ing))"});
    }
}

