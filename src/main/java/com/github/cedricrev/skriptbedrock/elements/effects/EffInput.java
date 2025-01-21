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

@Name(value="Forms - Input")
@Description(value={"Create input on custom form "})
@Examples(value={"form-input named \"enter text\""})
@RequiredPlugins(value={"Floodgate"})
@Since(value="1.0")
public class EffInput
        extends Effect {
    int pattern;
    Expression<String> name;
    Expression<String> placeholder;
    Expression<String> def;

    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        SkriptEvent skriptEvent;
        if (!(this.getParser().isCurrentSection(SecCreateCustomForm.class) || (skriptEvent = this.getParser().getCurrentSkriptEvent()) instanceof SectionSkriptEvent && ((SectionSkriptEvent)skriptEvent).isSection(SecFormResult.class))) {
            Skript.error((String)"You can't make a input outside of a Custom form creation section.", (ErrorQuality)ErrorQuality.SEMANTIC_ERROR);
            return false;
        }
        this.pattern = matchedPattern;
        this.name = (Expression<String>) exprs[0];
        if (matchedPattern > 0) {
            this.placeholder = (Expression<String>) exprs[1];
            if (matchedPattern > 1) {
                this.def = (Expression<String>) exprs[2];
            }
        }
        return true;
    }

    protected void execute(Event event) {
        Form form = FormManager.getFormManager().getForm(event);
        switch (this.pattern) {
            case 0: {
                ((CustomForm.Builder)form.getForm()).input((String)this.name.getSingle(event));
                break;
            }
            case 1: {
                ((CustomForm.Builder)form.getForm()).input((String)this.name.getSingle(event), (String)this.placeholder.getSingle(event));
                break;
            }
            case 2: {
                ((CustomForm.Builder)form.getForm()).input((String)this.name.getSingle(event), (String)this.placeholder.getSingle(event), (String)this.def.getSingle(event));
            }
        }
        form.addComponent(ComponentType.INPUT);
    }

    public String toString(Event e, boolean debug) {
        return "create form input";
    }

    static {
        Skript.registerEffect(EffInput.class, (String[])new String[]{"form(-| )input (with name|named) %string%", "form(-| )input (with name|named) %string% (with|and) [placeholder] %string%", "form(-| )input (with name|named) %string% (with|and) [placeholder] %string%(, | (with|and) ) [def[ault] [value]] %string%"});
    }
}

