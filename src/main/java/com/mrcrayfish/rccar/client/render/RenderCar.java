package com.mrcrayfish.rccar.client.render;

import com.mrcrayfish.rccar.block.BlockRamp;
import com.mrcrayfish.rccar.block.attribute.Angled;
import com.mrcrayfish.rccar.entity.EntityCar;
import com.mrcrayfish.rccar.event.ModEvents;
import com.mrcrayfish.rccar.init.ModItems;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class RenderCar extends Render<EntityCar>
{
	public static EntityItem currentCase = new EntityItem(Minecraft.getMinecraft().world, 0, 0, 0, new ItemStack(ModItems.case_standard));
	public static final EntityItem BASE = new EntityItem(Minecraft.getMinecraft().world, 0, 0, 0, new ItemStack(ModItems.car_base));
	public static final EntityItem WHEEL = new EntityItem(Minecraft.getMinecraft().world, 0, 0, 0, new ItemStack(ModItems.wheel));
	
	public static float offsetRotationYaw = 0F;
	public static float currentOffsetRotationYaw = 0F;
	
	static
	{
		currentCase.hoverStart = 0F;
		BASE.hoverStart = 0F;
		WHEEL.hoverStart = 0F;
	}
	
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
	public void doRender(EntityCar car, double x, double y, double z, float entityYaw, float partialTicks) 
	{
		currentCase.setEntityItemStack(car.getProperties().getCurrentCase().getStack());
		
		GlStateManager.pushMatrix();
		{
			GlStateManager.enableAlpha();
	        GlStateManager.disableCull();
	        
			GlStateManager.translate(x, y, z);

			float carPitch = car.prevCarPitch + (car.carPitch - car.prevCarPitch) * partialTicks;
			
			if(car.onGround)
			{
				if(car.getAngledSurface() != null)
				{
					switch(car.getAngledFacing())
					{
					case EAST:
						GlStateManager.rotate(-carPitch, 0, 0, 1);
						break;
					case NORTH:
						GlStateManager.rotate(-carPitch, 1, 0, 0);
						break;
					case SOUTH:
						GlStateManager.rotate(carPitch, 1, 0, 0);
						break;
					case WEST:
						GlStateManager.rotate(carPitch, 0, 0, 1);
						break;
					default:
						break;
					}
					GlStateManager.translate(0, -0.12, 0);
				}
			}
			
			GlStateManager.rotate(180F - entityYaw, 0, 1, 0);
			
			if(!car.onGround)
			{
				GlStateManager.rotate(-carPitch, 1, 0, 0);
			}

			GlStateManager.translate(0, 0, -0.4);
			
			GlStateManager.rotate(offsetRotationYaw + currentOffsetRotationYaw, 0, 1, 0);
			
			double wheelScale = car.getProperties().getWheelSize();
			GlStateManager.translate(0, (wheelScale - 1.0) * 0.125, 0);

			GlStateManager.pushMatrix();
			{
				Minecraft.getMinecraft().getRenderManager().doRenderEntity(currentCase, 0, 0, 0, 0F, 0F, true);
				Minecraft.getMinecraft().getRenderManager().doRenderEntity(BASE, 0, 0, 0, 0F, 0F, true);
			}
			GlStateManager.popMatrix();
			
			float wheelSpin = car.prevWheelRotation + (car.wheelRotation - car.prevWheelRotation) * partialTicks;
			
			GlStateManager.pushMatrix();
			{
				GlStateManager.translate(0.3, 0.13125, -0.4075);
				
				GlStateManager.pushMatrix();
				{
					GlStateManager.rotate(car.getWheelAngle(), 0, 1, 0);
					if(car.isMoving())
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
					GlStateManager.rotate(car.getWheelAngle(), 0, 1, 0);
					if(car.isMoving())
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
					if(car.isMoving())
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
					if(car.isMoving())
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
			
			GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
	        GlStateManager.enableTexture2D();
	        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
	        GlStateManager.enableCull();
		}
		GlStateManager.popMatrix();
		super.doRender(car, x, y, z, entityYaw, partialTicks);
	}
}
