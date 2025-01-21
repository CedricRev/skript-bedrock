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
import com.github.cedricrev.skriptbedrock.elements.events.FormSubmitEvent;
import com.github.cedricrev.skriptbedrock.elements.sections.SecCreateCustomForm;
import com.github.cedricrev.skriptbedrock.elements.sections.SecFormResult;
import com.github.cedricrev.skriptbedrock.forms.Form;
import org.bukkit.event.Event;
import org.geysermc.cumulus.component.util.ComponentType;
import org.geysermc.cumulus.response.CustomFormResponse;

@Name(value="Forms - Component result")
@Description(value={"Get result of component by index", "Can be used in custom form result section"})
@Examples(value={"run on form result:", "\tbroadcast \"%value of form-toggle 1%\""})
@RequiredPlugins(value={"Floodgate"})
@Since(value="3.0")
public class ExprCustomFormComponents
        extends SimpleExpression<Object> {
    Expression<Number> index;
    ComponentType type;

    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        SkriptEvent skriptEvent;
        if (!(this.getParser().isCurrentSection(SecCreateCustomForm.class) || (skriptEvent = this.getParser().getCurrentSkriptEvent()) instanceof SectionSkriptEvent && ((SectionSkriptEvent)skriptEvent).isSection(SecFormResult.class))) {
            Skript.error((String)"You can't get component value of form outside of a Result of custom form section.", (ErrorQuality)ErrorQuality.SEMANTIC_ERROR);
            return false;
        }
        this.type = ComponentType.values()[parseResult.mark];
        this.index = (Expression<Number>) exprs[0];
        return true;
    }

    protected Object[] get(Event event) {
        FormSubmitEvent submitEvent = (FormSubmitEvent)event;
        Form form = submitEvent.getForm();
        CustomFormResponse response = (CustomFormResponse)submitEvent.getResponse();
        int index = ((Number)this.index.getSingle(event)).intValue() - 1;
        int componentIndex = 0;
        int globalIndex = 0;
        for (ComponentType componentType : form.getComponents()) {
            if (componentType == this.type) {
                if (componentIndex == index) {
                    return new Object[]{response.valueAt(globalIndex)};
                }
                ++componentIndex;
            }
            response.next();
            ++globalIndex;
        }
        return null;
    }

    public boolean isSingle() {
        return true;
    }

    public Class<? extends Object> getReturnType() {
        return Object.class;
    }

    public String toString(Event event, boolean debug) {
        return "form result of " + this.type.toString() + " component";
    }

    static {
        Skript.registerExpression(ExprCustomFormComponents.class, Object.class, (ExpressionType)ExpressionType.COMBINED, (String[])new String[]{"[form[(-| )]](0\u00a6drop[(-| )]down|1\u00a6input|3\u00a6slider|4\u00a6step[(-| )]slider|5\u00a6toggle) %number% [value]", "value of [form[(-| )]](0\u00a6drop[(-| )]down|1\u00a6input|3\u00a6slider|4\u00a6step[(-| )]slider|5\u00a6toggle) %number%"});
    }
}

