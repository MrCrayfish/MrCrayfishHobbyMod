package com.mrcrayfish.rccar.gui;

import java.awt.Color;
import java.io.IOException;

import com.mrcrayfish.rccar.entity.EntityCar;
import com.mrcrayfish.rccar.entity.EntityCar.Case;
import com.mrcrayfish.rccar.network.PacketHandler;
import com.mrcrayfish.rccar.network.message.MessageUpdateProperties;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiPageButtonList.GuiResponder;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlider;
import net.minecraft.client.gui.GuiSlider.FormatHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.util.ResourceLocation;

public class GuiCarSettings extends GuiScreen implements GuiResponder, FormatHelper
{
	public static final int ID = 1;
	
	private static final ResourceLocation CAR_GUI_TEXTURE = new ResourceLocation("crccm:textures/gui/car_settings.png");
	
	private static final int X_SIZE = 256;
	private static final int Y_SIZE = 112;
	
	private int entityId;
	private EntityCar car;
	private Render<EntityCar> render;
	
	private int rotation;
	private boolean dirty = false;
	
	/* Components */
	private GuiButton btnCasePrev;
	private GuiButton btnCaseNext;
	private GuiSlider wheelSlider;
	
	public GuiCarSettings(int entityId) 
	{
		this.entityId = entityId;
	}

	@Override
	public void initGui() 
	{
		this.car = (EntityCar) mc.world.getEntityByID(entityId);
		this.render = mc.getRenderManager().getEntityRenderObject(car);
		
		int startX = (this.width - this.X_SIZE) / 2;
		int startY = (this.height - this.Y_SIZE) / 2;
		
		this.btnCasePrev = new GuiButton(1, startX + 135, startY + 5, 20, 20, "<");
		this.btnCasePrev.enabled = false;
		this.buttonList.add(this.btnCasePrev);
		
		this.btnCaseNext = new GuiButton(2, startX + 231, startY + 5, 20, 20, ">");
		this.buttonList.add(this.btnCaseNext);
		
		this.wheelSlider = new GuiSlider(this, 3, startX + 135, startY + 30, "Wheel Size", 1.0F, 3.0F, car.getProperties().getWheelSize(), this);
		this.wheelSlider.width = 116;
		this.buttonList.add(this.wheelSlider);
		
		updateButtons();
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
		int startX = (this.width - this.X_SIZE) / 2;
		int startY = (this.height - this.Y_SIZE) / 2;
        this.mc.getTextureManager().bindTexture(CAR_GUI_TEXTURE);
        
		GlStateManager.enableAlpha();
		GlStateManager.enableBlend();
        this.drawTexturedModalRect(startX, startY, 0, 0, this.X_SIZE, this.Y_SIZE);
		
		GlStateManager.pushMatrix();
		{
			double scale = 70;
			GlStateManager.translate(startX + 60, this.height / 2 + 30, 100);
			GlStateManager.scale(-scale, -scale, -scale);
			GlStateManager.rotate(180F, 0, 1, 0);
			GlStateManager.rotate(15F, 1, 0, 0);
			GlStateManager.rotate(rotation + partialTicks, 0, 1, 0);
			GlStateManager.translate(0, 0, -0.4);
			render.doRender(car, 0, 0, 0, 0F, 0F);
		}
		GlStateManager.popMatrix();
		
		this.drawCenteredString(fontRendererObj, this.car.getProperties().getCurrentCase().name(), startX + 193, startY + 10, Color.WHITE.getRGB());
		
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException 
	{
		if(button.id == btnCaseNext.id)
		{
			Case currentCase = this.car.getProperties().getCurrentCase();
			if(currentCase.ordinal() < Case.values().length - 1)
			{
				this.car.getProperties().setCurrentCase(Case.values()[currentCase.ordinal() + 1]);
			}
		}
		else if(button.id == btnCasePrev.id)
		{
			Case currentCase = this.car.getProperties().getCurrentCase();
			if(currentCase.ordinal() > 0)
			{
				this.car.getProperties().setCurrentCase(Case.values()[currentCase.ordinal() - 1]);
			}
		}
		updateButtons();
	}
	
	public void updateButtons()
	{
		Case currentCase = this.car.getProperties().getCurrentCase();
		if(currentCase.ordinal() + 1 == Case.values().length)
		{
			btnCaseNext.enabled = false;
		}
		else
		{
			btnCaseNext.enabled = true;
		}
		if(currentCase.ordinal() == 0)
		{
			btnCasePrev.enabled = false;
		}
		else
		{
			btnCasePrev.enabled = true;
		}
	}
	
	@Override
	public void onGuiClosed() 
	{
		if(dirty) PacketHandler.INSTANCE.sendToServer(new MessageUpdateProperties(car));
	}

	@Override
	public void setEntryValue(int id, boolean value) 
	{
		
	}

	@Override
	public void setEntryValue(int id, float value) 
	{
		this.dirty = true;
		if(id == this.wheelSlider.id)
		{
			this.car.getProperties().setWheelSize(value);
		}
	}

	@Override
	public void setEntryValue(int id, String value) 
	{
		
	}

	@Override
	public String getText(int id, String name, float value) 
	{
		switch(id)
		{
			case 3: return name + ": " + Float.toString(value).substring(0, 3) + "x";
		}
		return null;
	}
}
