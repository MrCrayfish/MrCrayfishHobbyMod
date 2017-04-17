package com.mrcrayfish.rccar.gui.component;

import java.awt.Color;
import java.util.List;

import com.google.common.base.Predicate;
import com.mrcrayfish.rccar.gui.GuiAdvancedButton;
import com.mrcrayfish.rccar.interfaces.IAttachment;
import com.mrcrayfish.rccar.interfaces.IItemSelector;
import com.mrcrayfish.rccar.util.GuiHelper;
import com.mrcrayfish.rccar.util.RenderUtil;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ItemSelector
{
	public static final Predicate<Item> ATTACHMENT_PREDICATE = new Predicate<Item>() {
		@Override
		public boolean apply(Item input)
		{
			return input instanceof IAttachment;
		}
	};
	
	private static final ResourceLocation INVENTORY_TEXTURE = new ResourceLocation("crccm:textures/gui/inventory_select.png");
	private static final int X_SIZE = 176;
	private static final int Y_SIZE = 100;
	
	private static final int HOVER_COLOR = new Color(1F, 1F, 1F, 0.25F).getRGB();
	private static final int SELECTED_COLOR = new Color(1F, 1F, 0F, 0.15F).getRGB();
	private static final int INVALID_COLOR = new Color(1F, 0F, 0F, 0.15F).getRGB();
	
	private GuiScreen gui;
	private IItemSelector controller;
	
	private GuiAdvancedButton btnAdd;
	private GuiAdvancedButton btnClose;
	
	private Predicate<Item> predicate;
	
	private int selected = -1;
	
	public ItemSelector(GuiScreen gui, IItemSelector controller, Predicate<Item> predicate) 
	{
		this.gui = gui;
		this.controller = controller;
		this.predicate = predicate;
	}
	
	public void init(List<GuiButton> buttonList)
	{
		int inventoryStartX = (gui.width - ItemSelector.getWidth()) / 2;
		int inventoryStartY = (gui.height - ItemSelector.getHeight()) / 2;
		
		this.btnAdd = new GuiAdvancedButton(inventoryStartX + 145, inventoryStartY - 15, 24, 16, "Add");
		this.btnAdd.enabled = false;
		
		this.btnClose = new GuiAdvancedButton(inventoryStartX + 122, inventoryStartY - 15, 20, 16, "X");
		this.btnClose.setListener(() -> {
			this.controller.closeItemSelector();
			return true;
		});
		
		buttonList.add(this.btnAdd);
		buttonList.add(this.btnClose);
	}
	
	public void close(List<GuiButton> buttonList)
	{
		buttonList.remove(this.btnAdd);
		buttonList.remove(this.btnClose);
	}
	
	public void render(int mouseX, int mouseY)
	{
		gui.drawDefaultBackground();
    	
    	GlStateManager.color(1.0F, 1.0F, 1.0F);
    	int startX = (gui.width - this.X_SIZE) / 2;
 		int startY = (gui.height - this.Y_SIZE) / 2;
 		gui.mc.getTextureManager().bindTexture(INVENTORY_TEXTURE);
 		GlStateManager.enableAlpha();
 		GlStateManager.enableBlend();
 		gui.drawTexturedModalRect(startX, startY, 0, 0, this.X_SIZE, this.Y_SIZE);
 		
 		gui.drawString(gui.mc.fontRendererObj, "Pick a module to attach", startX + 7, startY + 5, Color.WHITE.getRGB());
 		
 		this.btnAdd.drawButton(gui.mc, mouseX, mouseY);
 		this.btnClose.drawButton(gui.mc, mouseX, mouseY);
 		
 		InventoryPlayer inventory = gui.mc.player.inventory;
		for(int i = 9; i < inventory.getSizeInventory() - 5; i++) //The 4 is the inventory slots
		{
			int offsetX = (i % 9) * 18 + 7;
			int offsetY = (i / 9) * 18 - 1;
			
			ItemStack stack = inventory.getStackInSlot(i);
			if(!stack.isEmpty())
			{
				boolean match = predicate.apply(stack.getItem());
				
				if(match && GuiHelper.isMouseInside(mouseX, mouseY, startX + offsetX, startY + offsetY, startX + offsetX + 17, startY + offsetY + 17))
				{
					gui.drawRect(startX + offsetX, startY + offsetY, startX + offsetX + 18, startY + offsetY + 18, HOVER_COLOR);
				}
				
				if(stack != null)
				{
					RenderUtil.renderItem(startX + offsetX + 1, startY + offsetY + 1, stack, true);
				}
				
				if(!match)
				{
					gui.drawRect(startX + offsetX, startY + offsetY, startX + offsetX + 18, startY + offsetY + 18, INVALID_COLOR);
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
				boolean match = predicate.apply(stack.getItem());
				
				if(match && GuiHelper.isMouseInside(mouseX, mouseY, startX + offsetX, startY + offsetY, startX + offsetX + 17, startY + offsetY + 17))
				{
					gui.drawRect(startX + offsetX, startY + offsetY, startX + offsetX + 18, startY + offsetY + 18, HOVER_COLOR);
				}
				
				if(stack != null)
				{
					RenderUtil.renderItem(startX + offsetX + 1, startY + offsetY + 1, stack, true);
				}
				
				if(!match)
				{
					gui.drawRect(startX + offsetX, startY + offsetY, startX + offsetX + 18, startY + offsetY + 18, INVALID_COLOR);
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
			
			gui.drawRect(startX + selectedX, startY + selectedY, startX + selectedX + 18, startY + selectedY + 18, SELECTED_COLOR);
		}
		
	}
	
	public void onClick(int mouseX, int mouseY)
	{
		int startX = (gui.width - this.X_SIZE) / 2;
		int startY = (gui.height - this.Y_SIZE) / 2;
		
		InventoryPlayer inventory = gui.mc.player.inventory;
		
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
