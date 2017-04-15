package com.mrcrayfish.rccar.gui;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.mrcrayfish.rccar.client.render.RenderCar;
import com.mrcrayfish.rccar.entity.EntityCar;
import com.mrcrayfish.rccar.event.ModEvents;
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
import net.minecraft.client.resources.Language;
import net.minecraft.client.resources.LanguageManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.LanguageMap;

public class GuiCarSettings extends GuiScreen implements GuiResponder, FormatHelper
{
	public static final int ID = 1;
	
	private static final ResourceLocation CAR_GUI_TEXTURE = new ResourceLocation("crccm:textures/gui/car_settings.png");
	
	private static final int X_SIZE = 126;
	private static final int Y_SIZE = 112;
	
	private int entityId;
	private EntityCar car;
	private Render<EntityCar> render;
	
	private int rotation;
	private boolean dirty = false;
	
	private int mouseClickedX;
	private boolean canDrag = false;
	private boolean dragging = false;
	
	private List<GuiButton> components;
	private Page activePage = null;
	private GuiButton selectedButton = null;
	private GuiAdvancedButton btnExit;
	
	/* Main Page */
	private Page pageMain;
	private GuiAdvancedButton btnCasePrev;
	private GuiAdvancedButton btnCaseNext;
	private GuiSlider wheelSlider;
	private GuiAdvancedButton btnUpgrades;
	private GuiAdvancedButton btnAttachments;
	
	/* Attachments Page */
	private Page pageAttachments;
	private GuiAdvancedButton btnBack;
	
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
		
		int startX = (this.width - this.X_SIZE) / 2;
		int startY = (this.height - this.Y_SIZE) / 2;
		
		this.btnExit = new GuiAdvancedButton(startX + 171, startY - 15, 20, 16, "X");
		this.btnExit.setListener(() -> {
			this.mc.displayGuiScreen((GuiScreen)null);
            if (this.mc.currentScreen == null)
            {
                this.mc.setIngameFocus();
            }
            return true;
		});
		
		this.pageMain = new Page();
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
		
		this.pageAttachments = new Page();
		
		this.btnBack = new GuiAdvancedButton(startX + 75, startY - 15, 20, 16, "<");
		this.btnBack.setListener(() -> 
		{
			setActivePage(this.pageMain);
			return true;
		});
		this.pageAttachments.add(btnBack);
		
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
		//this.drawDefaultBackground();
		
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		int startX = (this.width - this.X_SIZE) / 2;
		int startY = (this.height - this.Y_SIZE) / 2;
        this.mc.getTextureManager().bindTexture(CAR_GUI_TEXTURE);
		GlStateManager.enableAlpha();
		GlStateManager.enableBlend();
        this.drawTexturedModalRect(startX + 70, startY, 0, 0, this.X_SIZE, this.Y_SIZE);

        this.drawCenteredString(fontRendererObj, "Workshop", startX + 132, startY - 12, Color.WHITE.getRGB());
        
        this.btnExit.drawButton(this.mc, mouseX, mouseY);
        
        for(int i = 0; i < components.size(); i++)
		{
			GuiButton button = components.get(i);
			button.drawButton(this.mc, mouseX, mouseY);
		}

        if(this.activePage != null)
        {
        	ExtendedRender render = this.activePage.getExtendedRender();
        	if(render != null)
        	{
        		render.render(startX, startY);
        	}
        }

		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException 
	{
		int startX = (this.width - this.X_SIZE) / 2;
		int startY = (this.height - this.Y_SIZE) / 2;

		if (mouseButton == 0)
        {
			if(!GuiHelper.isMouseInside(mouseX, mouseY, startX + 70, startY, 126, 112))
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
		int startX = (this.width - this.X_SIZE) / 2;
		int startY = (this.height - this.Y_SIZE) / 2;
		
		if((!GuiHelper.isMouseInside(mouseX, mouseY, startX + 130, startY, 126, 112) || dragging) && canDrag)
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
	
	public static class Page
	{
		private List<GuiButton> components = new ArrayList<GuiButton>();
		
		private ExtendedRender render;
		
		public void add(GuiButton component)
		{
			this.components.add(component);
		}

		public void show(List<GuiButton> buttonList)
		{
			for(GuiButton component : components)
			{
				buttonList.add(component);
			}
		}
		
		public void hide(List<GuiButton> buttonList)
		{
			for(GuiButton component : components)
			{
				buttonList.remove(component);
			}
		}
		
		public ExtendedRender getExtendedRender() 
		{
			return render;
		}
		
		public void setExtendedRender(ExtendedRender render) 
		{
			this.render = render;
		}
	}
	
	public static class GuiAdvancedButton extends GuiButton
	{
		private ClickListener listener;
		
		public GuiAdvancedButton(int x, int y, String buttonText) 
		{
			super(0, x, y, buttonText);
		}
		
		public GuiAdvancedButton(int x, int y, int widthIn, int heightIn, String buttonText) 
		{
			super(0, x, y, widthIn, heightIn, buttonText);
		}
		
		@Override
		public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) 
		{
			if(listener != null && super.mousePressed(mc, mouseX, mouseY))
			{
				return listener.handle();
			}
			return false;
		}
		
		public void setListener(ClickListener listener) 
		{
			this.listener = listener;
		}
	}
	
	public interface ClickListener 
	{
		public boolean handle();
	}
	
	public interface ExtendedRender
	{
		public void render(int startX, int startY);
	}
}
