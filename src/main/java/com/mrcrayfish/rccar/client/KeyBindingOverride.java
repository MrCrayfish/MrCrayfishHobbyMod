package com.mrcrayfish.rccar.client;

import com.mrcrayfish.rccar.event.ModEvents;
import com.mrcrayfish.rccar.util.ReflectionHelper;

import net.minecraft.client.settings.KeyBinding;

public class KeyBindingOverride extends KeyBinding
{
	private KeyBinding originalKeyBinding;
	
	public KeyBindingOverride(KeyBinding originalKeyBinding) 
	{
		super(originalKeyBinding.getKeyDescription(), originalKeyBinding.getKeyCode(), originalKeyBinding.getKeyCategory());
		ReflectionHelper.removeKeybinding(originalKeyBinding);
		KeyBinding.resetKeyBindingArrayAndHash();
		this.originalKeyBinding = originalKeyBinding;
	}
	
	@Override
	public boolean isPressed() 
	{
		if(ModEvents.renderCarView) return false;
		return this.originalKeyBinding.isPressed();
	}
	
	@Override
	public boolean isKeyDown() 
	{
		if(ModEvents.renderCarView) return false;
		return this.originalKeyBinding.isKeyDown();
	}
	
	public boolean isPressedBypass()
	{
		return this.originalKeyBinding.isPressed();
	}
	
	public boolean isKeyDownBypass()
	{
		return this.originalKeyBinding.isKeyDown();
	}
}
