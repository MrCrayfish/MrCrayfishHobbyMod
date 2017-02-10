package com.mrcrayfish.rccar.client;

import org.lwjgl.input.Keyboard;

import com.mrcrayfish.rccar.client.model.block.CustomLoader;
import com.mrcrayfish.rccar.client.render.RenderCar;
import com.mrcrayfish.rccar.entity.EntityCar;
import com.mrcrayfish.rccar.event.ModEvents;
import com.mrcrayfish.rccar.init.ModBlocks;
import com.mrcrayfish.rccar.init.ModItems;
import com.mrcrayfish.rccar.proxy.IProxy;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ClientProxy implements IProxy
{
	public static final KeyBinding KEY_CAMERA = new KeyBinding("key.camera.desc", Keyboard.KEY_C, "key.remote_control_mod");
	
	@Override
	public void preInit() 
	{
		RenderingRegistry.registerEntityRenderingHandler(EntityCar.class, new RenderFactory());
		
		MinecraftForge.EVENT_BUS.register(new ModEvents());
		
		ClientRegistry.registerKeyBinding(KEY_CAMERA);
		
		ModelLoaderRegistry.registerLoader(new CustomLoader());
	}
	
	@Override
	public void init() 
	{
		ModBlocks.registerRenders();
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
