package advancearmy.render.vehicle;
import com.mojang.math.Axis;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import wmlib.client.render.RenderRote;
import net.minecraft.core.BlockPos;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import wmlib.client.render.SARenderState;
import wmlib.client.render.RenderTypeVehicle;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.player.Player;
import wmlib.client.obj.SAObjModel;
import net.minecraft.client.CameraType;
import net.minecraft.Util;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.MobRenderer;
import org.joml.Matrix4f;
import advancearmy.entity.land.EntitySA_FTK_H;
import advancearmy.render.ModelNoneVehicle;
@OnlyIn(Dist.CLIENT)
public class RenderFTK_H extends MobRenderer<EntitySA_FTK_H, ModelNoneVehicle<EntitySA_FTK_H>>
{
	private static final ResourceLocation tex = ResourceLocation.tryParse("advancearmy:textures/mob/ftk_heavy.png");
	private static final ResourceLocation text = ResourceLocation.tryParse("advancearmy:textures/mob/track.png");
	private static final ResourceLocation fire_tex = ResourceLocation.tryParse("advancearmy:textures/entity/flash/tankmflash.png");
	private static final ResourceLocation ENCHANT_GLINT_LOCATION = ResourceLocation.tryParse("wmlib:textures/misc/vehicle_glint.png");
	private static final SAObjModel obj = new SAObjModel("advancearmy:textures/mob/ftk_heavy.obj");
	private static final SAObjModel objf = new SAObjModel("advancearmy:textures/mob/flash1.obj");
	RenderType rttrack = RenderTypeVehicle.objrender_track(text);
	RenderType f1 = SARenderState.getBlendDepthWrite(fire_tex);
    public RenderFTK_H(EntityRendererProvider.Context renderManagerIn)
    {
    	super(renderManagerIn, new ModelNoneVehicle(),4F);
        this.shadowStrength = 4F;
    }

    public ResourceLocation getTextureLocation(EntitySA_FTK_H entity)
    {
		return tex;
    }
    
    public boolean shouldRender(EntitySA_FTK_H entity, Frustum camera, double camX, double camY, double camZ) {
        return super.shouldRender(entity, camera, camX, camY, camZ)||entity.distanceToSqr(Minecraft.getInstance().player)<120;
    }
	RenderType rt = RenderTypeVehicle.objrender(tex);
	RenderType glint = SARenderState.getBlendGlowGlint(ENCHANT_GLINT_LOCATION);
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
	
    private void render_part(PoseStack stack,EntitySA_FTK_H entity, String name){
        obj.renderPart(name);
        if (entity.getEnc() > 0){
            renderEnchantGlint(stack, name);
			if(rt!=null)obj.setRenderType(rt);
        }
    }
	static int tick2 = 0;
	static int tick3 = 0;
	private void render_turret2(int packedLightIn, PoseStack stack, EntitySA_FTK_H entity, String name, float x, float y, float z, float bz, float rote, float pitch, float fire, int id){
		stack.pushPose();
		
		stack.translate(x, y, z);
		RenderRote.setRote(stack,rote, 0.0F, 1.0F, 0.0F);
		stack.translate(-x, -y, -z);
		
		stack.pushPose();
		stack.translate(x, y, z);
		render_part(stack, entity,"t"+name);
		stack.popPose();
		
		stack.translate(x, y, z+bz);
		RenderRote.setRote(stack,pitch, 1.0F, 0.0F, 0.0F);
		stack.translate(-x, -y, -z-bz);
		
		stack.pushPose();
		stack.translate(x, y, z);
		render_part(stack, entity,"b"+name);
		stack.popPose();
		
		if(fire <4){
			float size = (float)(5-fire) / 3F;
			if(id==1)renderFire(stack, size*0.5F, x+entity.turretX1, y, z+1.6F);
			if(id==4)renderFire(stack, size*0.5F, x+entity.turretX2, y, z+1.6F);
			RenderSystem.setShaderColor(1F, 0.1F, 0.1F,1F);
			if(id==2)renderFire(stack, size*0.5F, x, y, z+2F);
			if(id==3)renderFire(stack, size*0.5F, x, y, z+2F);
			RenderSystem.setShaderColor(1F, 1F, 1F,1F);
			if(id==5)renderFire(stack, size*0.5F, x+entity.turretX3, y, z+1.6F);
		}
		stack.popPose();
	}
	
	void renderFire(PoseStack stack, float size, float x, float y, float z){
		stack.pushPose();//glstart
		stack.translate(x, y, z);//
		stack.scale(size*0.75F, size*0.75F, size);
		stack.translate(-x, -y, -z);//
		stack.translate(x, y, z);
		objf.renderPart("flash1");
		stack.popPose();//glend
	}
	
    float iii;
	private float recoilTime = 0f; // 用于三角函数的时间参数
	private float recoilIntensity = 0f; // 震动强度
	private float[] randomFactors = new float[3]; // 存储随机因素
	public static double noise(double x, double y) {
		return Math.sin(x * 10 + y * 5) * 0.5 + 0.5; // 简化版本
	}
	
	void shock(PoseStack stack, int anim, float wsize, float size){
		if(anim < wsize) {
			float progress = anim / wsize;
			// 使用不同的三角函数创建多轴震动
			float pitchShake = (float)Math.sin(recoilTime) * recoilIntensity * (1.0f + randomFactors[0]);
			float yawShake = (float)Math.sin(recoilTime * 0.8f + 0.5f) * recoilIntensity * (0.7f + randomFactors[1]);
			float rollShake = (float)Math.cos(recoilTime * 1.2f) * recoilIntensity * 0.3f * randomFactors[2];
			// 应用旋转
			stack.mulPose(Axis.XP.rotationDegrees(pitchShake * size)); // 俯仰
			stack.mulPose(Axis.YP.rotationDegrees(-yawShake * size));   // 偏航
			stack.mulPose(Axis.ZP.rotationDegrees(rollShake * size));    // 滚动
			// 使用余弦函数创建平移效果 - 更加平滑
			float pushBack = (float)Math.cos(recoilTime * 0.5f) * recoilIntensity * 0.02f;
			stack.translate(0, 0, -pushBack * size);
			// 高频随机震动 - 使用噪声函数增加真实感
			if(recoilIntensity > 0.3f) {
				float noiseX = (float)this.noise(recoilTime * 10f, 0) * 0.005f * recoilIntensity;
				float noiseY = (float)this.noise(0, recoilTime * 10f) * 0.005f * recoilIntensity;
				float noiseZ = (float)this.noise(recoilTime * 10f, recoilTime * 10f) * 0.005f * recoilIntensity;
				stack.translate(noiseX * size, noiseY * size, noiseZ * size);
			}
		}
	}
    public void render(EntitySA_FTK_H entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource bufferIn, int packedLightIn)
    {
		super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
	    Minecraft mc = Minecraft.getInstance();
		stack.pushPose();
		stack.pushPose();
		
    	obj.setRender(rt,null,stack,packedLightIn);
		objf.setRender(f1,null,stack,0xF000F0);
		
		if(entity.getTargetType()==2){
			RenderSystem.setShaderColor(0.7F, 0.4F, 0.4F,1F);
			obj.setColor(-999, 0, 0, 1F);
		}
		if(entity.deathTime > 0){
			RenderSystem.setShaderColor(0.1F, 0.1F, 0.1F, 1F);
			obj.setColor(-999, 0, 0, 1F);
		}
		
		RenderRote.setRote(stack,180F, 0.0F, 1.0F, 0.0F);
		float wsize = 12;
		if(entity.anim1 < wsize) {
			if(entity.anim1 == 0) {
				recoilTime = 0f;
				recoilIntensity = 1.0f; // 初始强度
				for(int i = 0; i < 3; i++) {
					randomFactors[i] = (entity.level().random.nextFloat() - 0.5f) * 0.4f;
				}
			}
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
			stack.mulPose(Axis.YP.rotationDegrees(180.0F - entityYaw));
			shock(stack, entity.anim1,wsize,0.4F);
			stack.mulPose(Axis.YP.rotationDegrees(-(180.0F - entityYaw)));
		}
		
		{
			RenderRote.setRote(stack,180.0F - (entity.yHeadRotO + (entity.yHeadRot - entity.yHeadRotO) * partialTicks), 0.0F, 1.0F, 0.0F);
		}
		float th = entity.throttle;
		if(th>=0){
			RenderRote.setRote(stack,((entity.throttleMax-th)/(entity.throttleMax))*1.5F, 1.0F, 0.0F, 0.0F);
		}else if(th<0){
			RenderRote.setRote(stack,((entity.throttleMin-th)/(entity.throttleMin))*1.5F, 1.0F, 0.0F, 0.0F);
		}
		float limbSwing = this.F6(entity, partialTicks);
		float limbSwingAmount = this.F5(entity, partialTicks);
		float Ax1 = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * limbSwingAmount;
		stack.translate(0, Ax1 * (180F / (float)Math.PI) * 0.0002F , 0);
		RenderRote.setRote(stack,Ax1 * (180F / (float)Math.PI) * 0.0002F, 1.0F, 0.0F, 0.0F);
		RenderRote.setRote(stack,Ax1 * (180F / (float)Math.PI) * 0.0002F, 0.0F, 1.0F, 0.0F);
		float range = 0;
		{
			range = 0.0005F;
		}
		stack.pushPose();
		stack.translate(0, 1.02F, 6.12F);
		RenderRote.setRote(stack,entity.thpera, 1.0F, 0.0F, 0.0F);
		stack.translate(0, Ax1 * (180F / (float)Math.PI) * range, 0);
		stack.translate(0, -1.02F, -6.12F);
		stack.translate(0, 1.02F, 6.12F);
		render_part(stack, entity, "wheel1");
		stack.popPose();
		stack.pushPose();
		stack.translate(0, 0.64F, 4.96F);
		RenderRote.setRote(stack,entity.thpera, 1.0F, 0.0F, 0.0F);
		stack.translate(0, Ax1 * (180F / (float)Math.PI) * -range, 0);
		stack.translate(0, -0.64F, -4.96F);
		stack.translate(0, 0.64F, 4.96F);
		render_part(stack, entity, "wheel1");
		stack.popPose();
		stack.pushPose();
		stack.translate(0, 0.64F, 3.72F);
		RenderRote.setRote(stack,entity.thpera, 1.0F, 0.0F, 0.0F);
		stack.translate(0, Ax1 * (180F / (float)Math.PI) * range, 0);
		stack.translate(0, -0.64F, -3.72F);
		stack.translate(0, 0.64F, 3.72F);
		render_part(stack, entity, "wheel1");
		stack.popPose();
		stack.pushPose();
		stack.translate(0, 0.64F, 2.39F);
		RenderRote.setRote(stack,entity.thpera, 1.0F, 0.0F, 0.0F);
		stack.translate(0, Ax1 * (180F / (float)Math.PI) * -range, 0);
		stack.translate(0, -0.64F, -2.39F);
		stack.translate(0, 0.64F, 2.39F);
		render_part(stack, entity, "wheel1");
		stack.popPose();
		stack.pushPose();
		stack.translate(0, 0.64F, 1.24F);
		RenderRote.setRote(stack,entity.thpera, 1.0F, 0.0F, 0.0F);
		stack.translate(0, Ax1 * (180F / (float)Math.PI) * range, 0);
		stack.translate(0, -0.64F, -1.24F);
		stack.translate(0, 0.64F, 1.24F);
		render_part(stack, entity, "wheel1");
		stack.popPose();
		stack.pushPose();
		stack.translate(0, 0.64F, -0.2F);
		RenderRote.setRote(stack,entity.thpera, 1.0F, 0.0F, 0.0F);
		stack.translate(0, Ax1 * (180F / (float)Math.PI) * -range, 0);
		stack.translate(0, -0.64F, 0.2F);
		stack.translate(0, 0.64F, -0.2F);
		render_part(stack, entity, "wheel1");
		stack.popPose();
		stack.pushPose();
		stack.translate(0, 0.64F, -1.46F);
		RenderRote.setRote(stack,entity.thpera, 1.0F, 0.0F, 0.0F);
		stack.translate(0, Ax1 * (180F / (float)Math.PI) * range, 0);
		stack.translate(0, -0.64F, 1.46F);
		stack.translate(0, 0.64F, -1.46F);
		render_part(stack, entity, "wheel1");
		stack.popPose();
		stack.pushPose();
		stack.translate(0, 1.17F, -2.74F);
		RenderRote.setRote(stack,entity.thpera, 1.0F, 0.0F, 0.0F);
		stack.translate(0, Ax1 * (180F / (float)Math.PI) * -range, 0);
		stack.translate(0, -1.17F, 2.74F);
		stack.translate(0, 1.17F, -2.74F);
		render_part(stack, entity, "wheel2");
		stack.popPose();
		

		obj.setRenderType(rttrack);
		Matrix4f mover = new Matrix4f().translation(0, -entity.throttleRight, 0.0f);
		RenderSystem.setTextureMatrix(mover);
		obj.renderPart("track_r");
		RenderSystem.resetTextureMatrix();
		Matrix4f movel = new Matrix4f().translation(0, -entity.throttleLeft, 0.0f);
		RenderSystem.setTextureMatrix(movel);
		obj.renderPart("track_l");
		RenderSystem.resetTextureMatrix();
		obj.setRenderType(rt);
			
		render_part(stack, entity,"mat1");
		float rote1 = 180.0F - (entity.turretYawO + (entity.turretYaw - entity.turretYawO) * partialTicks) -(180.0F - entity.getYRot());
		float rote3 = 180.0F - entity.turretYaw2 -(180.0F - entity.getYRot());
		float rote4 = 180.0F - entity.rotation_3 -(180.0F - entity.getYRot());
		float rote5 = 180.0F - entity.rotation_4 -(180.0F - entity.getYRot());
		float rote6 = 180.0F - entity.rotation_5 -(180.0F - entity.getYRot());
		float rote7 = 180.0F - entity.rotation_6 -(180.0F - entity.getYRot());
		float rote8 = 180.0F - entity.rotation_7 -(180.0F - entity.getYRot());

		stack.pushPose();
		stack.translate(0, 0, 0);
		RenderRote.setRote(stack,rote1, 0.0F, 1.0F, 0.0F);
		stack.translate(0, 0, 0);
		render_part(stack, entity,"mat4");
		stack.translate(0, 3.71F, 1.85F);
		RenderRote.setRote(stack,entity.turretPitch, 1.0F, 0.0F, 0.0F);
		stack.translate(0, -3.71F, -1.85F);
		render_part(stack, entity,"mat5");
		if(entity.anim1 <10){
			float size = (float)(11-entity.anim1) / 3F;
			renderFire(stack, size, 0, 4F, 7F);
		}
		stack.popPose();
		render_turret2(packedLightIn, stack, entity, "1", -1.05F, 2.5F, 4F, 0.6F, rote3, entity.turretPitch2, entity.anim4, 1);
		render_turret2(packedLightIn, stack, entity, "2", -3.74F, 2.82F, 0.78F, 0.6F, rote4, entity.rotationp_3, entity.anim5, 2);
		render_turret2(packedLightIn, stack, entity, "2", 3.74F, 2.82F, 0.78F, 0.6F, rote5, entity.rotationp_4, entity.anim6, 3);
		render_turret2(packedLightIn, stack, entity, "3", -3.74F, 1.9F, 0.78F, 0.6F, rote6, entity.rotationp_5, entity.anim7, 4);
		render_turret2(packedLightIn, stack, entity, "3", 3.74F, 1.9F, 0.78F, 0.6F, rote7, entity.rotationp_6, entity.anim8, 5);
		
		stack.popPose();
	    stack.popPose();
		RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
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
            f5 = entity.walkAnimation.speed() + (entity.walkAnimation.speed() - entity.walkAnimation.speed()) * partialTicks;
            if (f5 > 1.0F)
            {
                f5 = 1.0F;
            }
        }
 		return f5;
 	}
}