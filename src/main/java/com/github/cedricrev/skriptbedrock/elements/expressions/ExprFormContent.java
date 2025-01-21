package com.github.cedricrev.skriptbedrock.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
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
import com.github.cedricrev.skriptbedrock.elements.sections.SecCreateModalForm;
import com.github.cedricrev.skriptbedrock.elements.sections.SecCreateSimpleForm;
import com.github.cedricrev.skriptbedrock.elements.sections.SecFormButton;
import com.github.cedricrev.skriptbedrock.forms.Form;
import com.github.cedricrev.skriptbedrock.forms.FormManager;
import org.bukkit.event.Event;
import org.geysermc.cumulus.form.ModalForm;
import org.geysermc.cumulus.form.SimpleForm;

@Name(value="Forms - Content")
@Description(value={"Get or set content of form", "Can be used in modal and simple forms"})
@Examples(value={"create modal form with name \"modal form\":", "\tset form's content to \"Hello world!\""})
@RequiredPlugins(value={"Floodgate"})
@Since(value="1.0")
public class ExprFormContent
        extends SimpleExpression<String> {
    private Expression<Form> form;

    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        if (matchedPattern > 1) {
            this.form = (Expression<Form>) exprs[0];
        } else {
            SkriptEvent skriptEvent;
            if (!(this.getParser().isCurrentSection(new Class[]{SecCreateModalForm.class, SecCreateSimpleForm.class}) || (skriptEvent = this.getParser().getCurrentSkriptEvent()) instanceof SectionSkriptEvent && ((SectionSkriptEvent)skriptEvent).isSection(SecFormButton.class))) {
                Skript.error((String)"You can't change content of form outside of a Modal or Simple form creation section.", (ErrorQuality)ErrorQuality.SEMANTIC_ERROR);
                return false;
            }
            this.form = null;
        }
        return true;
    }

    protected String[] get(Event e) {
        Form form = this.form == null ? FormManager.getFormManager().getForm(e) : (Form)this.form.getSingle(e);
        switch (form.getType()) {
            case SIMPLE_FORM: {
                return new String[]{((SimpleForm)((SimpleForm.Builder)form.getForm()).build()).content()};
            }
            case MODAL_FORM: {
                return new String[]{((ModalForm)((ModalForm.Builder)form.getForm()).build()).content()};
            }
        }
        Skript.error((String)"Custom forms doesn't support content", (ErrorQuality)ErrorQuality.SEMANTIC_ERROR);
        return null;
    }

    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        switch (mode) {
            case SET:
            case DELETE: {
                return new Class[]{String.class};
            }
        }
        return null;
    }

    public void change(Event e, Object[] delta, Changer.ChangeMode mode) {
        Form form = this.form == null ? FormManager.getFormManager().getForm(e) : (Form)this.form.getSingle(e);
        String content = mode == Changer.ChangeMode.SET ? (String)delta[0] : "";
        switch (form.getType()) {
            case SIMPLE_FORM: {
                ((SimpleForm.Builder)form.getForm()).content(content);
                break;
            }
            case MODAL_FORM: {
                ((ModalForm.Builder)form.getForm()).content(content);
                break;
            }
            default: {
                Skript.error((String)"Custom forms doesn't support content", (ErrorQuality)ErrorQuality.SEMANTIC_ERROR);
            }
        }
    }

    public boolean isSingle() {
        return true;
    }

    public Class<? extends String> getReturnType() {
        return String.class;
    }

    public String toString(Event e, boolean debug) {
        return "Content of form";
    }

    static {
        Skript.registerExpression(ExprFormContent.class, String.class, (ExpressionType)ExpressionType.COMBINED, (String[])new String[]{"form['s] content", "content of form", "content of %form%", "%form%'s content"});
    }
}

