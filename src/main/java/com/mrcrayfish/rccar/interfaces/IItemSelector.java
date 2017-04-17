package com.mrcrayfish.rccar.interfaces;

import com.google.common.base.Predicate;

import net.minecraft.item.Item;

public interface IItemSelector 
{
	public void openItemSelector(Predicate<Item> predicate);
	
	public void closeItemSelector();
}
