package com.vincentmet.mkm;

import com.mojang.blaze3d.vertex.PoseStack;
import com.vincentmet.mkm.rendering.GLScissorStack;
import com.vincentmet.mkm.rendering.ScrollingLabel;
import com.vincentmet.mkm.utils.IntCounter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntSupplier;

public class MacroScreen extends Screen {
    private int scrollDistance = 0;
    private static final Component MACRO_SET = new TranslatableComponent("mkm.text.macro_set");
    private final List<SingleLineTextField> ALL_FIELDS = new ArrayList<>();

    private final IntSupplier dataContainerX = ()->20;
    private final IntSupplier dataContainerY = ()->20;
    private final IntSupplier dataContainerWidth = ()->width-40;
    private final IntSupplier dataContainerHeight = ()->height-40;

    private VariableButton buttonSaveAll;
    private final IntSupplier buttonSaveAllX = ()->20;
    private final IntSupplier buttonSaveAllY = ()->0;
    private final IntSupplier buttonSaveAllWidth = ()->40;
    private final IntSupplier buttonSaveAllHeight = ()->20;

    private String lastLabelText = MACRO_SET.getString();
    private ScrollingLabel labelWhichSet;
    private final IntSupplier labelX = ()->60+2;
    private final IntSupplier labelY = ()->(20>>1)-(font.lineHeight>>1);
    private final IntSupplier labelWidth = ()-> 80-4;
    private final IntSupplier labelHeight = ()-> 20;

    private VariableButton buttonAddNewSet;
    private final IntSupplier buttonAddNewSetX = ()->width-120;
    private final IntSupplier buttonAddNewSetY = ()->0;
    private final IntSupplier buttonAddNewSetWidth = ()-> 20;
    private final IntSupplier buttonAddNewSetHeight = ()-> 20;

    private VariableButton buttonRemoveCurrentSet;
    private final IntSupplier buttonRemoveCurrentSetX = ()->width-100;
    private final IntSupplier buttonRemoveCurrentSetY = ()->0;
    private final IntSupplier buttonRemoveCurrentSetWidth = ()-> 20;
    private final IntSupplier buttonRemoveCurrentSetHeight = ()-> 20;

    private VariableButton buttonPrev;
    private final IntSupplier buttonPrevX = ()->width-60;
    private final IntSupplier buttonPrevY = ()->0;
    private final IntSupplier buttonPrevWidth = ()-> 20;
    private final IntSupplier buttonPrevHeight = ()-> 20;

    private VariableButton buttonNext;
    private final IntSupplier buttonNextX = ()->width-40;
    private final IntSupplier buttonNextY = ()->0;
    private final IntSupplier buttonNextWidth = ()-> 20;
    private final IntSupplier buttonNextHeight = ()-> 20;

    public MacroScreen() {
        super(new TextComponent("MKM"));
        IntCounter y = new IntCounter(dataContainerY.getAsInt(), 20);
        for (MacroKeybindWrapper keybind : Keybinds.getAllMacros()){
            final int finalY = y.getValue();
            ALL_FIELDS.add(new SingleLineTextField(()->(width>>1), ()->finalY, ()->(dataContainerWidth.getAsInt()>>1), ()->20, 0xFF000000, 0xFFAAAAAA, 0xFFFFFFFF, 0xFFFFFFFF, 0xFFEE00EE, keybind.getMacroGetterValue()));//todo set better selection color
            y.count();
        }
        buttonSaveAll = new VariableButton(buttonSaveAllX, buttonSaveAllY, buttonSaveAllWidth, buttonSaveAllHeight, new TranslatableComponent("selectWorld.edit.save").getString(), VariableButton.ButtonTexture.DEFAULT_NORMAL, mouseButton -> {
            IntCounter i = new IntCounter();
            for(MacroKeybindWrapper keybind : Keybinds.getAllMacros()){
                keybind.setConfig(ALL_FIELDS.get(i.getValue()).getText());
                i.count();
            }
            Config.writeConfigToDiskWithModFiles();
            if(Minecraft.getInstance().player!=null) Minecraft.getInstance().player.displayClientMessage(new TranslatableComponent("mkm.text.saved_macros_to_file"), false);
        });
        labelWhichSet = new ScrollingLabel(labelX, labelY, MACRO_SET.getString() + MacroManager.getCurrentMacroSetId(), labelWidth, 5, 1);
        buttonRemoveCurrentSet = new VariableButton(buttonRemoveCurrentSetX, buttonRemoveCurrentSetY, buttonRemoveCurrentSetWidth, buttonRemoveCurrentSetHeight, "-", VariableButton.ButtonTexture.DEFAULT_NORMAL, mouseButton -> {
            MacroManager.removeMacroSet(MacroManager.getCurrentMacroSetId());
            MacroManager.setCurrentMacroSetId(0);
            Config.writeConfigToDiskWithModFiles();
        });
        buttonAddNewSet = new VariableButton(buttonAddNewSetX, buttonAddNewSetY, buttonAddNewSetWidth, buttonAddNewSetHeight, "+", VariableButton.ButtonTexture.DEFAULT_NORMAL, mouseButton -> {
            MacroManager.addNewEmptyMacroSet();
            MacroManager.setCurrentMacroSetId(MacroManager.getAllMacros().size()-1);
            Config.writeConfigToDiskWithModFiles();
        });
        buttonPrev = new VariableButton(buttonPrevX, buttonPrevY, buttonPrevWidth, buttonPrevHeight, "<", VariableButton.ButtonTexture.DEFAULT_NORMAL, mouseButton -> {
            MacroManager.usePreviousMacroset();
        });
        buttonNext = new VariableButton(buttonNextX, buttonNextY, buttonNextWidth, buttonNextHeight, ">", VariableButton.ButtonTexture.DEFAULT_NORMAL, mouseButton -> {
            MacroManager.useNextMacroset();
        });
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        fill(stack, 0, 0, width, height, 0x88000000);
        fill(stack, dataContainerX.getAsInt(), dataContainerY.getAsInt(), dataContainerX.getAsInt() + dataContainerWidth.getAsInt(), dataContainerY.getAsInt() + dataContainerHeight.getAsInt(), 0x88000000);
        buttonPrev.render(stack, mouseX, mouseY, partialTicks);
        labelWhichSet.render(stack, mouseX, mouseY, partialTicks);
        buttonAddNewSet.render(stack, mouseX, mouseY, partialTicks);
        buttonRemoveCurrentSet.render(stack, mouseX, mouseY, partialTicks);
        buttonNext.render(stack, mouseX, mouseY, partialTicks);
        buttonSaveAll.render(stack, mouseX, mouseY, partialTicks);
        GLScissorStack.push(stack, dataContainerX.getAsInt(), dataContainerY.getAsInt(), dataContainerWidth.getAsInt(), dataContainerHeight.getAsInt());
        int i = 0;
        for(MacroKeybindWrapper keybind : Keybinds.getAllMacros()){
            int localY = dataContainerY.getAsInt() - scrollDistance + 20*i;
            drawString(stack, font, keybind.getTranslation(), dataContainerX.getAsInt() + 20, localY + ((20>>1) - (font.lineHeight>>1)), 0xFFFFFF);
            ALL_FIELDS.forEach(singleLineTextField -> singleLineTextField.render(stack, mouseX, mouseY, partialTicks));
            i++;
        }
        GLScissorStack.pop(stack);
    }
    
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        buttonAddNewSet.mouseClicked(mouseX, mouseY, button);
        buttonRemoveCurrentSet.mouseClicked(mouseX, mouseY, button);
        buttonPrev.mouseClicked(mouseX, mouseY, button);
        buttonNext.mouseClicked(mouseX, mouseY, button);
        buttonSaveAll.mouseClicked(mouseX, mouseY, button);
        ALL_FIELDS.forEach(singleLineTextField -> singleLineTextField.mouseClicked(mouseX, mouseY, button));
        return true;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE && this.shouldCloseOnEsc()) {
            this.onClose();
        }
        ALL_FIELDS.forEach(singleLineTextField -> singleLineTextField.keyPressed(keyCode, scanCode, modifiers));
        return true;
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        ALL_FIELDS.forEach(singleLineTextField -> singleLineTextField.charTyped(codePoint, modifiers));
        return true;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double dyScroll) {
        ALL_FIELDS.forEach(singleLineTextField -> singleLineTextField.mouseScrolled(mouseX, mouseY, dyScroll));
        if(isMouseInBounds(mouseX, mouseY, dataContainerX.getAsInt(), dataContainerY.getAsInt(), dataContainerX.getAsInt() + dataContainerWidth.getAsInt(), dataContainerY.getAsInt() + dataContainerHeight.getAsInt())){
            scrollDistance -= dyScroll*getScrollAmount();
        }
        applyScrollLimits();
        return true;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        ALL_FIELDS.forEach(singleLineTextField -> singleLineTextField.mouseReleased(mouseX, mouseY, button));
        return true;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dx, double dy) {
        ALL_FIELDS.forEach(singleLineTextField -> singleLineTextField.mouseDragged(mouseX, mouseY, button, dx, dy));
        return true;
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        ALL_FIELDS.forEach(singleLineTextField -> singleLineTextField.keyReleased(keyCode, scanCode, modifiers));
        return true;
    }

    @Override
    public void mouseMoved(double dx, double dy) {
        ALL_FIELDS.forEach(singleLineTextField -> singleLineTextField.mouseMoved(dx, dy));
    }

    public int getContentHeight(){
        return Math.max(20*Keybinds.getAllMacros().size(), dataContainerHeight.getAsInt());
    }

    private int getMaxScroll(){
        return Math.max(getContentHeight() - dataContainerHeight.getAsInt(), 0);
    }

    private void applyScrollLimits(){
        if(this.scrollDistance < 0){
            this.scrollDistance = 0;
        }

        if(this.scrollDistance > getMaxScroll()){
            this.scrollDistance = getMaxScroll();
        }
    }

    private int getScrollAmount(){
        return 10;
    }

    public int getScrollDistance() {
        return scrollDistance;
    }

    public void setScrollDistance(int scrollDistance) {
        this.scrollDistance = scrollDistance;
        applyScrollLimits();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    public static boolean isMouseInBounds(double mouseX, double mouseY, int x1, int y1, int x2, int y2){
        return x2 > mouseX && mouseX >= x1 && y2 > mouseY && mouseY >= y1;
    }
}