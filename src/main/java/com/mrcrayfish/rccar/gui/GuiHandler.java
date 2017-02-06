package com.mrcrayfish.rccar.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler 
{
	private static int entityId = -1;
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) 
	{
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) 
	{
		if(ID == GuiCar.ID && entityId != -1)
		{
			return new GuiCar(entityId);
		}
		return null;
	}
	
	public static void setCar(int entityId)
	{
		GuiHandler.entityId = entityId;
	}
}
