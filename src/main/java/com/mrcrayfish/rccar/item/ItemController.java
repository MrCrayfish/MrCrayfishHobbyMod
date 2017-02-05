package com.mrcrayfish.rccar.item;

import java.util.List;

import com.mrcrayfish.rccar.MrCrayfishRCCarMod;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class ItemController extends Item
{
	public ItemController() 
	{
		this.setUnlocalizedName("controller");
		this.setRegistryName("controller");
		this.setCreativeTab(MrCrayfishRCCarMod.TAB_CAR);
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) 
	{
		if(stack.hasTagCompound())
		{
			if(stack.getTagCompound().hasKey("linked_car"))
			{
				tooltip.add(TextFormatting.GOLD.toString() + TextFormatting.BOLD.toString() + "Linked");
				tooltip.add(stack.getTagCompound().getString("linked_car"));
				return;
			}
		}
		tooltip.add(TextFormatting.RED.toString() + TextFormatting.BOLD.toString() + "Unlinked");
		tooltip.add("Right click a car with this controller to link them together");
	}
}