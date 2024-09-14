package mod.beethoven92.betterendforge.common.world.structure;

import mod.beethoven92.betterendforge.BetterEnd;
import mod.beethoven92.betterendforge.common.util.AdvMathHelper;
import mod.beethoven92.betterendforge.common.world.moderngen.decorator.Decoration;
import mod.beethoven92.betterendforge.common.world.structure.piece.MountainPiece;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.StructureStart;

import javax.annotation.Nullable;
import java.util.Random;

public class MountainStructure extends MapGenStructure
{

	public Decoration getDecorationStage()
	{
		return Decoration.RAW_GENERATION;
	}
	
	@Override
	public String getStructureName() 
	{
		return BetterEnd.MOD_ID + ":mountain_structure";
	}

	protected StructureStart getStructureStart(int chunkX, int chunkZ)
	{
		return new Start(this, this.world, this.rand, chunkX, chunkZ);
	}

	@Override
	protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ) {
		return true;
	}

	@Nullable
	@Override
	public BlockPos getNearestStructurePos(World worldIn, BlockPos pos, boolean findUnexplored) {
		return BlockPos.ORIGIN;
	}

	public static class Start extends StructureStart
	{
		MapGenStructure structure;
		public Start()
		{
		}

		public Start(MapGenStructure structure, World worldIn, Random random, int chunkX, int chunkZ)
		{
			super(chunkX, chunkZ);
			this.structure = structure;
			this.create(worldIn, random, chunkX, chunkZ);
		}

		private void create(World worldIn, Random rand, int chunkX, int chunkZ)
		{
			int x = (chunkX << 4) | AdvMathHelper.nextInt(rand, 4, 12);
			int z = (chunkZ << 4) | AdvMathHelper.nextInt(rand, 4, 12);
			int y = worldIn.getHeight(x, z);
			if (y > 5) 
			{
				float radius = AdvMathHelper.nextInt(rand, 50, 100);
				float height = radius * MathHelper.nextFloat(rand, 0.8F, 1.2F);
				MountainPiece piece = new MountainPiece(new BlockPos(x, y, z), radius, height, rand, worldIn.getBiome(new BlockPos(x,y,z)));
				this.components.add(piece);
			}
			this.updateBoundingBox();
		}
	}
}