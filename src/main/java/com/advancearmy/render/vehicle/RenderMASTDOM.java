package advancearmy.render.vehicle;

import com.mojang.math.Axis;
import org.lwjgl.opengl.GL12;

import advancearmy.entity.land.EntitySA_MASTDOM;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;

import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.BlockPos;


import wmlib.client.render.SARenderHelper;
import wmlib.client.render.SARenderHelper.RenderTypeSA;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;


import wmlib.client.render.SARenderState;
import wmlib.client.render.RenderTypeVehicle;
import org.joml.Matrix4f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.RenderType;
import wmlib.client.obj.SAObjModel;
import net.minecraft.client.CameraType;
import net.minecraft.Util;
import com.mojang.blaze3d.systems.RenderSystem;
import advancearmy.render.ModelNoneVehicle;
@OnlyIn(Dist.CLIENT)
public class RenderMASTDOM extends MobRenderer<EntitySA_MASTDOM, ModelNoneVehicle<EntitySA_MASTDOM>>
{
	private static final ResourceLocation tex = ResourceLocation.tryParse("advancearmy:textures/mob/mast.png");
	private static final SAObjModel obj = new SAObjModel("advancearmy:textures/mob/mast.obj");
	private SAObjModel powermodel = new SAObjModel("wmlib:textures/entity/flash/power.obj");
	private static final ResourceLocation fire_tex1 = ResourceLocation.tryParse("wmlib:textures/entity/flash/power1.png");
	private static final ResourceLocation fire_tex2 = ResourceLocation.tryParse("wmlib:textures/entity/flash/power3.png");
	public static final ResourceLocation ENCHANT_GLINT_LOCATION = ResourceLocation.tryParse("wmlib:textures/misc/vehicle_glint.png");
	
	RenderType rt = RenderTypeVehicle.objrender(tex);
	RenderType f1 = SARenderState.getBlendGlowGlint(fire_tex1);
	RenderType f2 = SARenderState.getBlendGlowGlint(fire_tex2);
	RenderType glint = SARenderState.getBlendGlowGlint(ENCHANT_GLINT_LOCATION);
	
    public RenderMASTDOM(EntityRendererProvider.Context context)
    {
    	super(context, new ModelNoneVehicle(),4F);
        this.shadowStrength = 4F;
    }

    public ResourceLocation getTextureLocation(EntitySA_MASTDOM entity)
    {
		return tex;
    }
    
    public boolean shouldRender(EntitySA_MASTDOM entity, Frustum camera, double camX, double camY, double camZ) {
        return super.shouldRender(entity, camera, camX, camY, camZ)||entity.distanceToSqr(camX,camY,camZ)<10;
    }
	
    private void render_part(PoseStack stack, EntitySA_MASTDOM entity, String name) {
        obj.renderPart(name);
        if (entity.getEnc() > 0){
            renderEnchantGlint(stack, name);
			obj.setRenderType(rt);
        }
    }

    private void renderEnchantGlint(PoseStack stack, String name) {
		obj.setRender(glint, null, null, 15728880);
        stack.scale(1.01001F, 1.01001F, 1.01001F);
        long time = (long)((double)Util.getMillis() * Minecraft.getInstance().options.glintSpeed().get() * 8.0);
        float u = (float)(time % 110000L) / 110000.0f;
        float v = (float)(time % 30000L) / 30000.0f;
		Matrix4f move = new Matrix4f().translation(u, v, 0.0f);
        RenderSystem.setTextureMatrix(move);
        obj.renderPart(name);
		RenderSystem.resetTextureMatrix();
    }

    private float currentTiltAngle = 0.0F;
    private float targetTiltAngle = 0.0F;
	
	int angle_time = 20;
	int move_time = 0;
	
	static float rote_wheel = 0;
    float iii;
	int barrel_r;
	private float recoilTime = 0f; // 用于三角函数的时间参数
	private float recoilIntensity = 0f; // 震动强度
	private float[] randomFactors = new float[3]; // 存储随机因素
	public static double noise(double x, double y) {
		return Math.sin(x * 10 + y * 5) * 0.5 + 0.5; // 简化版本
	}

    public void render(EntitySA_MASTDOM entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight0)
    {
		super.render(entity, entityYaw, partialTicks, stack, buffer, packedLight0);
	    stack.pushPose();
		stack.pushPose();
		
		Minecraft mc = Minecraft.getInstance();
		int packedLight=packedLight0;
		//if(entity.anim1<3)packedLight=0xF000F0;
		if(entity.getTargetType()==2){
			RenderSystem.setShaderColor(0.7F, 0.4F, 0.4F,1F);
			obj.setColor(-999, 0, 0, 1F);
		}
		if(entity.deathTime > 0){
			RenderSystem.setShaderColor(0.1F, 0.1F, 0.1F, 1F);
			obj.setColor(-999, 0, 0, 1F);
		}
		obj.setRender(rt, null, stack, packedLight);
		
		float wsize = 12*entity.w1barrelsize;
		if(entity.anim1 < wsize) {
			// 动画开始时初始化
			if(entity.anim1 == 0) {
				recoilTime = 0f;
				recoilIntensity = 1.0f; // 初始强度
				// 生成随机因素
				for(int i = 0; i < 3; i++) {
					randomFactors[i] = (entity.level().random.nextFloat() - 0.5f) * 0.4f;
				}
			}
			// 更新时间 - 基于动画进度
			float progress = entity.anim1 / wsize;
			recoilTime += 0.2f; // 控制振荡速度
			// 使用三角函数计算震动强度 - 随时间衰减
			recoilIntensity = (1.0f - progress) * (0.5f + 0.5f * (float)Math.sin(recoilTime));
			// 添加额外的随机高频震动
			float highFreqShake = (float)Math.sin(recoilTime * 3f) * 0.1f * (1.0f - progress);
		} else {
			recoilIntensity = 0f;
		}
		{
			stack.mulPose(Axis.YP.rotationDegrees(180.0F - entity.turretYaw));
			if(entity.anim1 < wsize) {
				float progress = entity.anim1 / wsize;
				// 使用不同的三角函数创建多轴震动
				float pitchShake = (float)Math.sin(recoilTime) * recoilIntensity * (1.0f + randomFactors[0]);
				float yawShake = (float)Math.sin(recoilTime * 0.8f + 0.5f) * recoilIntensity * (0.7f + randomFactors[1]);
				float rollShake = (float)Math.cos(recoilTime * 1.2f) * recoilIntensity * 0.3f * randomFactors[2];
				// 应用旋转
				stack.mulPose(Axis.XP.rotationDegrees(pitchShake * 6f)); // 俯仰
				stack.mulPose(Axis.YP.rotationDegrees(-yawShake * 5f));   // 偏航
				stack.mulPose(Axis.ZP.rotationDegrees(rollShake * 4f));    // 滚动
				// 使用余弦函数创建平移效果 - 更加平滑
				float pushBack = (float)Math.cos(recoilTime * 0.5f) * recoilIntensity * 0.02f;
				stack.translate(0, 0, -pushBack);
				// 高频随机震动 - 使用噪声函数增加真实感
				if(recoilIntensity > 0.3f) {
					float noiseX = (float)this.noise(recoilTime * 10f, 0) * 0.005f * recoilIntensity;
					float noiseY = (float)this.noise(0, recoilTime * 10f) * 0.005f * recoilIntensity;
					float noiseZ = (float)this.noise(recoilTime * 10f, recoilTime * 10f) * 0.005f * recoilIntensity;
					stack.translate(noiseX, noiseY, noiseZ);
				}
			}
			stack.mulPose(Axis.YP.rotationDegrees(-(180.0F - entity.turretYaw)));
		}
		
		stack.mulPose(Axis.YP.rotationDegrees(180.0F));
		
		{
			stack.mulPose(Axis.YP.rotationDegrees(180.0F - (entity.yRotO + (entity.getYRot() - entity.yRotO) * partialTicks)));
		}

		float limbSwing = this.F6(entity, partialTicks);//
		float limbSwingAmount = this.F5(entity, partialTicks);//
		float Ax1 = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * limbSwingAmount;//
		stack.translate(0, Ax1 * (180F / (float)Math.PI) * 0.0002F , 0);
		render_part(stack,entity,"mat1");

		obj.setRenderType(RenderTypeVehicle.objrender_track(tex));
		Matrix4f mover = new Matrix4f().translation(0, -entity.throttleRight, 0.0f);
        RenderSystem.setTextureMatrix(mover);
		obj.renderPart("track_r");
		RenderSystem.resetTextureMatrix();
		Matrix4f movel = new Matrix4f().translation(0, -entity.throttleLeft, 0.0f);
        RenderSystem.setTextureMatrix(movel);
		obj.renderPart("track_l");
		RenderSystem.resetTextureMatrix();
		//obj.setMoveTex(0,0);
		obj.setRenderType(rt);
		
		{
			if(entity.getRemain1()<2)iii=0;
			if(iii<entity.getRemain1()){
				iii+=0.8F;
			}else{
				iii-=1.5F;
			}
			float size2 = iii/10F * (1 + partialTicks * 0.1F*(10-iii)/10);
			stack.pushPose();
			stack.translate(0F, 0F, 0F);
			stack.mulPose(Axis.YP.rotationDegrees(180.0F - (entity.turretYawO + (entity.turretYaw - entity.turretYawO) * partialTicks) -(180.0F - (entity.yRotO + (entity.getYRot() - entity.yRotO) * partialTicks))));
			stack.translate(-0F, -0F, -0F);
			render_part(stack,entity,"mat4");
			
			stack.translate(0F, 3.18F, 2.2F);
			stack.mulPose(Axis.XP.rotationDegrees(entity.turretPitchO + (entity.turretPitch - entity.turretPitchO) * partialTicks));
			stack.translate(0F, -3.18F, -2.2F);
			render_part(stack,entity,"mat5");
			stack.pushPose();

			stack.translate(0F, 3.18F, 3.3F);
			stack.scale(size2*0.15F, size2*0.15F, size2*0.15F);
			//RenderSystem.setShaderColor(1F, 1F, 1F, entity.getRemain1()/ 20F);
			long time = (long)((double)Util.getMillis() * Minecraft.getInstance().options.glintSpeed().get() * 8.0);
			float u = (float)(time % 110000L) / 110000.0f;
			float v = (float)(time % 30000L) / 30000.0f;
			Matrix4f move = new Matrix4f().translation(u, v, 0.0f);
			powermodel.setRender(f1,null,stack,0xF000F0);
			RenderSystem.setTextureMatrix(move);
			powermodel.renderPart("mat1");
			powermodel.setRender(f2,null,stack,0xF000F0);
			powermodel.renderPart("mat3");
			RenderSystem.resetTextureMatrix();
			stack.popPose();

			stack.popPose();
		}
		
		stack.popPose();
		RenderSystem.setShaderColor(1, 1, 1, 1);
	    stack.popPose();
    }
	
    public float F6(LivingEntity entity, float partialTicks){
 		float f6 = 0;
 		if (!entity.isPassenger())
        {
            f6 = entity.walkAnimation.position() - entity.walkAnimation.speed() * (1.0F - partialTicks);
        }
 		return f6;
 	}
 	public float F5(LivingEntity entity, float partialTicks){
 		float f5 = 0;
 		if (!entity.isPassenger())
        {
            f5 = entity.walkAnimation.speed();
            if (f5 > 1.0F)
            {
                f5 = 1.0F;
            }
        }
 		return f5;
 	}
}