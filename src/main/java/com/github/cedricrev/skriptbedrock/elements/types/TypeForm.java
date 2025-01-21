package com.github.cedricrev.skriptbedrock.elements.types;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import com.github.cedricrev.skriptbedrock.forms.Form;

public class TypeForm {
    public static void register() {
        Classes.registerClass((ClassInfo)new ClassInfo(Form.class, "form").user(new String[]{"forms?"}).name("Form").description(new String[]{"Represent a form (com.lotzy.skcrew.spigot.floodgate.forms.Form class)"}).since("1.0").parser((Parser)new Parser<Form>(){

            public Form parse(String s2, ParseContext context) {
                return null;
            }

            public boolean canParse(ParseContext ctx) {
                return false;
            }

            public String toString(Form o, int flags) {
                return o.toString();
            }

            public String toVariableNameString(Form o) {
                return o.toString();
            }
        }));
    }
}

