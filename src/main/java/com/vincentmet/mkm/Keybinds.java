package com.vincentmet.mkm;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.ClientRegistry;
import java.util.ArrayList;
import java.util.List;

public class Keybinds {
    public static final KeyMapping OPEN_EDITOR = new KeyMapping("mkm.keys.editor", InputConstants.KEY_PERIOD, "mkm.keys.editor");
    public static final MacroKeybindWrapper MACRO0 = new MacroKeybindWrapper("mkm.keys.macro0", InputConstants.KEY_NUMPAD0, "mkm.keys.macros", Config.getMacro0(), Config::setMacro0);
    public static final MacroKeybindWrapper MACRO1 = new MacroKeybindWrapper("mkm.keys.macro1", InputConstants.KEY_NUMPAD1, "mkm.keys.macros", Config.getMacro1(), Config::setMacro1);
    public static final MacroKeybindWrapper MACRO2 = new MacroKeybindWrapper("mkm.keys.macro2", InputConstants.KEY_NUMPAD2, "mkm.keys.macros", Config.getMacro2(), Config::setMacro2);
    public static final MacroKeybindWrapper MACRO3 = new MacroKeybindWrapper("mkm.keys.macro3", InputConstants.KEY_NUMPAD3, "mkm.keys.macros", Config.getMacro3(), Config::setMacro3);
    public static final MacroKeybindWrapper MACRO4 = new MacroKeybindWrapper("mkm.keys.macro4", InputConstants.KEY_NUMPAD4, "mkm.keys.macros", Config.getMacro4(), Config::setMacro4);
    public static final MacroKeybindWrapper MACRO5 = new MacroKeybindWrapper("mkm.keys.macro5", InputConstants.KEY_NUMPAD5, "mkm.keys.macros", Config.getMacro5(), Config::setMacro5);
    public static final MacroKeybindWrapper MACRO6 = new MacroKeybindWrapper("mkm.keys.macro6", InputConstants.KEY_NUMPAD6, "mkm.keys.macros", Config.getMacro6(), Config::setMacro6);
    public static final MacroKeybindWrapper MACRO7 = new MacroKeybindWrapper("mkm.keys.macro7", InputConstants.KEY_NUMPAD7, "mkm.keys.macros", Config.getMacro7(), Config::setMacro7);
    public static final MacroKeybindWrapper MACRO8 = new MacroKeybindWrapper("mkm.keys.macro8", InputConstants.KEY_NUMPAD8, "mkm.keys.macros", Config.getMacro8(), Config::setMacro8);
    public static final MacroKeybindWrapper MACRO9 = new MacroKeybindWrapper("mkm.keys.macro9", InputConstants.KEY_NUMPAD9, "mkm.keys.macros", Config.getMacro9(), Config::setMacro9);

    public static List<MacroKeybindWrapper> getAllMacros(){
        List<MacroKeybindWrapper> list = new ArrayList<>();
        list.add(MACRO0);
        list.add(MACRO1);
        list.add(MACRO2);
        list.add(MACRO3);
        list.add(MACRO4);
        list.add(MACRO5);
        list.add(MACRO6);
        list.add(MACRO7);
        list.add(MACRO8);
        list.add(MACRO9);
        return list;
    }

    public static void registerKeybinds(){
        ClientRegistry.registerKeyBinding(OPEN_EDITOR);
        getAllMacros().forEach(ClientRegistry::registerKeyBinding);
    }
}