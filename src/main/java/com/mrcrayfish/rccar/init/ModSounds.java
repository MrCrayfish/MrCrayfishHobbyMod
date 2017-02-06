package com.mrcrayfish.rccar.init;

import java.util.Random;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModSounds {
	
	public static SoundEvent connect;
	
	public static void register()
	{
		connect = registerSound("crccm:connect");
	}

	private static SoundEvent registerSound(String soundNameIn)
    {
		ResourceLocation sound = new ResourceLocation(soundNameIn);
        return GameRegistry.register(new SoundEvent(sound).setRegistryName(soundNameIn));
    }
}
