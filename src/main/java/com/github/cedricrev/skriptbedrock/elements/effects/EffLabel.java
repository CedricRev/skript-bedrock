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

@Name(value="Forms - Label")
@Description(value={"Create label on custom form"})
@Examples(value={"form-label named \"sample text\""})
@RequiredPlugins(value={"Floodgate"})
@Since(value="1.0")
public class EffLabel
        extends Effect {
    Expression<String> name;

    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        SkriptEvent skriptEvent;
        if (!(this.getParser().isCurrentSection(SecCreateCustomForm.class) || (skriptEvent = this.getParser().getCurrentSkriptEvent()) instanceof SectionSkriptEvent && ((SectionSkriptEvent)skriptEvent).isSection(SecFormResult.class))) {
            Skript.error((String)"You can't make a label outside of a Custom form creation section.", (ErrorQuality)ErrorQuality.SEMANTIC_ERROR);
            return false;
        }
        this.name = (Expression<String>) exprs[0];
        return true;
    }

    protected void execute(Event event) {
        Form form = FormManager.getFormManager().getForm(event);
        ((CustomForm.Builder)form.getForm()).label((String)this.name.getSingle(event));
        form.addComponent(ComponentType.LABEL);
    }

    public String toString(Event e, boolean debug) {
        return "create form label";
    }

    static {
        Skript.registerEffect(EffLabel.class, (String[])new String[]{"form(-| )label [(with (name|title)|named)] %string%"});
    }
}

