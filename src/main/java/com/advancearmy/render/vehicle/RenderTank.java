package advancearmy.render.vehicle;
import com.mojang.math.Axis;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;


import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import advancearmy.entity.land.EntitySA_Tank;
import wmlib.client.obj.SAObjModel;
import net.minecraft.client.renderer.RenderType;
import wmlib.client.render.SARenderState;
import wmlib.client.render.RenderTypeVehicle;
import net.minecraft.Util;
import org.joml.Matrix4f;
import net.minecraft.client.Camera;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.common.MinecraftForge;
import advancearmy.render.ModelNoneVehicle;
@OnlyIn(Dist.CLIENT)
public class RenderTank extends MobRenderer<EntitySA_Tank, ModelNoneVehicle<EntitySA_Tank>> {
	public ResourceLocation tex = ResourceLocation.tryParse("wmlib:textures/entity/m1tank.png");
	private static final SAObjModel m2hb = new SAObjModel("advancearmy:textures/mob/m2hb.obj");
	private static final ResourceLocation m2tex = ResourceLocation.tryParse("advancearmy:textures/mob/m2hb.png");
	private static final ResourceLocation m2tex2 = ResourceLocation.tryParse("advancearmy:textures/mob/m2hb_2.png");
	private static final ResourceLocation tex1 = ResourceLocation.tryParse("advancearmy:textures/mob/m1tank.png");
	private static final ResourceLocation tex2 = ResourceLocation.tryParse("advancearmy:textures/mob/m1tank2.png");
	private static final SAObjModel obj = new SAObjModel("wmlib:textures/entity/m1tank.obj");
	private static final ResourceLocation tankmflash = ResourceLocation.tryParse("advancearmy:textures/entity/flash/tankmflash.png");
	private static final ResourceLocation fire_tex = ResourceLocation.tryParse("advancearmy:textures/entity/flash/muzzleflash3.png");
	public static final ResourceLocation ENCHANT_GLINT_LOCATION = ResourceLocation.tryParse("wmlib:textures/misc/vehicle_glint.png");
	RenderType rt = RenderTypeVehicle.objrender(tex);
	RenderType rt1 = RenderTypeVehicle.objrender(m2tex);
	RenderType f1 = SARenderState.getBlendDepthWrite(fire_tex);
	RenderType f2 = SARenderState.getBlendDepthWrite(tankmflash);
	RenderType glint = SARenderState.getBlendGlowGlint(ENCHANT_GLINT_LOCATION);
	
    public RenderTank(EntityRendererProvider.Context context) {
        super(context, new ModelNoneVehicle(),4.0F);
        this.shadowRadius = 4.0F;
    }
    @Override
    public ResourceLocation getTextureLocation(EntitySA_Tank entity) {
        return tex;
    }

    private void render_part(EntitySA_Tank entity, String name, PoseStack stack, int packedLight) {
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
	private void render_wheel(PoseStack stack, EntitySA_Tank entity, String name, float x, float y, float z, float rote){
		stack.pushPose();//glstart
		stack.translate(x, y, z);//
		stack.mulPose(Axis.XP.rotationDegrees(entity.thpera));
		stack.translate(-x, -y, -z);//
		stack.translate(x, y, z);
		obj.renderPart(name);
		stack.popPose();//glend
	}

	// 使用实例变量
	private float recoilTime = 0f; // 用于三角函数的时间参数
	private float recoilIntensity = 0f; // 震动强度
	private float[] randomFactors = new float[3]; // 存储随机因素
	public static double noise(double x, double y) {
		// 实现2D Perlin噪声
		// 这里可以使用现有的噪声库，或者简化实现
		return Math.sin(x * 10 + y * 5) * 0.5 + 0.5; // 简化版本
	}
	public float currentTilt = 0;
	public double prevHorizontalVelocity = 0;
	public double prevDotProduct = 0;
	public int stopTime = 0;
	public double getMaxSpeed() {
		return 0.3;
	}
	
    @Override
    public void render(EntitySA_Tank entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight0) {
        super.render(entity, entityYaw, partialTicks, stack, buffer, packedLight0);
		int packedLight=packedLight0;
		//if(entity.anim1<3)packedLight=0xF000F0;
		if(entity.getTargetType()==2){
			RenderSystem.setShaderColor(0.7F, 0.4F, 0.4F,1F);
			obj.setColor(-999, 0, 0, 1F);
			m2hb.setColor(-999, 0, 0, 1F);
		}
		if(entity.deathTime > 0){
			RenderSystem.setShaderColor(0.1F, 0.1F, 0.1F, 1F);
			obj.setColor(-999, 0, 0, 1F);
			m2hb.setColor(-999, 0, 0, 1F);
		}
		obj.setRender(rt, null, stack, packedLight);
		stack.pushPose();
		Minecraft mc = Minecraft.getInstance();
		// 1.20.1 版本的等效代码
		/*Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
		camera.setRender(null, mc.level, (Entity)(mc.getCameraEntity() == null ? mc.player : mc.getCameraEntity()), 
			!mc.options.getCameraType().isFirstPerson(), mc.options.getCameraType().isMirrored(), partialTicks);
		Vec3 cameraPos = camera.getPosition();
		double camx = cameraPos.x;
		double camy = cameraPos.y;
		double camz = cameraPos.z;
		double d0 = Mth.lerp(partialTicks, entity.xOld, entity.getX());
		double d1 = Mth.lerp(partialTicks, entity.yOld, entity.getY());
		double d2 = Mth.lerp(partialTicks, entity.zOld, entity.getZ());
		double xIn = d0 - camx;
		double yIn = d1 - camy;
		double zIn = d2 - camz;
		// 使用事件系统处理相机设置
		float yaw = camera.getYRot();
		float pitch = camera.getXRot();
		float roll = 0;
		net.minecraftforge.client.event.ViewportEvent.ComputeCameraAngles event = 
			new net.minecraftforge.client.event.ViewportEvent.ComputeCameraAngles(
				mc.gameRenderer, camera, partialTicks, yaw, pitch, roll);
		MinecraftForge.EVENT_BUS.post(event);
		stack.mulPoseMatrix(RenderSystem.getModelViewMatrix());
		stack.mulPose(Axis.ZP.rotationDegrees(event.getRoll()));
		stack.mulPose(Axis.XP.rotationDegrees(camera.getXRot()));
		stack.mulPose(Axis.YP.rotationDegrees(camera.getYRot() + 180.0F));
		stack.translate(xIn, yIn, zIn);
		stack.mulPose(Axis.YP.rotationDegrees(180F));
		RenderSystem.applyModelViewMatrix();*/

		if(entity.anim1 < 24) {
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
			float progress = entity.anim1 / 24.0f;
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
			if(entity.anim1 < 24) {
				float progress = entity.anim1 / 24.0f;
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
		
		
		{
			//boolean flag1 = false;
			//
			//flag1 = angle_system.angle_rote(entity);
			stack.mulPose(Axis.YP.rotationDegrees(180.0F));
			stack.mulPose(Axis.YP.rotationDegrees(180.0F - (entity.yHeadRotO + (entity.yHeadRot - entity.yHeadRotO) * partialTicks)));
			
		}
		
		stack.mulPose(Axis.XP.rotationDegrees(entity.currentTilt));

		float limbSwing = this.F6(entity, partialTicks);//
		float limbSwingAmount = this.F5(entity, partialTicks);//
		float Ax1 = (float)Math.cos(limbSwing * 0.6662F + (float)Math.PI) * limbSwingAmount;//
		stack.translate(0, Ax1 * (180F / (float)Math.PI) * 0.0002F , 0);
		stack.mulPose(Axis.XP.rotationDegrees(Ax1 * (180F / (float)Math.PI) * 0.0002F));
		stack.mulPose(Axis.YP.rotationDegrees(Ax1 * (180F / (float)Math.PI) * 0.0002F));
		
		render_part(entity,"body", stack, packedLight);
		stack.pushPose();//glstart
		stack.translate(1.7F, 0, 0F);
		if(entity.trackr % 8 == 0) {
			obj.renderPart("track1");
		}
		else if(entity.trackr % 7 == 0) {
			obj.renderPart("track2");
		}
		else if(entity.trackr % 6 == 0) {
			obj.renderPart("track3");
		}
		else if(entity.trackr % 5 == 0) {
			obj.renderPart("track4");
		}
		else if(entity.trackr % 4 == 0) {
			obj.renderPart("track5");
		}
		else if(entity.trackr % 3 == 0) {
			obj.renderPart("track6");
		}
		else if(entity.trackr % 2 == 0) {
			obj.renderPart("track7");
		}
		else {
			obj.renderPart("track8");
		}
		stack.popPose();//glend
		stack.pushPose();//glstart
		stack.translate(-1.7F, 0, 0F);
		if(entity.trackl % 8 == 0) {
			obj.renderPart("track1");
		}
		else if(entity.trackl % 7 == 0) {
			obj.renderPart("track2");
		}
		else if(entity.trackl % 6 == 0) {
			obj.renderPart("track3");
		}
		else if(entity.trackl % 5 == 0) {
			obj.renderPart("track4");
		}
		else if(entity.trackl % 4 == 0) {
			obj.renderPart("track5");
		}
		else if(entity.trackl % 3 == 0) {
			obj.renderPart("track6");
		}
		else if(entity.trackl % 2 == 0) {
			obj.renderPart("track7");
		}
		else {
			obj.renderPart("track8");
		}
		stack.popPose();//glend
		render_wheel(stack, entity, "wheel1", 0, 0.98F+0.25F, 3.36F, entity.thpera);
		float range = 0;
		{
			range = 0.0005F;
		}
		stack.pushPose();//glstart
		stack.translate(0, Ax1 * (180F / (float)Math.PI) * -range, 0);
		render_wheel(stack, entity, "wheel1", 0, 0.48F+0.1F, 2.28F, entity.thpera);
		stack.popPose();//glend
		stack.pushPose();//glstart
		stack.translate(0, Ax1 * (180F / (float)Math.PI) * range, 0);
		render_wheel(stack, entity, "wheel1", 0, 0.48F, 1.21F, entity.thpera);
		stack.popPose();//glend
		stack.pushPose();//glstart
		stack.translate(0, Ax1 * (180F / (float)Math.PI) * -range, 0);
		render_wheel(stack, entity, "wheel1", 0, 0.48F, 0.35F, entity.thpera);
		stack.popPose();//glend
		stack.pushPose();//glstart
		stack.translate(0, Ax1 * (180F / (float)Math.PI) * range, 0);
		render_wheel(stack, entity, "wheel1", 0, 0.48F, -0.5F, entity.thpera);
		stack.popPose();//glend
		stack.pushPose();//glstart
		stack.translate(0, Ax1 * (180F / (float)Math.PI) * -range, 0);
		render_wheel(stack, entity, "wheel1", 0, 0.48F, -1.37F, entity.thpera);
		stack.popPose();//glend
		stack.pushPose();//glstart
		stack.translate(0, Ax1 * (180F / (float)Math.PI) * range, 0);
		render_wheel(stack, entity, "wheel1", 0, 0.48F, -2.26F, entity.thpera);
		stack.popPose();//glend
		stack.pushPose();//glstart
		stack.translate(0, Ax1 * (180F / (float)Math.PI) * -range, 0);
		stack.popPose();//glend
		stack.pushPose();//glstart
		stack.translate(0, Ax1 * (180F / (float)Math.PI) * range, 0);
		render_wheel(stack, entity, "wheel1", 0, 0.48F, -3.16F, entity.thpera);
		stack.popPose();//glend
		render_wheel(stack, entity, "wheel2", 0, 0.97F+0.05F, -4.43F, entity.thpera);
		{
			float size2 = entity.level().getRandom().nextInt(4) * 0.3F + 1;
			stack.pushPose();//glstart
			stack.translate(0F, 0F, 0F);
			stack.mulPose(Axis.YP.rotationDegrees(180.0F - (entity.turretYawO + (entity.turretYaw - entity.turretYawO) * partialTicks) -(180.0F - (entity.yHeadRotO + (entity.yHeadRot - entity.yHeadRotO) * partialTicks))));
			stack.translate(-0F, -0F, -0F);
			render_part(entity,"mat4", stack, packedLight);
			render_part(entity,"close", stack, packedLight);

			m2hb.setRender(rt1, null, stack, packedLight);
			
			stack.pushPose();//glstart
			stack.translate(-0.64F, 3.51F, -1.06F);
			stack.mulPose(Axis.YP.rotationDegrees(180.0F - (entity.turretYawO1 + (entity.turretYaw1 - entity.turretYawO1) * partialTicks)-(180.0F - entity.turretYaw)));
			stack.translate(0.64F, -3.51F, 1.06F);
			stack.pushPose();//glstart
			stack.translate(-0.64F, 3.51F, -1.06F);
			m2hb.renderPart("turret");
			stack.popPose();//glend
			stack.translate(-0.64F, 3.51F, -0.68F);
			stack.mulPose(Axis.XP.rotationDegrees((entity.turretPitchO1 + (entity.turretPitch1 - entity.turretPitchO1) * partialTicks)));
			stack.translate(0.64F, -3.51F, 0.68F);
			stack.pushPose();//glstart
			stack.translate(-0.64F, 3.51F, -1.06F);
			m2hb.renderPart("barrel");
			if(entity.ammo){
				m2hb.renderPart("box");
				if(entity.count==0)m2hb.renderPart("ammo1");
				if(entity.count==1)m2hb.renderPart("ammo2");
				if(entity.count==2)m2hb.renderPart("ammo3");
				if(entity.anim3<4){
					if(entity.level().getRandom().nextInt(4)==1){
						m2hb.renderPart("shell1");
					}
					if(entity.level().getRandom().nextInt(4)==2){
						m2hb.renderPart("shell2");
					}
					if(entity.level().getRandom().nextInt(4)==3){
						m2hb.renderPart("shell3");
					}
					if(entity.level().getRandom().nextInt(4)==0){
						m2hb.renderPart("shell4");
					}
				}
			}

			m2hb.setRender(f1,null,null,0xF000F0);
			stack.scale(size2*1.5F, size2*1.5F, 1);
			if(entity.anim3<5)m2hb.renderPart("flash");
			
			stack.popPose();//glend
			stack.popPose();//glend
			
			obj.setRenderType(rt);
			obj.setRenderLight(packedLight);
			stack.translate(0F, 2.41F, 2F);
			stack.mulPose(Axis.XP.rotationDegrees((entity.turretPitchO + (entity.turretPitch - entity.turretPitchO) * partialTicks)));
			stack.translate(0F, -2.41F, -2F);
			render_part(entity,"mat5", stack, packedLight);
			stack.pushPose();//glstart
			stack.translate(-0.23F, 2.41F, 2.96F);
			stack.scale(size2, size2, size2);
			
			obj.setRender(f1,null,null,0xF000F0);
			if(entity.anim2<8){
				obj.renderPart("flash2");
			}
			
			stack.popPose();//glend
			stack.pushPose();//glstart
			stack.translate(entity.fireposX1, entity.fireposY1, entity.fireposZ1);
			if(entity.anim1 < 8){
				float size = (1 + partialTicks * 0.1F)*(float)(4+entity.anim1) / 4F;
				stack.scale(size,size,size);
			}
			if(entity.anim1 >= 8 && entity.anim1<18){
				float size = (1 + partialTicks * 0.1F)*(float)(20-entity.anim1) / 6F;
				stack.scale(size,size,size);
			}
			stack.translate(-entity.fireposX1, -entity.fireposY1, -entity.fireposZ1);
			stack.translate(entity.fireposX1, entity.fireposY1, entity.fireposZ1);
			
			obj.setRender(f2,null,null,0xF000F0);
			if(entity.anim1<18){
				obj.renderPart("flash1");
			}
			
			stack.popPose();//glend
			
			{
				if(entity.anim1 >= 0 && entity.anim1 < 4){
					stack.translate(0.0F, 0.0F, -entity.anim1 * 0.4F);
				}
				if(entity.anim1 >= 4 && entity.anim1 < 15){
					stack.translate(0.0F, 0.0F, -1.2F);
					stack.translate(0.0F, 0.0F, entity.anim1 * 0.1F);
				}
			}
			obj.setRenderLight(packedLight);
			obj.setRenderType(rt);
			render_part(entity,"mat6", stack, packedLight);
			stack.popPose();//glend
		}
	    stack.popPose();
		RenderSystem.setShaderColor(1, 1, 1, 1);
    }
	
	public float F6(LivingEntity entity, float partialTicks) {
		float position = 0.0F;
		return position;
	}
	public float F5(LivingEntity entity, float partialTicks) {
		float speed = 0.0F;
		return speed;
	}
}