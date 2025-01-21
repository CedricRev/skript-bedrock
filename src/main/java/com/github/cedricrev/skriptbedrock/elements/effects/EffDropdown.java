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

@Name(value="Forms - Dropdown")
@Description(value={"Create dropdown on custom form"})
@Examples(value={"form-dropdown named \"dropdown\" with elements \"option 1\",\"option 2\""})
@RequiredPlugins(value={"Floodgate"})
@Since(value="1.0")
public class EffDropdown
        extends Effect {
    int pattern;
    Expression<String> name;
    Expression<String> elements;
    Expression<Number> def;

    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        SkriptEvent skriptEvent;
        if (!(this.getParser().isCurrentSection(SecCreateCustomForm.class) || (skriptEvent = this.getParser().getCurrentSkriptEvent()) instanceof SectionSkriptEvent && ((SectionSkriptEvent)skriptEvent).isSection(SecFormResult.class))) {
            Skript.error((String)"You can't make a dropdown outside of a Custom form creation section.", (ErrorQuality)ErrorQuality.SEMANTIC_ERROR);
            return false;
        }
        this.pattern = matchedPattern;
        this.name = (Expression<String>) exprs[0];
        this.elements = (Expression<String>) exprs[1];
        if (matchedPattern > 0) {
            this.def = (Expression<Number>) exprs[2];
        }
        return true;
    }

    protected void execute(Event event) {
        Form form = FormManager.getFormManager().getForm(event);
        switch (this.pattern) {
            case 0: {
                ((CustomForm.Builder)form.getForm()).dropdown((String)this.name.getSingle(event), (String[])this.elements.getArray(event));
                break;
            }
            case 1: {
                String[] el = (String[])this.elements.getArray(event);
                int def = ((Number)this.def.getSingle(event)).intValue();
                if (def > el.length) {
                    def = 1;
                }
                ((CustomForm.Builder)form.getForm()).dropdown((String)this.name.getSingle(event), --def, el);
            }
        }
        form.addComponent(ComponentType.DROPDOWN);
    }

    public String toString(Event e, boolean debug) {
        return "create form dropdown";
    }

    static {
        Skript.registerEffect(EffDropdown.class, (String[])new String[]{"form(-| )drop[(-| )]down (with name|named) %string% (with|and) [elements] %strings%", "form(-| )drop[(-| )]down (with name|named) %string% (with|and) [elements] %strings%(, | (with|and) ) [def[ault] [(element [index]|index)]] %number%"});
    }
}

