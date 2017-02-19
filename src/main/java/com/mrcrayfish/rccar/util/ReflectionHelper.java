package com.mrcrayfish.rccar.util;

import java.lang.reflect.Field;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.client.settings.KeyBinding;

public class ReflectionHelper 
{
	public static void removeKeybinding(KeyBinding binding)
	{
		try 
		{
			Field field = KeyBinding.class.getField("KEYBIND_ARRAY");
			if(!field.isAccessible()) field.setAccessible(true);
			List<KeyBinding> KEYBIND_ARRAY = (List<KeyBinding>) field.get(null);
			KEYBIND_ARRAY.remove(binding);
		} 
		catch (NoSuchFieldException e) 
		{
			e.printStackTrace();
		} 
		catch (SecurityException e) 
		{
			e.printStackTrace();
		} 
		catch (IllegalArgumentException e) 
		{
			e.printStackTrace();
		} catch (IllegalAccessException e) 
		{
			e.printStackTrace();
		}
	}
}
