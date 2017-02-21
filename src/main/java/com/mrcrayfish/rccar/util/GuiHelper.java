package com.mrcrayfish.rccar.util;

public class GuiHelper 
{
	public static boolean isMouseInside(int mouseX, int mouseY, int x, int y, int width, int height)
	{
		return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
	}
}
