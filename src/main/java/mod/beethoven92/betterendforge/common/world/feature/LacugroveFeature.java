package mod.beethoven92.betterendforge.common.world.feature;

import java.util.List;
import java.util.Random;
import java.util.function.Function;

import mod.beethoven92.betterendforge.common.init.ModBlocks;
import mod.beethoven92.betterendforge.common.init.ModTags;
import mod.beethoven92.betterendforge.common.util.BlockHelper;
import mod.beethoven92.betterendforge.common.util.ModMathHelper;
import mod.beethoven92.betterendforge.common.util.SplineHelper;
import mod.beethoven92.betterendforge.common.util.sdf.PosInfo;
import mod.beethoven92.betterendforge.common.util.sdf.SDF;
import mod.beethoven92.betterendforge.common.util.sdf.operator.SDFDisplacement;
import mod.beethoven92.betterendforge.common.util.sdf.operator.SDFSubtraction;
import mod.beethoven92.betterendforge.common.util.sdf.operator.SDFTranslate;
import mod.beethoven92.betterendforge.common.util.sdf.primitive.SDFSphere;
import mod.beethoven92.betterendforge.common.util.sdf.vector.Vector3f;
import mod.beethoven92.betterendforge.common.world.generator.OpenSimplexNoise;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class LacugroveFeature extends WorldGenerator
{
	private static final Function<IBlockState, Boolean> REPLACE;
	private static final Function<IBlockState, Boolean> IGNORE;
	private static final Function<PosInfo, IBlockState> POST;

	public LacugroveFeature(){
		super();
	}

	public LacugroveFeature(boolean notify){
		super(notify);
	}
	
	static
	{
		REPLACE = (state) -> {
			if (ModTags.END_GROUND.contains(state.getBlock()))
			{
				return true;
			}
			if (ModBlocks.LACUGROVE.isTreeLog(state)) 
			{
				return true;
			}
			if (state.getBlock() == ModBlocks.LACUGROVE_LEAVES)
			{
				return true;
			}
			if (state.getMaterial().equals(Material.PLANTS)) 
			{
				return true;
			}
			return state.getMaterial().isReplaceable();
		};
		
		IGNORE = (state) -> {
			return ModBlocks.LACUGROVE.isTreeLog(state);
		};
		
		POST = (info) -> {
			if (ModBlocks.LACUGROVE.isTreeLog(info.getStateUp()) && ModBlocks.LACUGROVE.isTreeLog(info.getStateDown()))
			{
				return ModBlocks.LACUGROVE.log.getDefaultState();
			}
			return info.getState();
		};
	}


	@Override
	public boolean generate(World world, Random random,
							BlockPos blockPos)
	{
		if (!ModTags.END_GROUND.contains(world.getBlockState(blockPos.down()).getBlock())) return false;
		
		float size = ModMathHelper.randRange(15, 25, random);
		List<Vector3f> spline = SplineHelper.makeSpline(0, 0, 0, 0, size, 0, 6);
		SplineHelper.offsetParts(spline, random, 1F, 0, 1F);
		
		if (!SplineHelper.canGenerate(spline, blockPos, world, REPLACE))
		{
			return false;
		}
		
		OpenSimplexNoise noise = new OpenSimplexNoise(random.nextLong());
		
		float radius = ModMathHelper.randRange(6F, 8F, random);
		radius *= (size - 15F) / 20F + 1F;
		Vector3f center = spline.get(4);
		leavesBall(world, blockPos.add(center.getX(), center.getY(), center.getZ()), radius, random, noise);
		
		radius = ModMathHelper.randRange(1.2F, 1.8F, random);
		SDF function = SplineHelper.buildSDF(spline, radius, 0.7F, (bpos) -> {
			return ModBlocks.LACUGROVE.bark.getDefaultState();
		});
		
		function.setReplaceFunction(REPLACE);
		function.addPostProcess(POST);
		function.fillRecursive(world, blockPos);
		
		spline = spline.subList(4, 6);
		SplineHelper.fillSpline(spline, world, ModBlocks.LACUGROVE.bark.getDefaultState(), blockPos, REPLACE);
		
		Mutable mut = new Mutable();
		int offset = random.nextInt(2);
		for (int i = 0; i < 100; i++) 
		{
			double px = blockPos.getX() + ModMathHelper.randRange(-5, 5, random);
			double pz = blockPos.getZ() + ModMathHelper.randRange(-5, 5, random);
			mut.setX(ModMathHelper.floor(px + 0.5));
			mut.setZ(ModMathHelper.floor(pz + 0.5));
			if (((mut.getX() + mut.getZ() + offset) & 1) == 0) 
			{
				double distance = 3.5 - ModMathHelper.length(px - blockPos.getX(), pz - blockPos.getZ()) * 0.5;
				if (distance > 0) 
				{
					int minY = ModMathHelper.floor(blockPos.getY() - distance * 0.5);
					int maxY = ModMathHelper.floor(blockPos.getY() + distance + random.nextDouble());
					boolean generate = false;
					for (int y = minY; y < maxY; y++) 
					{
						mut.setY(y);
						if (ModTags.END_GROUND.contains(world.getBlockState(mut).getBlock()))
						{
							generate = true;
							break;
						}
					}
					if (generate)
					{
						int top = maxY - 1;
						for (int y = top; y >= minY; y--) 
						{
							mut.setY(y);
							IBlockState state = world.getBlockState(mut);
							if (state.getMaterial().isReplaceable() || state.getMaterial().equals(Material.PLANTS) || ModTags.END_GROUND.contains(state.getBlock()))
							{
								BlockHelper.setWithoutUpdate(world, mut, y == top ? ModBlocks.LACUGROVE.bark : ModBlocks.LACUGROVE.log);
							}
							else {
								break;
							}
						}
					}
				}
			}
		}
		
		return true;
	}
	
	private void leavesBall(World world, BlockPos pos, float radius, Random random, OpenSimplexNoise noise)
	{
		SDF sphere = new SDFSphere().setRadius(radius).setBlock(ModBlocks.LACUGROVE_LEAVES.getDefaultState());
		sphere = new SDFDisplacement().setFunction((vec) -> { return (float) noise.eval(vec.getX() * 0.2, vec.getY() * 0.2, vec.getZ() * 0.2) * 3; }).setSource(sphere);
		sphere = new SDFDisplacement().setFunction((vec) -> { return random.nextFloat() * 3F - 1.5F; }).setSource(sphere);
		sphere = new SDFSubtraction().setSourceA(sphere).setSourceB(new SDFTranslate().setTranslate(0, -radius - 2, 0).setSource(sphere));
		Mutable mut = new Mutable();
		sphere.addPostProcess((info) -> {
			if (random.nextInt(5) == 0) 
			{
				for (EnumFacing dir: EnumFacing.values())
				{
					IBlockState state = info.getState(dir, 2);
					if (state.getBlock()== Blocks.AIR)
					{
						return info.getState();
					}
				}
				info.setState(ModBlocks.LACUGROVE.bark.getDefaultState());
				for (int x = -6; x < 7; x++) 
				{
					int ax = Math.abs(x);
					mut.setX(x + info.getPos().getX());
					for (int z = -6; z < 7; z++) 
					{
						int az = Math.abs(z);
						mut.setZ(z + info.getPos().getZ());
						for (int y = -6; y < 7; y++) 
						{
							int ay = Math.abs(y);
							int d = ax + ay + az;
							if (d < 7) {
								mut.setY(y + info.getPos().getY());
								IBlockState state = info.getState(mut);
							}
						}
					}
				}
			}
			return info.getState();
		});
		sphere.fillRecursiveIgnore(world, pos, IGNORE);
		
		if (radius > 5) {
			int count = (int) (radius * 2.5F);
			for (int i = 0; i < count; i++) 
			{
				BlockPos p = pos.add(random.nextGaussian() * 1, random.nextGaussian() * 1, random.nextGaussian() * 1);
				boolean place = true;
				for (EnumFacing d: EnumFacing.values())
				{
					IBlockState state = world.getBlockState(p.offset(d));
					if (!ModBlocks.LACUGROVE.isTreeLog(state) && state.getBlock()!=(ModBlocks.LACUGROVE_LEAVES))
					{
						place = false;
						break;
					}
				}
				if (place) 
				{
					BlockHelper.setWithoutUpdate(world, p, ModBlocks.LACUGROVE.bark);
				}
			}
		}
		
		BlockHelper.setWithoutUpdate(world, pos, ModBlocks.LACUGROVE.bark);
	}
}
