package com.mrcrayfish.rccar.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;

public class RenderUtil
{
	public static void renderItem(int x, int y, ItemStack stack, boolean overlay)
	{
		GlStateManager.disableDepth();
		GlStateManager.enableLighting();
		RenderHelper.enableGUIStandardItemLighting();
		Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(stack, x, y);
		if(overlay) Minecraft.getMinecraft().getRenderItem().renderItemOverlays(Minecraft.getMinecraft().fontRendererObj, stack, x, y);
		GlStateManager.enableAlpha();
		GlStateManager.disableLighting();
		GlStateManager.enableDepth();
	}
}
