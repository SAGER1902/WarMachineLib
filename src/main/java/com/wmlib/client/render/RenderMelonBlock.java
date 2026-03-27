package wmlib.client.render;

import wmlib.common.block.AbstractFacingBlock;
import wmlib.common.block.MelonBlock;
import wmlib.common.tileentity.MelonBlockTileEntity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wmlib.client.obj.SAObjModel;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.util.Util;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.entity.Entity;
@OnlyIn(Dist.CLIENT)
public class RenderMelonBlock extends TileEntityRenderer<MelonBlockTileEntity> {
	private static final ResourceLocation tex = new ResourceLocation("wmlib:textures/marker/soldier_type.png");
	private static final SAObjModel soldier_type = new SAObjModel("wmlib:textures/marker/soldier_type.obj");
	public SAObjModel obj = new SAObjModel("wmlib:textures/blocks/block.obj");
	private static final ResourceLocation ENCHANT_GLINT_LOCATION = new ResourceLocation("textures/misc/enchanted_item_glint.png");
	public RenderMelonBlock(TileEntityRendererDispatcher rendererDispatcherIn) {
		super(rendererDispatcherIn);
	}
	
	private static void setupGlintTexturing(float p_228548_0_) {
		RenderSystem.matrixMode(5890);
		RenderSystem.pushMatrix();
		RenderSystem.loadIdentity();
		long i = Util.getMillis() * 8L;
		float f = (float)(i % 110000L) / 110000.0F;
		float f1 = (float)(i % 30000L) / 30000.0F;
		RenderSystem.translatef(-f, f1, 0.0F);
		RenderSystem.rotatef(10.0F, 0.0F, 0.0F, 1.0F);
		RenderSystem.scalef(p_228548_0_, p_228548_0_, p_228548_0_);
		RenderSystem.matrixMode(5888);
	}
	
	@Override
	public void render(MelonBlockTileEntity entity, float partialTicks, MatrixStack matrixStackIn,
			IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
		MelonBlock block = (MelonBlock) entity.getBlockState().getBlock();
		if(block == null) return ;
		matrixStackIn.pushPose();
		/*matrixStackIn.scale(- 1, - 1, 1);
		float size = 1F;
		matrixStackIn.scale(size, size, size);
		matrixStackIn.translate(- 0.5D, -2.25D, 0.5D);
		Direction facing = entity.getBlockState().getValue(AbstractFacingBlock.FACING);
		if(facing == Direction.SOUTH) matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(180));
		else if(facing == Direction.WEST) matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(- 90));
		else if(facing == Direction.EAST) matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(90));*/
		GL11.glPushMatrix();//glstart
		Minecraft mc = Minecraft.getInstance();
		ActiveRenderInfo activeRenderInfoIn = Minecraft.getInstance().getEntityRenderDispatcher().camera;
		activeRenderInfoIn.setup(mc.level, (Entity)(mc.getCameraEntity() == null ? mc.player : mc.getCameraEntity()), 
		!mc.options.getCameraType().isFirstPerson(), mc.options.getCameraType().isMirrored(), partialTicks);
		Vector3d avector3d = activeRenderInfoIn.getPosition();
		double camx = avector3d.x();
		double camy = avector3d.y();
		double camz = avector3d.z();
		double d0 = entity.getBlockPos().getX();
		double d1 = entity.getBlockPos().getY();
		double d2 = entity.getBlockPos().getZ();
		double xIn = d0 - camx;
		double yIn = d1 - camy;
		double zIn = d2 - camz;
    	
		net.minecraftforge.client.event.EntityViewRenderEvent.CameraSetup cameraSetup = net.minecraftforge.client.ForgeHooksClient.onCameraSetup(mc.gameRenderer, activeRenderInfoIn, partialTicks);
		activeRenderInfoIn.setAnglesInternal(cameraSetup.getYaw(), cameraSetup.getPitch());
		GL11.glRotatef(cameraSetup.getRoll(), 0.0F, 0.0F, 1.0F);
		GL11.glRotatef(activeRenderInfoIn.getXRot(), 1.0F, 0.0F, 0.0F);
		GL11.glRotatef(activeRenderInfoIn.getYRot() + 180.0F, 0.0F, 1.0F, 0.0F);
		GL11.glTranslatef((float) xIn+0.5F, (float) yIn, (float) zIn+0.5F);
		Direction facing = entity.getBlockState().getValue(AbstractFacingBlock.FACING);
		if(facing == Direction.SOUTH)GL11.glRotatef(180F, 0.0F, 1.0F, 0.0F);
		else if(facing == Direction.WEST)GL11.glRotatef(-90F, 0.0F, 1.0F, 0.0F);
		else if(facing == Direction.EAST)GL11.glRotatef(90F, 0.0F, 1.0F, 0.0F);
		if(block.lvl > 4){
			mc.getTextureManager().bind(tex);
			//soldier_type.renderPart("box");
			/*GL11.glTranslatef(0, 0, 0);
			GL11.glRotatef(block.iii*1F, 0.0F, 1.0F, 0.0F);
			GL11.glTranslatef(0, 0, 0);*/
			GlStateManager._disableCull();//
			GlStateManager._depthMask(false);//
			RenderSystem.enableBlend();
			//SARenderHelper.enableBlendMode(RenderTypeSA.ADDITIVE);//ADDITIVE
			//GL11.glTranslatef(0, 0.5F, 0);
			if(block.lvl==5)soldier_type.renderPart("assult");
			if(block.lvl==6)soldier_type.renderPart("recon");
			if(block.lvl==7)soldier_type.renderPart("engineer");
			if(block.lvl==8)soldier_type.renderPart("medic");
			if(block.lvl==9)soldier_type.renderPart("support");
			//SARenderHelper.disableBlendMode(RenderTypeSA.ADDITIVE);//
			RenderSystem.disableBlend();
			GlStateManager._depthMask(true);
			GlStateManager._enableCull();
		}
		if(block.lvl == 1){
			setupGlintTexturing(8F);
			mc.getTextureManager().bind(ENCHANT_GLINT_LOCATION);
			RenderSystem.enableBlend();
			RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
			GL11.glPushMatrix();//glstart
			obj.renderPart("enc");
			GL11.glPopMatrix();//glend
			RenderSystem.matrixMode(5890);
			RenderSystem.popMatrix();
			RenderSystem.matrixMode(5888);
			RenderSystem.defaultBlendFunc();
			RenderSystem.disableBlend();
		}
		GL11.glPopMatrix();//glend
		matrixStackIn.popPose();
	}
}
