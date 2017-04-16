package com.mrcrayfish.rccar.gui.component;

import java.awt.Color;

import com.mrcrayfish.rccar.gui.GuiAdvancedButton;
import com.mrcrayfish.rccar.gui.GuiCarSettings;
import com.mrcrayfish.rccar.util.GuiHelper;
import com.mrcrayfish.rccar.util.RenderUtil;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class InventorySelector
{
	private static final ResourceLocation INVENTORY_TEXTURE = new ResourceLocation("crccm:textures/gui/inventory_select.png");
	private static final int X_SIZE = 176;
	private static final int Y_SIZE = 100;
	
	private static final int HOVER_COLOR = new Color(1F, 1F, 1F, 0.25F).getRGB();
	private static final int SELECTED_COLOR = new Color(1F, 1F, 0F, 0.15F).getRGB();
	private static final int INVALID_COLOR = new Color(1F, 0F, 0F, 0.15F).getRGB();
	
	private GuiCarSettings settings;
	
	private GuiAdvancedButton btnAdd;
	private GuiAdvancedButton btnClose;
	
	private int selected = -1;
	
	public InventorySelector(GuiCarSettings settings) 
	{
		this.settings = settings;
		this.init();
	}
	
	private void init()
	{
		int inventoryStartX = (settings.width - InventorySelector.getWidth()) / 2;
		int inventoryStartY = (settings.height - InventorySelector.getHeight()) / 2;
		
		this.btnAdd = new GuiAdvancedButton(inventoryStartX + 145, inventoryStartY - 15, 24, 16, "Add");
		this.btnAdd.enabled = false;
		
		this.btnClose = new GuiAdvancedButton(inventoryStartX + 122, inventoryStartY - 15, 20, 16, "X");
		this.btnClose.setListener(() -> {
			this.settings.closeInventorySelector();
			return true;
		});
	}
	
	public void render(int mouseX, int mouseY)
	{
		settings.drawDefaultBackground();
    	
    	GlStateManager.color(1.0F, 1.0F, 1.0F);
    	int startX = (settings.width - this.X_SIZE) / 2;
 		int startY = (settings.height - this.Y_SIZE) / 2;
 		settings.mc.getTextureManager().bindTexture(INVENTORY_TEXTURE);
 		GlStateManager.enableAlpha();
 		GlStateManager.enableBlend();
 		settings.drawTexturedModalRect(startX, startY, 0, 0, this.X_SIZE, this.Y_SIZE);
 		
 		settings.drawString(settings.mc.fontRendererObj, "Pick a module to attach", startX + 7, startY + 5, Color.WHITE.getRGB());
 		
 		this.btnAdd.drawButton(settings.mc, mouseX, mouseY);
 		this.btnClose.drawButton(settings.mc, mouseX, mouseY);
 		
 		InventoryPlayer inventory = settings.mc.player.inventory;
		for(int i = 9; i < inventory.getSizeInventory() - 5; i++) //The 4 is the inventory slots
		{
			int offsetX = (i % 9) * 18 + 7;
			int offsetY = (i / 9) * 18 - 1;
			
			ItemStack stack = inventory.getStackInSlot(i);
			if(!stack.isEmpty())
			{
				boolean isModule = stack.getItem() instanceof ItemEgg;
				
				if(isModule && GuiHelper.isMouseInside(mouseX, mouseY, startX + offsetX, startY + offsetY, startX + offsetX + 17, startY + offsetY + 17))
				{
					settings.drawRect(startX + offsetX, startY + offsetY, startX + offsetX + 18, startY + offsetY + 18, HOVER_COLOR);
				}
				
				if(stack != null)
				{
					RenderUtil.renderItem(startX + offsetX + 1, startY + offsetY + 1, stack, true);
				}
				
				if(!isModule)
				{
					settings.drawRect(startX + offsetX, startY + offsetY, startX + offsetX + 18, startY + offsetY + 18, INVALID_COLOR);
				}
			}
		}
		
		for(int i = 0; i < 9; i++)
		{
			int offsetX = (i % 9) * 18 + 7;
			int offsetY = (i / 9) * 18 + 75;
			
			ItemStack stack = inventory.getStackInSlot(i);
			if(!stack.isEmpty())
			{
				boolean isModule = stack.getItem() instanceof ItemEgg;
				
				if(isModule && GuiHelper.isMouseInside(mouseX, mouseY, startX + offsetX, startY + offsetY, startX + offsetX + 17, startY + offsetY + 17))
				{
					settings.drawRect(startX + offsetX, startY + offsetY, startX + offsetX + 18, startY + offsetY + 18, HOVER_COLOR);
				}
				
				if(stack != null)
				{
					RenderUtil.renderItem(startX + offsetX + 1, startY + offsetY + 1, stack, true);
				}
				
				if(!isModule)
				{
					settings.drawRect(startX + offsetX, startY + offsetY, startX + offsetX + 18, startY + offsetY + 18, INVALID_COLOR);
				}
			}
		}
		
		if(this.selected != -1)
		{
			int selectedX = (this.selected % 9) * 18 + 7;
			int selectedY = (this.selected / 9) * 18 - 1;
			
			if(selectedX < 9)
			{
				selectedY += 4;
			}
			
			settings.drawRect(startX + selectedX, startY + selectedY, startX + selectedX + 18, startY + selectedY + 18, SELECTED_COLOR);
		}
		
	}
	
	public void onClick(int mouseX, int mouseY)
	{
		int startX = (settings.width - this.X_SIZE) / 2;
		int startY = (settings.height - this.Y_SIZE) / 2;
		
		if(this.btnAdd.mousePressed(settings.mc, mouseX, mouseY))
		{
			this.btnAdd.playPressSound(settings.mc.getSoundHandler());
			settings.setSelectedButton(this.btnAdd);
			return;
		}
		
		if(this.btnClose.mousePressed(settings.mc, mouseX, mouseY))
		{
			this.btnClose.playPressSound(settings.mc.getSoundHandler());
			settings.setSelectedButton(this.btnClose);
			return;
		}
		
		InventoryPlayer inventory = settings.mc.player.inventory;
		
		for(int i = 9; i < inventory.getSizeInventory() - 5; i++) //The 4 is the inventory slots
		{
			int offsetX = (i % 9) * 18 + 7;
			int offsetY = (i / 9) * 18 - 1;
			
			ItemStack stack = inventory.getStackInSlot(i);
			if(!stack.isEmpty())
			{
				boolean isModule = stack.getItem() instanceof ItemEgg;
				
				if(isModule && GuiHelper.isMouseInside(mouseX, mouseY, startX + offsetX, startY + offsetY, startX + offsetX + 17, startY + offsetY + 17))
				{
					if(this.selected == i) {
						this.selected = -1;
						this.btnAdd.enabled = false;
						return;
					}
					this.selected = i;
					this.btnAdd.enabled = true;
				}
			}
		}
		
		for(int i = 0; i < 9; i++)
		{
			int offsetX = (i % 9) * 18 + 7;
			int offsetY = (i / 9) * 18 + 75;
			
			ItemStack stack = inventory.getStackInSlot(i);
			if(!stack.isEmpty())
			{
				boolean isModule = stack.getItem() instanceof ItemEgg;
				
				if(isModule && GuiHelper.isMouseInside(mouseX, mouseY, startX + offsetX, startY + offsetY, startX + offsetX + 17, startY + offsetY + 17))
				{
					if(this.selected == i) {
						this.selected = -1;
						this.btnAdd.enabled = false;
						return;
					}
					this.selected = i;
					this.btnAdd.enabled = true;
				}
			}
		}
	}
	
	public static int getWidth() 
	{
		return X_SIZE;
	}
	
	public static int getHeight() 
	{
		return Y_SIZE;
	}
}
