package advancearmy.render.vehicle;
import advancearmy.entity.sea.EntitySA_BattleShip;
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
import advancearmy.render.ModelNoneVehicle;
@OnlyIn(Dist.CLIENT)
public class RenderBattleShip extends MobRenderer<EntitySA_BattleShip, ModelNoneVehicle<EntitySA_BattleShip>>
{
	private static final ResourceLocation tex = ResourceLocation.tryParse("advancearmy:textures/mob/battleship.png");
	private static final SAObjModel obj = new SAObjModel("advancearmy:textures/mob/battleship.obj");
	
	private static final SAObjModel objf = new SAObjModel("advancearmy:textures/mob/flash1.obj");
	private static final ResourceLocation fire_tex = ResourceLocation.tryParse("advancearmy:textures/entity/flash/tankmflash.png");
	RenderType f1 = SARenderState.getBlendDepthWrite(fire_tex);
	
	public static final ResourceLocation ENCHANT_GLINT_LOCATION = ResourceLocation.tryParse("wmlib:textures/misc/vehicle_glint.png");
	
	
    public RenderBattleShip(EntityRendererProvider.Context renderManagerIn)
    {
    	super(renderManagerIn, new ModelNoneVehicle(),4F);
        this.shadowStrength = 4F;
    }

    public ResourceLocation getTextureLocation(EntitySA_BattleShip entity)
    {
		return tex;
    }
    
    public boolean shouldRender(EntitySA_BattleShip entity, Frustum camera, double camX, double camY, double camZ) {
		if(entity.distanceToSqr(camX, camY, camZ)<900){
			return true;
		}else{
			return super.shouldRender(entity, camera, camX, camY, camZ);
		}
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
	
    private void render_part(PoseStack stack,EntitySA_BattleShip entity, String name){
        obj.renderPart(name);
        if (entity.getEnc() > 0){
            renderEnchantGlint(stack, name);
			if(rt!=null)obj.setRenderType(rt);
        }
    }
	static int tick2 = 0;
	static int tick3 = 0;
	private void render_turret2(int packedLightIn, PoseStack stack, EntitySA_BattleShip entity, String name, float x, float y, float z, float bz, float rote, float pitch, float fire, float tick, int id){
		stack.pushPose();//glstart
		
		stack.translate(x, y, z);//
		RenderRote.setRote(stack,rote, 0.0F, 1.0F, 0.0F);
		stack.translate(-x, -y, -z);//
		
		stack.pushPose();//glstart
		stack.translate(x, y, z);
		render_part(stack,entity,"t"+name);
		stack.popPose();//glend
		
		stack.translate(x, y, z+bz);//
		RenderRote.setRote(stack,pitch, 1.0F, 0.0F, 0.0F);
		stack.translate(-x, -y, -z-bz);//
		
		stack.pushPose();//glstart
		stack.translate(x, y, z);
		render_part(stack,entity,"b"+name);
		stack.popPose();//glend
		
		if(id==1){
			stack.pushPose();//glstart
			stack.translate(x, y, z);
			render_part(stack,entity,"b3");
			stack.popPose();//glend
			
			stack.pushPose();//glstart
			stack.translate(x, y, z);//
			RenderRote.setRote(stack,tick*10F, 0.0F, 0.0F, 1.0F);
			stack.translate(-x, -y, -z);//
			stack.translate(x, y, z);
			render_part(stack,entity,"b31");
			stack.popPose();//glend
			
			if(fire <4){
				float size2 = entity.level().random.nextInt(4) * 0.3F + 1;
				stack.pushPose();//glstart
				stack.translate(x, y+0.1F, z);//
				RenderRote.setRote(stack,tick*10F, 0.0F, 0.0F, 1.0F);
				stack.scale(size2, size2, 1);
				stack.translate(-x, -y-0.1F, -z);//
				stack.translate(x, y+0.1F, z);
				obj.setRender(f1,null,stack,0xF000F0);
				if(entity.level().random.nextInt(3)==1){
					obj.renderPart("mat_1");
				}else if(entity.level().random.nextInt(3)==2){
					obj.renderPart("mat_2");
				}else{
					obj.renderPart("mat_3");
				}
				obj.setRender(rt,null,stack,packedLightIn);
				stack.popPose();//glend
			}
		}
		if(id == 2){
			float size = (float)(10-fire) / 3F;
			if(fire<10)renderFire(stack, size*0.7F, x, y, z-4F);
		}
		stack.popPose();//glend
	}


	private void render_turret(PoseStack stack, EntitySA_BattleShip entity, String name, float x, float y, float z, float rote, float pitch, int id){
		stack.pushPose();//glstart
		stack.translate(x, y, z);//
		RenderRote.setRote(stack,rote, 0.0F, 1.0F, 0.0F);
		stack.translate(-x, -y, -z);//
		
		stack.pushPose();//glstart
		stack.translate(x, y, z);
		render_part(stack,entity,"t"+name);
		stack.popPose();//glend
		
		stack.translate(x, y, z+2.6F);//
		RenderRote.setRote(stack,pitch, 1.0F, 0.0F, 0.0F);
		stack.translate(-x, -y, -z-2.6F);//
		
		stack.pushPose();//glstart
		stack.translate(x, y, z);
		render_part(stack,entity,"b"+name);
		stack.popPose();//glend
		
		if(id == 1){
			float size = (float)(10-entity.cf1) / 3F;
			float size2 = (float)(10-entity.cf2) / 3F;
			float size3 = (float)(10-entity.cf3) / 3F;
			if(entity.cf1<10)renderFire(stack, size, x-0.89F, y, z);
			if(entity.cf2<10)renderFire(stack, size2, x+0.89F, y, z);
			if(entity.cf3<10)renderFire(stack, size3, x, y, z);
		}
		if(id == 2){
			float size = (float)(10-entity.cf4) / 3F;
			float size2 = (float)(10-entity.cf5) / 3F;
			float size3 = (float)(10-entity.cf6) / 3F;
			if(entity.cf4<10)renderFire(stack, size, x-0.89F, y, z);
			if(entity.cf5<10)renderFire(stack, size2, x+0.89F, y, z);
			if(entity.cf6<10)renderFire(stack, size3, x, y, z);
		}
		if(id == 3){
			float size = (float)(10-entity.cf7) / 3F;
			float size2 = (float)(10-entity.cf8) / 3F;
			float size3 = (float)(10-entity.cf9) / 3F;
			if(entity.cf7<10)renderFire(stack, size, x-0.89F, y, z);
			if(entity.cf8<10)renderFire(stack, size2, x+0.89F, y, z);
			if(entity.cf9<10)renderFire(stack, size3, x, y, z);
		}
		stack.popPose();//glend
	}
	
	void renderFire(PoseStack stack, float size, float x, float y, float z){
		stack.pushPose();//glstart
		stack.translate(x, y+0.1F, z+7F);//
		stack.scale(size*0.75F, size*0.75F, size);
		stack.translate(-x, -y-0.1F, -z-7F);//
		stack.translate(x, y+0.1F, z+7F);
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
	float shockanim;
    public void render(EntitySA_BattleShip entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLightIn)
    {
		super.render(entity, entityYaw, partialTicks, stack, buffer, packedLightIn);
		Minecraft mc = Minecraft.getInstance();
		stack.pushPose();
		stack.pushPose();

		RenderRote.setRote(stack,180F, 0.0F, 1.0F, 0.0F);
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
		float wsize = 12;
		if(shockanim<wsize)++wsize;
		if(entity.anim1 < wsize||entity.anim3 < wsize||entity.anim4 < wsize) {
			// 动画开始时初始化
			if(entity.anim1 == 0||entity.anim3 == 0||entity.anim4 == 0) {
				shockanim = 0;
				recoilTime = 0f;
				recoilIntensity = 1.0f; // 初始强度
				// 生成随机因素
				for(int i = 0; i < 3; i++) {
					randomFactors[i] = (entity.level().random.nextFloat() - 0.5f) * 0.4f;
				}
			}
			// 更新时间 - 基于动画进度
			float progress = (shockanim) / wsize;
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
			shock(stack, entity.anim4,wsize,0.4F);
			shock(stack, entity.anim3,wsize,0.4F);
			shock(stack, entity.anim1,wsize,0.4F);
			stack.mulPose(Axis.YP.rotationDegrees(-(180.0F - entityYaw)));
		}
		
		float mainRot = 180.0F - (entity.yHeadRotO + (entity.yHeadRot - entity.yHeadRotO) * partialTicks);
		{
			RenderRote.setRote(stack, mainRot, 0.0F, 1.0F, 0.0F);
		}

		float th = entity.throttle;
		if(th>=0){
			RenderRote.setRote(stack,((entity.throttleMax-th)/(entity.throttleMax))*1.5F, 1.0F, 0.0F, 0.0F);
		}else if(th<0){
			RenderRote.setRote(stack,((entity.throttleMin-th)/(entity.throttleMin))*1.5F, 1.0F, 0.0F, 0.0F);
		}
		float limbSwing = this.F6(entity, partialTicks);//
		float limbSwingAmount = this.F5(entity, partialTicks);//
		float Ax1 = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * limbSwingAmount;//
		stack.translate(0, Ax1 * (180F / (float)Math.PI) * 0.0002F , 0);
		RenderRote.setRote(stack,Ax1 * (180F / (float)Math.PI) * 0.0002F, 1.0F, 0.0F, 0.0F);
		RenderRote.setRote(stack,Ax1 * (180F / (float)Math.PI) * 0.0002F, 0.0F, 1.0F, 0.0F);
		render_part(stack,entity,"mat1");
		float rote1 = 180.0F - (entity.turretYawO + (entity.turretYaw - entity.turretYawO) * partialTicks) -mainRot;
		float rote2 = 180.0F - entity.turretYaw1 -mainRot;
		float rote3 = 180.0F - entity.turretYaw2 -mainRot;
		float rote4 = 180.0F - entity.rotation_3 -mainRot;
		float rote5 = 180.0F - entity.rotation_4 -mainRot;
		float rote6 = 180.0F - entity.rotation_5 -mainRot;
		float rote7 = 180.0F - entity.rotation_6 -mainRot;
		float rote8 = 180.0F - entity.rotation_7 -mainRot;
		if(entity.anim7<5){
			if(tick2<36F){
				++tick2;
			}else{
				tick2 = 0;
			}
		}
		if(entity.anim8<5){
			if(tick3<36F){
				++tick3;
			}else{
				tick3 = 0;
			}
		}
		if(iii<360F){
			++iii;
		}else{
			iii = 0;
		}
		stack.pushPose();//glstart
		stack.translate(0.0F, 0, 3.27F);
		RenderRote.setRote(stack,iii, 0.0F, 1.0F, 0.0F);
		stack.translate(0.0F, 0, -3.27F);
		obj.renderPart("rader");
		stack.popPose();//glend
		stack.pushPose();//glstart
		stack.translate(0.0F, 0, -1.26F);
		RenderRote.setRote(stack,-iii, 0.0F, 1.0F, 0.0F);
		stack.translate(0.0F, 0, 1.26F);
		obj.renderPart("rader2");
		stack.popPose();//glend

		render_turret(stack,entity, "1", 0, 4.19F, 10.82F, rote1, entity.turretPitch,1);
		render_turret(stack,entity, "1", 0, 3F, 17.89F, rote2, entity.turretPitch1,2);
		render_turret(stack,entity, "1", 0, 3.1F, -10.45F, rote3, entity.turretPitch2,3);
		render_turret2(packedLightIn, stack,entity, "2", -4.38F, 3.1F, 4.37F, 0.6F, rote4, entity.rotationp_3, entity.anim5, 0, 2);
		render_turret2(packedLightIn, stack,entity, "2", 4.38F, 3.1F, 4.37F, 0.6F, rote5, entity.rotationp_4, entity.anim6, 0, 2);
		render_turret2(packedLightIn, stack,entity, "3", -3.63F, 6.9F, -1.3F, 0, rote6, entity.rotationp_5, entity.anim7, tick2, 1);
		render_turret2(packedLightIn, stack,entity, "3", 3.63F, 6.9F, -1.3F, 0, rote7, entity.rotationp_6, entity.anim8, tick3, 1);
		render_turret2(packedLightIn, stack,entity, "4", 0, 5.33F, -5.57F, 0, rote8, entity.rotationp_7, 0, 0, 0);
		RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
		stack.popPose();
	    stack.popPose();
    }
	
    public float F6(LivingEntity entity, float partialTicks){
 		float f6 = 0;
 		if (!entity.isPassenger())
        {
            f6 = entity.walkAnimation.position() - entity.walkAnimation.speed()*0.25F * (1.0F - partialTicks);
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