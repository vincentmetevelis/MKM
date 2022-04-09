package com.vincentmet.mkm;

import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.TranslatableComponent;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class MacroKeybindWrapper extends KeyMapping {
    private final Supplier<String> configGetter;
    private final Consumer<String> configSetter;
    private final TranslatableComponent translatableComponent;
    private final SingleLineTextField commandTextLine;
    private final VariableButton saveButton;

    public MacroKeybindWrapper(String _name, int _defaultKey, String _category, Supplier<String> _configGetter, Consumer<String> _configSetter) {
        super(_name, _defaultKey, _category);
        configGetter = _configGetter;
        configSetter = _configSetter;
        translatableComponent = new TranslatableComponent(_name);
        commandTextLine = new SingleLineTextField(0, 0, 100, 20, 0xFF000000, 0xFFAAAAAA, 0xFFFFFFFF, 0xFFFFFFFF, "");
        saveButton = new VariableButton(0, 0, 20, 20, VariableButton.ButtonTexture.DEFAULT_NORMAL, (mouseButton)->setConfig(commandTextLine.getText()));
    }

    public TranslatableComponent getTranslation(){
        return translatableComponent;
    }

    public void setConfig(String command){
        configSetter.accept(command);
    }

    public String getConfigValue(){
        return configGetter.get();
    }

    public SingleLineTextField getCommandTextLine() {
        return commandTextLine;
    }

    public VariableButton getSaveButton() {
        return saveButton;
    }
}