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
	public static Item wrench;
	
	public static Item car_base;
	public static Item case_standard;
	public static Item case_different;
	
	public static void init()
	{
		controller = new ItemController();
		car = new ItemCar();
		wheel = new ItemWheel();
		wrench = registerPart("wrench");
		car_base = registerPart("car_base");
		case_standard = registerPart("case_standard");
		case_different = registerPart("case_different");
	}
	
	public static Item registerPart(String id)
	{
		return new Item().setUnlocalizedName(id).setRegistryName(id);
	}
	
	public static void register()
	{
		GameRegistry.register(controller);
		GameRegistry.register(car);
		GameRegistry.register(wheel);
		GameRegistry.register(wrench);
		GameRegistry.register(car_base);
		GameRegistry.register(case_standard);
		GameRegistry.register(case_different);

	}
	
	public static void registerRenders()
	{
		registerRender(controller);
		registerRender(car);
		registerRender(wheel);
		registerRender(wrench);
		registerRender(car_base);
		registerRender(case_standard);
		registerRender(case_different);
	}
	
	private static void registerRender(Item item)
	{
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0, new ModelResourceLocation(Reference.MOD_ID + ":" + item.getUnlocalizedName().substring(5), "inventory"));
	}
}
