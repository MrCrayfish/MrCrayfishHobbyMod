package com.mrcrayfish.rccar.gui;

import org.lwjgl.opengl.GL11;

import com.mrcrayfish.rccar.entity.EntityCar;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class GuiCarSettings extends GuiScreen
{
	public static final int ID = 1;
	
	private static final ResourceLocation CAR_GUI_TEXTURE = new ResourceLocation("crccm:textures/gui/car_settings.png");
	
	private static final int X_SIZE = 256;
	private static final int Y_SIZE = 112;
	
	private int entityId;
	private EntityCar car;
	private Render<EntityCar> render;
	
	private int rotation;
	
	public GuiCarSettings(int entityId) 
	{
		this.entityId = entityId;
	}

	@Override
	public void initGui() 
	{
		this.car = (EntityCar) mc.world.getEntityByID(entityId);
		this.render = mc.getRenderManager().getEntityRenderObject(car);
	}
	
	@Override
	public void updateScreen() 
	{
		super.updateScreen();
		rotation++;
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) 
	{
		this.drawDefaultBackground();
		
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		int var5 = (this.width - this.X_SIZE) / 2;
		int var6 = (this.height - this.Y_SIZE) / 2;
        this.mc.getTextureManager().bindTexture(CAR_GUI_TEXTURE);
        
		GlStateManager.enableAlpha();
		GlStateManager.enableBlend();
        this.drawTexturedModalRect(var5, var6, 0, 0, this.X_SIZE, this.Y_SIZE);
		
		GlStateManager.pushMatrix();
		{
			double scale = 70;
			GlStateManager.translate(var5 + 60, this.height / 2 + 20, 100);
			GlStateManager.scale(-scale, -scale, -scale);
			GlStateManager.rotate(180F, 0, 1, 0);
			GlStateManager.rotate(15F, 1, 0, 0);
			GlStateManager.rotate(rotation + partialTicks, 0, 1, 0);
			GlStateManager.translate(0, 0, -0.4);
			render.doRender(car, 0, 0, 0, 0F, 0F);
		}
		GlStateManager.popMatrix();
		
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
}
