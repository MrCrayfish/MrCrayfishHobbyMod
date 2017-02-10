package com.mrcrayfish.rccar.client.render;

import com.mrcrayfish.rccar.block.BlockRamp;
import com.mrcrayfish.rccar.block.attribute.Angled;
import com.mrcrayfish.rccar.entity.EntityCar;
import com.mrcrayfish.rccar.init.ModItems;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RenderCar extends Render<EntityCar>
{
	public static EntityItem currentCase = new EntityItem(Minecraft.getMinecraft().world, 0, 0, 0, new ItemStack(ModItems.case_standard));
	public static final EntityItem BASE = new EntityItem(Minecraft.getMinecraft().world, 0, 0, 0, new ItemStack(ModItems.car_base));
	public static final EntityItem WHEEL = new EntityItem(Minecraft.getMinecraft().world, 0, 0, 0, new ItemStack(ModItems.wheel));
	
	static
	{
		currentCase.hoverStart = 0F;
		BASE.hoverStart = 0F;
		WHEEL.hoverStart = 0F;
	}
	
	private Angled angledSurface;
	private EnumFacing facing;
	
	public RenderCar(RenderManager renderManager) 
	{
		super(renderManager);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityCar entity) 
	{
		return null;
	}

	@Override
	public void doRender(EntityCar entity, double x, double y, double z, float entityYaw, float partialTicks) 
	{
		currentCase.setEntityItemStack(entity.getCurrentCaseItem());
		
		GlStateManager.pushMatrix();
		{
			GlStateManager.translate(x, y, z);
			GlStateManager.rotate(180F - entityYaw, 0, 1, 0);
			
			updatedAngledSurface(entity.posX, entity.posY, entity.posZ);
			if(angledSurface != null)
			{
				GlStateManager.translate(0, -0.12, 0);
				GlStateManager.rotate(25F, 1, 0, 0);
			}
			
			GlStateManager.translate(0, 0, -0.4);
			Minecraft.getMinecraft().getRenderManager().doRenderEntity(currentCase, 0, 0, 0, 0F, 0F, true);
			Minecraft.getMinecraft().getRenderManager().doRenderEntity(BASE, 0, 0, 0, 0F, 0F, true);
			
			float wheelSpin = entity.prevWheelRotation + (entity.wheelRotation - entity.prevWheelRotation) * partialTicks;
			double wheelScale = 1.5;
			
			GlStateManager.pushMatrix();
			{
				GlStateManager.translate(0.3, 0.13125, -0.4075);
				
				GlStateManager.pushMatrix();
				{
					GlStateManager.rotate(entity.getWheelAngle(), 0, 1, 0);
					if(entity.isMoving())
					{
						GlStateManager.rotate(-wheelSpin, 1, 0, 0);
					}
					GlStateManager.translate(0.0625 * wheelScale - 0.0625, -0.5375 * wheelScale, 0.0);
					GlStateManager.scale(wheelScale, wheelScale, wheelScale);
					Minecraft.getMinecraft().getRenderManager().doRenderEntity(WHEEL, 0, 0, 0, 0f, 0f, true);
				}
				GlStateManager.popMatrix();
				
				GlStateManager.translate(-0.6, 0, 0);
				
				GlStateManager.pushMatrix();
				{
					GlStateManager.rotate(entity.getWheelAngle(), 0, 1, 0);
					if(entity.isMoving())
					{
						GlStateManager.rotate(-wheelSpin, 1, 0, 0);
					}
					GlStateManager.translate(-0.0625 * wheelScale + 0.0625, -0.5375 * wheelScale, 0.0);
					GlStateManager.scale(wheelScale, wheelScale, wheelScale);
					Minecraft.getMinecraft().getRenderManager().doRenderEntity(WHEEL, 0, 0, 0, 0f, 0f, true);
				}
				GlStateManager.popMatrix();
			}
			GlStateManager.popMatrix();
			
			GlStateManager.pushMatrix();
			{
				GlStateManager.translate(0.3, 0.13125, 0.4075);
				
				GlStateManager.pushMatrix();
				{
					if(entity.isMoving())
					{
						GlStateManager.rotate(-wheelSpin, 1, 0, 0);
					}
					
					GlStateManager.translate(0.0625 * wheelScale - 0.0625, -0.5375 * wheelScale, 0.0);
					GlStateManager.scale(wheelScale, wheelScale, wheelScale);
					Minecraft.getMinecraft().getRenderManager().doRenderEntity(WHEEL, 0, 0, 0, 0f, 0f, true);
				}
				GlStateManager.popMatrix();
				
				GlStateManager.translate(-0.6, 0, 0);
				
				GlStateManager.pushMatrix();
				{
					if(entity.isMoving())
					{
						GlStateManager.rotate(-wheelSpin, 1, 0, 0);
					}
					GlStateManager.translate(-0.0625 * wheelScale + 0.0625, -0.5375 * wheelScale, 0.0);
					GlStateManager.scale(wheelScale, wheelScale, wheelScale);
					Minecraft.getMinecraft().getRenderManager().doRenderEntity(WHEEL, 0, 0, 0, 0f, 0f, true);
					RenderHelper.disableStandardItemLighting();
				}
				GlStateManager.popMatrix();
			}
			GlStateManager.popMatrix();
		}
		GlStateManager.popMatrix();
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}
	
	public void updatedAngledSurface(double posX, double posY, double posZ)
	{
		this.angledSurface = null;
		
		World world = Minecraft.getMinecraft().world;
		BlockPos pos = new BlockPos(posX, posY, posZ);
		
		IBlockState inside = world.getBlockState(pos);
		if(inside.getBlock() instanceof Angled)
		{
			this.angledSurface = (Angled) inside.getBlock();
			this.facing = inside.getValue(angledSurface.getDirectionProp());
			return;
		}
		
		IBlockState below = world.getBlockState(pos.down());
		if(below.getBlock() instanceof Angled)
		{
			this.angledSurface = (Angled) below.getBlock();
			this.facing = below.getValue(angledSurface.getDirectionProp());
		}
	}
}