package mod.beethoven92.betterendforge.common.world.biome.cave;

import mod.beethoven92.betterendforge.client.audio.BetterEndMusicNames;
import mod.beethoven92.betterendforge.common.init.ModFeatures;
import mod.beethoven92.betterendforge.common.init.ModSoundEvents;
import mod.beethoven92.betterendforge.common.world.biome.BetterEndCaveBiome;
import mod.beethoven92.betterendforge.common.world.biome.BiomeTemplate;

public class EmptySmaragdantCaveBiome extends BetterEndCaveBiome
{
	public EmptySmaragdantCaveBiome() 
	{
		super(new BiomeTemplate("empty_smaragdant_cave")
				.setFogColor(0, 253, 182)
				.setFogDensity(2.0F)
				.setFoliageColor(0, 131, 145)
				.setGrassColor(0, 131, 145)
				.setWaterColor(31, 167, 212)
				.setWaterFogColor(31, 167, 212)
						.setMusic(BetterEndMusicNames.MUSIC_CAVES)
						.setAmbientSound(ModSoundEvents.AMBIENT_CAVES));
				//setParticles(ModParticleTypes.SMARAGDANT.get(), 0.001F));
		
		this.addFloorFeature(ModFeatures.SMARAGDANT_CRYSTAL, 1);
		this.addFloorFeature(ModFeatures.SMARAGDANT_CRYSTAL_SHARD, 20);		
		this.addCeilFeature(ModFeatures.END_STONE_STALACTITE, 1);
	}
	
	@Override
	public float getFloorDensity() 
	{
		return 0.1F;
	}
	
	@Override
	public float getCeilDensity() 
	{
		return 0.1F;
	}
}
