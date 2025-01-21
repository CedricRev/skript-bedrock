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
import ch.njol.skript.lang.TriggerItem;
import ch.njol.util.Kleenean;
import java.util.List;

import com.github.cedricrev.skriptbedrock.forms.Form;
import com.github.cedricrev.skriptbedrock.forms.FormManager;
import org.bukkit.event.Event;
import org.geysermc.cumulus.form.util.FormType;

@Name(value="Forms - Custom form")
@Description(value={"Create custom form"})
@Examples(value={"create custom form named \"custom form\":", "\trun on form close:", "\t\tbroadcast \"closed\"", "\trun on form open:", "\t\tbroadcast \"opened\"", "\tinput named \"enter password\"", "\trun on form result:", "\t\tbroadcast input 1 value"})
@RequiredPlugins(value={"Floodgate"})
@Since(value="1.0")
public class SecCreateCustomForm
        extends EffectSection {
    Expression<String> title;

    public boolean init(Expression<?>[] exprs, int i, Kleenean kln, SkriptParser.ParseResult pr, SectionNode sn, List<TriggerItem> list) {
        this.title = (Expression<String>) exprs[0];
        if (this.hasSection()) {
            this.loadOptionalCode(sn);
        }
        return true;
    }

    protected TriggerItem walk(Event event) {
        Form form = new Form(FormType.CUSTOM_FORM, (String)this.title.getSingle(event));
        FormManager.getFormManager().setForm(event, form);
        return this.walk(event, true);
    }

    public String toString(Event event, boolean bln) {
        return "create custom form " + this.title.toString(event, bln);
    }

    static {
        Skript.registerSection(SecCreateCustomForm.class, (String[])new String[]{"create [a] [new] custom form (with (name|title)|named) %string%"});
    }
}

