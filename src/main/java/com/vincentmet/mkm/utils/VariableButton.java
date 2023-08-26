package com.vincentmet.mkm.utils;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.vincentmet.mkm.BaseClass;
import com.vincentmet.mkm.normalmacros.MacroManager;
import com.vincentmet.mkm.normalmacros.MacroScreen;
import com.vincentmet.mkm.rendering.Color;
import com.vincentmet.mkm.rendering.GLScissorStack;
import com.vincentmet.mkm.rendering.ScrollingLabel;
import com.vincentmet.mkm.utils.MouseButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.function.Consumer;
import java.util.function.IntSupplier;

@OnlyIn(Dist.CLIENT)
public class VariableButton implements GuiEventListener {
	private IntSupplier x, y, width, height;
	private int scrollingDistance = 0;
	private ScrollingLabel text;//todo change to ScrollingLabel???
	private ButtonTexture texture;
	private Consumer<MouseButton> onClickCallback;
	
	public VariableButton(IntSupplier x, IntSupplier y, IntSupplier width, IntSupplier height, String text, ButtonTexture texture, Consumer<MouseButton> onClickCallback){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.text = new ScrollingLabel(()->(x.getAsInt() + (width.getAsInt()>>1) - (Minecraft.getInstance().font.width(text)>>1)), ()->(y.getAsInt() + (height.getAsInt()>>1) - (Minecraft.getInstance().font.lineHeight>>1)), text, ()->(width.getAsInt()), 5, 2);
		this.texture = texture;
		this.onClickCallback = onClickCallback;
	}
	
	private IntSupplier getMaxTextWidth(){
		return width;
	}

	private int getStringWidth(){
		return Minecraft.getInstance().font.width(text.getText());
	}

	public void setScrollingDistance(int scrollingDistance){
		this.scrollingDistance = scrollingDistance;
		text.setParentScrollingDistance(scrollingDistance);
	}
	
	public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		internalRender(matrixStack, mouseX, mouseY, partialTicks, texture);
	}
	
	private void internalRender(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks, ButtonTexture texture){
		GLScissorStack.push(matrixStack, x.getAsInt(), y.getAsInt() - scrollingDistance, width.getAsInt(), height.getAsInt());
		Color.color(0xFFFFFFFF);
		Lighting.setupForFlatItems();

		int texU = texture.getU();
		int texV = texture.getV();
		int texWidth = texture.getTexWidth();
		int texHeight = texture.getTexHeight();
		int texP = texture.getBorderSize(); // P for Padding

		RenderSystem.setShaderTexture(0, texture.getTexture());
		
		int right = x.getAsInt() + width.getAsInt() - texP;
		int bottom = y.getAsInt() - scrollingDistance + height.getAsInt() - texP;
		int texRight = texU + texture.getWidth() - texP;
		int texBottom = texV + texture.getWidth() - texP;
		
		GuiComponent.blit(matrixStack, x.getAsInt(), y.getAsInt() - scrollingDistance, texU, texV, texP, texP, texWidth, texHeight);// Left Top corner
		GuiComponent.blit(matrixStack, right, y.getAsInt() - scrollingDistance, texRight, texV, texP, texP, texWidth, texHeight);// Right Top corner
		GuiComponent.blit(matrixStack, right, bottom, texRight, texBottom, texP, texP, texWidth, texHeight);// Right Bottom corner
		GuiComponent.blit(matrixStack, x.getAsInt(), bottom, texU, texBottom, texP, texP, texWidth, texHeight);// Left Bottom corner
		
		
		int strippedButtonTexWidth = texture.getWidth() - 2*texP;
		int strippedButtonTexHeight = texture.getHeight() - 2*texP;
		
		for (int left = x.getAsInt() + texP; left < right; left += strippedButtonTexWidth) {// Fill the Middle
			for (int top = y.getAsInt() - scrollingDistance + texP; top < bottom; top += strippedButtonTexHeight) {
				GuiComponent.blit(matrixStack, left, top, texU + texP, texV + texP, Math.min(strippedButtonTexWidth, right - left), Math.min(strippedButtonTexHeight, bottom - top), texWidth, texHeight);
			}
		}
		for (int left = x.getAsInt() + texP; left < right; left += strippedButtonTexWidth) {// Top and Bottom Edges
			GuiComponent.blit(matrixStack, left, y.getAsInt() - scrollingDistance, texU + texP, texV, Math.min(strippedButtonTexWidth, right - left), texP, texWidth, texHeight);// Top
			GuiComponent.blit(matrixStack, left, bottom, texU + texP, texBottom, Math.min(strippedButtonTexWidth, right - left), texP, texWidth, texHeight);// Bottom
		}
		for (int top = y.getAsInt() - scrollingDistance + texP; top < bottom; top += strippedButtonTexHeight) {// Left and Right Edges
			GuiComponent.blit(matrixStack, x.getAsInt(), top, texU, texV + texP, texP, Math.min(strippedButtonTexHeight, bottom - top), texWidth, texHeight);// Left
			GuiComponent.blit(matrixStack, right, top, texRight, texV + texP, texP, Math.min(strippedButtonTexHeight, bottom - top), texWidth, texHeight);// Right
		}
		text.render(matrixStack, mouseX, mouseY, partialTicks);
		if(MacroScreen.isMouseInBounds(mouseX, mouseY, x.getAsInt(), y.getAsInt() - scrollingDistance, x.getAsInt()+width.getAsInt(), y.getAsInt() - scrollingDistance + height.getAsInt()) && texture == ButtonTexture.DEFAULT_NORMAL){
			if(MacroScreen.isMouseInBounds(mouseX, mouseY, GLScissorStack.getIntersectionArea().getX(), GLScissorStack.getIntersectionArea().getY(), GLScissorStack.getIntersectionArea().getX() + GLScissorStack.getIntersectionArea().getWidth(), GLScissorStack.getIntersectionArea().getY() + GLScissorStack.getIntersectionArea().getHeight())){
				internalRender(matrixStack, mouseX, mouseY, partialTicks, ButtonTexture.DEFAULT_PRESSED);
			}
		}
		GLScissorStack.pop(matrixStack);
	}
	
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton){
		if(MacroScreen.isMouseInBounds(mouseX, mouseY, x.getAsInt(), y.getAsInt() - scrollingDistance, x.getAsInt()+width.getAsInt(), y.getAsInt()-scrollingDistance+height.getAsInt())){
			if(onClickCallback != null){
				onClickCallback.accept(MouseButton.getButtonFromGlButton(mouseButton));
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isMouseOver(double p_94748_, double p_94749_) {

		return true;
	}

	@Override
	public void setFocused(boolean p_265728_) {

	}

	@Override
	public boolean isFocused() {
		return false;
	}

	public IntSupplier getX(){
		return x;
	}
	
	public IntSupplier getY(){
		return y;
	}
	
	public IntSupplier getWidth(){
		return width;
	}
	
	public IntSupplier getHeight(){
		return height;
	}
	
	public ButtonTexture getTexture(){
		return texture;
	}
	
	public ScrollingLabel getButtonText(){
		return text;
	}
	
	public Consumer<MouseButton> getOnClickCallback(){
		return onClickCallback;
	}
	
	public void setX(IntSupplier x){
		this.x = x;
	}
	
	public void setY(IntSupplier y){
		this.y = y;
	}
	
	public void setWidth(IntSupplier width){
		this.width = width;
	}
	
	public void setHeight(IntSupplier height){
		this.height = height;
	}
	
	public void setTexture(ButtonTexture texture){
		this.texture = texture;
	}
	
	public void setOnClickCallback(Consumer<MouseButton> onClickCallback){
		this.onClickCallback = onClickCallback;
	}
	
	public static class ButtonTexture {
		public static final ButtonTexture DEFAULT_NORMAL = new ButtonTexture(0, 0, 24, 24, 72, 72, 2, new ResourceLocation(BaseClass.MODID, "textures/gui/button_square.png"));
		public static final ButtonTexture DEFAULT_PRESSED = new ButtonTexture(24, 0, 24, 24, 72, 72, 2, new ResourceLocation(BaseClass.MODID, "textures/gui/button_square.png"));
		public static final ButtonTexture DEFAULT_DISABLED = new ButtonTexture(48, 0, 24, 24, 72, 72, 2, new ResourceLocation(BaseClass.MODID, "textures/gui/button_square.png"));
		public static final ButtonTexture DEFAULT_BLUE = new ButtonTexture(0, 24, 24, 24, 72, 72, 2, new ResourceLocation(BaseClass.MODID, "textures/gui/button_square.png"));
		public static final ButtonTexture DEFAULT_GREEN = new ButtonTexture(24, 24, 24, 24, 72, 72, 2, new ResourceLocation(BaseClass.MODID, "textures/gui/button_square.png"));
		private final ResourceLocation texture;
		private final int u;
		private final int v;
		private final int width;
		private final int height;
		private final int texWidth;
		private final int texHeight;
		private final int borderSize;
		
		public ButtonTexture(int u, int v, int width, int height, int texWidth, int texHeight, int borderSize, ResourceLocation texture) {
			this.texture = texture;
			this.u = u;
			this.v = v;
			this.width = width;
			this.height = height;
			this.texWidth = texWidth;
			this.texHeight = texHeight;
			this.borderSize = borderSize;
		}
		
		public ResourceLocation getTexture(){
			return texture;
		}
		
		public int getU() {
			return this.u;
		}
		
		public int getV() {
			return this.v;
		}
		
		public int getWidth(){
			return width;
		}
		
		public int getHeight(){
			return height;
		}
		
		public int getTexWidth() {
			return this.texWidth;
		}
		
		public int getTexHeight() {
			return this.texHeight;
		}
		
		public int getBorderSize() {
			return this.borderSize;
		}
	}
}