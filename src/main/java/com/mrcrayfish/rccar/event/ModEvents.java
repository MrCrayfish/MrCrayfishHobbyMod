package com.mrcrayfish.rccar.event;

import java.util.List;

import org.lwjgl.input.Keyboard;

import com.mrcrayfish.rccar.RemoteMovementInput;
import com.mrcrayfish.rccar.entity.EntityCar;
import com.mrcrayfish.rccar.entity.EntityCar.Turn;
import com.mrcrayfish.rccar.init.ModItems;
import com.mrcrayfish.rccar.network.PacketHandler;
import com.mrcrayfish.rccar.network.message.MessageExplodeCar;
import com.mrcrayfish.rccar.network.message.MessageMoveCar;
import com.mrcrayfish.rccar.network.message.MessageTurnCar;
import com.mrcrayfish.rccar.proxy.ClientProxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EntitySelectors;
import net.minecraft.world.World;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.EntityViewRenderEvent.FOVModifier;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class ModEvents 
{
	private static Entity renderEntity = null;
	public static boolean renderCarView = false;
	
	public static boolean isRemoteControlling = false;
	
	@SubscribeEvent
	public void onInput(PlayerTickEvent event)
	{
		Minecraft mc = Minecraft.getMinecraft();
		EntityPlayer player = mc.player;
		if(event.player == player)
		{
			isRemoteControlling = false;
			
			ItemStack stack = player.getHeldItemMainhand();
			if(stack.getItem() == ModItems.controller)
			{
				if(stack.hasTagCompound())
				{
					if(stack.getTagCompound().hasKey("linked_car"))
					{
						isRemoteControlling = true;
						
						String uuid = stack.getTagCompound().getString("linked_car");
						if(Keyboard.isKeyDown(Keyboard.KEY_W))
						{
							moveCarForward(uuid);
						}
						if(Keyboard.isKeyDown(Keyboard.KEY_A))
						{
							turnCar(uuid, Turn.LEFT);
						}
						if(Keyboard.isKeyDown(Keyboard.KEY_D))
						{
							turnCar(uuid, Turn.RIGHT);
						}
						if(Keyboard.isKeyDown(Keyboard.KEY_SPACE))
						{
							PacketHandler.INSTANCE.sendToServer(new MessageExplodeCar(uuid));
							renderEntity = null;
							mc.setRenderViewEntity(player);
							renderCarView = false;
						}
						if(ClientProxy.KEY_CAMERA.isPressed())
						{
							if(renderCarView) 
							{
								renderEntity = null;
								mc.setRenderViewEntity(player);
								renderCarView = false;
								return;
							}
							World world = mc.world;
							List<EntityCar> cars = world.getEntities(EntityCar.class, EntitySelectors.IS_ALIVE);
							for(EntityCar car : cars)
							{
								if(car.getUniqueID().toString().equals(uuid))
								{
									renderEntity = car;
									mc.setRenderViewEntity(renderEntity);
									renderCarView = true;
									break;
								}
							}
						}
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onTick(TickEvent.ClientTickEvent event)
	{
		Minecraft mc = Minecraft.getMinecraft();
		if(mc.player != null)
		{
			EntityPlayerSP player = mc.player;
			if(!(player.movementInput instanceof RemoteMovementInput))
			{
				player.movementInput = new RemoteMovementInput(player.movementInput);
			}
			
			if(renderEntity != null)
			{
				if(player.getDistanceToEntity(renderEntity) >= 64F)
				{
					renderEntity = null;
					mc.setRenderViewEntity(player);
					renderCarView = false;
				}
			}
		}
	}

	@SubscribeEvent
	public void onCameraSetup(EntityViewRenderEvent.CameraSetup event)
	{
		if(renderCarView && Minecraft.getMinecraft().gameSettings.thirdPersonView == 1)
		{
			GlStateManager.translate(0, -0.5, 2);
			GlStateManager.rotate(5F, 1, 0, 0);
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
	
	public void turnCar(String uuid, Turn turn)
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
			foundCar.turn(turn);
			PacketHandler.INSTANCE.sendToServer(new MessageTurnCar(uuid, turn));
		}
	}
}
