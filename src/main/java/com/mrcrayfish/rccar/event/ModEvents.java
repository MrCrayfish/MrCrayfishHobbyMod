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
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
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
							System.out.println("called?");
							PacketHandler.INSTANCE.sendToServer(new MessageExplodeCar(uuid));
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
				if(player.getDistanceToEntity(renderEntity) >= 256F)
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
	
	@SubscribeEvent
	public void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event)
	{
		if(renderCarView) event.setCanceled(true);
		System.out.println("Cancelling");
	}
	
	@SubscribeEvent
	public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event)
	{
		if(renderCarView) event.setCanceled(true);
	}
	
	@SubscribeEvent
	public void onRightClickBlock(PlayerInteractEvent.EntityInteract event)
	{
		if(renderCarView) event.setCanceled(true);
	}
	
	@SubscribeEvent
	public void onAttackEntity(AttackEntityEvent event)
	{
		if(renderCarView) event.setCanceled(true);
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
