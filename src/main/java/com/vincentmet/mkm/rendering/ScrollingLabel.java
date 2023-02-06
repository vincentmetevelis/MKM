package com.vincentmet.mkm.rendering;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;

import java.util.function.IntSupplier;

public class ScrollingLabel implements IRenderable{
    private static final Font FONT = Minecraft.getInstance().font;
    
    private IntSupplier x, y;
    private String text;
    private final IntSupplier maxWidth;
    private int beginEndPauseDuration, scrollingSpeed, textWidth, maxOffset;
    
    public ScrollingLabel(IntSupplier x, IntSupplier y, String text, IntSupplier maxWidth, int beginEndPauseDuration, int scrollingSpeed){//beginEndPauseDuration in ticks // scrollingSpeed calculated as: 1/x
        this.x = x;
        this.y = y;
        this.text = text;
        this.maxWidth = maxWidth;
        this.beginEndPauseDuration = beginEndPauseDuration;
        this.scrollingSpeed = scrollingSpeed;
    
        this.textWidth = FONT.width(text);
        this.maxOffset = this.textWidth - this.maxWidth.getAsInt();
    }
    
    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks){
        GLScissorStack.push(matrixStack, x.getAsInt(), y.getAsInt(), maxWidth.getAsInt(), FONT.lineHeight);
        if(this.maxOffset >= 0){
            int currentOffset = Math.min((int)((System.currentTimeMillis()/50/scrollingSpeed)%(textWidth+beginEndPauseDuration*2)), maxOffset + 2*this.beginEndPauseDuration);
            int localOffsetPause;
            if(currentOffset < beginEndPauseDuration){
                localOffsetPause = 0;
            }else{
                localOffsetPause = Math.min(currentOffset - beginEndPauseDuration, maxOffset);
            }
            FONT.drawShadow(matrixStack, text, x.getAsInt()-localOffsetPause, y.getAsInt(), 0xFFFFFF);//stack, text, x, y, color
        }else{
            FONT.drawShadow(matrixStack, text, x.getAsInt(), y.getAsInt(), 0xFFFFFF);//stack, text, x, y, color
        }
        GLScissorStack.pop(matrixStack);
    }
    
    public IntSupplier getX(){
        return x;
    }
    
    public IntSupplier getY(){
        return y;
    }
    
    public void setX(IntSupplier x){
        this.x = x;
    }
    
    public void setY(IntSupplier y){
        this.y = y;
    }
    
    public String getText(){
        return text;
    }

    public void setText(String text){
        this.text = text;
        this.textWidth = FONT.width(text);
        this.maxOffset = this.textWidth - this.maxWidth.getAsInt();
    }
    
    public IntSupplier getWidth(){
        return maxWidth;
    }
    
    public int getBeginEndPauseDuration(){
        return beginEndPauseDuration;
    }

    public void setBeginEndPauseDuration(int beginEndPauseDuration) {
        this.beginEndPauseDuration = beginEndPauseDuration;
    }

    public int getScrollingSpeed(){
        return scrollingSpeed;
    }

    public void setScrollingSpeed(int scrollingSpeed) {
        this.scrollingSpeed = scrollingSpeed;
    }
}
