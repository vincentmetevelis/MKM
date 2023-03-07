package com.vincentmet.mkm.timedmacros;

import com.mojang.blaze3d.vertex.PoseStack;
import com.vincentmet.mkm.Config;
import com.vincentmet.mkm.normalmacros.MacroScreen;
import com.vincentmet.mkm.rendering.GLScissorStack;
import com.vincentmet.mkm.rendering.ScrollingLabel;
import com.vincentmet.mkm.utils.IntCounter;
import com.vincentmet.mkm.utils.VariableButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntSupplier;

public class TimedMacroScreen extends Screen {
    private int scrollDistance = 0;
    private static final Component TIMED_MACROS = Component.translatable("mkm.text.timed_macros");
    private final List<TimedMacroGuiLine> ALL_TIMED_MACRO_FIELDS = new ArrayList<>();

    private final IntSupplier dataContainerX = ()->20;
    private final IntSupplier dataContainerY = ()->20;
    private final IntSupplier dataContainerWidth = ()->width-40;
    private final IntSupplier dataContainerHeight = ()->height-40;

    private VariableButton buttonSwitchToNormalMacrosScreen;
    private final IntSupplier buttonSwitchToNormalMacrosScreenX = ()->20;
    private final IntSupplier buttonSwitchToNormalMacrosScreenY = ()->0;
    private final IntSupplier buttonSwitchToNormalMacrosScreenWidth = ()->80;
    private final IntSupplier buttonSwitchToNormalMacrosScreenHeight = ()->20;

    private VariableButton buttonSaveAll;
    private final IntSupplier buttonSaveAllX = ()->100;
    private final IntSupplier buttonSaveAllY = ()->0;
    private final IntSupplier buttonSaveAllWidth = ()->40;
    private final IntSupplier buttonSaveAllHeight = ()->20;

    private ScrollingLabel labelTitle;
    private final IntSupplier labelX = ()->140+2;
    private final IntSupplier labelY = ()->(20>>1)-(font.lineHeight>>1);
    private final IntSupplier labelWidth = ()-> 80-4;

    private VariableButton buttonAddNewEntry;
    private final IntSupplier buttonAddNewSetX = ()->width-40;
    private final IntSupplier buttonAddNewSetY = ()->0;
    private final IntSupplier buttonAddNewSetWidth = ()-> 20;
    private final IntSupplier buttonAddNewSetHeight = ()-> 20;


    public TimedMacroScreen() {
        super(Component.literal("MKM"));
        TimedMacroManager.deleteMarkedForDeletion();
        IntCounter tempId = new IntCounter();
        IntCounter y = new IntCounter(dataContainerY.getAsInt(), 20);
        for (TimedMacro timedMacro : TimedMacroManager.getAllTimedMacros()){
            final int finalY = y.getValue();
            ALL_TIMED_MACRO_FIELDS.add(new TimedMacroGuiLine(tempId.getValue(), dataContainerX, ()->finalY, dataContainerWidth, ()->20, timedMacro));
            tempId.count();
            y.count();
        }
        buttonSwitchToNormalMacrosScreen = new VariableButton(buttonSwitchToNormalMacrosScreenX, buttonSwitchToNormalMacrosScreenY, buttonSwitchToNormalMacrosScreenWidth, buttonSwitchToNormalMacrosScreenHeight, Component.translatable("mkm.text.macros").getString(), VariableButton.ButtonTexture.DEFAULT_NORMAL, mouseButton -> {
            Minecraft.getInstance().setScreen(new MacroScreen());
        });
        buttonSaveAll = new VariableButton(buttonSaveAllX, buttonSaveAllY, buttonSaveAllWidth, buttonSaveAllHeight, Component.translatable("selectWorld.edit.save").getString(), VariableButton.ButtonTexture.DEFAULT_NORMAL, mouseButton -> {
            ALL_TIMED_MACRO_FIELDS.forEach(TimedMacroGuiLine::save);
            Config.writeConfigToDiskWithModFiles();
            if(Minecraft.getInstance().player!=null) Minecraft.getInstance().player.displayClientMessage(Component.translatable("mkm.text.saved_macros_to_file"), false);
        });
        labelTitle = new ScrollingLabel(labelX, labelY, TIMED_MACROS.getString(), labelWidth, 5, 1);
        buttonAddNewEntry = new VariableButton(buttonAddNewSetX, buttonAddNewSetY, buttonAddNewSetWidth, buttonAddNewSetHeight, "+", VariableButton.ButtonTexture.DEFAULT_NORMAL, mouseButton -> {
            TimedMacroManager.addNewEmptyTimedMacro();
            Config.writeConfigToDiskWithModFiles();
            MinecraftForge.EVENT_BUS.post(new AddNewTimedMacroEvent());
        });
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        fill(stack, 0, 0, width, height, 0x88000000);
        fill(stack, dataContainerX.getAsInt(), dataContainerY.getAsInt(), dataContainerX.getAsInt() + dataContainerWidth.getAsInt(), dataContainerY.getAsInt() + dataContainerHeight.getAsInt(), 0x88000000);
        labelTitle.render(stack, mouseX, mouseY, partialTicks);
        buttonAddNewEntry.render(stack, mouseX, mouseY, partialTicks);
        buttonSaveAll.render(stack, mouseX, mouseY, partialTicks);
        buttonSwitchToNormalMacrosScreen.render(stack, mouseX, mouseY, partialTicks);
        GLScissorStack.push(stack, dataContainerX.getAsInt(), dataContainerY.getAsInt(), dataContainerWidth.getAsInt(), dataContainerHeight.getAsInt());
        ALL_TIMED_MACRO_FIELDS.forEach(timedMacroGuiLine -> timedMacroGuiLine.render(stack, mouseX, mouseY, partialTicks));
        GLScissorStack.pop(stack);
    }
    
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        buttonAddNewEntry.mouseClicked(mouseX, mouseY, button);
        buttonSaveAll.mouseClicked(mouseX, mouseY, button);
        buttonSwitchToNormalMacrosScreen.mouseClicked(mouseX, mouseY, button);
        if(isMouseInBounds(mouseX, mouseY, dataContainerX.getAsInt(), dataContainerY.getAsInt(), dataContainerX.getAsInt() + dataContainerWidth.getAsInt(), dataContainerY.getAsInt() + dataContainerHeight.getAsInt())){
            ALL_TIMED_MACRO_FIELDS.forEach(singleLineTextField -> singleLineTextField.mouseClicked(mouseX, mouseY, button));
        }
        return true;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE && this.shouldCloseOnEsc()) {
            this.onClose();
        }
        ALL_TIMED_MACRO_FIELDS.forEach(singleLineTextField -> singleLineTextField.keyPressed(keyCode, scanCode, modifiers));
        return true;
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        ALL_TIMED_MACRO_FIELDS.forEach(singleLineTextField -> singleLineTextField.charTyped(codePoint, modifiers));
        return true;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double dyScroll) {
        if(isMouseInBounds(mouseX, mouseY, dataContainerX.getAsInt(), dataContainerY.getAsInt(), dataContainerX.getAsInt() + dataContainerWidth.getAsInt(), dataContainerY.getAsInt() + dataContainerHeight.getAsInt())){
            setScrollDistance((int)(scrollDistance - dyScroll*getScrollAmount()));
        }
        ALL_TIMED_MACRO_FIELDS.forEach(timedMacroGuiLine -> timedMacroGuiLine.mouseScrolled(mouseX, mouseY, dyScroll));
        return true;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        ALL_TIMED_MACRO_FIELDS.forEach(timedMacroGuiLine -> timedMacroGuiLine.mouseReleased(mouseX, mouseY, button));
        return true;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dx, double dy) {
        ALL_TIMED_MACRO_FIELDS.forEach(timedMacroGuiLine -> timedMacroGuiLine.mouseDragged(mouseX, mouseY, button, dx, dy));
        return true;
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        ALL_TIMED_MACRO_FIELDS.forEach(timedMacroGuiLine -> timedMacroGuiLine.keyReleased(keyCode, scanCode, modifiers));
        return true;
    }

    @Override
    public void mouseMoved(double dx, double dy) {
        ALL_TIMED_MACRO_FIELDS.forEach(timedMacroGuiLine -> timedMacroGuiLine.mouseMoved(dx, dy));
    }

    public int getContentHeight(){
        return 20 * ALL_TIMED_MACRO_FIELDS.size();
    }

    public int getMaxScroll(){
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
        ALL_TIMED_MACRO_FIELDS.forEach(timedMacroGuiLine -> {
            timedMacroGuiLine.setScrollingDistance(getScrollDistance());
        });
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    public static boolean isMouseInBounds(double mouseX, double mouseY, int x1, int y1, int x2, int y2){
        return x2 > mouseX && mouseX >= x1 && y2 > mouseY && mouseY >= y1;
    }
}