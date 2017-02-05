package com.mrcrayfish.rccar.render;

import org.lwjgl.opengl.GL11;

import com.mrcrayfish.rccar.entity.EntityCar;
import com.mrcrayfish.rccar.init.ModItems;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class RenderCar extends Render<EntityCar>
{
	public static final EntityItem CAR = new EntityItem(Minecraft.getMinecraft().world, 0, 0, 0, new ItemStack(ModItems.car));
	public static final EntityItem WHEEL = new EntityItem(Minecraft.getMinecraft().world, 0, 0, 0, new ItemStack(ModItems.wheel));
	
	static
	{
		CAR.hoverStart = 0F;
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
	public void doRender(EntityCar entity, double x, double y, double z, float entityYaw, float partialTicks) 
	{
		GlStateManager.pushMatrix();
		{
			GlStateManager.translate(x, y, z);
			GlStateManager.rotate(180F - entityYaw, 0, 1, 0);
			Minecraft.getMinecraft().getRenderManager().doRenderEntity(CAR, 0, 0, 0, 0f, 0f, true);
			
			GlStateManager.pushMatrix();
			{
				GlStateManager.translate(0.3, 0.13125, -0.4075);
				
				GlStateManager.pushMatrix();
				{
					if(entity.isMoving())
					{
						GlStateManager.rotate(-entity.ticksExisted * 20F, 1, 0, 0);
					}
					GlStateManager.translate(0, -0.5375, 0.0);
					Minecraft.getMinecraft().getRenderManager().doRenderEntity(WHEEL, 0, 0, 0, 0f, 0f, true);
				}
				GlStateManager.popMatrix();
				
				GlStateManager.translate(-0.6, 0, 0);
				
				GlStateManager.pushMatrix();
				{
					if(entity.isMoving())
					{
						GlStateManager.rotate(-entity.ticksExisted * 20F, 1, 0, 0);
					}
					GlStateManager.translate(0, -0.5375, 0.0);
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
						GlStateManager.rotate(-entity.ticksExisted * 20F, 1, 0, 0);
					}
					GlStateManager.translate(0, -0.5375, 0.0);
					Minecraft.getMinecraft().getRenderManager().doRenderEntity(WHEEL, 0, 0, 0, 0f, 0f, true);
				}
				GlStateManager.popMatrix();
				
				GlStateManager.translate(-0.6, 0, 0);
				
				GlStateManager.pushMatrix();
				{
					if(entity.isMoving())
					{
						GlStateManager.rotate(-entity.ticksExisted * 20F, 1, 0, 0);
					}
					GlStateManager.translate(0, -0.5375, 0.0);
					Minecraft.getMinecraft().getRenderManager().doRenderEntity(WHEEL, 0, 0, 0, 0f, 0f, true);
				}
				GlStateManager.popMatrix();
			}
			GlStateManager.popMatrix();
		}
		GlStateManager.popMatrix();
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}
}
