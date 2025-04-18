package mod.beethoven92.betterendforge.client.renderer;

import mod.beethoven92.betterendforge.BetterEnd;
import mod.beethoven92.betterendforge.client.model.CubozoaModel;
import mod.beethoven92.betterendforge.common.entity.CubozoaEntity;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.layers.LayerSpiderEyes;
import net.minecraft.util.ResourceLocation;

public class CubozoaRenderer extends RenderLiving<CubozoaEntity> {
	private static final ResourceLocation[] TEXTURE = new ResourceLocation[2];
	private static final ResourceLocation[] GLOW = new ResourceLocation[2];

	public CubozoaRenderer(RenderManager renderManager) {
		super(renderManager, new CubozoaModel(), 0.5f);
		this.addLayer(new LayerRenderer<CubozoaEntity>() {
			@Override
			public void doRenderLayer(CubozoaEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
				bindTexture(GLOW[entity.getVariant()]);
				GlStateManager.enableBlend();
				GlStateManager.disableAlpha();
				GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
				GlStateManager.depthMask(!entity.isInvisible());
				GlStateManager.pushMatrix();
				int lvt_9_1_ = 61680;
				int lvt_10_1_ = lvt_9_1_ % 65536;
				int lvt_11_1_ = lvt_9_1_ / 65536;
				OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)lvt_10_1_, (float)lvt_11_1_);
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				((CubozoaRenderer)(Object)renderManager.getEntityRenderObject(entity)).mainModel.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);

				lvt_9_1_ = entity.getBrightnessForRender();
				lvt_10_1_ = lvt_9_1_ % 65536;
				lvt_11_1_ = lvt_9_1_ / 65536;
				OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)lvt_10_1_, (float)lvt_11_1_);
				GlStateManager.popMatrix();
				GlStateManager.depthMask(true);
				GlStateManager.disableBlend();
				GlStateManager.enableAlpha();
			}

			@Override
			public boolean shouldCombineTextures() {
				return false;
			}
		});
	}

	@Override
	protected void preRenderCallback(CubozoaEntity entity, float partialTickTime) {
		float scale = entity.getScale();
		GlStateManager.scale(scale, scale, scale);
	}

	@Override
	protected ResourceLocation getEntityTexture(CubozoaEntity entity) {
		return TEXTURE[entity.getVariant()];
	}

	static {
		TEXTURE[0] = new ResourceLocation(BetterEnd.MOD_ID, "textures/entity/cubozoa/cubozoa.png");
		TEXTURE[1] = new ResourceLocation(BetterEnd.MOD_ID, "textures/entity/cubozoa/cubozoa_sulphur.png");

		GLOW[0] = new ResourceLocation(BetterEnd.MOD_ID, "textures/entity/cubozoa/cubozoa_glow.png");
		GLOW[1] = new ResourceLocation(BetterEnd.MOD_ID, "textures/entity/cubozoa/cubozoa_sulphur_glow.png");
	}
}