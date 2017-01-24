package com.mrcrayfish.rccar.entity;

import com.mrcrayfish.rccar.init.ModItems;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class EntityCar extends Entity
{
	public EntityCar(World worldIn) 
	{
		super(worldIn);
		this.setSize(0.8F, 0.5F);
		this.stepHeight = 0.5F;
	}
	
	public EntityCar(World worldIn, double x, double y, double z) 
	{
		this(worldIn);
		this.setPosition(x, y, z);
		this.moveRelative(0F, 0.05F, 0.1F);
	}
	
	@Override
	public boolean canBeCollidedWith()
	{
		return true;
	}

	@Override
	public void onUpdate() 
	{
		super.onUpdate();
		this.motionY += -0.05D;
		
		this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
		
		this.motionX *= 0.9;
		this.motionZ *= 0.9;
	}
	
	@Override
	public boolean processInitialInteract(EntityPlayer player, EnumHand hand) 
	{
		ItemStack stack = player.getHeldItem(hand);
		if(!stack.isEmpty())
		{
			if(stack.getItem() == ModItems.controller)
			{
				if(!stack.hasTagCompound())
				{
					stack.setTagCompound(new NBTTagCompound());
				}
				if(!stack.getTagCompound().hasKey("linked_car"))
				{
					stack.getTagCompound().setString("linked_car", getUniqueID().toString());
				}
				return true;
			}
		}
		return false;
	}

	@Override
	protected void entityInit() 
	{
		
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) 
	{
		
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) 
	{
		
	}

}
