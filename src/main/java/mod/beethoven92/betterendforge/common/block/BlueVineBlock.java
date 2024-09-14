package mod.beethoven92.betterendforge.common.block;

import mod.beethoven92.betterendforge.common.block.BlockProperties.TripleShape;
import mod.beethoven92.betterendforge.common.block.template.UpDownPlantBlock;
import mod.beethoven92.betterendforge.common.init.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class BlueVineBlock extends UpDownPlantBlock
{
	public static final PropertyEnum<TripleShape> SHAPE = BlockProperties.TRIPLE_SHAPE;

	public BlueVineBlock(Material mat)
	{
		super(mat);
		this.setDefaultState(this.blockState.getBaseState().withProperty(SHAPE, TripleShape.BOTTOM));
	}

	@Override
	protected boolean isTerrain(IBlockState state)
	{
		return state.getBlock() == ModBlocks.END_MOSS || state.getBlock() == ModBlocks.END_MYCELIUM;
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, SHAPE);
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(SHAPE, TripleShape.fromIndex(meta));
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(SHAPE).getIndex();
	}

	public int quantityDropped(Random random)
	{
		return 0;
	}

	public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack)
	{
		if (!worldIn.isRemote && stack.getItem() == Items.SHEARS)
		{
			spawnAsEntity(worldIn, pos, new ItemStack(state.getBlock(), 1, 0));
		}
		else
		{
			super.harvestBlock(worldIn, player, pos, state, te, stack);
		}
	}
}