package com.github.cedricrev.skriptbedrock.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
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
import com.github.cedricrev.skriptbedrock.elements.events.BaseFormEvent;
import com.github.cedricrev.skriptbedrock.elements.sections.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

@Name(value="Forms - Form player")
@Description(value={"Get player inside forms sections"})
@RequiredPlugins(value={"Floodgate"})
@Since(value="3.0")
public class ExprFormPlayer
        extends SimpleExpression<Player> {
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        SkriptEvent skriptEvent;
        if (!(this.getParser().isCurrentSection(new Class[]{SecCreateCustomForm.class, SecCreateModalForm.class, SecCreateSimpleForm.class}) || (skriptEvent = this.getParser().getCurrentSkriptEvent()) instanceof SectionSkriptEvent && (((SectionSkriptEvent)skriptEvent).isSection(SecFormResult.class) || ((SectionSkriptEvent)skriptEvent).isSection(SecFormOpenClose.class) || ((SectionSkriptEvent)skriptEvent).isSection(SecFormButton.class)))) {
            Skript.error((String)"You can't use a form-player outside of a form creation section.", (ErrorQuality)ErrorQuality.SEMANTIC_ERROR);
            return false;
        }
        return true;
    }

    protected Player[] get(Event event) {
        return new Player[]{((BaseFormEvent)event).getPlayer()};
    }

    public boolean isSingle() {
        return true;
    }

    public Class<? extends Player> getReturnType() {
        return Player.class;
    }

    public String toString(Event event, boolean debug) {
        return "form-player";
    }

    static {
        Skript.registerExpression(ExprFormPlayer.class, Player.class, (ExpressionType)ExpressionType.SIMPLE, (String[])new String[]{"form(-| )player"});
    }
}

