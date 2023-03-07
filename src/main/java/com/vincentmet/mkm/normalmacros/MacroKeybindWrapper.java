package com.vincentmet.mkm.normalmacros;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.client.settings.KeyConflictContext;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class MacroKeybindWrapper extends KeyMapping {
    private final Supplier<String> currentMacroGetter;
    private final Consumer<String> currentMacroSetter;
    private final MutableComponent translatableComponent;

    public MacroKeybindWrapper(String _name, int _defaultKey, String _category, Supplier<String> _currentMacroGetter, Consumer<String> _currentMacroSetter) {
        super(_name, KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, _defaultKey, _category);
        currentMacroGetter = _currentMacroGetter;
        currentMacroSetter = _currentMacroSetter;
        translatableComponent = Component.translatable(_name);
    }

    public MutableComponent getTranslation(){
        return translatableComponent;
    }

    public void setConfig(String command){
        currentMacroSetter.accept(command);
    }

    public String getMacroGetterValue(){
        return currentMacroGetter.get();
    }
}