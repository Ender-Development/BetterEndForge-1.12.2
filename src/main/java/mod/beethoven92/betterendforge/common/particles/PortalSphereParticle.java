package mod.beethoven92.betterendforge.common.particles;

import mod.beethoven92.betterendforge.common.util.AdvMathHelper;
import mod.beethoven92.betterendforge.common.util.ModMathHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.particle.ParticleSimpleAnimated;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.World;
import net.minecraft.util.math.MathHelper;

public class PortalSphereParticle extends ParticleSimpleAnimated
{
	protected static final TextureAtlasSprite PARTICLE_ATLAS = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("betterendforge:particle/spritesheet");
	private int ticks;
	private double preVX;
	private double preVY;
	private double preVZ;
	private double nextVX;
	private double nextVY;
	private double nextVZ;

	protected PortalSphereParticle(World world, double x, double y, double z)
	{
		super(world, x, y, z, 0, 0, 0);
		setParticleTexture(PARTICLE_ATLAS);
		particleTextureIndexY = 3;
		this.particleMaxAge = ModMathHelper.randRange(20, 80, rand);
		this.particleScale = ModMathHelper.randRange(0.05F, 0.15F, rand);
		this.setRBGColorF(0xFE, 0xBB, 0xD5);
		this.setColorFade(0xBBFEE4);

		preVX = rand.nextGaussian() * 0.02;
		preVY = rand.nextGaussian() * 0.02;
		preVZ = rand.nextGaussian() * 0.02;

		nextVX = rand.nextGaussian() * 0.02;
		nextVY = rand.nextGaussian() * 0.02;
		nextVZ = rand.nextGaussian() * 0.02;
	}

	@Override
	public void onUpdate()
	{
		ticks++;
		if (ticks > 30)
		{
			preVX = nextVX;
			preVY = nextVY;
			preVZ = nextVZ;
			nextVX = rand.nextGaussian() * 0.02;
			nextVY = rand.nextGaussian() * 0.02;
			nextVZ = rand.nextGaussian() * 0.02;
			ticks = 0;
		}
		double delta = (double) ticks / 30.0;

		this.motionX = AdvMathHelper.lerp(delta, preVX, nextVX);
		this.motionY = AdvMathHelper.lerp(delta, preVY, nextVY);
		this.motionZ = AdvMathHelper.lerp(delta, preVZ, nextVZ);

		super.onUpdate();
	}

	public static class Factory implements IParticleFactory
	{

		@Override
		public Particle createParticle(int particleID, World world, double x, double y, double z,
									   double xSpeed, double ySpeed, double zSpeed, int... args)
		{
			return new PortalSphereParticle(world, x, y, z);
		}
	}
}
