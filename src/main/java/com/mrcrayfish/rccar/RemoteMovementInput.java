package com.mrcrayfish.rccar;

import com.mrcrayfish.rccar.event.ModEvents;

import net.minecraft.client.Minecraft;
import net.minecraft.util.MovementInput;

public class RemoteMovementInput extends MovementInput 
{
	private MovementInput original;
	
	public RemoteMovementInput(MovementInput original) 
	{
		this.original = original;
	}
	
	@Override
	public void updatePlayerMoveState() 
	{
		original.updatePlayerMoveState();
		
		this.jump = original.jump;
		this.sneak = original.sneak;
		
		if(ModEvents.isRemoteControlling)
		{
			this.moveForward = 0;
			this.moveStrafe = 0;
			
			Minecraft mc = Minecraft.getMinecraft();
			mc.mouseHelper.deltaX = mc.mouseHelper.deltaY = 0;
		}
		else
		{
			this.moveForward = original.moveForward;
			this.moveStrafe = original.moveStrafe;
		}
	}
}
