package com.mrcrayfish.rccar.init;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModCrafting 
{
	public static void init()
	{
		GameRegistry.addRecipe(new ItemStack(ModItems.controller), " B ", "LIL", "IRI", 'B', Blocks.IRON_BARS, 'I', Items.IRON_INGOT, 'R', Items.REDSTONE, 'L', Blocks.LEVER);
		GameRegistry.addRecipe(new ItemStack(ModItems.car), " C ", "CFC", "BRB", 'C', new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, 14), 'F', Blocks.FURNACE, 'R', Items.REDSTONE, 'B', new ItemStack(Blocks.WOOL, 1, 15));
	}
}
