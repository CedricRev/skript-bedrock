package com.github.cedricrev.skriptbedrock.elements.types;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import com.github.cedricrev.skriptbedrock.elements.events.FormCloseEvent;

public class TypeCloseReason {
    public static void register() {
        Classes.registerClass((ClassInfo)new ClassInfo(FormCloseEvent.CloseReason.class, "closereason").user(new String[]{"close ?reasons?"}).name("Close Reason").description(new String[]{"Close reason (represents com.lotzy.skcrew.spigot.floodgate.forms.events.FormCloseEvent.CloseReason class)"}).since("3.0").parser((Parser)new Parser<FormCloseEvent.CloseReason>(){

            public FormCloseEvent.CloseReason parse(String s2, ParseContext context) {
                return null;
            }

            public String toString(FormCloseEvent.CloseReason o, int flags) {
                return o.toString().toLowerCase().replace('_', ' ');
            }

            public String toVariableNameString(FormCloseEvent.CloseReason o) {
                return o.toString().toLowerCase().replace('_', ' ');
            }
        }));
    }
}

