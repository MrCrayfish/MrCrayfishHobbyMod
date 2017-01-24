package com.mrcrayfish.rccar.item;

import com.mrcrayfish.rccar.MrCrayfishRCCarMod;
import com.mrcrayfish.rccar.entity.EntityCar;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemCar extends Item 
{
	public ItemCar() 
	{
		this.setUnlocalizedName("car");
		this.setRegistryName("car");
		this.setCreativeTab(MrCrayfishRCCarMod.TAB_CAR);
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) 
	{
		if(!worldIn.isRemote)
		{
			if(facing == EnumFacing.UP)
			{
				BlockPos up = pos.up();
				EntityCar car = new EntityCar(worldIn, up.getX(), up.getY(), up.getZ());
				worldIn.spawnEntity(car);
			}
		}
		return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
	}
}
