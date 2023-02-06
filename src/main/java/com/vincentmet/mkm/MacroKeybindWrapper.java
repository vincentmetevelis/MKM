package com.vincentmet.mkm;

import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.TranslatableComponent;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class MacroKeybindWrapper extends KeyMapping {
    private final Supplier<String> currentMacroGetter;
    private final Consumer<String> currentMacroSetter;
    private final TranslatableComponent translatableComponent;

    public MacroKeybindWrapper(String _name, int _defaultKey, String _category, Supplier<String> _currentMacroGetter, Consumer<String> _currentMacroSetter) {
        super(_name, _defaultKey, _category);
        currentMacroGetter = _currentMacroGetter;
        currentMacroSetter = _currentMacroSetter;
        translatableComponent = new TranslatableComponent(_name);
    }

    public TranslatableComponent getTranslation(){
        return translatableComponent;
    }

    public void setConfig(String command){
        currentMacroSetter.accept(command);
    }

    public String getMacroGetterValue(){
        return currentMacroGetter.get();
    }
}