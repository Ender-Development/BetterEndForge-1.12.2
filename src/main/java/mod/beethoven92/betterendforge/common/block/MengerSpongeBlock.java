package mod.beethoven92.betterendforge.common.block;

import java.util.Queue;

import com.google.common.collect.Lists;

import mod.beethoven92.betterendforge.common.init.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockSponge;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MengerSpongeBlock extends BlockSponge
{
	public MengerSpongeBlock()
	{
		super();
	}

	@Override
	protected void tryAbsorb(World worldIn, BlockPos pos, IBlockState state) {
		if (this.absorbWater(worldIn, pos))
		{
			worldIn.setBlockState(pos, ModBlocks.MENGER_SPONGE_WET.getDefaultState(), 2);
			worldIn.playEvent(2001, pos, Block.getStateId(Blocks.WATER.getDefaultState()));
		}
	}


	private boolean absorbWater(World worldIn, BlockPos pos)
	{
		Queue<Tuple<BlockPos, Integer>> queue = Lists.newLinkedList();
		queue.add(new Tuple<>(pos, 0));
		int i = 0;

		while (!queue.isEmpty())
		{
			Tuple<BlockPos, Integer> tuple = queue.poll();
			BlockPos blockpos = tuple.getFirst();
			int j = tuple.getSecond();

			for (EnumFacing direction : EnumFacing.values())
			{
				BlockPos blockpos1 = blockpos.offset(direction);
				IBlockState blockstate = worldIn.getBlockState(blockpos1);
				Material material = blockstate.getMaterial();
				if (material == Material.WATER)
				{
					if (blockstate.getBlock() instanceof BlockLiquid)
					{
						worldIn.setBlockState(blockpos1, Blocks.AIR.getDefaultState(), 3);
						++i;
						if (j < 6)
						{
							queue.add(new Tuple<>(blockpos1, j + 1));
						}
					}
//					else if (material == Material.OCEAN_PLANT || material == Material.SEA_GRASS)
//					{
//						TileEntity tileentity = blockstate.hasTileEntity() ? worldIn.getTileEntity(blockpos1) : null;
//						spawnDrops(blockstate, worldIn, blockpos1, tileentity);
//						worldIn.setBlockState(blockpos1, Blocks.AIR.getDefaultState(), 3);
//						++i;
//						if (j < 6)
//						{
//							queue.add(new Tuple<>(blockpos1, j + 1));
//						}
//					}
				}
			}

			if (i > 64)
			{
				break;
			}
		}

		return i > 0;
	}
}
