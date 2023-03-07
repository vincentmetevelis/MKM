package com.vincentmet.mkm.normalmacros;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.ClientRegistry;

import java.util.ArrayList;
import java.util.List;

public class Keybinds {
    public static final KeyMapping OPEN_EDITOR = new KeyMapping("mkm.keys.editor", InputConstants.KEY_PERIOD, "mkm.keys.editor");
    public static final KeyMapping PREV_MACROSET = new KeyMapping("mkm.keys.prev_macroset", InputConstants.KEY_PAGEUP, "mkm.keys.macroset_selection");
    public static final KeyMapping NEXT_MACROSET = new KeyMapping("mkm.keys.next_macroset", InputConstants.KEY_PAGEDOWN, "mkm.keys.macroset_selection");
    public static final MacroKeybindWrapper MACRO0 = new MacroKeybindWrapper("mkm.keys.macro0", InputConstants.KEY_NUMPAD0, "mkm.keys.macros", ()->MacroManager.getCurrentMacroSet().getMacro0(),  (newValue)->MacroManager.getCurrentMacroSet().setMacro0(newValue));
    public static final MacroKeybindWrapper MACRO1 = new MacroKeybindWrapper("mkm.keys.macro1", InputConstants.KEY_NUMPAD1, "mkm.keys.macros", ()->MacroManager.getCurrentMacroSet().getMacro1(),  (newValue)->MacroManager.getCurrentMacroSet().setMacro1(newValue));
    public static final MacroKeybindWrapper MACRO2 = new MacroKeybindWrapper("mkm.keys.macro2", InputConstants.KEY_NUMPAD2, "mkm.keys.macros", ()->MacroManager.getCurrentMacroSet().getMacro2(),  (newValue)->MacroManager.getCurrentMacroSet().setMacro2(newValue));
    public static final MacroKeybindWrapper MACRO3 = new MacroKeybindWrapper("mkm.keys.macro3", InputConstants.KEY_NUMPAD3, "mkm.keys.macros", ()->MacroManager.getCurrentMacroSet().getMacro3(),  (newValue)->MacroManager.getCurrentMacroSet().setMacro3(newValue));
    public static final MacroKeybindWrapper MACRO4 = new MacroKeybindWrapper("mkm.keys.macro4", InputConstants.KEY_NUMPAD4, "mkm.keys.macros", ()->MacroManager.getCurrentMacroSet().getMacro4(),  (newValue)->MacroManager.getCurrentMacroSet().setMacro4(newValue));
    public static final MacroKeybindWrapper MACRO5 = new MacroKeybindWrapper("mkm.keys.macro5", InputConstants.KEY_NUMPAD5, "mkm.keys.macros", ()->MacroManager.getCurrentMacroSet().getMacro5(),  (newValue)->MacroManager.getCurrentMacroSet().setMacro5(newValue));
    public static final MacroKeybindWrapper MACRO6 = new MacroKeybindWrapper("mkm.keys.macro6", InputConstants.KEY_NUMPAD6, "mkm.keys.macros", ()->MacroManager.getCurrentMacroSet().getMacro6(),  (newValue)->MacroManager.getCurrentMacroSet().setMacro6(newValue));
    public static final MacroKeybindWrapper MACRO7 = new MacroKeybindWrapper("mkm.keys.macro7", InputConstants.KEY_NUMPAD7, "mkm.keys.macros", ()->MacroManager.getCurrentMacroSet().getMacro7(),  (newValue)->MacroManager.getCurrentMacroSet().setMacro7(newValue));
    public static final MacroKeybindWrapper MACRO8 = new MacroKeybindWrapper("mkm.keys.macro8", InputConstants.KEY_NUMPAD8, "mkm.keys.macros", ()->MacroManager.getCurrentMacroSet().getMacro8(),  (newValue)->MacroManager.getCurrentMacroSet().setMacro8(newValue));
    public static final MacroKeybindWrapper MACRO9 = new MacroKeybindWrapper("mkm.keys.macro9", InputConstants.KEY_NUMPAD9, "mkm.keys.macros", ()->MacroManager.getCurrentMacroSet().getMacro9(),  (newValue)->MacroManager.getCurrentMacroSet().setMacro9(newValue));

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
        ClientRegistry.registerKeyBinding(PREV_MACROSET);
        ClientRegistry.registerKeyBinding(NEXT_MACROSET);
        getAllMacros().forEach(ClientRegistry::registerKeyBinding);
    }
}