package com.mrcrayfish.rccar.proxy;

import org.lwjgl.input.Keyboard;

import com.mrcrayfish.rccar.entity.EntityCar;
import com.mrcrayfish.rccar.event.ModEvents;
import com.mrcrayfish.rccar.init.ModItems;
import com.mrcrayfish.rccar.render.RenderCar;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ClientProxy implements IProxy
{
	public static final KeyBinding KEY_CAMERA = new KeyBinding("key.camera.desc", Keyboard.KEY_C, "key.remote_control_mod");
	
	@Override
	public void preInit() 
	{
		RenderingRegistry.registerEntityRenderingHandler(EntityCar.class, new RenderFactory());
		
		MinecraftForge.EVENT_BUS.register(new ModEvents());
		
		ClientRegistry.registerKeyBinding(KEY_CAMERA);
	}
	
	@Override
	public void init() 
	{
		ModItems.registerRenders();
	}
	
	public static class RenderFactory implements IRenderFactory
	{
		@Override
		public Render createRenderFor(RenderManager manager) 
		{
			return new RenderCar(manager);
		}
	}
}
