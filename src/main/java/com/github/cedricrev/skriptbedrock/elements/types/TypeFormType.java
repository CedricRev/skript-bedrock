package com.github.cedricrev.skriptbedrock.elements.types;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import org.geysermc.cumulus.form.util.FormType;

public class TypeFormType {
    public static void register() {
        Classes.registerClass((ClassInfo)new ClassInfo(FormType.class, "formtype").user(new String[]{"form ?types?"}).name("Form Type").description(new String[]{"Form types (represents org.geysermc.cumulus.form.util.FormType class)"}).since("3.0").parser((Parser)new Parser<FormType>(){

            public FormType parse(String s2, ParseContext context) {
                return null;
            }

            public String toString(FormType o, int flags) {
                return o.toString().toLowerCase().replace('_', ' ') + " form";
            }

            public String toVariableNameString(FormType o) {
                return o.toString().toLowerCase().replace('_', ' ') + " form";
            }
        }));
    }
}

