package com.mrcrayfish.rccar.gui;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.mrcrayfish.rccar.client.render.RenderCar;
import com.mrcrayfish.rccar.entity.EntityCar;
import com.mrcrayfish.rccar.event.ModEvents;
import com.mrcrayfish.rccar.gui.component.ModuleSelector;
import com.mrcrayfish.rccar.gui.component.Page;
import com.mrcrayfish.rccar.gui.interfaces.ExtendedRender;
import com.mrcrayfish.rccar.init.ModCases;
import com.mrcrayfish.rccar.network.PacketHandler;
import com.mrcrayfish.rccar.network.message.MessageUpdateProperties;
import com.mrcrayfish.rccar.object.Case;
import com.mrcrayfish.rccar.util.GuiHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiPageButtonList.GuiResponder;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlider;
import net.minecraft.client.gui.GuiSlider.FormatHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class GuiCarSettings extends GuiScreen implements GuiResponder, FormatHelper
{
	public static final int ID = 1;

	private static final ResourceLocation CAR_GUI_TEXTURE = new ResourceLocation("crccm:textures/gui/car_settings.png");
	private static final int GUI_WIDTH = 126;
	private static final int GUI_HEIGHT = 112;

	private int entityId;
	private EntityCar car;
	private Render<EntityCar> render;
	
	private int rotation;
	private boolean dirty = false;
	private ModuleSelector selector = null;
	
	private int mouseClickedX;
	private boolean canDrag = false;
	private boolean dragging = false;
	
	private List<GuiButton> components;
	private Page activePage = null;
	private GuiButton selectedButton = null;
	
	/* Universal Components */
	private GuiAdvancedButton btnExit;
	private GuiAdvancedButton btnBack;
	
	/* Main Page */
	private Page pageMain;
	private GuiAdvancedButton btnCasePrev;
	private GuiAdvancedButton btnCaseNext;
	private GuiSlider wheelSlider;
	private GuiAdvancedButton btnUpgrades;
	private GuiAdvancedButton btnAttachments;
	
	/* Attachments Page */
	private Page pageAttachments;
	private GuiAdvancedButton btnAttachmentHood;
	
	public GuiCarSettings(int entityId) 
	{
		this.entityId = entityId;
	}

	@Override
	public void initGui() 
	{
		this.components = new ArrayList<>();
		this.car = (EntityCar) mc.world.getEntityByID(entityId);
		this.render = mc.getRenderManager().getEntityRenderObject(car);
		
		int startX = (this.width - this.GUI_WIDTH) / 2;
		int startY = (this.height - this.GUI_HEIGHT) / 2;
		
		this.btnBack = new GuiAdvancedButton(startX + 75, startY - 15, 20, 16, "<");
		this.btnBack.setListener(() -> 
		{
			setActivePage(this.pageMain);
			return true;
		});
		
		this.btnExit = new GuiAdvancedButton(startX + 171, startY - 15, 20, 16, "X");
		this.btnExit.setListener(() -> 
		{
			this.mc.displayGuiScreen((GuiScreen)null);
            if (this.mc.currentScreen == null)
            {
                this.mc.setIngameFocus();
            }
            return true;
		});
		
		this.pageMain = new Page("Workshop");
		this.pageMain.setExtendedRender((s, e) -> 
		{
			this.drawCenteredString(fontRendererObj, I18n.format(String.format("case.%s.name", this.car.getProperties().getCurrentCase().id)), startX + 132, startY + 10, Color.WHITE.getRGB());
		});
		
		this.btnCasePrev = new GuiAdvancedButton(startX + 75, startY + 5, 20, 20, "<");
		this.btnCasePrev.enabled = false;
		this.btnCasePrev.setListener(() -> 
		{
			Case currentCase = this.car.getProperties().getCurrentCase();
			if(currentCase.ordinal() > 0)
			{
				this.car.getProperties().setCurrentCase(ModCases.CASES.get(currentCase.ordinal() - 1));
			}
			updateButtons();
			return true;
		});
		this.pageMain.add(this.btnCasePrev);

		this.btnCaseNext = new GuiAdvancedButton(startX + 171, startY + 5, 20, 20, ">");
		this.btnCaseNext.setListener(() -> 
		{
			Case currentCase = this.car.getProperties().getCurrentCase();
			if(currentCase.ordinal() < ModCases.length() - 1)
			{
				this.car.getProperties().setCurrentCase(ModCases.CASES.get(currentCase.ordinal() + 1));
			}
			updateButtons();
			return true;
		});
		this.pageMain.add(this.btnCaseNext);
		
		this.wheelSlider = new GuiSlider(this, 3, startX + 75, startY + 28, "Wheel Size", 1.0F, 3.0F, car.getProperties().getWheelSize(), this);
		this.wheelSlider.width = 116;
		this.pageMain.add(this.wheelSlider);
		
		this.btnUpgrades = new GuiAdvancedButton(startX + 75, startY + 51, 116, 20, "Upgrades");
		this.pageMain.add(this.btnUpgrades);
		
		this.btnAttachments = new GuiAdvancedButton(startX + 75, startY + 74, 116, 20, "Attachments");
		this.btnAttachments.setListener(() -> 
		{
			setActivePage(this.pageAttachments);
			return true;
		});
		this.pageMain.add(this.btnAttachments);
		
		setActivePage(this.pageMain);
		
		this.pageAttachments = new Page("Attachments");
		
		this.btnAttachmentHood = new GuiAdvancedButton(startX + 75, startY + 5, 90, 20, "Hood");
		this.btnAttachmentHood.setListener(() -> {
			this.selector = new ModuleSelector(this);
			return true;
		});
		this.pageAttachments.add(this.btnAttachmentHood);

		this.pageAttachments.add(this.btnBack);

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
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		int startX = (this.width - this.GUI_WIDTH) / 2;
		int startY = (this.height - this.GUI_HEIGHT) / 2;
        this.mc.getTextureManager().bindTexture(CAR_GUI_TEXTURE);
		GlStateManager.enableAlpha();
		GlStateManager.enableBlend();
        this.drawTexturedModalRect(startX + 70, startY, 0, 0, this.GUI_WIDTH, this.GUI_HEIGHT);

        this.drawCenteredString(fontRendererObj, this.activePage.getTitle(), startX + 132, startY - 12, Color.WHITE.getRGB());
        
        this.btnExit.drawButton(this.mc, mouseX, mouseY);
        
        for(int i = 0; i < components.size(); i++)
		{
			GuiButton button = components.get(i);
			button.drawButton(this.mc, mouseX, mouseY);
		}

        ExtendedRender render = this.activePage.getExtendedRender();
    	if(render != null)
    	{
    		render.render(startX, startY);
    	}

        if(selector != null)
        {
        	selector.render(mouseX, mouseY);
        }
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException 
	{
		int startX = (this.width - this.GUI_WIDTH) / 2;
		int startY = (this.height - this.GUI_HEIGHT) / 2;

		if (mouseButton == 0)
        {
			if(selector != null)
			{
				selector.onClick(mouseX, mouseY);
				return;
			}
			
			if(!GuiHelper.isMouseInside(mouseX, mouseY, startX + 70, startY, startX + 70 + this.GUI_WIDTH, startY + this.GUI_HEIGHT))
			{
				canDrag = true;
				mouseClickedX = mouseX;
			}
			
			if(this.btnExit.mousePressed(this.mc, mouseX, mouseY))
			{
				this.btnExit.playPressSound(mc.getSoundHandler());
				this.selectedButton = this.btnExit;
			}
			
			for(int i = 0; i < components.size(); i++)
			{
				GuiButton button = components.get(i);
				if(button.mousePressed(this.mc, mouseX, mouseY))
				{
					button.playPressSound(mc.getSoundHandler());
					this.selectedButton = button;
				}
			}
        }
	}

	@Override
	protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) 
	{
		int startX = (this.width - this.GUI_WIDTH) / 2;
		int startY = (this.height - this.GUI_HEIGHT) / 2;
		
		if((!GuiHelper.isMouseInside(mouseX, mouseY, startX + 70, startY, startX + 70 + this.GUI_WIDTH, startY + this.GUI_HEIGHT) || dragging) && canDrag)
		{
			int deltaMouseX = mouseClickedX - mouseX;
			RenderCar.currentOffsetRotationYaw = -deltaMouseX;
			dragging = true;
		}
	}
	
	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) 
	{
		if (this.selectedButton != null && state == 0)
        {
            this.selectedButton.mouseReleased(mouseX, mouseY);
            this.selectedButton = null;
        }
		
		RenderCar.offsetRotationYaw += RenderCar.currentOffsetRotationYaw;
		RenderCar.currentOffsetRotationYaw = 0F;
		dragging = false;
		canDrag = false;
	}
	
	public void updateButtons()
	{
		Case currentCase = this.car.getProperties().getCurrentCase();
		if(currentCase.ordinal() + 1 == ModCases.length())
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
		RenderCar.offsetRotationYaw = 0F;
		ModEvents.inSettingsGui = false;
		ModEvents.setView(null);
		Minecraft.getMinecraft().gameSettings.thirdPersonView = 0;
	}
	
	@Override
	public boolean doesGuiPauseGame() 
	{
		return false;
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
	
	public void setActivePage(Page page) 
	{
		if(this.activePage != null) 
		{
			this.activePage.hide(components);
		}
		page.show(components);
		this.activePage = page;
	}
	
	public void setSelectedButton(GuiButton selectedButton) 
	{
		this.selectedButton = selectedButton;
	}

	public void closeInventorySelector()
	{
		this.selector = null;
	}
}
