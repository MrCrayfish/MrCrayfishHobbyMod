package com.mrcrayfish.rccar.gui.component;

import java.util.ArrayList;
import java.util.List;

import com.mrcrayfish.rccar.gui.interfaces.ExtendedRender;

import net.minecraft.client.gui.GuiButton;

public class Page
{
	private List<GuiButton> components = new ArrayList<GuiButton>();
	
	private ExtendedRender render;
	
	public void add(GuiButton component)
	{
		this.components.add(component);
	}

	public void show(List<GuiButton> buttonList)
	{
		for(GuiButton component : components)
		{
			buttonList.add(component);
		}
	}
	
	public void hide(List<GuiButton> buttonList)
	{
		for(GuiButton component : components)
		{
			buttonList.remove(component);
		}
	}
	
	public ExtendedRender getExtendedRender() 
	{
		return render;
	}
	
	public void setExtendedRender(ExtendedRender render) 
	{
		this.render = render;
	}
}
