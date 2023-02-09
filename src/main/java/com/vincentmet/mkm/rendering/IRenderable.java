package com.vincentmet.mkm.rendering;

import com.mojang.blaze3d.vertex.PoseStack;

public interface IRenderable {
    void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks);
}