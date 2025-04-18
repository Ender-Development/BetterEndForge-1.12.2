package mod.beethoven92.betterendforge.client.renderer;

import mod.beethoven92.betterendforge.common.block.EternalPedestal;
import mod.beethoven92.betterendforge.common.block.template.PedestalBlock;
import mod.beethoven92.betterendforge.common.init.ModBlocks;
import mod.beethoven92.betterendforge.common.init.ModItems;
import mod.beethoven92.betterendforge.common.tileentity.PedestalTileEntity;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderEnderCrystal;
import net.minecraft.client.renderer.tileentity.TileEntityBeaconRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.util.vector.Vector3f;
import scala.collection.parallel.ParIterableLike;

public class PedestalRenderer extends TileEntitySpecialRenderer<PedestalTileEntity> {
	@Override
	public void render(PedestalTileEntity tileEntity, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		if (tileEntity.isEmpty()) {
			return;
		}

		IBlockState state = tileEntity.getWorld().getBlockState(tileEntity.getPos());
		if (!(state.getBlock() instanceof PedestalBlock)) {
			return;
		}

		ItemStack activeItem = tileEntity.getStack();
		GlStateManager.pushMatrix();
		RenderHelper.enableStandardItemLighting();
		GlStateManager.translate((float) x + 0.5F, (float) y + ((PedestalBlock) state.getBlock()).getHeight(state), (float) z + 0.5F);
		Minecraft minecraft = Minecraft.getMinecraft();
		Vector3f translate = new Vector3f(0, 0, 0); // Adjust translation as needed

		if (activeItem.getItem() instanceof ItemBlock) {
			GlStateManager.scale(1.5F, 1.5F, 1.5F);
		} else {
			GlStateManager.scale(1.25F, 1.25F, 1.25F);
		}

		int age = tileEntity.getAge();
		if (state.getBlock() == ModBlocks.ETERNAL_PEDESTAL && state.getValue(EternalPedestal.ACTIVATED)) {
			float[] colors = EternalCrystalRenderer.colors(age);
			int yPos = tileEntity.getPos().getY();
			//Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("textures/entity/end_gateway_beam.png"));
			//TileEntityBeaconRenderer.renderBeamSegment(x,y,z, partialTicks, 1, age, yPos, 1024 - yPos, colors, 0.13F, 0.16F);
			//BeamRenderer.renderLightBeam(age, -yPos, 1024 - yPos, colors, 0.25F, 0.13F, 0.16F);
			float altitude = MathHelper.sin((tileEntity.getAge() + partialTicks) / 10.0F) * 0.1F + 0.1F;
			GlStateManager.translate(0.0D, altitude, 0.0D);
		}

		if (activeItem.getItem() == Items.END_CRYSTAL) {
			EntityEnderCrystal entityEnderCrystal = new EntityEnderCrystal(getWorld());
			entityEnderCrystal.ticksExisted = 0;
			entityEnderCrystal.innerRotation = age;
			GlStateManager.pushMatrix();
			GlStateManager.scale(0.3, 0.3, 0.3);
			minecraft.getRenderManager().getEntityRenderObject(entityEnderCrystal).doRender(entityEnderCrystal, 0, 0, 0, 0, partialTicks);
			GlStateManager.popMatrix();
		} else if (activeItem.getItem() == ModItems.ETERNAL_CRYSTAL) {
			EternalCrystalRenderer.render(age, partialTicks, (float) x, (float) y, (float) z);
		} else {
			float rotation = (age + partialTicks) / 25.0F + 6.0F;
			GlStateManager.rotate((float) (rotation/Math.PI/2*180), 0.0F, 1.0F, 0.0F);
			minecraft.getRenderItem().renderItem(activeItem, net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType.GROUND);
		}
		RenderHelper.disableStandardItemLighting();
		GlStateManager.popMatrix();
	}

	@Override
	public boolean isGlobalRenderer(PedestalTileEntity te) {
		return true;
	}
}
