package com.mrcrayfish.rccar.gui;

import com.mrcrayfish.rccar.gui.interfaces.ClickListener;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class GuiAdvancedButton extends GuiButton
{
	private ClickListener listener;
	
	public GuiAdvancedButton(int x, int y, String buttonText) 
	{
		super(0, x, y, buttonText);
	}
	
	public GuiAdvancedButton(int x, int y, int widthIn, int heightIn, String buttonText) 
	{
		super(0, x, y, widthIn, heightIn, buttonText);
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
