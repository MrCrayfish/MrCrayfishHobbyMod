package com.mrcrayfish.rccar.block;

import java.util.List;

import com.mrcrayfish.rccar.MrCrayfishRCCarMod;
import com.mrcrayfish.rccar.block.attribute.Angled;
import com.mrcrayfish.rccar.client.model.block.properties.UnlistedBooleanProperty;
import com.mrcrayfish.rccar.client.model.block.properties.UnlistedTextureProperty;
import com.mrcrayfish.rccar.init.ModBlocks;
import com.mrcrayfish.rccar.util.RotationHelper;
import com.mrcrayfish.rccar.util.StateHelper;
import com.mrcrayfish.rccar.util.StateHelper.RelativeFacing;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockRamp extends BlockObject implements Angled
{
	public static final PropertyBool STACKED = PropertyBool.create("stacked");
	
	public static final UnlistedBooleanProperty METAL = new UnlistedBooleanProperty();

	private static final AxisAlignedBB BASE = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.0625, 1.0);
	private static final AxisAlignedBB BASE_STACKED = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.5625, 1.0);
	
	private static final AxisAlignedBB NORTH_ONE = RotationHelper.getBlockBounds(EnumFacing.NORTH, 0.125, 0.0625, 0.0, 1.0, 0.125, 1.0);
	private static final AxisAlignedBB NORTH_TWO = RotationHelper.getBlockBounds(EnumFacing.NORTH, 0.25, 0.125, 0.0, 1.0, 0.1875, 1.0);
	private static final AxisAlignedBB NORTH_THREE = RotationHelper.getBlockBounds(EnumFacing.NORTH, 0.375, 0.1875, 0.0, 1.0, 0.25, 1.0);
	private static final AxisAlignedBB NORTH_FOUR = RotationHelper.getBlockBounds(EnumFacing.NORTH, 0.5, 0.25, 0.0, 1.0, 0.3125, 1.0);
	private static final AxisAlignedBB NORTH_FIVE = RotationHelper.getBlockBounds(EnumFacing.NORTH, 0.625, 0.3125, 0.0, 1.0, 0.375, 1.0);
	private static final AxisAlignedBB NORTH_SIX = RotationHelper.getBlockBounds(EnumFacing.NORTH, 0.75, 0.375, 0.0, 1.0, 0.4375, 1.0);
	private static final AxisAlignedBB NORTH_SEVEN = RotationHelper.getBlockBounds(EnumFacing.NORTH, 0.875, 0.4375, 0.0, 1.0, 0.5, 1.0);
	
	private static final AxisAlignedBB EAST_ONE = RotationHelper.getBlockBounds(EnumFacing.EAST, 0.125, 0.0625, 0.0, 1.0, 0.125, 1.0);
	private static final AxisAlignedBB EAST_TWO = RotationHelper.getBlockBounds(EnumFacing.EAST, 0.25, 0.125, 0.0, 1.0, 0.1875, 1.0);
	private static final AxisAlignedBB EAST_THREE = RotationHelper.getBlockBounds(EnumFacing.EAST, 0.375, 0.1875, 0.0, 1.0, 0.25, 1.0);
	private static final AxisAlignedBB EAST_FOUR = RotationHelper.getBlockBounds(EnumFacing.EAST, 0.5, 0.25, 0.0, 1.0, 0.3125, 1.0);
	private static final AxisAlignedBB EAST_FIVE = RotationHelper.getBlockBounds(EnumFacing.EAST, 0.625, 0.3125, 0.0, 1.0, 0.375, 1.0);
	private static final AxisAlignedBB EAST_SIX = RotationHelper.getBlockBounds(EnumFacing.EAST, 0.75, 0.375, 0.0, 1.0, 0.4375, 1.0);
	private static final AxisAlignedBB EAST_SEVEN = RotationHelper.getBlockBounds(EnumFacing.EAST, 0.875, 0.4375, 0.0, 1.0, 0.5, 1.0);
	
	private static final AxisAlignedBB SOUTH_ONE = RotationHelper.getBlockBounds(EnumFacing.SOUTH, 0.125, 0.0625, 0.0, 1.0, 0.125, 1.0);
	private static final AxisAlignedBB SOUTH_TWO = RotationHelper.getBlockBounds(EnumFacing.SOUTH, 0.25, 0.125, 0.0, 1.0, 0.1875, 1.0);
	private static final AxisAlignedBB SOUTH_THREE = RotationHelper.getBlockBounds(EnumFacing.SOUTH, 0.375, 0.1875, 0.0, 1.0, 0.25, 1.0);
	private static final AxisAlignedBB SOUTH_FOUR = RotationHelper.getBlockBounds(EnumFacing.SOUTH, 0.5, 0.25, 0.0, 1.0, 0.3125, 1.0);
	private static final AxisAlignedBB SOUTH_FIVE = RotationHelper.getBlockBounds(EnumFacing.SOUTH, 0.625, 0.3125, 0.0, 1.0, 0.375, 1.0);
	private static final AxisAlignedBB SOUTH_SIX = RotationHelper.getBlockBounds(EnumFacing.SOUTH, 0.75, 0.375, 0.0, 1.0, 0.4375, 1.0);
	private static final AxisAlignedBB SOUTH_SEVEN = RotationHelper.getBlockBounds(EnumFacing.SOUTH, 0.875, 0.4375, 0.0, 1.0, 0.5, 1.0);
	
	private static final AxisAlignedBB WEST_ONE = RotationHelper.getBlockBounds(EnumFacing.WEST, 0.125, 0.0625, 0.0, 1.0, 0.125, 1.0);
	private static final AxisAlignedBB WEST_TWO = RotationHelper.getBlockBounds(EnumFacing.WEST, 0.25, 0.125, 0.0, 1.0, 0.1875, 1.0);
	private static final AxisAlignedBB WEST_THREE = RotationHelper.getBlockBounds(EnumFacing.WEST, 0.375, 0.1875, 0.0, 1.0, 0.25, 1.0);
	private static final AxisAlignedBB WEST_FOUR = RotationHelper.getBlockBounds(EnumFacing.WEST, 0.5, 0.25, 0.0, 1.0, 0.3125, 1.0);
	private static final AxisAlignedBB WEST_FIVE = RotationHelper.getBlockBounds(EnumFacing.WEST, 0.625, 0.3125, 0.0, 1.0, 0.375, 1.0);
	private static final AxisAlignedBB WEST_SIX = RotationHelper.getBlockBounds(EnumFacing.WEST, 0.75, 0.375, 0.0, 1.0, 0.4375, 1.0);
	private static final AxisAlignedBB WEST_SEVEN = RotationHelper.getBlockBounds(EnumFacing.WEST, 0.875, 0.4375, 0.0, 1.0, 0.5, 1.0);
	
	private static final AxisAlignedBB NORTH_ONE_STACKED = RotationHelper.getBlockBounds(EnumFacing.NORTH, 0.125, 0.5625, 0.0, 1.0, 0.625, 1.0);
	private static final AxisAlignedBB NORTH_TWO_STACKED = RotationHelper.getBlockBounds(EnumFacing.NORTH, 0.25, 0.625, 0.0, 1.0, 0.6875, 1.0);
	private static final AxisAlignedBB NORTH_THREE_STACKED = RotationHelper.getBlockBounds(EnumFacing.NORTH, 0.375, 0.6875, 0.0, 1.0, 0.75, 1.0);
	private static final AxisAlignedBB NORTH_FOUR_STACKED = RotationHelper.getBlockBounds(EnumFacing.NORTH, 0.5, 0.75, 0.0, 1.0, 0.8125, 1.0);
	private static final AxisAlignedBB NORTH_FIVE_STACKED = RotationHelper.getBlockBounds(EnumFacing.NORTH, 0.625, 0.8125, 0.0, 1.0, 0.875, 1.0);
	private static final AxisAlignedBB NORTH_SIX_STACKED = RotationHelper.getBlockBounds(EnumFacing.NORTH, 0.75, 0.875, 0.0, 1.0, 0.9375, 1.0);
	private static final AxisAlignedBB NORTH_SEVEN_STACKED = RotationHelper.getBlockBounds(EnumFacing.NORTH, 0.875, 0.9375, 0.0, 1.0, 1.0, 1.0);
	
	private static final AxisAlignedBB EAST_ONE_STACKED = RotationHelper.getBlockBounds(EnumFacing.EAST, 0.125, 0.5625, 0.0, 1.0, 0.625, 1.0);
	private static final AxisAlignedBB EAST_TWO_STACKED = RotationHelper.getBlockBounds(EnumFacing.EAST, 0.25, 0.625, 0.0, 1.0, 0.6875, 1.0);
	private static final AxisAlignedBB EAST_THREE_STACKED = RotationHelper.getBlockBounds(EnumFacing.EAST, 0.375, 0.6875, 0.0, 1.0, 0.75, 1.0);
	private static final AxisAlignedBB EAST_FOUR_STACKED = RotationHelper.getBlockBounds(EnumFacing.EAST, 0.5, 0.75, 0.0, 1.0, 0.8125, 1.0);
	private static final AxisAlignedBB EAST_FIVE_STACKED = RotationHelper.getBlockBounds(EnumFacing.EAST, 0.625, 0.8125, 0.0, 1.0, 0.875, 1.0);
	private static final AxisAlignedBB EAST_SIX_STACKED = RotationHelper.getBlockBounds(EnumFacing.EAST, 0.75, 0.875, 0.0, 1.0, 0.9375, 1.0);
	private static final AxisAlignedBB EAST_SEVEN_STACKED = RotationHelper.getBlockBounds(EnumFacing.EAST, 0.875, 0.9375, 0.0, 1.0, 1.0, 1.0);
	
	private static final AxisAlignedBB SOUTH_ONE_STACKED = RotationHelper.getBlockBounds(EnumFacing.SOUTH, 0.125, 0.5625, 0.0, 1.0, 0.625, 1.0);
	private static final AxisAlignedBB SOUTH_TWO_STACKED = RotationHelper.getBlockBounds(EnumFacing.SOUTH, 0.25, 0.625, 0.0, 1.0, 0.6875, 1.0);
	private static final AxisAlignedBB SOUTH_THREE_STACKED = RotationHelper.getBlockBounds(EnumFacing.SOUTH, 0.375, 0.6875, 0.0, 1.0, 0.75, 1.0);
	private static final AxisAlignedBB SOUTH_FOUR_STACKED = RotationHelper.getBlockBounds(EnumFacing.SOUTH, 0.5, 0.75, 0.0, 1.0, 0.8125, 1.0);
	private static final AxisAlignedBB SOUTH_FIVE_STACKED = RotationHelper.getBlockBounds(EnumFacing.SOUTH, 0.625, 0.8125, 0.0, 1.0, 0.875, 1.0);
	private static final AxisAlignedBB SOUTH_SIX_STACKED = RotationHelper.getBlockBounds(EnumFacing.SOUTH, 0.75, 0.875, 0.0, 1.0, 0.9375, 1.0);
	private static final AxisAlignedBB SOUTH_SEVEN_STACKED = RotationHelper.getBlockBounds(EnumFacing.SOUTH, 0.875, 0.9375, 0.0, 1.0, 1.0, 1.0);
	
	private static final AxisAlignedBB WEST_ONE_STACKED = RotationHelper.getBlockBounds(EnumFacing.WEST, 0.125, 0.5625, 0.0, 1.0, 0.625, 1.0);
	private static final AxisAlignedBB WEST_TWO_STACKED = RotationHelper.getBlockBounds(EnumFacing.WEST, 0.25, 0.625, 0.0, 1.0, 0.6875, 1.0);
	private static final AxisAlignedBB WEST_THREE_STACKED = RotationHelper.getBlockBounds(EnumFacing.WEST, 0.375, 0.6875, 0.0, 1.0, 0.75, 1.0);
	private static final AxisAlignedBB WEST_FOUR_STACKED = RotationHelper.getBlockBounds(EnumFacing.WEST, 0.5, 0.75, 0.0, 1.0, 0.8125, 1.0);
	private static final AxisAlignedBB WEST_FIVE_STACKED = RotationHelper.getBlockBounds(EnumFacing.WEST, 0.625, 0.8125, 0.0, 1.0, 0.875, 1.0);
	private static final AxisAlignedBB WEST_SIX_STACKED = RotationHelper.getBlockBounds(EnumFacing.WEST, 0.75, 0.875, 0.0, 1.0, 0.9375, 1.0);
	private static final AxisAlignedBB WEST_SEVEN_STACKED = RotationHelper.getBlockBounds(EnumFacing.WEST, 0.875, 0.9375, 0.0, 1.0, 1.0, 1.0);
	
	private static final AxisAlignedBB BOUNDING_BOX = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.5, 1.0);
	 
	public BlockRamp() 
	{
		super(Material.ROCK);
		this.setUnlocalizedName("ramp");
		this.setRegistryName("ramp");
		this.setCreativeTab(MrCrayfishRCCarMod.TAB_CAR);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(STACKED, false));
	}
	
	@Override
	public boolean isBlockNormalCube(IBlockState blockState) 
	{
		return false;
	}
    
    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess worldIn, BlockPos pos, EnumFacing side) 
    {
        return false;
    }
    
    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) 
    {
    	if(!worldIn.isRemote)
    	{
    		if(!state.getValue(STACKED))
    		{
    			ItemStack heldStack = playerIn.getHeldItem(hand);
        		if(heldStack.getItem() == Item.getItemFromBlock(this) && facing == EnumFacing.UP)
        		{
        			worldIn.setBlockState(pos, state.withProperty(STACKED, true), 11);
        			
        			SoundType soundtype = this.getSoundType(state, worldIn, pos, playerIn);
                    worldIn.playSound(playerIn, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
        			
                    if(!playerIn.capabilities.isCreativeMode)
                    {
                    	heldStack.shrink(1);
                    	if(heldStack.isEmpty())
                    	{
                    		playerIn.inventory.deleteStack(heldStack);
                    	}
                    }
        		}
    		}
    	}
    	return true;
    }
    
    @Override
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, Entity entityIn, boolean p_185477_7_) 
    {
    	if(state.getValue(STACKED)) 
		{
			super.addCollisionBoxToList(pos, entityBox, collidingBoxes, BASE_STACKED);
			switch(state.getValue(FACING))
			{
			case NORTH:
				super.addCollisionBoxToList(pos, entityBox, collidingBoxes, NORTH_ONE_STACKED);
				super.addCollisionBoxToList(pos, entityBox, collidingBoxes, NORTH_TWO_STACKED);
				super.addCollisionBoxToList(pos, entityBox, collidingBoxes, NORTH_THREE_STACKED);
				super.addCollisionBoxToList(pos, entityBox, collidingBoxes, NORTH_FOUR_STACKED);
				super.addCollisionBoxToList(pos, entityBox, collidingBoxes, NORTH_FIVE_STACKED);
				super.addCollisionBoxToList(pos, entityBox, collidingBoxes, NORTH_SIX_STACKED);
				super.addCollisionBoxToList(pos, entityBox, collidingBoxes, NORTH_SEVEN_STACKED);
				break;
			case EAST:
				super.addCollisionBoxToList(pos, entityBox, collidingBoxes, EAST_ONE_STACKED);
				super.addCollisionBoxToList(pos, entityBox, collidingBoxes, EAST_TWO_STACKED);
				super.addCollisionBoxToList(pos, entityBox, collidingBoxes, EAST_THREE_STACKED);
				super.addCollisionBoxToList(pos, entityBox, collidingBoxes, EAST_FOUR_STACKED);
				super.addCollisionBoxToList(pos, entityBox, collidingBoxes, EAST_FIVE_STACKED);
				super.addCollisionBoxToList(pos, entityBox, collidingBoxes, EAST_SIX_STACKED);
				super.addCollisionBoxToList(pos, entityBox, collidingBoxes, EAST_SEVEN_STACKED);
				break;
			case SOUTH:
				super.addCollisionBoxToList(pos, entityBox, collidingBoxes, SOUTH_ONE_STACKED);
				super.addCollisionBoxToList(pos, entityBox, collidingBoxes, SOUTH_TWO_STACKED);
				super.addCollisionBoxToList(pos, entityBox, collidingBoxes, SOUTH_THREE_STACKED);
				super.addCollisionBoxToList(pos, entityBox, collidingBoxes, SOUTH_FOUR_STACKED);
				super.addCollisionBoxToList(pos, entityBox, collidingBoxes, SOUTH_FIVE_STACKED);
				super.addCollisionBoxToList(pos, entityBox, collidingBoxes, SOUTH_SIX_STACKED);
				super.addCollisionBoxToList(pos, entityBox, collidingBoxes, SOUTH_SEVEN_STACKED);
				break;
			default:
				super.addCollisionBoxToList(pos, entityBox, collidingBoxes, WEST_ONE_STACKED);
				super.addCollisionBoxToList(pos, entityBox, collidingBoxes, WEST_TWO_STACKED);
				super.addCollisionBoxToList(pos, entityBox, collidingBoxes, WEST_THREE_STACKED);
				super.addCollisionBoxToList(pos, entityBox, collidingBoxes, WEST_FOUR_STACKED);
				super.addCollisionBoxToList(pos, entityBox, collidingBoxes, WEST_FIVE_STACKED);
				super.addCollisionBoxToList(pos, entityBox, collidingBoxes, WEST_SIX_STACKED);
				super.addCollisionBoxToList(pos, entityBox, collidingBoxes, WEST_SEVEN_STACKED);
				break;
			}
		} 
		else 
		{
			super.addCollisionBoxToList(pos, entityBox, collidingBoxes, BASE);
			switch(state.getValue(FACING))
			{
			case NORTH:
				super.addCollisionBoxToList(pos, entityBox, collidingBoxes, NORTH_ONE);
				super.addCollisionBoxToList(pos, entityBox, collidingBoxes, NORTH_TWO);
				super.addCollisionBoxToList(pos, entityBox, collidingBoxes, NORTH_THREE);
				super.addCollisionBoxToList(pos, entityBox, collidingBoxes, NORTH_FOUR);
				super.addCollisionBoxToList(pos, entityBox, collidingBoxes, NORTH_FIVE);
				super.addCollisionBoxToList(pos, entityBox, collidingBoxes, NORTH_SIX);
				super.addCollisionBoxToList(pos, entityBox, collidingBoxes, NORTH_SEVEN);
				break;
			case EAST:
				super.addCollisionBoxToList(pos, entityBox, collidingBoxes, EAST_ONE);
				super.addCollisionBoxToList(pos, entityBox, collidingBoxes, EAST_TWO);
				super.addCollisionBoxToList(pos, entityBox, collidingBoxes, EAST_THREE);
				super.addCollisionBoxToList(pos, entityBox, collidingBoxes, EAST_FOUR);
				super.addCollisionBoxToList(pos, entityBox, collidingBoxes, EAST_FIVE);
				super.addCollisionBoxToList(pos, entityBox, collidingBoxes, EAST_SIX);
				super.addCollisionBoxToList(pos, entityBox, collidingBoxes, EAST_SEVEN);
				break;
			case SOUTH:
				super.addCollisionBoxToList(pos, entityBox, collidingBoxes, SOUTH_ONE);
				super.addCollisionBoxToList(pos, entityBox, collidingBoxes, SOUTH_TWO);
				super.addCollisionBoxToList(pos, entityBox, collidingBoxes, SOUTH_THREE);
				super.addCollisionBoxToList(pos, entityBox, collidingBoxes, SOUTH_FOUR);
				super.addCollisionBoxToList(pos, entityBox, collidingBoxes, SOUTH_FIVE);
				super.addCollisionBoxToList(pos, entityBox, collidingBoxes, SOUTH_SIX);
				super.addCollisionBoxToList(pos, entityBox, collidingBoxes, SOUTH_SEVEN);
				break;
			default:
				super.addCollisionBoxToList(pos, entityBox, collidingBoxes, WEST_ONE);
				super.addCollisionBoxToList(pos, entityBox, collidingBoxes, WEST_TWO);
				super.addCollisionBoxToList(pos, entityBox, collidingBoxes, WEST_THREE);
				super.addCollisionBoxToList(pos, entityBox, collidingBoxes, WEST_FOUR);
				super.addCollisionBoxToList(pos, entityBox, collidingBoxes, WEST_FIVE);
				super.addCollisionBoxToList(pos, entityBox, collidingBoxes, WEST_SIX);
				super.addCollisionBoxToList(pos, entityBox, collidingBoxes, WEST_SEVEN);
				break;
			}
		}
    }

    @Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) 
	{
		if(!state.getValue(STACKED))
		{
			return BOUNDING_BOX;
		}
		return FULL_BLOCK_AABB;
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return ((EnumFacing) state.getValue(FACING)).getHorizontalIndex() + (((Boolean) state.getValue(STACKED)) ? 4 : 0);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta % 4)).withProperty(STACKED, meta / 4 == 1);
	}
	
	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) 
	{
		IExtendedBlockState extendedState = (IExtendedBlockState) state;
		
		extendedState = extendedState.withProperty(METAL, true);
		
		IBlockState relativeState = StateHelper.getRelativeBlockState(world, pos.down(), state.getValue(FACING), RelativeFacing.OPPOSITE);
		if(StateHelper.getRelativeBlock(world, pos.down(), state.getValue(FACING), RelativeFacing.OPPOSITE) == ModBlocks.ramp)
		{
			RelativeFacing relativeFacing = StateHelper.getRelativeFacing(world, pos.down(), state.getValue(FACING), RelativeFacing.OPPOSITE);
			if(relativeFacing == RelativeFacing.SAME)
			{
				extendedState = extendedState.withProperty(METAL, !relativeState.getValue(STACKED));
			}
		}
		return extendedState;
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		BlockStateContainer.Builder builder = new BlockStateContainer.Builder(this);
		builder.add(FACING, STACKED);
		builder.add(METAL);
		return builder.build();
	}
	
	public Axis getAxis(IBlockState state) 
	{
		 return ((EnumFacing) state.getValue(FACING)).getAxis();
	}

	@Override
	public BlockRenderLayer getBlockLayer() 
	{
		return BlockRenderLayer.CUTOUT;
	}
	
	@Override
	public float getAngle() 
	{
		return 22.5F;
	}

	@Override
	public PropertyDirection getDirectionProp() 
	{
		return FACING;
	}
}