package com.vincentmet.mkm;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;
import java.util.function.IntSupplier;

public class MacroScreen extends Screen {
    private int scrollDistance = 0;

    private final IntSupplier dataContainerX = ()->20;
    private final IntSupplier dataContainerY = ()->20;
    private final IntSupplier dataContainerWidth = ()->width-40;
    private final IntSupplier dataContainerHeight = ()->height-40;

    public MacroScreen() {
        super(new TextComponent("MKM"));
        for (MacroKeybindWrapper keybind : Keybinds.getAllMacros()){
            keybind.getCommandTextLine().setText(keybind.getConfigValue());
        }
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        fill(stack, 0, 0, width, height, 0x88000000);
        fill(stack, dataContainerX.getAsInt(), dataContainerY.getAsInt(), dataContainerX.getAsInt() + dataContainerWidth.getAsInt(), dataContainerY.getAsInt() + dataContainerHeight.getAsInt(), 0x88000000);

        GLScissorStack.push(stack, dataContainerX.getAsInt(), dataContainerY.getAsInt(), dataContainerWidth.getAsInt(), dataContainerHeight.getAsInt());
        int i = 0;
        for(MacroKeybindWrapper keybind : Keybinds.getAllMacros()){
            int localY = dataContainerY.getAsInt() - scrollDistance + 20*i;
            keybind.getCommandTextLine().setX(width>>1);
            keybind.getCommandTextLine().setY(localY);
            keybind.getCommandTextLine().setWidth((dataContainerWidth.getAsInt()>>1)-20);
            keybind.getCommandTextLine().setHeight(20);
            keybind.getSaveButton().setX(dataContainerX.getAsInt() + dataContainerWidth.getAsInt() - 20);
            keybind.getSaveButton().setY(localY);
            drawString(stack, font, keybind.getTranslation(), dataContainerX.getAsInt() + 20, localY + (20/2 - font.lineHeight/2), 0xFFFFFF);
            keybind.getCommandTextLine().render(stack, mouseX, mouseY, partialTicks);
            keybind.getSaveButton().render(stack, mouseX, mouseY, partialTicks);
            i++;
        }
        GLScissorStack.pop(stack);
    }
    
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for(MacroKeybindWrapper keybind : Keybinds.getAllMacros()){
            keybind.getCommandTextLine().mouseClicked(mouseX, mouseY, button);
            keybind.getSaveButton().mouseClicked(mouseX, mouseY, button);
        }
        return true;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 256 && this.shouldCloseOnEsc()) {
            this.onClose();
        }
        for(MacroKeybindWrapper keybind : Keybinds.getAllMacros()){
            keybind.getCommandTextLine().keyPressed(keyCode, scanCode, modifiers);
        }
        return true;
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        for(MacroKeybindWrapper keybind : Keybinds.getAllMacros()){
            keybind.getCommandTextLine().charTyped(codePoint, modifiers);
        }
        return true;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double dyScroll) {
        if(isMouseInBounds(mouseX, mouseY, dataContainerX.getAsInt(), dataContainerY.getAsInt(), dataContainerX.getAsInt() + dataContainerWidth.getAsInt(), dataContainerY.getAsInt() + dataContainerHeight.getAsInt())){
            scrollDistance -= dyScroll*getScrollAmount();
        }
        applyScrollLimits();
        return true;
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