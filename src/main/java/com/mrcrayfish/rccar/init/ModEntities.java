package com.mrcrayfish.rccar.init;

import com.mrcrayfish.rccar.MrCrayfishRCCarMod;
import com.mrcrayfish.rccar.entity.EntityCar;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class ModEntities 
{
	public static void register()
	{
		EntityRegistry.registerModEntity(new ResourceLocation("crccm:car"), EntityCar.class, "crccmCar", 0, MrCrayfishRCCarMod.instance, 64, 1, true);
	}
}
