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

@Name(value="Forms - Custom Slider")
@Description(value={"Create slider on custom forms"})
@Examples(value={"form-slider named \"slider\" with min 5 and max 10"})
@RequiredPlugins(value={"Floodgate"})
@Since(value="1.0")
public class EffSlider
        extends Effect {
    int pattern;
    Expression<String> name;
    Expression<Number> min;
    Expression<Number> max;
    Expression<Number> def;
    Expression<Number> step;

    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        SkriptEvent skriptEvent;
        if (!(this.getParser().isCurrentSection(SecCreateCustomForm.class) || (skriptEvent = this.getParser().getCurrentSkriptEvent()) instanceof SectionSkriptEvent && ((SectionSkriptEvent)skriptEvent).isSection(SecFormResult.class))) {
            Skript.error((String)"You can't make a slider outside of a Custom form creation section.", (ErrorQuality)ErrorQuality.SEMANTIC_ERROR);
            return false;
        }
        this.pattern = matchedPattern;
        this.name = (Expression<String>) exprs[0];
        this.min = (Expression<Number>) exprs[1];
        this.max = (Expression<Number>) exprs[2];
        if (matchedPattern > 0) {
            this.def = (Expression<Number>) exprs[3];
            if (matchedPattern > 1) {
                this.step = (Expression<Number>) exprs[4];
            }
        }
        return true;
    }

    protected void execute(Event event) {
        Form form = FormManager.getFormManager().getForm(event);
        float min2 = ((Number)this.min.getSingle(event)).floatValue();
        float max = ((Number)this.max.getSingle(event)).floatValue();
        if (max < min2) {
            float t2 = min2;
            min2 = max;
            max = t2;
        }
        switch (this.pattern) {
            case 0: {
                ((CustomForm.Builder)form.getForm()).slider((String)this.name.getSingle(event), min2, max);
                break;
            }
            case 1: {
                float def = ((Number)this.def.getSingle(event)).floatValue();
                if (def < min2) {
                    def = min2;
                }
                if (def > max) {
                    def = max;
                }
                ((CustomForm.Builder)form.getForm()).slider((String)this.name.getSingle(event), min2, max, def);
                break;
            }
            case 2: {
                int dif;
                int step;
                float def = ((Number)this.def.getSingle(event)).floatValue();
                if (def < min2) {
                    def = min2;
                }
                if (def > max) {
                    def = max;
                }
                if ((step = ((Number)this.step.getSingle(event)).intValue()) > (dif = (int)(max - min2)) || step < 0) {
                    step = 1;
                }
                ((CustomForm.Builder)form.getForm()).slider((String)this.name.getSingle(event), min2, max, (float)step, def);
            }
        }
        form.addComponent(ComponentType.SLIDER);
    }

    public String toString(Event e, boolean debug) {
        return "create form slider";
    }

    static {
        Skript.registerEffect(EffSlider.class, (String[])new String[]{"form(-| )slider (with name|named) %string% (with|and) [min[imum] [value]] %number%(, | (with|and) ) [max[imum] [value]] %number%", "form(-| )slider (with name|named) %string% (with|and) [min[imum] [value]] %number%(, | (with|and) ) [max[imum] [value]] %number%(, | (with|and) ) [def[ault] [value]] %number%", "form(-| )slider (with name|named) %string% (with|and) [min[imum] [value]] %number%(, | (with|and) ) [max[imum] [value]] %number%(, | (with|and) ) [def[ault] [value]] %number%(, | (with|and) ) [[step] [value]] %number%"});
    }
}

