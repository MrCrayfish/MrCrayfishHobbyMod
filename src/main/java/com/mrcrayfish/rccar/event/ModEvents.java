package com.mrcrayfish.rccar.event;

import java.util.List;

import org.lwjgl.input.Keyboard;

import com.mrcrayfish.rccar.entity.EntityCar;
import com.mrcrayfish.rccar.init.ModItems;
import com.mrcrayfish.rccar.network.PacketHandler;
import com.mrcrayfish.rccar.network.message.MessageExplodeCar;
import com.mrcrayfish.rccar.network.message.MessageMoveCar;
import com.mrcrayfish.rccar.network.message.MessageTurnCar;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EntitySelectors;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ModEvents 
{
	@SubscribeEvent
	public void onInput(LivingUpdateEvent event)
	{
		EntityPlayer player = Minecraft.getMinecraft().player;
		if(event.getEntity() == player)
		{
			ItemStack stack = Minecraft.getMinecraft().player.getHeldItemMainhand();
			if(stack.getItem() == ModItems.controller)
			{
				if(stack.hasTagCompound())
				{
					if(stack.getTagCompound().hasKey("linked_car"))
					{
						event.setCanceled(true);
						
						String uuid = stack.getTagCompound().getString("linked_car");
						if(Keyboard.isKeyDown(Keyboard.KEY_W))
						{
							moveCarForward(uuid);
						}
						if(Keyboard.isKeyDown(Keyboard.KEY_A))
						{
							turnCar(uuid, -5F);
						}
						if(Keyboard.isKeyDown(Keyboard.KEY_D))
						{
							turnCar(uuid, 5F);
						}
						if(Keyboard.isKeyDown(Keyboard.KEY_SPACE))
						{
							PacketHandler.INSTANCE.sendToServer(new MessageExplodeCar(uuid));
						}
					}
				}
			}
		}
	}
	
	public void moveCarForward(String uuid)
	{
		World world = Minecraft.getMinecraft().world;
		List<EntityCar> cars = world.getEntities(EntityCar.class, EntitySelectors.IS_ALIVE);
		for(EntityCar car : cars)
		{
			if(car.getUniqueID().toString().equals(uuid))
			{
				car.increaseSpeed();
				break;
			}
		}
		PacketHandler.INSTANCE.sendToServer(new MessageMoveCar(uuid));
	}
	
	public void turnCar(String uuid, float yaw)
	{
		World world = Minecraft.getMinecraft().world;
		EntityCar foundCar = null;
		List<EntityCar> cars = world.getEntities(EntityCar.class, EntitySelectors.IS_ALIVE);
		for(EntityCar car : cars)
		{
			if(car.getUniqueID().toString().equals(uuid))
			{
				foundCar = car;
				break;
			}
		}
		if(foundCar != null)
		{
			foundCar.rotationYaw += yaw;
			PacketHandler.INSTANCE.sendToServer(new MessageTurnCar(uuid, foundCar.rotationYaw));
		}
	}
}
