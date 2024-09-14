package mod.beethoven92.betterendforge.mixin;

import java.util.Random;

import net.minecraft.util.math.MathHelper;
import net.minecraft.world.gen.feature.WorldGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import mod.beethoven92.betterendforge.common.init.ModBlocks;
import mod.beethoven92.betterendforge.common.util.BlockHelper;
import mod.beethoven92.betterendforge.common.util.ModMathHelper;
import net.minecraft.util.math.BlockPos;

@Mixin(MathHelper.class) //ChorusPlantFeature.class)
public abstract class ChorusPlantFeatureMixin
{
//	@Inject(method = "generate", at = @At("HEAD"), cancellable = true)
//	private void be_generate(ISeedReader worldIn, ChunkGenerator chunkGenerator, Random random,
//			BlockPos blockPos, NoFeatureConfig config, CallbackInfoReturnable<Boolean> info)
//	{
//		final ISeedReader structureWorldAccess = worldIn;
//		if (structureWorldAccess.isAirBlock(blockPos) && structureWorldAccess.getBlockState(blockPos.down()).isIn(ModBlocks.CHORUS_NYLIUM.get())) {
//			ChorusFlowerBlock.generatePlant(structureWorldAccess, blockPos, random, ModMathHelper.randRange(8, 16, random));
//			BlockState bottom = structureWorldAccess.getBlockState(blockPos);
//			if (bottom.isIn(Blocks.CHORUS_PLANT)) {
//				BlockHelper.setWithoutUpdate(
//						structureWorldAccess,
//						blockPos,
//						bottom.with(SixWayBlock.DOWN, true)
//				);
//			}
//			info.setReturnValue(true);
//		}
//	}
}
