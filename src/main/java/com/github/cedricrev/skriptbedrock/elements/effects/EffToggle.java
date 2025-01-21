package com.github.cedricrev.skriptbedrock.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.RequiredPlugins;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SectionSkriptEvent;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.log.ErrorQuality;
import ch.njol.util.Kleenean;
import com.github.cedricrev.skriptbedrock.elements.sections.SecCreateCustomForm;
import com.github.cedricrev.skriptbedrock.elements.sections.SecFormResult;
import com.github.cedricrev.skriptbedrock.forms.Form;
import com.github.cedricrev.skriptbedrock.forms.FormManager;
import org.bukkit.event.Event;
import org.geysermc.cumulus.component.util.ComponentType;
import org.geysermc.cumulus.form.CustomForm;

@Name(value="Forms - Toggle")
@Description(value={"Create toggle on custom form "})
@Examples(value={"form-toggle named \"toggle\" with default value false"})
@RequiredPlugins(value={"Floodgate"})
@Since(value="1.0")
public class EffToggle
        extends Effect {
    int pattern;
    Expression<String> name;
    Expression<Boolean> def;

    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        SkriptEvent skriptEvent;
        if (!(this.getParser().isCurrentSection(SecCreateCustomForm.class) || (skriptEvent = this.getParser().getCurrentSkriptEvent()) instanceof SectionSkriptEvent && ((SectionSkriptEvent)skriptEvent).isSection(SecFormResult.class))) {
            Skript.error((String)"You can't make a toggle outside of a Custom form creation section.", (ErrorQuality)ErrorQuality.SEMANTIC_ERROR);
            return false;
        }
        this.pattern = matchedPattern;
        this.name = (Expression<String>) exprs[0];
        if (matchedPattern > 0) {
            this.def = (Expression<Boolean>) exprs[1];
        }
        return true;
    }

    protected void execute(Event event) {
        Form form = FormManager.getFormManager().getForm(event);
        switch (this.pattern) {
            case 0: {
                ((CustomForm.Builder)form.getForm()).toggle((String)this.name.getSingle(event));
                break;
            }
            case 1: {
                ((CustomForm.Builder)form.getForm()).toggle((String)this.name.getSingle(event), ((Boolean)this.def.getSingle(event)).booleanValue());
            }
        }
        form.addComponent(ComponentType.TOGGLE);
    }

    public String toString(Event e, boolean debug) {
        return "create form toggle";
    }

    static {
        Skript.registerEffect(EffToggle.class, (String[])new String[]{"form(-| )toggle (with name|named) %string%", "form(-| )toggle (with name|named) %string% (with|and) [def[ault]] [value] %boolean%"});
    }
}

