package com.mrcrayfish.rccar.client.model.block;

import java.util.Collection;
import java.util.Collections;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableSet;
import com.mrcrayfish.rccar.client.model.block.baked.BakedRampSlope;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;

public class ModelRamp implements IModel
{
	@Override
	public Collection<ResourceLocation> getDependencies() 
	{
		return Collections.emptySet();
	}

	@Override
	public Collection<ResourceLocation> getTextures() 
	{
		return ImmutableSet.of(new ResourceLocation("minecraft", "blocks/hardened_clay"), new ResourceLocation("minecraft", "blocks/stone_slab_top"), new ResourceLocation("minecraft", "blocks/anvil_base"));
	}

	@Override
	public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) 
	{
		return new BakedRampSlope(format, bakedTextureGetter);
	}

	@Override
	public IModelState getDefaultState() 
	{
		return TRSRTransformation.identity();
	}
}
