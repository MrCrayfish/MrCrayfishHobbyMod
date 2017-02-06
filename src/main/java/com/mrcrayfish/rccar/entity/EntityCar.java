package com.mrcrayfish.rccar.entity;

import com.mrcrayfish.rccar.init.ModItems;
import com.mrcrayfish.rccar.init.ModSounds;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class EntityCar extends Entity
{
	private boolean isMoving = false;
	private double currentSpeed;
	
	public static final double MAX_SPEED = 5;
	
	private float wheelAngle = 0F;
	private float prevWheelAngle = 0F;
	public float wheelRotation = 0F;
	public float prevWheelRotation = 0F;
	
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
		
		this.prevWheelAngle = this.wheelAngle;
		this.prevWheelRotation = this.wheelRotation;
		
		this.rotationYaw -= (this.wheelAngle / 2F) * (this.currentSpeed / MAX_SPEED);
		this.motionY -= 0.05D;
		this.wheelRotation += this.currentSpeed * 10F;
		
		if(this.currentSpeed > 0.01)
		{
			this.motionX = -Math.sin((double) ((rotationYaw + wheelAngle) * (float) Math.PI / 180.0F)) * currentSpeed / 16D;
			this.motionZ = Math.cos((double) ((rotationYaw + wheelAngle) * (float) Math.PI / 180.0F)) * currentSpeed / 16D;
		}
		
		this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);

		this.isMoving = this.motionX >= 0.01 || this.motionX <= -0.01 || this.motionZ >= 0.01 || this.motionZ <= -0.01;
		
		if(isCollidedHorizontally)
		{
			this.currentSpeed *= 0.5D;
		}
		
		if(currentSpeed > 3)
		{
			this.createRunningParticles();
		}
		
		this.currentSpeed *= 0.95D;
		this.wheelAngle *= 0.9F;
	}
	
	@Override
	public boolean processInitialInteract(EntityPlayer player, EnumHand hand) 
	{
		if(!world.isRemote && player.isSneaking())
		{
			this.setDead();
			EntityItem item = new EntityItem(world, posX, posY, posZ, new ItemStack(ModItems.car));
			world.spawnEntity(item);
			return true;
		}
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
					if(!world.isRemote)
					{
						world.playSound((EntityPlayer)null, getPosition(), ModSounds.connect, SoundCategory.AMBIENT, 1.0F, 1.0F);
						player.sendMessage(new TextComponentString(TextFormatting.GOLD.toString() + TextFormatting.BOLD.toString() + "Car succesfully linked!"));
						player.sendMessage(new TextComponentString("Use WASD to drive around. Press C to view the car's camera."));
					}
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
	
	public boolean isMoving() 
	{
		return isMoving;
	}
	
	public void increaseSpeed()
	{
		currentSpeed += 0.5D;
		if(currentSpeed > MAX_SPEED)
		{
			currentSpeed = MAX_SPEED;
		}
	}
	
	public double getSpeedPercentage()
	{
		return currentSpeed / MAX_SPEED;
	}
	
	@Override
	protected boolean canBeRidden(Entity entityIn) 
	{
		return true;
	}
	
	@Override
	protected void playStepSound(BlockPos pos, Block blockIn) {}
	
	public void turn(Turn turn)
	{
		switch(turn)
		{
		case LEFT:
			wheelAngle = MathHelper.clamp(wheelAngle += 2F, -25, 25);
			break;
		case RIGHT:
			wheelAngle = MathHelper.clamp(wheelAngle -= 2F, -25, 25);
			break;
		}
	}
	
	public float getWheelAngle() 
	{
		return wheelAngle;
	}
	
	public static enum Turn 
	{
		LEFT, RIGHT;
	}
}
