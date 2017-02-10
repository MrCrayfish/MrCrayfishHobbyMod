package com.mrcrayfish.rccar.block.attribute;

import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.util.EnumFacing;

public interface Angled 
{
	float getAngle();
	
	PropertyDirection getDirectionProp();
}
