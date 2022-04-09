package com.vincentmet.mkm;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import java.util.function.Consumer;
import java.util.function.IntSupplier;

@OnlyIn(Dist.CLIENT)
public class VariableButton {
	private int x, y, width, height;
	private ButtonTexture texture;
	private Consumer<MouseButton> onClickCallback;
	
	public VariableButton(int x, int y, int width, int height, ButtonTexture texture, Consumer<MouseButton> onClickCallback){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.texture = texture;
		this.onClickCallback = onClickCallback;
	}
	
	private IntSupplier getMaxTextWidth(){
		return ()->width;
	}
	
	private int getStringWidth(){
		return Minecraft.getInstance().font.width(getButtonText());
	}
	
	public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		internalRender(matrixStack, mouseX, mouseY, partialTicks, texture);
	}
	
	private void internalRender(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks, ButtonTexture texture){
		Color.color(0xFFFFFFFF);
		Lighting.setupForFlatItems();

		int texU = texture.getU();
		int texV = texture.getV();
		int texWidth = texture.getTexWidth();
		int texHeight = texture.getTexHeight();
		int texP = texture.getBorderSize(); // P for Padding

		RenderSystem.setShaderTexture(0, texture.getTexture());
		
		int right = x + width - texP;
		int bottom = y + height - texP;
		int texRight = texU + texture.getWidth() - texP;
		int texBottom = texV + texture.getWidth() - texP;
		
		GuiComponent.blit(matrixStack, x, y, texU, texV, texP, texP, texWidth, texHeight);// Left Top corner
		GuiComponent.blit(matrixStack, right, y, texRight, texV, texP, texP, texWidth, texHeight);// Right Top corner
		GuiComponent.blit(matrixStack, right, bottom, texRight, texBottom, texP, texP, texWidth, texHeight);// Right Bottom corner
		GuiComponent.blit(matrixStack, x, bottom, texU, texBottom, texP, texP, texWidth, texHeight);// Left Bottom corner
		
		
		int strippedButtonTexWidth = texture.getWidth() - 2*texP;
		int strippedButtonTexHeight = texture.getHeight() - 2*texP;
		
		for (int left = x + texP; left < right; left += strippedButtonTexWidth) {// Fill the Middle
			for (int top = y + texP; top < bottom; top += strippedButtonTexHeight) {
				GuiComponent.blit(matrixStack, left, top, texU + texP, texV + texP, Math.min(strippedButtonTexWidth, right - left), Math.min(strippedButtonTexHeight, bottom - top), texWidth, texHeight);
			}
		}
		for (int left = x + texP; left < right; left += strippedButtonTexWidth) {// Top and Bottom Edges
			GuiComponent.blit(matrixStack, left, y, texU + texP, texV, Math.min(strippedButtonTexWidth, right - left), texP, texWidth, texHeight);// Top
			GuiComponent.blit(matrixStack, left, bottom, texU + texP, texBottom, Math.min(strippedButtonTexWidth, right - left), texP, texWidth, texHeight);// Bottom
		}
		for (int top = y + texP; top < bottom; top += strippedButtonTexHeight) {// Left and Right Edges
			GuiComponent.blit(matrixStack, x, top, texU, texV + texP, texP, Math.min(strippedButtonTexHeight, bottom - top), texWidth, texHeight);// Left
			GuiComponent.blit(matrixStack, right, top, texRight, texV + texP, texP, Math.min(strippedButtonTexHeight, bottom - top), texWidth, texHeight);// Right
		}
		Minecraft.getInstance().font.draw(matrixStack, getButtonText(), x + (width>>1) - (getStringWidth()>>1), y + (height>>1) - (Minecraft.getInstance().font.lineHeight>>1), 0xFFFFFF);

		if(MacroScreen.isMouseInBounds(mouseX, mouseY, x, y, x+width, y+height) && texture == ButtonTexture.DEFAULT_NORMAL){
			internalRender(matrixStack, mouseX, mouseY, partialTicks, ButtonTexture.DEFAULT_PRESSED);
		}
	}
	
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton){
		if(MacroScreen.isMouseInBounds(mouseX, mouseY, x, y, x+width, y+height)){
			if(onClickCallback != null){
				onClickCallback.accept(MouseButton.getButtonFromGlButton(mouseButton));
				return true;
			}
		}
		return false;
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public int getWidth(){
		return width;
	}
	
	public int getHeight(){
		return height;
	}
	
	public ButtonTexture getTexture(){
		return texture;
	}
	
	public String getButtonText(){
		return "S";
	}
	
	public Consumer<MouseButton> getOnClickCallback(){
		return onClickCallback;
	}
	
	public void setX(int x){
		this.x = x;
	}
	
	public void setY(int y){
		this.y = y;
	}
	
	public void setWidth(int width){
		this.width = width;
	}
	
	public void setHeight(int height){
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