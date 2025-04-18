package mod.beethoven92.betterendforge.mixin.minecraft;

import mod.beethoven92.betterendforge.BetterEnd;
import mod.beethoven92.betterendforge.common.util.WorldDataAPI;
import mod.beethoven92.betterendforge.common.world.generator.GeneratorOptions;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.gen.feature.WorldGenSpikes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WorldGenSpikes.EndSpike.class)
public abstract class EndSpikeMixin {
    @Final
    @Shadow
    private int height;

    //Used to return the appropriate height of the replaced pillars
    @Inject(method = "getHeight", at = @At("HEAD"), cancellable = true)
    private void be_getSpikeHeight(CallbackInfoReturnable<Integer> info) {

        if (!GeneratorOptions.isDirectSpikeHeight()) {
            int x = getCenterX();
            int z = getCenterZ();
            String pillarID = String.format("%d_%d", x, z);
            NBTTagCompound pillar = WorldDataAPI.getCompoundTag(BetterEnd.MOD_ID, "pillars");
            int minY = pillar.hasKey(pillarID) ? pillar.getInteger(pillarID) : 65;
            int maxY = minY + height - 54;
            info.setReturnValue(maxY);
        }
    }

    @Shadow
    public int getCenterX() {
        return 0;
    }

    @Shadow
    public int getCenterZ() {
        return 0;
    }
}
