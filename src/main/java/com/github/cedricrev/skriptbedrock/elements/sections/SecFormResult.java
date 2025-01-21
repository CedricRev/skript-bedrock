package com.github.cedricrev.skriptbedrock.elements.sections;

import ch.njol.skript.Skript;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.RequiredPlugins;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.EffectSection;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.Trigger;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.skript.log.ErrorQuality;
import ch.njol.skript.variables.Variables;
import ch.njol.util.Kleenean;
import java.util.List;

import com.github.cedricrev.skriptbedrock.elements.events.FormSubmitEvent;
import com.github.cedricrev.skriptbedrock.forms.Form;
import com.github.cedricrev.skriptbedrock.forms.FormManager;
import org.bukkit.event.Event;

@Name(value="Forms - Result")
@Description(value={"This executed when custom form submit", "Can only be used in custom form section"})
@Examples(value={"create custom form named \"custom form\":", "\tinput named \"enter password\"", "\trun on form result:", "\t\tbroadcast input 1 value"})
@RequiredPlugins(value={"Floodgate"})
@Since(value="1.0")
public class SecFormResult
        extends EffectSection {
    private Trigger trigger;

    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean kleenean, SkriptParser.ParseResult parseResult, SectionNode sectionNode, List<TriggerItem> items) {
        if (!this.getParser().isCurrentSection(SecCreateCustomForm.class)) {
            Skript.error((String)"You can't make a result section outside of a Custom form creation section.", (ErrorQuality)ErrorQuality.SEMANTIC_ERROR);
            return false;
        }
        if (this.hasSection()) {
            this.trigger = this.loadCode(sectionNode, "form submit event", new Class[]{FormSubmitEvent.class});
        }
        return true;
    }

    public TriggerItem walk(Event e) {
        Form form = FormManager.getFormManager().getForm(e);
        if (form == null) {
            return this.walk(e, false);
        }
        if (this.hasSection()) {
            Object variables = Variables.copyLocalVariables((Event)e);
            if (variables != null) {
                form.setOnResult(event -> {
                    Variables.setLocalVariables((Event)event, (Object)variables);
                    this.trigger.execute((Event)event);
                });
            } else {
                form.setOnResult(event -> this.trigger.execute((Event)event));
            }
        } else {
            form.setOnResult(null);
        }
        return this.walk(e, false);
    }

    public String toString(Event e, boolean debug) {
        return "result of custom form";
    }

    static {
        Skript.registerSection(SecFormResult.class, (String[])new String[]{"run on form (result|submit)"});
    }
}

