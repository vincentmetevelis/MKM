package com.vincentmet.mkm.rendering;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;

public interface IRenderable {
    void render(GuiGraphics stack, int mouseX, int mouseY, float partialTicks);
}