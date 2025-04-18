package mod.beethoven92.betterendforge.common.world.feature;

import java.util.List;
import java.util.Random;
import java.util.function.Function;

import com.google.common.collect.Lists;
import mod.beethoven92.betterendforge.common.block.JellyshroomCapBlock;
import mod.beethoven92.betterendforge.common.init.ModBlocks;
import mod.beethoven92.betterendforge.common.init.ModTags;
import mod.beethoven92.betterendforge.common.util.ModMathHelper;
import mod.beethoven92.betterendforge.common.util.SplineHelper;
import mod.beethoven92.betterendforge.common.util.sdf.SDF;
import mod.beethoven92.betterendforge.common.util.sdf.operator.SDFFlatWave;
import mod.beethoven92.betterendforge.common.util.sdf.operator.SDFScale3D;
import mod.beethoven92.betterendforge.common.util.sdf.operator.SDFSmoothUnion;
import mod.beethoven92.betterendforge.common.util.sdf.operator.SDFSubtraction;
import mod.beethoven92.betterendforge.common.util.sdf.operator.SDFTranslate;
import mod.beethoven92.betterendforge.common.util.sdf.primitive.SDFSphere;
import mod.beethoven92.betterendforge.common.util.sdf.vector.Vector3f;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class JellyshroomFeature extends WorldGenerator
{
	private static final Function<IBlockState, Boolean> REPLACE;
	private static final List<Vector3f> ROOT;

	public JellyshroomFeature(){
		super();
	}

	public JellyshroomFeature(boolean notify){
		super(notify);
	}
	
	static 
	{
		ROOT = Lists.newArrayList(
			new Vector3f(0.1F,  0.70F, 0),
			new Vector3f(0.3F,  0.30F, 0),
			new Vector3f(0.7F,  0.05F, 0),
			new Vector3f(0.8F, -0.20F, 0)
		);
		SplineHelper.offset(ROOT, new Vector3f(0, -0.45F, 0));
		
		REPLACE = (state) -> {
			if (ModTags.END_GROUND.contains(state.getBlock()) || state.getMaterial().equals(Material.PLANTS))
			{
				return true;
			}
			return state.getMaterial().isReplaceable();
		};
	}


	@Override
	public boolean generate(World world, Random rand, BlockPos pos)
	{
		if (!ModTags.END_GROUND.contains(world.getBlockState(pos.down()).getBlock())) return false;
		
		IBlockState bark = ModBlocks.JELLYSHROOM.bark.getDefaultState();
		IBlockState membrane = ModBlocks.JELLYSHROOM_CAP_PURPLE.getDefaultState();
		
		int height = ModMathHelper.randRange(5, 8, rand);
		float radius = height * ModMathHelper.randRange(0.15F, 0.25F, rand);
		List<Vector3f> spline = SplineHelper.makeSpline(0, -1, 0, 0, height, 0, 3);
		SplineHelper.offsetParts(spline, rand, 0.5F, 0, 0.5F);
		SDF sdf = SplineHelper.buildSDF(spline, radius, 0.8F, (bpos) -> {
			return bark;
		});
		
		radius = height * ModMathHelper.randRange(0.7F, 0.9F, rand);
		if (radius < 1.5F) 
		{
			radius = 1.5F;
		}
		final float membraneRadius = radius;
		SDF cap = makeCap(membraneRadius, rand, membrane);
		final Vector3f last = spline.get(spline.size() - 1);
		cap = new SDFTranslate().setTranslate(last.getX(), last.getY(), last.getZ()).setSource(cap);
		sdf = new SDFSmoothUnion().setRadius(3F).setSourceA(sdf).setSourceB(cap);
		sdf.setReplaceFunction(REPLACE).addPostProcess((info) -> {
			if (ModBlocks.JELLYSHROOM.isTreeLog(info.getState())) 
			{
				if (ModBlocks.JELLYSHROOM.isTreeLog(info.getStateUp()) && ModBlocks.JELLYSHROOM.isTreeLog(info.getStateDown()))
				{
					return ModBlocks.JELLYSHROOM.log.getDefaultState();
				}
			}
			else if (info.getState().getBlock()==ModBlocks.JELLYSHROOM_CAP_PURPLE)
			{
				float dx = info.getPos().getX() - pos.getX() - last.getX();
				float dz = info.getPos().getZ() - pos.getZ() - last.getZ();
				float distance = ModMathHelper.length(dx, dz) / membraneRadius * 7F;
				int color = MathHelper.clamp(ModMathHelper.floor(distance), 0, 7);
				return info.getState().withProperty(JellyshroomCapBlock.COLOR, color);
			}
			return info.getState();
		}).fillRecursive(world, pos);
		radius = height * 0.5F;
		makeRoots(world, pos.add(0, 2, 0), radius, rand, bark);
		
		return true;
	}

	private void makeRoots(World world, BlockPos pos, float radius, Random random, IBlockState wood)
	{
		int count = (int) (radius * 3.5F);
		for (int i = 0; i < count; i++) 
		{
			float angle = (float) i / (float) count * ModMathHelper.PI2;
			float scale = radius * ModMathHelper.randRange(0.85F, 1.15F, random);
			
			List<Vector3f> branch = SplineHelper.copySpline(ROOT);
			SplineHelper.rotateSpline(branch, angle);
			SplineHelper.scale(branch, scale);
			Vector3f last = branch.get(branch.size() - 1);
			if (ModTags.GEN_TERRAIN.contains(world.getBlockState(pos.add(last.getX(), last.getY(), last.getZ())).getBlock()))
			{
				SplineHelper.fillSpline(branch, world, wood, pos, REPLACE);
			}
		}
	}
	
	private SDF makeCap(float radius, Random random, IBlockState cap)
	{
		SDF sphere = new SDFSphere().setRadius(radius).setBlock(cap);
		SDF sub = new SDFTranslate().setTranslate(0, -4, 0).setSource(sphere);
		sphere = new SDFSubtraction().setSourceA(sphere).setSourceB(sub);
		sphere = new SDFScale3D().setScale(1, 0.5F, 1).setSource(sphere);
		sphere = new SDFTranslate().setTranslate(0, 1 - radius * 0.5F, 0).setSource(sphere);
		
		float angle = random.nextFloat() * ModMathHelper.PI2;
		int count = (int) ModMathHelper.randRange(radius * 0.5F, radius, random);
		if (count < 3) 
		{
			count = 3;
		}
		sphere = new SDFFlatWave().setAngle(angle).setRaysCount(count).setIntensity(0.2F).setSource(sphere);
		
		return sphere;
	}
}
