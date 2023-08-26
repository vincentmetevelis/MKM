package com.vincentmet.mkm.utils;

import com.mojang.blaze3d.vertex.PoseStack;
import com.vincentmet.mkm.normalmacros.MacroScreen;
import com.vincentmet.mkm.rendering.GLScissorStack;
import com.vincentmet.mkm.utils.Selection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.events.GuiEventListener;
import org.lwjgl.glfw.GLFW;

import java.util.OptionalInt;
import java.util.function.IntSupplier;
import java.util.regex.Pattern;

public class SingleLineTextField implements GuiEventListener {
    private static final int SHIFT_DOWN = 0b0001 , CTRL_DOWN = 0b0010, ALT_DOWN = 0b0100, WINKEY_DOWN = 0b1000;
    private IntSupplier x, y, width, height;
    private int scrollingDistance = 0;
    private final int backgroundColor, borderColor, cursorColor, textColor, selectionColor;
    private final Selection selection;
    
    //Textarea including border
    private final IntSupplier TEXTBOX_X = () -> x.getAsInt();
    private final IntSupplier TEXTBOX_Y = () -> y.getAsInt();
    private final IntSupplier TEXTBOX_WIDTH = () -> width.getAsInt();
    private final IntSupplier TEXTBOX_HEIGHT = () -> height.getAsInt();
    
    //Textarea excluding border
    private final IntSupplier INSIDE_BOX_X = () -> x.getAsInt()+1;
    private final IntSupplier INSIDE_BOX_Y = () -> y.getAsInt()+1;
    private final IntSupplier INSIDE_BOX_WIDTH = () -> width.getAsInt()-2;
    private final IntSupplier INSIDE_BOX_HEIGHT = () -> height.getAsInt()-2;
    
    //Textarea text only
    private final IntSupplier TEXTAREA_X = () -> INSIDE_BOX_X.getAsInt() + 1;
    private final IntSupplier TEXTAREA_Y = () -> INSIDE_BOX_Y.getAsInt() + (INSIDE_BOX_HEIGHT.getAsInt()>>1) - (Minecraft.getInstance().font.lineHeight>>1);
    private final IntSupplier TEXTAREA_WIDTH = () -> INSIDE_BOX_WIDTH.getAsInt() - 2;
    private final IntSupplier TEXTAREA_HEIGHT = () -> Minecraft.getInstance().font.lineHeight;
    
    
    private String text;
    private int offset = 0;
    private int cursorPos;
    private boolean isFocused = false;

    private OptionalInt cursorPosAtMouseClickDown = OptionalInt.empty();
    
    public SingleLineTextField(IntSupplier x, IntSupplier y, IntSupplier width, IntSupplier height, int backgroundColor, int borderColor, int cursorColor, int textColor, int selectionColor, String initialText){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.backgroundColor = backgroundColor;
        this.borderColor = borderColor;
        this.cursorColor = cursorColor;
        this.textColor = textColor;
        this.selectionColor = selectionColor;
        this.text = initialText;
        this.selection = new Selection(this.text.length());
        this.setToMaxCursorPos();
    }

    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks){
        GuiComponent.fill(matrixStack, TEXTBOX_X.getAsInt(), TEXTBOX_Y.getAsInt() - scrollingDistance, TEXTBOX_X.getAsInt() + TEXTBOX_WIDTH.getAsInt(), TEXTBOX_Y.getAsInt() - scrollingDistance + TEXTBOX_HEIGHT.getAsInt(), borderColor);
        GuiComponent.fill(matrixStack, INSIDE_BOX_X.getAsInt(), INSIDE_BOX_Y.getAsInt() - scrollingDistance, INSIDE_BOX_X.getAsInt() + INSIDE_BOX_WIDTH.getAsInt(), INSIDE_BOX_Y.getAsInt() - scrollingDistance + INSIDE_BOX_HEIGHT.getAsInt(), backgroundColor);
        renderSelection(matrixStack, mouseX, mouseY, partialTicks);
        GLScissorStack.push(matrixStack, TEXTAREA_X.getAsInt(), TEXTAREA_Y.getAsInt() - scrollingDistance, TEXTAREA_WIDTH.getAsInt(), TEXTAREA_HEIGHT.getAsInt());
        Minecraft.getInstance().font.draw(matrixStack, text, TEXTAREA_X.getAsInt() - getOffset(), TEXTAREA_Y.getAsInt()-scrollingDistance, textColor);
        GLScissorStack.pop(matrixStack);
        renderCursor(matrixStack, mouseX, mouseY, partialTicks);
    }

    private void renderSelection(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks){
        selection.setMaxLength(text.length());
        if (selection.isAnythingSelected()){
            int glScissorBoxX = TEXTAREA_X.getAsInt();
            int glScissorBoxY = TEXTAREA_Y.getAsInt() - scrollingDistance - 1;
            int glScissorBoxWidth = TEXTAREA_X.getAsInt() + TEXTAREA_WIDTH.getAsInt();
            int glScissorBoxHeight = TEXTAREA_Y.getAsInt() - scrollingDistance + TEXTAREA_HEIGHT.getAsInt() + 1;
            GLScissorStack.push(matrixStack, glScissorBoxX, glScissorBoxY, glScissorBoxWidth, glScissorBoxHeight);
            int selectionBoxX = TEXTAREA_X.getAsInt() - getOffset() + getWidthForNChars(selection.getPos1());
            int selectionBoxY = TEXTAREA_Y.getAsInt() - scrollingDistance - 1;
            int selectionBoxWidth = TEXTAREA_X.getAsInt() - getOffset() + getWidthForNChars(selection.getPos2());
            int selectionBoxHeight = TEXTAREA_Y.getAsInt() - scrollingDistance + TEXTAREA_HEIGHT.getAsInt() + 1;
            GuiComponent.fill(matrixStack, selectionBoxX, selectionBoxY, selectionBoxWidth, selectionBoxHeight, selectionColor);
            GLScissorStack.pop(matrixStack);
        }
    }
    
    private void renderCursor(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks){
        if(isFocused && System.currentTimeMillis()%1000>=500){
            GuiComponent.fill(matrixStack, TEXTAREA_X.getAsInt() + getCursorPosInPx() - getOffset(), TEXTAREA_Y.getAsInt() - scrollingDistance - 1, TEXTAREA_X.getAsInt() + getCursorPosInPx() - getOffset() + 1, TEXTAREA_Y.getAsInt() - scrollingDistance + TEXTAREA_HEIGHT.getAsInt() + 1, cursorColor);
        }
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers){
        if(isFocused){
            switch(keyCode) {
                case GLFW.GLFW_KEY_LEFT:
                    if ((modifiers & CTRL_DOWN) == CTRL_DOWN) {
                        if ((modifiers & SHIFT_DOWN) == SHIFT_DOWN) {
                            if (selection.getPos1() == selection.getPos2() && getCursorPos() == selection.getPos1()){
                                selection.setPos1(getPrevWordEndingPos(getCursorPos()));
                            }else if (getCursorPos() == selection.getPos2()){
                                selection.setPos2(getPrevWordEndingPos(selection.getPos2()));
                            }else{
                                selection.setPos1(getPrevWordEndingPos(getCursorPos()));
                            }
                            setCursorPos(getPrevWordEndingPos(getCursorPos()));
                        }else{
                            setCursorPos(getPrevWordEndingPos(getCursorPos()));
                            selection.setBothPos(getCursorPos());
                        }
                    }else{
                        if ((modifiers & SHIFT_DOWN) == SHIFT_DOWN) {
                            if (selection.getPos1() == selection.getPos2() && getCursorPos() == selection.getPos1()){
                                selection.decreasePos1();
                            }else if (getCursorPos() == selection.getPos2()){
                                selection.decreasePos2();
                            }else{
                                selection.decreasePos1();
                            }
                            setCursorOneToLeft();
                        }else{
                            setCursorOneToLeft();
                            selection.setBothPos(getCursorPos());
                        }
                    }
                    break;
                case GLFW.GLFW_KEY_RIGHT:
                    if ((modifiers & CTRL_DOWN) == CTRL_DOWN){
                        if ((modifiers & SHIFT_DOWN) == SHIFT_DOWN) {
                            if (selection.getPos1() == selection.getPos2() && getCursorPos() == selection.getPos2()){
                                selection.setPos2(getNextWordStartingPos(getCursorPos()));
                            }else if (getCursorPos() == selection.getPos1()){
                                selection.setPos1(getNextWordStartingPos(selection.getPos1()));
                            }else{
                                selection.setPos2(getNextWordStartingPos(getCursorPos()));
                            }
                            setCursorPos(getNextWordStartingPos(getCursorPos()));
                        }else{
                            setCursorPos(getNextWordStartingPos(getCursorPos()));
                            selection.setBothPos(getCursorPos());
                        }
                    }else{
                        if ((modifiers & SHIFT_DOWN) == SHIFT_DOWN) {
                            if (selection.getPos1() == selection.getPos2() && getCursorPos() == selection.getPos2()){
                                selection.increasePos2();
                            }else if (getCursorPos() == selection.getPos1()){
                                selection.increasePos1();
                            }else{
                                selection.increasePos2();
                            }
                            setCursorOneToRight();
                        }else{
                            setCursorOneToRight();
                            selection.setBothPos(getCursorPos());
                        }
                    }
                    break;
                case GLFW.GLFW_KEY_HOME:
                case GLFW.GLFW_KEY_PAGE_UP:
                    setCursorToOrigin();
                    selection.reset();
                    break;
                case GLFW.GLFW_KEY_END:
                case GLFW.GLFW_KEY_PAGE_DOWN:
                    setToMaxCursorPos();
                    selection.setBothPos(getMaxCursorPos());
                    break;
                case GLFW.GLFW_KEY_BACKSPACE:
                    if (selection.isAnythingSelected()){
                        deleteSelection();
                    }else{
                        if((modifiers & CTRL_DOWN) == CTRL_DOWN){
                            StringBuilder sb = new StringBuilder();
                            sb.append(text.substring(0, getPrevWordEndingPos(getCursorPos())));
                            sb.append(text.substring(getCursorPos()));
                            text = sb.toString();
                        }else{
                            removeCharBeforeCursor();
                        }
                    }
                    break;
                case GLFW.GLFW_KEY_DELETE:
                    if (selection.isAnythingSelected()){
                        deleteSelection();
                    }else{
                        if((modifiers & CTRL_DOWN) == CTRL_DOWN){
                            StringBuilder sb = new StringBuilder();
                            sb.append(text.substring(0, getCursorPos()));
                            sb.append(text.substring(getNextWordStartingPos(getCursorPos())));
                            text = sb.toString();
                        }else{
                            removeCharAfterCursor();
                        }
                    }
                    break;
                case GLFW.GLFW_KEY_V:
                    if((modifiers & CTRL_DOWN) == CTRL_DOWN){
                        deleteSelection();
                        String currentText = getText();
                        String clipboardText = Minecraft.getInstance().keyboardHandler.getClipboard();
                        String sb = currentText.substring(0, getCursorPos()) + clipboardText + currentText.substring(getCursorPos(), getMaxCursorPos());
                        setText(sb);
                        setCursorToRight(clipboardText.length());
                    }
                    break;
                case  GLFW.GLFW_KEY_A:
                    if((modifiers & CTRL_DOWN) == CTRL_DOWN){
                        selection.setPos1(0);
                        selection.setPos2(text.length());
                        setToMaxCursorPos();
                    }
                    break;
                case  GLFW.GLFW_KEY_C:
                    if((modifiers & CTRL_DOWN) == CTRL_DOWN){
                        Minecraft.getInstance().keyboardHandler.setClipboard(getTextInSelection());
                    }
                    break;
                case  GLFW.GLFW_KEY_X:
                    if((modifiers & CTRL_DOWN) == CTRL_DOWN){
                        Minecraft.getInstance().keyboardHandler.setClipboard(getTextInSelection());
                        deleteSelection();
                    }
                    break;
            }
        }
        return true;
    }

    public boolean charTyped(char codePoint, int modifiers){
        if(isFocused){
            if(selection.isAnythingSelected()){
                deleteSelection();
            }
            StringBuilder newString = new StringBuilder(text);
            newString.insert(getCursorPos(), codePoint);
            text = newString.toString();
            setCursorOneToRight();
        }
        return true;
    }

    @Override
    public void setFocused(boolean p_265728_) {

    }

    @Override
    public boolean isFocused() {
        return false;
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button){
        if(MacroScreen.isMouseInBounds(mouseX, mouseY, TEXTAREA_X.getAsInt(), INSIDE_BOX_Y.getAsInt() - scrollingDistance, TEXTAREA_X.getAsInt() + TEXTAREA_WIDTH.getAsInt(), INSIDE_BOX_Y.getAsInt() - scrollingDistance + INSIDE_BOX_HEIGHT.getAsInt())){
            isFocused = true;
            int localMouseX = (int)mouseX - TEXTAREA_X.getAsInt();
            setCursorPos(getCharPosFromX(localMouseX + getOffset()));
            cursorPosAtMouseClickDown = OptionalInt.of(cursorPos);
        }else{
            isFocused = false;
        }
        return true;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if(MacroScreen.isMouseInBounds(mouseX, mouseY, TEXTAREA_X.getAsInt(), INSIDE_BOX_Y.getAsInt() - scrollingDistance, TEXTAREA_X.getAsInt() + TEXTAREA_WIDTH.getAsInt(), INSIDE_BOX_Y.getAsInt() - scrollingDistance + INSIDE_BOX_HEIGHT.getAsInt())){
            int localMouseX = (int)mouseX - TEXTAREA_X.getAsInt();
            setCursorPos(getCharPosFromX(localMouseX + getOffset()));
            if (cursorPosAtMouseClickDown.isPresent()){
                selection.setBothPos(cursorPosAtMouseClickDown.getAsInt(), cursorPos);
            }
        }
        return true;
    }

    public void setScrollingDistance(int scrollingDistance) {
        this.scrollingDistance = scrollingDistance;
    }

    public void setText(String newText){
        text = newText;
    }

    public void deleteSelection(){
        if (selection.isAnythingSelected()){
            setText(text.substring(0, selection.getPos1()) + text.substring(selection.getPos2()));
            selection.setBothPos(selection.getPos1());
        }
    }

    public int getNextWordStartingPos(int startingPos){
        int currentCheckingPos = startingPos;
        char[] textCharArray = text.toCharArray();
        boolean foundSeparator = false;
        while(!foundSeparator && currentCheckingPos >= 0 && currentCheckingPos < getMaxCursorPos()){
            if(Pattern.compile("[^a-zA-Z_0-9]").matcher(String.valueOf(textCharArray[currentCheckingPos])).matches()){
                foundSeparator = true;
            }
            currentCheckingPos++;
        }
        return currentCheckingPos;
    }

    public int getPrevWordEndingPos(int startingPos){
        int currentCheckingPos = startingPos;
        char[] textCharArray = text.toCharArray();
        boolean foundSeparator = false;
        while(!foundSeparator && currentCheckingPos > 0 && currentCheckingPos <= getMaxCursorPos()){
            currentCheckingPos--;
            if(Pattern.compile("[^a-zA-Z_0-9]").matcher(String.valueOf(textCharArray[currentCheckingPos])).matches()){
                foundSeparator = true;
            }
        }
        return currentCheckingPos;
    }

    public String getTextInSelection(){
        return this.text.substring(selection.getPos1(), selection.getPos2());
    }
    
    private void removeCharBeforeCursor(){
        if(getCursorPos() > 0){
            StringBuilder currentText = new StringBuilder(text);
            currentText.deleteCharAt(getCursorPos()-1);
            text = currentText.toString();
            if(getCursorPos() < getMaxCursorPos())setCursorOneToLeft();
        }
    }
    
    private void removeCharAfterCursor(){
        if(getCursorPos() < getMaxCursorPos()){
            StringBuilder currentText = new StringBuilder(text);
            currentText.deleteCharAt(getCursorPos());
            text = currentText.toString();
        }
    }
    
    private int getCharPosFromX(int x){
        if(x > getMaxCursorPosInPx()){
            return getMaxCursorPos();
        }
        for(int lastPos = 0; lastPos < text.length(); lastPos++){
            int strWidth = getStringWidth(text.substring(0, lastPos));
            if(x < strWidth){
                return lastPos;
            }
        }
        return 0;
    }
    
    private void setOffsetToOrigin(){
        setOffset(0);
    }
    
    private int getOffset(){
        if(offset < 0) setOffsetToOrigin();
        if(offset > getMaxOffset()) setOffset(getMaxOffset());
        return offset;
    }
    
    private void setOffset(int x){
        offset = x;
    }
    
    private int getMaxOffset(){
        return -Math.min(0, TEXTAREA_WIDTH.getAsInt() - getTextWidth());
    }
    
    private int getCursorPos(){
        if(cursorPos<0) setCursorToOrigin();
        if(cursorPos>getMaxCursorPos()) setCursorPos(getMaxCursorPos());
        return cursorPos;
    }
    
    private int getCursorPosInPx(){
        return getWidthForNChars(getCursorPos());
    }
    
    private void setCursorOneToLeft(){
        setCursorPos(getCursorPos()-1);
    }

    private void setCursorToLeft(int amount){
        setCursorPos(getCursorPos()-amount);
    }

    private void setCursorToRight(int amount){
        setCursorPos(getCursorPos()+amount);
    }
    
    private void setCursorOneToRight(){
        setCursorPos(getCursorPos()+1);
    }
    
    private void setOffsetSoCursorIsVisible(){
        if(getCursorPosInPx() - TEXTAREA_WIDTH.getAsInt() > getOffset()){
            setOffset(getCursorPosInPx() - TEXTAREA_WIDTH.getAsInt());
        }
        if(getCursorPosInPx() - getOffset() < 0){
            setOffset(getCursorPosInPx());
        }
    }
    
    private void setCursorPos(int x){
        if(x<0){
            setCursorToOrigin();
        }else if(x>getMaxCursorPos()){
            setCursorPos(getMaxCursorPos());
        }else{
            this.cursorPos = x;
        }
        setOffsetSoCursorIsVisible();
    }
    
    private int getMaxCursorPos(){
        return text.length();
    }
    
    private int getMaxCursorPosInPx(){
        return getStringWidth(text);
    }
    
    private void setToMaxCursorPos(){
        setCursorPos(getMaxCursorPos());
    }
    
    private void setCursorToOrigin(){
        setCursorPos(0);
    }
    
    private int getTextWidth(){
        return getStringWidth(text);
    }
    
    //Like the other method below, but uses the current text
    private int getWidthForNChars(int n){
        return getStringWidth(text.substring(0, n));
    }
    
    //Basically a parameterized substring (i.e. when n = 6, it gets the string width of the first 6 chars in the string)
    private int getWidthForNChars(String text, int n){
        return getStringWidth(text.substring(0, n));
    }

    private int getWidthForNChars(String text, int startingPos, int n){
        return getStringWidth(text.substring(startingPos, startingPos + n));
    }

    private int getWidthForNChars(int startingPos, int n){
        return getStringWidth(text.substring(startingPos, startingPos + n));
    }

    //Just a wrapper for the Mojang their method
    private int getStringWidth(String text){
        return Minecraft.getInstance().font.width(text);
    }
    
    public String getText(){
        return text;
    }

    public void setX(IntSupplier x) {
        this.x = x;
    }

    public void setY(IntSupplier y) {
        this.y = y;
    }

    public void setWidth(IntSupplier width) {
        this.width = width;
    }

    public void setHeight(IntSupplier height) {
        this.height = height;
    }
}