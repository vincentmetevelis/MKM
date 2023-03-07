package com.vincentmet.mkm.timedmacros;

import com.mojang.blaze3d.vertex.PoseStack;
import com.vincentmet.mkm.rendering.IRenderable;
import com.vincentmet.mkm.rendering.ScrollingLabel;
import com.vincentmet.mkm.utils.BooleanContainer;
import com.vincentmet.mkm.utils.SingleLineTextField;
import com.vincentmet.mkm.utils.VariableButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.glfw.GLFW;

import java.util.function.IntSupplier;

public class TimedMacroGuiLine implements IRenderable, GuiEventListener {//todo make sure that this scrolls as well when main page is scrolled
    private final TimedMacro macro;
    private final ScrollingLabel label;
    private final SingleLineTextField macroTextField;
    private final SingleLineTextField timingTextField;
    private final VariableButton removeButton;

    public TimedMacroGuiLine(int tempId, IntSupplier xSupplier, IntSupplier ySupplier, IntSupplier widthSupplier, IntSupplier heightSupplier, TimedMacro macro){//label 25%, macro text field 50%, duration 25%-20px, remove 20px
        this.macro = macro;
        this.label = new ScrollingLabel(()->xSupplier.getAsInt()+2, ()->ySupplier.getAsInt() + (heightSupplier.getAsInt()>>1) - (Minecraft.getInstance().font.lineHeight>>1), macro.getName(), ()->(widthSupplier.getAsInt()>>2)-4, 5, 1);
        this.macroTextField = new SingleLineTextField(()->xSupplier.getAsInt() + (widthSupplier.getAsInt()>>2), ySupplier, ()->widthSupplier.getAsInt()>>1, heightSupplier, 0xFF000000, 0xFFAAAAAA, 0xFFFFFFFF, 0xFFFFFFFF, 0xFFEE00EE, macro.getMacro());
        this.timingTextField = new SingleLineTextField(()->xSupplier.getAsInt() + (widthSupplier.getAsInt()/4*3), ySupplier, ()->(widthSupplier.getAsInt()>>2)-20, heightSupplier, 0xFF000000, 0xFFAAAAAA, 0xFFFFFFFF, 0xFFFFFFFF, 0xFFEE00EE, String.valueOf(macro.getTimingInTicks()));
        this.removeButton = new VariableButton(()->xSupplier.getAsInt() + widthSupplier.getAsInt() - 20, ySupplier, ()->20, heightSupplier, "-", VariableButton.ButtonTexture.DEFAULT_NORMAL, mouseButton -> {
            System.out.println("temp id: " + tempId);
            TimedMacroManager.markIfForDeletion(tempId);
            MinecraftForge.EVENT_BUS.post(new RemoveTimedMacroEvent(tempId));
        });
    }

    public void save(){
        macro.setMacro(macroTextField.getText());
        macro.setTimingInTicks(Integer.parseInt(timingTextField.getText()));
    }

    public void setScrollingDistance(int scrollingDistance){
        label.setParentScrollingDistance(scrollingDistance);
        macroTextField.setScrollingDistance(scrollingDistance);
        timingTextField.setScrollingDistance(scrollingDistance);
        removeButton.setScrollingDistance(scrollingDistance);
    }

    public ScrollingLabel getLabel() {
        return label;
    }

    public SingleLineTextField getTextField() {
        return macroTextField;
    }

    public SingleLineTextField getTimingTextField() {
        return timingTextField;
    }

    public VariableButton getRemoveButton() {
        return removeButton;
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        label.render(stack, mouseX, mouseY, partialTicks);
        macroTextField.render(stack, mouseX, mouseY, partialTicks);
        timingTextField.render(stack, mouseX, mouseY, partialTicks);
        removeButton.render(stack, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        macroTextField.mouseClicked(mouseX, mouseY, button);
        timingTextField.mouseClicked(mouseX, mouseY, button);
        removeButton.mouseClicked(mouseX, mouseY, button);
        return true;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        macroTextField.keyPressed(keyCode, scanCode, modifiers);
        timingTextField.keyPressed(keyCode, scanCode, modifiers);
        return true;
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        macroTextField.charTyped(codePoint, modifiers);
        timingTextField.charTyped(codePoint, modifiers);
        return true;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double dyScroll) {
        macroTextField.mouseScrolled(mouseX, mouseY, dyScroll);
        timingTextField.mouseScrolled(mouseX, mouseY, dyScroll);
        return true;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        macroTextField.mouseReleased(mouseX, mouseY, button);
        timingTextField.mouseReleased(mouseX, mouseY, button);
        return true;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dx, double dy) {
        macroTextField.mouseDragged(mouseX, mouseY, button, dx, dy);
        timingTextField.mouseDragged(mouseX, mouseY, button, dx, dy);
        return true;
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        macroTextField.keyReleased(keyCode, scanCode, modifiers);
        timingTextField.keyReleased(keyCode, scanCode, modifiers);
        return true;
    }

    @Override
    public void mouseMoved(double dx, double dy) {
        macroTextField.mouseMoved(dx, dy);
        timingTextField.mouseMoved(dx, dy);
    }
}