package com.vincentmet.mkm;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.common.util.Lazy;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class Keybinds {
    public static final Lazy<KeyMapping> OPEN_EDITOR = Lazy.of(()->new KeyMapping("key.categories.mkm.editor",KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_PERIOD, "key.categories.mkm.editor"));
    public static final Lazy<KeyMapping> PREV_MACROSET = Lazy.of(()->new KeyMapping("key.categories.mkm.prev_macroset", KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_PAGE_UP, "key.categories.mkm.macroset_selection"));
    public static final Lazy<KeyMapping> NEXT_MACROSET = Lazy.of(()->new KeyMapping("key.categories.mkm.next_macroset", KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_PAGE_DOWN, "key.categories.mkm.macroset_selection"));
    public static final Lazy<MacroKeybindWrapper> MACRO0 = Lazy.of(()->new MacroKeybindWrapper("mkm.keys.macro0", GLFW.GLFW_KEY_KP_0, "key.categories.mkm.macros", ()->MacroManager.getCurrentMacroSet().getMacro0(),  (newValue)->MacroManager.getCurrentMacroSet().setMacro0(newValue)));
    public static final Lazy<MacroKeybindWrapper> MACRO1 = Lazy.of(()->new MacroKeybindWrapper("mkm.keys.macro1", GLFW.GLFW_KEY_KP_1, "key.categories.mkm.macros", ()->MacroManager.getCurrentMacroSet().getMacro1(),  (newValue)->MacroManager.getCurrentMacroSet().setMacro1(newValue)));
    public static final Lazy<MacroKeybindWrapper> MACRO2 = Lazy.of(()->new MacroKeybindWrapper("mkm.keys.macro2", GLFW.GLFW_KEY_KP_2, "key.categories.mkm.macros", ()->MacroManager.getCurrentMacroSet().getMacro2(),  (newValue)->MacroManager.getCurrentMacroSet().setMacro2(newValue)));
    public static final Lazy<MacroKeybindWrapper> MACRO3 = Lazy.of(()->new MacroKeybindWrapper("mkm.keys.macro3", GLFW.GLFW_KEY_KP_3, "key.categories.mkm.macros", ()->MacroManager.getCurrentMacroSet().getMacro3(),  (newValue)->MacroManager.getCurrentMacroSet().setMacro3(newValue)));
    public static final Lazy<MacroKeybindWrapper> MACRO4 = Lazy.of(()->new MacroKeybindWrapper("mkm.keys.macro4", GLFW.GLFW_KEY_KP_4, "key.categories.mkm.macros", ()->MacroManager.getCurrentMacroSet().getMacro4(),  (newValue)->MacroManager.getCurrentMacroSet().setMacro4(newValue)));
    public static final Lazy<MacroKeybindWrapper> MACRO5 = Lazy.of(()->new MacroKeybindWrapper("mkm.keys.macro5", GLFW.GLFW_KEY_KP_5, "key.categories.mkm.macros", ()->MacroManager.getCurrentMacroSet().getMacro5(),  (newValue)->MacroManager.getCurrentMacroSet().setMacro5(newValue)));
    public static final Lazy<MacroKeybindWrapper> MACRO6 = Lazy.of(()->new MacroKeybindWrapper("mkm.keys.macro6", GLFW.GLFW_KEY_KP_6, "key.categories.mkm.macros", ()->MacroManager.getCurrentMacroSet().getMacro6(),  (newValue)->MacroManager.getCurrentMacroSet().setMacro6(newValue)));
    public static final Lazy<MacroKeybindWrapper> MACRO7 = Lazy.of(()->new MacroKeybindWrapper("mkm.keys.macro7", GLFW.GLFW_KEY_KP_7, "key.categories.mkm.macros", ()->MacroManager.getCurrentMacroSet().getMacro7(),  (newValue)->MacroManager.getCurrentMacroSet().setMacro7(newValue)));
    public static final Lazy<MacroKeybindWrapper> MACRO8 = Lazy.of(()->new MacroKeybindWrapper("mkm.keys.macro8", GLFW.GLFW_KEY_KP_8, "key.categories.mkm.macros", ()->MacroManager.getCurrentMacroSet().getMacro8(),  (newValue)->MacroManager.getCurrentMacroSet().setMacro8(newValue)));
    public static final Lazy<MacroKeybindWrapper> MACRO9 = Lazy.of(()->new MacroKeybindWrapper("mkm.keys.macro9", GLFW.GLFW_KEY_KP_9, "key.categories.mkm.macros", ()->MacroManager.getCurrentMacroSet().getMacro9(),  (newValue)->MacroManager.getCurrentMacroSet().setMacro9(newValue)));

    public static List<Lazy<MacroKeybindWrapper>> getAllMacros(){
        List<Lazy<MacroKeybindWrapper>> list = new ArrayList<>();
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
}