package wmlib.client.render;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import wmlib.common.bullet.EntityRad;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.renderer.culling.ClippingHelper;
import wmlib.client.obj.SAObjModel;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.math.vector.Vector3d;
import wmlib.client.render.SARenderHelper;
import wmlib.client.render.SARenderHelper.RenderTypeSA;
import net.minecraft.client.renderer.RenderType;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.Direction;
import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class RenderRad extends MobRenderer<EntityRad, ModelNone<EntityRad>>
{
	public ResourceLocation tex = new ResourceLocation("wmlib:textures/blocks/rad_1.png");
    private static final SAObjModel obj = new SAObjModel("wmlib:textures/blocks/b_rad.obj");
	
    public RenderRad(EntityRendererManager renderManagerIn)
    {
    	super(renderManagerIn, new ModelNone(),0.5F);
    }
	public boolean shouldRender(EntityRad p_225626_1_, ClippingHelper p_225626_2_, double p_225626_3_, double p_225626_5_, double p_225626_7_) {
       return true;
	}
    public ResourceLocation getTextureLocation(EntityRad entity)
    {
		return tex;
    }
	private static final float HALF_SQRT_3 = (float)(Math.sqrt(3.0D) / 2.0D);
	int time = 0;
    public void render(EntityRad entity, float entityYaw, float partialTicks, MatrixStack stack, IRenderTypeBuffer bufferIn, int packedLightIn)
    {
		super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
		{
	    stack.pushPose();

		GL11.glPushMatrix();

		if(time<360){
			++time;
		}else{
			time = 0;
		}
		SARenderHelper.enableBlendMode(RenderTypeSA.ADDITIVE);//ALPHA
		/*{
         float f5 = ((float)time + partialTicks) / 200.0F;
         float f7 = Math.min(f5 > 0.8F ? (f5 - 0.8F) / 0.2F : 0.0F, 1.0F);
         Random random = new Random(432L);
         IVertexBuilder ivertexbuilder2 = bufferIn.getBuffer(RenderType.lightning());
         stack.pushPose();
         stack.translate(0.0D, -1.0D, -2.0D);

         for(int i = 0; (float)i < (f5 + f5 * f5) / 2.0F * 60.0F; ++i) {
            stack.mulPose(Vector3f.XP.rotationDegrees(random.nextFloat() * 360.0F));
            stack.mulPose(Vector3f.YP.rotationDegrees(random.nextFloat() * 360.0F));
            stack.mulPose(Vector3f.ZP.rotationDegrees(random.nextFloat() * 360.0F));
            stack.mulPose(Vector3f.XP.rotationDegrees(random.nextFloat() * 360.0F));
            stack.mulPose(Vector3f.YP.rotationDegrees(random.nextFloat() * 360.0F));
            stack.mulPose(Vector3f.ZP.rotationDegrees(random.nextFloat() * 360.0F + f5 * 90.0F));
            float f3 = random.nextFloat() * 20.0F + 5.0F + f7 * 10.0F;
            float f4 = random.nextFloat() * 2.0F + 1.0F + f7 * 2.0F;
            Matrix4f matrix4f = stack.last().pose();
            int j = (int)(255.0F * (1.0F - f7));
            vertex01(ivertexbuilder2, matrix4f, j);
            vertex2(ivertexbuilder2, matrix4f, f3, f4);
            vertex3(ivertexbuilder2, matrix4f, f3, f4);
            vertex01(ivertexbuilder2, matrix4f, j);
            vertex3(ivertexbuilder2, matrix4f, f3, f4);
            vertex4(ivertexbuilder2, matrix4f, f3, f4);
            vertex01(ivertexbuilder2, matrix4f, j);
            vertex4(ivertexbuilder2, matrix4f, f3, f4);
            vertex2(ivertexbuilder2, matrix4f, f3, f4);
         }

         stack.popPose();
      }*/

		Minecraft mc = Minecraft.getInstance();
		ActiveRenderInfo activeRenderInfoIn = Minecraft.getInstance().getEntityRenderDispatcher().camera;
		activeRenderInfoIn.setup(mc.level, (Entity)(mc.getCameraEntity() == null ? mc.player : mc.getCameraEntity()), 
		!mc.options.getCameraType().isFirstPerson(), mc.options.getCameraType().isMirrored(), partialTicks);
		Vector3d avector3d = activeRenderInfoIn.getPosition();
		double camx = avector3d.x();
		double camy = avector3d.y();
		double camz = avector3d.z();
		double d0 = MathHelper.lerp((double)partialTicks, entity.xOld, entity.getX());
		double d1 = MathHelper.lerp((double)partialTicks, entity.yOld, entity.getY());
		double d2 = MathHelper.lerp((double)partialTicks, entity.zOld, entity.getZ());
		double xIn = d0 - camx;
		double yIn = d1 - camy;
		double zIn = d2 - camz;
    	Minecraft.getInstance().getTextureManager().bind(tex);
		net.minecraftforge.client.event.EntityViewRenderEvent.CameraSetup cameraSetup = net.minecraftforge.client.ForgeHooksClient.onCameraSetup(mc.gameRenderer, activeRenderInfoIn, partialTicks);
		activeRenderInfoIn.setAnglesInternal(cameraSetup.getYaw(), cameraSetup.getPitch());
		GL11.glRotatef(cameraSetup.getRoll(), 0.0F, 0.0F, 1.0F);
		GL11.glRotatef(activeRenderInfoIn.getXRot(), 1.0F, 0.0F, 0.0F);
		GL11.glRotatef(activeRenderInfoIn.getYRot() + 180.0F, 0.0F, 1.0F, 0.0F);
		GL11.glTranslatef((float) xIn, (float) yIn, (float) zIn);
		
		GL11.glRotatef(180F, 0.0F, 1.0F, 0.0F);
		/*float size = partialTicks * 0.5F + 1;//0.4F
		GlStateManager._scalef(size, size, size);*/
		GL11.glPushMatrix();//glstart
		/*GlStateManager._disableCull();//
		GlStateManager._depthMask(false);//
		RenderSystem.enableBlend();*/
		
		if(entity.isAggressive()){
			GL11.glColor4f(0.0F, 1.0F, 0.0F, entity.iii*0.02F);
		}else{
			GL11.glColor4f(1.0F, 1.0F, 1.0F, entity.iii*0.02F);
		}
		
		/*for(int k2 = -entity.range; k2 <= entity.range; ++k2) {
		  for(int l2 = -entity.range; l2 <= entity.range; ++l2) {
			 for(int j = -(int)(entity.range*0.2F); j <= (int)(entity.range*0.2F); ++j) {
				int i3 = (int)entity.getX() + k2;
				int k = (int)entity.getY() + j;
				int l = (int)entity.getZ() + l2;
				BlockPos blockpos = new BlockPos(i3, k, l);
				BlockState blockstate = entity.level.getBlockState(blockpos);
				BlockPos blockpos1 = new BlockPos(i3, k+1, l);
				BlockState blockstate1 = entity.level.getBlockState(blockpos1);
				if (!blockstate.isAir(entity.level, blockpos) && blockstate1.isAir(entity.level, blockpos1)){
					if(entity.level.random.nextInt(2)==1){
						GL11.glPushMatrix();//glstart
						GL11.glTranslatef((float) -k2, (float) j, (float) -l2);
						obj.renderPart("mat1");
						GL11.glPopMatrix();//glend
						
					}
				}
			 }
		  }
		}*/
		renderOptimized(entity);
		SARenderHelper.disableBlendMode(RenderTypeSA.ADDITIVE);//ADDITIVE
		/*RenderSystem.disableBlend();
		GlStateManager._depthMask(true);
		GlStateManager._enableCull();*/
	   
		GL11.glPopMatrix();//glend
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		GL11.glPopMatrix();
	    stack.popPose();
		}
	}
	
	public void renderOptimized(EntityRad entity) {
		final int centerX = (int) entity.getX();
		final int centerY = (int) entity.getY()+1;
		final int centerZ = (int) entity.getZ();
		final int range = entity.range;
		final int verticalRange = Math.max(1, (int) (range * 0.2F));
		// 使用 mutable BlockPos 避免创建新对象
		BlockPos.Mutable mutablePos = new BlockPos.Mutable();
		for (int dx = -range; dx <= range; dx += 1) { // 可以调整步长来优化
			for (int dz = -range; dz <= range; dz += 1) {
				// 快速距离检查 - 跳过明显超出范围的位置
				if (dx * dx + dz * dz > range * range) continue;
				for (int dy = -verticalRange; dy <= verticalRange; dy += 1) {
					mutablePos.set(centerX + dx, centerY + dy, centerZ + dz);
					BlockState groundState = entity.level.getBlockState(mutablePos);
					if (groundState.isAir(entity.level, mutablePos)) continue;
					// 检查上方的方块
					mutablePos.move(Direction.UP);
					BlockState aboveState = entity.level.getBlockState(mutablePos);
					mutablePos.move(Direction.DOWN); // 恢复位置
					if (aboveState.isAir(entity.level, mutablePos) && entity.level.random.nextBoolean()) {
						GL11.glPushMatrix();//glstart
						GL11.glTranslatef(-dx-1, dy + 1, -dz-1);
						obj.renderPart("mat1");
						GL11.glPopMatrix();//glend
					}
				}
			}
		}
	}
	
	/*private static void vertex01(IVertexBuilder p_229061_0_, Matrix4f p_229061_1_, int p_229061_2_) {
	  p_229061_0_.vertex(p_229061_1_, 0.0F, 0.0F, 0.0F).color(255, 255, 255, p_229061_2_).endVertex();
	  p_229061_0_.vertex(p_229061_1_, 0.0F, 0.0F, 0.0F).color(255, 255, 255, p_229061_2_).endVertex();
	}

	private static void vertex2(IVertexBuilder p_229060_0_, Matrix4f p_229060_1_, float p_229060_2_, float p_229060_3_) {
	  p_229060_0_.vertex(p_229060_1_, -HALF_SQRT_3 * p_229060_3_, p_229060_2_, -0.5F * p_229060_3_).color(255, 0, 255, 0).endVertex();
	}

	private static void vertex3(IVertexBuilder p_229062_0_, Matrix4f p_229062_1_, float p_229062_2_, float p_229062_3_) {
	  p_229062_0_.vertex(p_229062_1_, HALF_SQRT_3 * p_229062_3_, p_229062_2_, -0.5F * p_229062_3_).color(255, 0, 255, 0).endVertex();
	}

	private static void vertex4(IVertexBuilder p_229063_0_, Matrix4f p_229063_1_, float p_229063_2_, float p_229063_3_) {
	  p_229063_0_.vertex(p_229063_1_, 0.0F, p_229063_2_, 1.0F * p_229063_3_).color(255, 0, 255, 0).endVertex();
	}*/
}