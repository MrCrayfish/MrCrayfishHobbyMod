package com.mrcrayfish.rccar.gui;

import com.mrcrayfish.rccar.gui.interfaces.ClickListener;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

public class GuiAdvancedButton extends GuiButton
{
	private ResourceLocation icon;
	private int iconX, iconY;
	
	private ClickListener listener;
	
	public GuiAdvancedButton(int x, int y, String buttonText) 
	{
		super(0, x, y, buttonText);
	}
	
	public GuiAdvancedButton(int x, int y, int widthIn, int heightIn, String buttonText) 
	{
		super(0, x, y, widthIn, heightIn, buttonText);
	}
	
	public GuiAdvancedButton(int x, int y, ResourceLocation icon, int iconX, int iconY) 
	{
		super(0, x, y, 20, 20, "");
		this.icon = icon;
		this.iconX = iconX;
		this.iconY = iconY;
	}
	
	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY) 
	{
		super.drawButton(mc, mouseX, mouseY);
		if(icon != null)
		{
			int state = getHoverState(this.hovered);
			mc.getTextureManager().bindTexture(icon);
			this.drawTexturedModalRect(this.xPosition + 4, this.yPosition + 4, iconX, iconY + 12 * state, 12, 12);
		}
	}

	@Override
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) 
	{
		if(listener != null && super.mousePressed(mc, mouseX, mouseY))
		{
			return listener.handle();
		}
		return false;
	}
	
	public void setListener(ClickListener listener) 
	{
		this.listener = listener;
	}
}
