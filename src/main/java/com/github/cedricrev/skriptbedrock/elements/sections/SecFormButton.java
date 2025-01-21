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
import org.geysermc.cumulus.form.ModalForm;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.cumulus.form.util.FormType;
import org.geysermc.cumulus.util.FormImage;

@Name(value="Forms - Button section")
@Description(value={"Create buttons on modal or simple form", "Cannot be used on custom forms"})
@Examples(value={"create modal form named \"Modal form\":", "\tbutton named \"I like skript!\":", "\t\tbroadcast \"Thank you!!!\"", "\tbutton named \"I didnt like skript!\":", "\t\tbroadcast \"USE DENIZEN INSTEAD!!!\""})
@RequiredPlugins(value={"Floodgate"})
@Since(value="1.0")
public class SecFormButton
        extends EffectSection {
    private Trigger trigger;
    private Expression<String> text;
    private Expression<String> image;

    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean kleenean, SkriptParser.ParseResult parseResult, SectionNode sectionNode, List<TriggerItem> items) {
        if (!this.getParser().isCurrentSection(new Class[]{SecCreateModalForm.class, SecCreateSimpleForm.class})) {
            Skript.error((String)"You can't make a form button outside of a Form creation section.", (ErrorQuality)ErrorQuality.SEMANTIC_ERROR);
            return false;
        }
        if (this.getParser().isCurrentSection(SecCreateModalForm.class) && matchedPattern > 0) {
            Skript.error((String)"You can't make a form button with image on Modal forms.", (ErrorQuality)ErrorQuality.SEMANTIC_ERROR);
            return false;
        }
        this.text = (Expression<String>) exprs[0];
        this.image = matchedPattern > 0 ? (Expression<String>) exprs[1] : null;
        if (this.hasSection()) {
            assert (sectionNode != null);
            this.trigger = this.loadCode(sectionNode, "form button click event", new Class[]{FormSubmitEvent.class});
        }
        return true;
    }

    public TriggerItem walk(Event event) {
        Form form;
        block12: {
            block11: {
                form = FormManager.getFormManager().getForm(event);
                if (form == null) {
                    return this.walk(event, false);
                }
                if (form.getType() != FormType.MODAL_FORM) break block11;
                switch (form.getLastButton()) {
                    case 0: {
                        ((ModalForm.Builder)form.getForm()).button1((String)this.text.getSingle(event));
                        break block12;
                    }
                    case 1: {
                        ((ModalForm.Builder)form.getForm()).button2((String)this.text.getSingle(event));
                        break block12;
                    }
                    default: {
                        return this.walk(event, false);
                    }
                }
            }
            if (this.image == null) {
                ((SimpleForm.Builder)form.getForm()).button((String)this.text.getSingle(event));
            } else {
                ((SimpleForm.Builder)form.getForm()).button((String)this.text.getSingle(event), FormImage.Type.URL, (String)this.image.getSingle(event));
            }
        }
        if (this.hasSection()) {
            Object variables = Variables.copyLocalVariables((Event)event);
            if (variables != null) {
                form.setButton(evt -> {
                    Variables.setLocalVariables((Event)evt, (Object)variables);
                    this.trigger.execute((Event)evt);
                });
            } else {
                form.setButton(evt -> this.trigger.execute((Event)evt));
            }
        } else {
            form.setButton(null);
        }
        return this.walk(event, false);
    }

    public String toString(Event e, boolean debug) {
        return "create form button " + this.text.toString(e, debug);
    }

    static {
        Skript.registerSection(SecFormButton.class, (String[])new String[]{"form(-| )button ((with (name|title))|named) %string%", "form(-| )button ((with (name|title))|named) %string% with image %string%"});
    }
}

