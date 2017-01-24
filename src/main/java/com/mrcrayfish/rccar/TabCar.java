package com.mrcrayfish.rccar;

import com.mrcrayfish.rccar.init.ModItems;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class TabCar extends CreativeTabs
{
	public TabCar() 
	{
		super("tabJax");
	}

	@Override
	public ItemStack getTabIconItem() 
	{
		return new ItemStack(ModItems.controller);
	}
}
