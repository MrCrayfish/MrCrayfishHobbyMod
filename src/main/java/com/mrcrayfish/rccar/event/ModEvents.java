package com.mrcrayfish.rccar.event;

import java.util.List;

import org.lwjgl.input.Keyboard;

import com.mrcrayfish.rccar.RemoteMovementInput;
import com.mrcrayfish.rccar.client.ClientProxy;
import com.mrcrayfish.rccar.client.KeyBindingOverride;
import com.mrcrayfish.rccar.entity.EntityCar;
import com.mrcrayfish.rccar.entity.EntityCar.Turn;
import com.mrcrayfish.rccar.init.ModItems;
import com.mrcrayfish.rccar.network.PacketHandler;
import com.mrcrayfish.rccar.network.message.MessageMoveCar;
import com.mrcrayfish.rccar.network.message.MessageTurnCar;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.model.ModelBiped.ArmPose;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EntitySelectors;
import net.minecraft.world.World;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class ModEvents 
{
	private static Entity renderEntity = null;
	public static boolean renderCarView = false;
	
	public static boolean isRemoteControlling = false;
	
	private static KeyBindingOverride keyLeftClick;
	private static KeyBindingOverride keyRightClick;
	
	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent event)
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
						if(ClientProxy.KEY_CAMERA.isPressed())
						{
							if(renderCarView) 
							{
								setView(null);
								return;
							}
							World world = mc.world;
							List<EntityCar> cars = world.getEntities(EntityCar.class, EntitySelectors.IS_ALIVE);
							for(EntityCar car : cars)
							{
								if(car.getUniqueID().toString().equals(uuid))
								{
									setView(car);
									break;
								}
							}
						}
					}
				}
			}
		}
	}
	
	public void setView(Entity entity)
	{
		Minecraft mc = Minecraft.getMinecraft();
		EntityPlayer player = mc.player;
		if(entity != null)
		{
			renderEntity = entity;
			mc.setRenderViewEntity(renderEntity);
			renderCarView = true;
		}
		else
		{
			renderEntity = null;
			mc.setRenderViewEntity(player);
			renderCarView = false;
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
			
			if(!(mc.gameSettings.keyBindAttack instanceof KeyBindingOverride))
			{
				mc.gameSettings.keyBindAttack = keyLeftClick = new KeyBindingOverride(mc.gameSettings.keyBindAttack);
			}
			
			if(!(mc.gameSettings.keyBindUseItem instanceof KeyBindingOverride))
			{
				mc.gameSettings.keyBindUseItem = keyRightClick = new KeyBindingOverride(mc.gameSettings.keyBindUseItem);
			}
			
			if(renderEntity != null)
			{
				if(player.getDistanceToEntity(renderEntity) >= 256F)
				{
					setView(player);
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onKeyInput(KeyInputEvent event)
	{
		if(keyLeftClick != null && keyLeftClick.isPressed())
		{
			System.out.println("Captured left click");
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

	@SubscribeEvent 
	public void onRenderPlayer(RenderPlayerEvent.Pre event)
	{
		event.getRenderer().getMainModel().rightArmPose = ArmPose.BOW_AND_ARROW;
		event.getRenderer().getMainModel().leftArmPose = ArmPose.BOW_AND_ARROW;
		//ItemStack stack = event.getEntityPlayer().getHeldItemMainhand();
		//if(!stack.isEmpty() && stack.getItem() == ModItems.controller)
		//{
			
			
		//}
	}
	
	@SubscribeEvent
	public void onRnderLast(RenderWorldLastEvent event)
	{
		if(renderCarView && renderEntity != null)
		{
			Minecraft mc = Minecraft.getMinecraft();
			Render render = mc.getRenderManager().getEntityRenderObject(mc.player);
			if(render instanceof RenderPlayer)
			{
				RenderPlayer renderPlayer = (RenderPlayer) render;
				ModelPlayer playerModel = renderPlayer.getMainModel();
				mc.getTextureManager().bindTexture(mc.player.getLocationSkin());
				
				double deltaX = mc.player.posX - renderEntity.prevPosX - (renderEntity.posX - renderEntity.prevPosX) * event.getPartialTicks();
				double deltaY = mc.player.posY - renderEntity.prevPosY - (renderEntity.posY - renderEntity.prevPosY) * event.getPartialTicks();
				double deltaZ = mc.player.posZ - renderEntity.prevPosZ - (renderEntity.posZ - renderEntity.prevPosZ) * event.getPartialTicks();
				
				
				GlStateManager.pushMatrix();
				{
					GlStateManager.translate(deltaX, deltaY, deltaZ);
					GlStateManager.rotate(180F, 1, 0, 0);
					GlStateManager.translate(0, -1.5, 0);
					GlStateManager.rotate(mc.player.rotationYaw, 0, 1, 0);
					RenderHelper.enableGUIStandardItemLighting();
					GlStateManager.enableAlpha();
					playerModel.rightArmPose = ArmPose.BOW_AND_ARROW;
					playerModel.render(mc.player, 0F, 0F, 0, 0f, mc.player.rotationPitch, 0.0625F);
				}
				GlStateManager.popMatrix();
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
