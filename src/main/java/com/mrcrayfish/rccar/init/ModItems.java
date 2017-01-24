package com.mrcrayfish.rccar.init;

import com.mrcrayfish.rccar.Reference;
import com.mrcrayfish.rccar.item.ItemCar;
import com.mrcrayfish.rccar.item.ItemController;
import com.mrcrayfish.rccar.item.ItemWheel;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModItems 
{
	public static Item controller;
	public static Item car;
	public static Item wheel;
	
	public static void init()
	{
		controller = new ItemController();
		car = new ItemCar();
		wheel = new ItemWheel();
	}
	
	public static void register()
	{
		GameRegistry.register(controller);
		GameRegistry.register(car);
		GameRegistry.register(wheel);
	}
	
	public static void registerRenders()
	{
		registerRender(controller);
		registerRender(car);
		registerRender(wheel);
	}
	
	private static void registerRender(Item item)
	{
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0, new ModelResourceLocation(Reference.MOD_ID + ":" + item.getUnlocalizedName().substring(5), "inventory"));
	}
}
