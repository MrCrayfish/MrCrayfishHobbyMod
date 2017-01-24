package com.mrcrayfish.rccar.proxy;

import com.mrcrayfish.rccar.entity.EntityCar;
import com.mrcrayfish.rccar.init.ModItems;
import com.mrcrayfish.rccar.render.RenderCar;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ClientProxy implements IProxy
{
	@Override
	public void preInit() 
	{
		RenderingRegistry.registerEntityRenderingHandler(EntityCar.class, new RenderFactory());
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
