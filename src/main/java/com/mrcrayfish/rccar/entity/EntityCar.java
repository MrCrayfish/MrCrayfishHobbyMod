package com.mrcrayfish.rccar.entity;

import com.mrcrayfish.rccar.MrCrayfishRCCarMod;
import com.mrcrayfish.rccar.block.attribute.Angled;
import com.mrcrayfish.rccar.gui.GuiCarSettings;
import com.mrcrayfish.rccar.gui.GuiHandler;
import com.mrcrayfish.rccar.init.ModItems;
import com.mrcrayfish.rccar.init.ModSounds;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class EntityCar extends Entity
{
	//private static final DataParameter<Float> CURRENT_SPEED = EntityDataManager.<Float>createKey(EntityCar.class, DataSerializers.FLOAT);
	
	private boolean isMoving = false;
	private double currentSpeed;
	
	public static final double MAX_SPEED = 10;
	
	public float carPitch;
	public float prevCarPitch;
	private float wheelAngle;
	private float prevWheelAngle;
	public float wheelRotation;
	public float prevWheelRotation;
	
	private Case currentCase = Case.STANDARD;
	private ItemStack currentCaseItem = new ItemStack(ModItems.case_4wd);
	private float wheelSize = 1.0F;
	
	private Angled angledSurface;
	private EnumFacing angledFacing;
	
	public EntityCar(World worldIn) 
	{
		super(worldIn);
		this.setSize(0.8F, 0.5F);
		this.stepHeight = 0.5F;
		this.onGround = true;
	}
	
	public EntityCar(World worldIn, double x, double y, double z) 
	{
		this(worldIn);
		this.setPosition(x, y, z);
	}
	
	@Override
	protected void entityInit() 
	{
		//this.dataManager.register(CURRENT_SPEED, 0F);
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
		
		/*if(!world.isRemote)
		{
			this.dataManager.set(CURRENT_SPEED, (float) this.currentSpeed);
		}
		else
		{
			this.currentSpeed = this.dataManager.get(CURRENT_SPEED);
		}*/

		this.prevCarPitch = this.carPitch;
		this.prevWheelAngle = this.wheelAngle;
		this.prevWheelRotation = this.wheelRotation;
		
		boolean wasOnSurface = this.angledSurface != null;
		this.updateAngledSurface();
		
		if(this.angledSurface != null)
		{
			this.carPitch = -25F;
		}
		else if(carPitch < 0 || !this.onGround)
		{
			if(this.carPitch < 85F)
			{
				this.carPitch += 5F;
			}
		}
		else
		{
			this.carPitch = 0F;
		}

		double deltaYaw = (this.wheelAngle / 2F);
		if(currentSpeed <= 2.5)
		{
			deltaYaw *= currentSpeed / 2.5;
		}
		
		this.rotationYaw -= deltaYaw;
		
		this.wheelRotation += this.currentSpeed * 10F;
		
		if(this.currentSpeed > 0.01)
		{
			if(this.onGround) this.motionX = -Math.sin((double) ((rotationYaw + wheelAngle) * (float) Math.PI / 180.0F)) * currentSpeed / 16D;
			if(wasOnSurface && this.angledSurface == null)
			{
				this.motionY = MathHelper.sin(-carPitch * 0.017453292F) * (currentSpeed / MAX_SPEED);
			}
			if(this.onGround) this.motionZ = Math.cos((double) ((rotationYaw + wheelAngle) * (float) Math.PI / 180.0F)) * currentSpeed / 16D;
		}
		
		this.isMoving = this.motionX >= 0.01 || this.motionX <= -0.01 || this.motionZ >= 0.01 || this.motionZ <= -0.01;
		
		this.motionY -= 0.1D;
		
		if(isMoving)
		{
			this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
		}
		
		if(isCollidedHorizontally)
		{
			this.currentSpeed *= 0.2D;
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
		if(world.isRemote && stack.getItem() == ModItems.wrench)
		{
			GuiHandler.setCar(getEntityId());
			player.openGui(MrCrayfishRCCarMod.instance, GuiCarSettings.ID, world, 0, 0, 0);
			return true;
		}
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
		return true;
	}
	
	private void updateAngledSurface()
	{
		this.angledSurface = null;
		
		BlockPos pos = new BlockPos(posX, posY, posZ);
		
		AxisAlignedBB boundingBox = getEntityBoundingBox();
		
		if(checkForAngledBlockAt(pos)) return;
	}
	
	private boolean checkForAngledBlockAt(BlockPos pos)
	{
		IBlockState inside = world.getBlockState(pos);
		if(inside.getBlock() instanceof Angled)
		{
			this.angledSurface = (Angled) inside.getBlock();
			this.angledFacing = inside.getValue(angledSurface.getDirectionProp());
			return true;
		}
		
		IBlockState below = world.getBlockState(pos.down());
		if(below.getBlock() instanceof Angled)
		{
			this.angledSurface = (Angled) below.getBlock();
			this.angledFacing = below.getValue(angledSurface.getDirectionProp());
			return true;
		}
		return false;
	}
	
	public Angled getAngledSurface() 
	{
		return angledSurface;
	}
	
	public EnumFacing getAngledFacing() 
	{
		return angledFacing;
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) 
	{
		this.currentSpeed = compound.getDouble("speed");
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) 
	{
		compound.setDouble("speed", this.currentSpeed);
		compound.setFloat("carPitch", this.carPitch);
		compound.setFloat("prevCarPitch", this.prevCarPitch);
	}
	
	@Override
	protected boolean canBeRidden(Entity entityIn) 
	{
		return true;
	}
	
	@Override
	protected void playStepSound(BlockPos pos, Block blockIn) {}
	
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
	
	public ItemStack getCurrentCaseItem() 
	{
		return currentCaseItem;
	}
	
	public static enum Turn 
	{
		LEFT, RIGHT;
	}
	
	public static enum Case
	{
		STANDARD,
		DIFFERENT;
	}
}
