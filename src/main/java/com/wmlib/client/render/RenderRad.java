package wmlib.client.render;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import wmlib.common.bullet.EntityRad;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.client.Minecraft;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import wmlib.client.obj.SAObjModel;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.RenderType;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.client.renderer.culling.Frustum;
import wmlib.client.render.SARenderState;
import net.minecraft.util.RandomSource;
import org.joml.Matrix4f;
@OnlyIn(Dist.CLIENT)
public class RenderRad extends EntityRenderer<EntityRad>
{
	public ResourceLocation tex = ResourceLocation.tryParse("wmlib:textures/block/rad_1.png");
    private static final SAObjModel obj = new SAObjModel("wmlib:textures/block/b_rad.obj");
	RenderType f1 = SARenderState.getBlendDepthWrite(tex);
    public RenderRad(EntityRendererProvider.Context context) {
        super(context);
    }

    public ResourceLocation getTextureLocation(EntityRad entity)
    {
		return tex;
    }
	public boolean shouldRender(EntityRad p_225626_1_, Frustum p_225626_2_, double p_225626_3_, double p_225626_5_, double p_225626_7_) {
      return true;
	}
	
	
    // 动画周期参数
    int ANIMATION_DURATION = 360; // 动画总时长(tick)
    float FADE_START_RATIO = 0.8f; // 淡出开始的百分比(0-1)
    float FADE_DURATION_RATIO = 0.2f; // 淡出持续时间的百分比(0-1)
    
    // 尺寸参数
    float BASE_SCALE = 1.0f; // 基础缩放
    float MAX_SCALE = 2.0f; // 最大缩放倍数
    
    // 粒子参数
    float PARTICLE_COUNT_MULTIPLIER = 60.0f; // 粒子数量系数
    float PARTICLE_BASE_SIZE = 5.0f; // 粒子基础尺寸
    float PARTICLE_VARIATION = 20.0f; // 粒子尺寸变化范围
    float PARTICLE_GROWTH_FACTOR = 10.0f; // 粒子生长系数
    
    // 三角形参数
    float TRIANGLE_WIDTH = 1.0f; // 三角形宽度系数
    float TRIANGLE_HEIGHT = 1.0f; // 三角形高度系数
    
    // 旋转参数
    float ROTATION_SPEED = 90.0f; // 旋转速度系数
	
    private static final float HALF_SQRT_3 = (float)(Math.sqrt(3.0) / 2.0);
    public void render(EntityRad entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight)
    {
		super.render(entity, entityYaw, partialTicks, stack, buffer, packedLight);
		{
			stack.pushPose();
			Minecraft mc = Minecraft.getInstance();
			if(entity.getRadType()==2){
				float size = ((float)entity.getStayTime() + partialTicks) / entity.max_time;
				// 修改：在0~0.1的进度内完成粒子数量的变化，但旋转速度继续随时间增长
				float particleProgress = Math.min(size / 0.1f, 1.0f); // 0~0.1映射到0~1，仅用于粒子数量
				float size2 = Math.min(size > 0.8f ? (size - 0.8f) / 0.2f : 0.0f, 1.0f);
				RandomSource rand = RandomSource.create(432L);
				VertexConsumer vb = buffer.getBuffer(SARenderState.getLightning());
				stack.pushPose();
				stack.translate(0.0f, 5, 0);
				int count = 0;
				
				// 修改：粒子数量在particleProgress=1时达到60，之后保持
				int targetParticleCount = (int)(particleProgress * 60.0f);
				if (particleProgress >= 1.0f) {
					targetParticleCount = 60; // 达到最大值后保持
				}
				
				while (count < targetParticleCount) {
					stack.mulPose(Axis.XP.rotationDegrees(rand.nextFloat() * 360.0f));
					stack.mulPose(Axis.YP.rotationDegrees(rand.nextFloat() * 360.0f));
					stack.mulPose(Axis.ZP.rotationDegrees(rand.nextFloat() * 360.0f));
					stack.mulPose(Axis.XP.rotationDegrees(rand.nextFloat() * 360.0f));
					stack.mulPose(Axis.YP.rotationDegrees(rand.nextFloat() * 360.0f));
					
					// 修改：恢复原来的旋转速度逻辑，让旋转继续随时间增强
					// 使用原始的size变量，而不是progress，这样旋转会持续增强
					stack.mulPose(Axis.ZP.rotationDegrees(rand.nextFloat() * 360.0f + size * 200.0f));
					
					float rand1 = rand.nextFloat() * 20.0f + 20.0f + size2 * 40.0f;
					float rand2 = rand.nextFloat() * 2.0f + 1.0f + size2 * 1.0f;
					Matrix4f mart = stack.last().pose();
					int count2 = (int)(255.0f * (1.0f - size2));
					RenderRad.vertex01(vb, mart, count2);
					RenderRad.vertex2(vb, mart, rand1, rand2);
					RenderRad.vertex3(vb, mart, rand1, rand2);
					RenderRad.vertex01(vb, mart, count2);
					RenderRad.vertex3(vb, mart, rand1, rand2);
					RenderRad.vertex4(vb, mart, rand1, rand2);
					RenderRad.vertex01(vb, mart, count2);
					RenderRad.vertex4(vb, mart, rand1, rand2);
					RenderRad.vertex2(vb, mart, rand1, rand2);
					++count;
				}
				stack.popPose();
			}else{
				if(entity.isAggressive()){
					RenderSystem.setShaderColor(0.0F, 1.0F, 0.0F, entity.iii*0.02F);
				}else{
					RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, entity.iii*0.02F);
				}
				obj.setRender(f1, null, stack, packedLight);
				stack.mulPose(Axis.YP.rotationDegrees(180.0F));
				renderOptimized(stack,entity);
			}
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
			stack.popPose();
		}
	}
	
	private static void vertex01(VertexConsumer vx, Matrix4f mart, int alpha) {
		vx.vertex(mart, 0.0f, 0.0f, 0.0f).color(255, 255, 255, alpha).endVertex();
	}
	static float widthScale = 1f;
	static float heightScale = 1f;
	private static void vertex2(VertexConsumer vx, Matrix4f mart, float y, float z) {
		vx.vertex(mart, 
				  -HALF_SQRT_3 * z * widthScale, 
				  y * heightScale, 
				  -0.5f * z * widthScale)
		  .color(255, 100, 0, 0).endVertex();
	}

	private static void vertex3(VertexConsumer vx, Matrix4f mart, float y, float z) {
		vx.vertex(mart, 
				  HALF_SQRT_3 * z * widthScale, 
				  y * heightScale, 
				  -0.5f * z * widthScale)
		  .color(255, 200, 0, 0).endVertex();
	}

	private static void vertex4(VertexConsumer vx, Matrix4f mart, float y, float z) {
		vx.vertex(mart, 
				  0.0f, 
				  y * heightScale, 
				  1.0f * z * widthScale)
		  .color(255, 100, 0, 0).endVertex();
	}
	
	public void renderOptimized(PoseStack stack, EntityRad entity) {
		final int centerX = (int) entity.getX();
		final int centerY = (int) entity.getY()+1;
		final int centerZ = (int) entity.getZ();
		final int range = entity.range;
		final int verticalRange = Math.max(1, (int) (range * 0.2F));
		final Level level = entity.level();
		// 使用 mutable BlockPos 避免创建新对象
		BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
		for (int dx = -range; dx <= range; dx += 1) { // 可以调整步长来优化
			for (int dz = -range; dz <= range; dz += 1) {
				// 快速距离检查 - 跳过明显超出范围的位置
				if (dx * dx + dz * dz > range * range) continue;
				for (int dy = -verticalRange; dy <= verticalRange; dy += 1) {
					mutablePos.set(centerX + dx, centerY + dy, centerZ + dz);
					BlockState groundState = level.getBlockState(mutablePos);
					if (groundState.isAir()) continue;
					// 检查上方的方块
					mutablePos.move(Direction.UP);
					BlockState aboveState = level.getBlockState(mutablePos);
					mutablePos.move(Direction.DOWN); // 恢复位置
					if (aboveState.isAir() && level.random.nextBoolean()) {
						stack.pushPose();
						stack.translate(-dx-1, dy + 2, -dz-1);
						obj.renderPart("mat1");
						stack.popPose();
					}
				}
			}
		}
	}
}