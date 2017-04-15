package com.mrcrayfish.rccar.object;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class Case 
{
	public String id;
	
	public AttachmentProperties left_attachment;
	public AttachmentProperties right_attachment;
	public AttachmentProperties top_attachment;
	
	public transient Item item;
	public transient int ordinal;
	
	public int ordinal() 
	{
		return ordinal;
	}
	
	public static class AttachmentProperties 
	{
		public boolean enabled;
		public float x;
		public float y;
		public float z;
	}
}
