package advancearmy.render;

import com.mojang.math.Axis;
import advancearmy.entity.EntitySA_LandBase;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.core.BlockPos;
import wmlib.client.render.SARenderHelper;
import wmlib.client.render.SARenderHelper.RenderTypeSA;
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
@OnlyIn(Dist.CLIENT)
public class RenderTankBase extends MobRenderer<EntitySA_LandBase, ModelNoneVehicle<EntitySA_LandBase>>
{
	public ResourceLocation tex = ResourceLocation.tryParse("advancearmy:textures/mob/t90.png");
	public SAObjModel obj = new SAObjModel("advancearmy:textures/mob/t90.obj");
	public SAObjModel mg = new SAObjModel("advancearmy:textures/mob/kord.obj");

	private static final ResourceLocation tankmflash = ResourceLocation.tryParse("advancearmy:textures/entity/flash/tankmflash.png");
	private static final ResourceLocation fire_tex = ResourceLocation.tryParse("advancearmy:textures/entity/flash/muzzleflash3.png");
	private static final ResourceLocation ENCHANT_GLINT_LOCATION = ResourceLocation.tryParse("wmlib:textures/misc/vehicle_glint.png");

	
	RenderType rt;
	RenderType mgrt;
	RenderType f1 = SARenderState.getBlendDepthWrite(fire_tex);
	RenderType f2 = SARenderState.getBlendDepthWrite(tankmflash);
	RenderType glint = SARenderState.getBlendGlowGlint(ENCHANT_GLINT_LOCATION);
	
    public RenderTankBase(EntityRendererProvider.Context context) {
        super(context, new ModelNoneVehicle(),4.0F);
        this.shadowRadius = 4.0F;
    }
    public ResourceLocation getTextureLocation(EntitySA_LandBase entity)
    {
		return tex;
    }
    public boolean shouldRender(EntitySA_LandBase entity, Frustum camera, double camX, double camY, double camZ) {
        return super.shouldRender(entity, camera, camX, camY, camZ)||entity.distanceToSqr(camX,camY,camZ)<10;
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
    private void render_part(PoseStack stack, EntitySA_LandBase entity, String name){
        obj.renderPart(name);
        if (entity.getEnc() > 0){
            renderEnchantGlint(stack, name);
			if(rt!=null)obj.setRenderType(rt);
        }
    }
	
	private void render_turret(PoseStack stack, EntitySA_LandBase entity, float x, float y, float z, float bz, float rote, float pitch, float fire, float tick, int id){
		float size2 = entity.level().random.nextInt(4) * 0.3F + 1;
		stack.pushPose();
		stack.translate(x, y, z);//
		stack.mulPose(Axis.YP.rotationDegrees(rote));
		stack.translate(-x, -y, -z);//
		
		stack.pushPose();
		stack.translate(x, y, z);
		mg.renderPart("turret");
		stack.popPose();
		
		stack.translate(x, y, z+bz);//
		stack.mulPose(Axis.XP.rotationDegrees(pitch));
		stack.translate(-x, -y, -z-bz);//
		
		stack.pushPose();
		stack.translate(x, y, z);
		mg.renderPart("barrel");
		if(entity.ammo){
			mg.renderPart("box");
			if(EntitySA_LandBase.count==0)mg.renderPart("ammo1");
			if(EntitySA_LandBase.count==1)mg.renderPart("ammo2");
			if(EntitySA_LandBase.count==2)mg.renderPart("ammo3");
			if(fire<4){
				if(entity.level().random.nextInt(4)==1){
					mg.renderPart("shell1");
				}
				if(entity.level().random.nextInt(4)==2){
					mg.renderPart("shell2");
				}
				if(entity.level().random.nextInt(4)==3){
					mg.renderPart("shell3");
				}
				if(entity.level().random.nextInt(4)==0){
					mg.renderPart("shell4");
				}
			}
		}
		stack.popPose();
		stack.pushPose();
		stack.translate(x, y, z);
		if(fire <4){
			stack.pushPose();
			mg.setRender(f1,null,null,0xF000F0);
			stack.scale(size2*1.5F, size2*1.5F, 1);
			mg.renderPart("flash");
			if(entity.level().random.nextInt(2)==1){
				mg.renderPart("mat_1");
			}else if(entity.level().random.nextInt(2)==2){
				mg.renderPart("mat_2");
			}else{
				mg.renderPart("mat_3");
			}
			stack.popPose();
		}
		stack.popPose();
		stack.popPose();
	}

    float iii;
	int fire_rote;
	private float recoilTime = 0f; // 用于三角函数的时间参数
	private float recoilIntensity = 0f; // 震动强度
	private float[] randomFactors = new float[3]; // 存储随机因素
	public static double noise(double x, double y) {
		return Math.sin(x * 10 + y * 5) * 0.5 + 0.5; // 简化版本
	}
	
    public void render(EntitySA_LandBase entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight0)
    {
		super.render(entity, entityYaw, partialTicks, stack, buffer, packedLight0);
		if(entity.obj!=null)obj = entity.obj;
		if(entity.mgobj!=null)mg = entity.mgobj;
		if(entity.tex!=null)tex = entity.tex;
	    stack.pushPose();
		stack.pushPose();
		int packedLight=packedLight0;
		//if(entity.anim1<3)packedLight=0xF000F0;
		/*if(entity.anim1<3){
			rt = RenderTypeVehicle.objrender_track(tex);
		}else*/{
			rt = RenderTypeVehicle.objrender(tex);
		}
		
		obj.setRender(rt, null, stack, packedLight);
		if(entity.getTargetType()==2){
			RenderSystem.setShaderColor(0.7F, 0.4F, 0.4F,1F);
			obj.setColor(-999, 0, 0, 1F);
			mg.setColor(-999, 0, 0, 1F);
		}
		if(entity.deathTime > 0){
			RenderSystem.setShaderColor(0.1F, 0.1F, 0.1F, 1F);
			obj.setColor(-999, 0, 0, 1F);
			mg.setColor(-999, 0, 0, 1F);
		}
		Minecraft mc = Minecraft.getInstance();
		
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
		//if(entity.deathTime > 0)//GL11.glColor4f(0.1F, 0.1F, 0.1F, 1F);
		
		stack.mulPose(Axis.YP.rotationDegrees(180.0F - (entity.yHeadRotO + (entity.yHeadRot - entity.yHeadRotO) * partialTicks)));
		stack.mulPose(Axis.XP.rotationDegrees(entity.currentTilt));
		render_part(stack, entity,"body");
		
		{
			//RenderSystem.disableDepthTest();
			RenderSystem.depthMask(false);
			RenderSystem.enableBlend();
			RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
			obj.renderPart("body_light");
			RenderSystem.defaultBlendFunc();
			RenderSystem.disableBlend();
			RenderSystem.depthMask(true);
			//RenderSystem.enableDepthTest();
		}
		
		if(entity.tracktex!=null){
			obj.setRenderType(RenderTypeVehicle.objrender_track(entity.tracktex));
		}else{
			obj.setRenderType(RenderTypeVehicle.objrender_track(tex));
		}
		Matrix4f mover = new Matrix4f().translation(0, -entity.throttleRight, 0.0f);
        RenderSystem.setTextureMatrix(mover);
		//obj.setMoveTex(0, -entity.throttleRight);
		obj.renderPart("track_r");
		RenderSystem.resetTextureMatrix();
		
		//obj.setMoveTex(0, -entity.throttleLeft);
		Matrix4f movel = new Matrix4f().translation(0, -entity.throttleLeft, 0.0f);
        RenderSystem.setTextureMatrix(movel);
		obj.renderPart("track_l");
		RenderSystem.resetTextureMatrix();
		//obj.setMoveTex(0,0);
		obj.setRenderType(rt);
		
		float range = 0;
		{
			range = 0.0005F;
		}
		float limbSwing = this.F6(entity, partialTicks);//
		float limbSwingAmount = this.F5(entity, partialTicks);//
		float Ax1 = (float)Math.cos(limbSwing * 0.6662F + (float)Math.PI) * limbSwingAmount;//
		for(int t1 = 0; t1 < entity.wheelcount; ++t1){
			stack.pushPose();
			String tu1 = String.valueOf(t1 + 1);
			stack.translate(entity.wheelx[t1], entity.wheely[t1], entity.wheelz[t1]);//
			if(entity.wheelturn[t1])stack.mulPose(Axis.YP.rotationDegrees(entity.rote_wheel));
			stack.mulPose(Axis.XP.rotationDegrees(entity.thpera*entity.wheel_rotex));
			stack.mulPose(Axis.YP.rotationDegrees(entity.thpera*entity.wheel_rotey));
			stack.mulPose(Axis.ZP.rotationDegrees(entity.thpera*entity.wheel_rotez));
			if(t1%2==0){
				stack.translate(0, Ax1 * (180F / (float)Math.PI) * range, 0);
			}else{
				stack.translate(0, Ax1 * (180F / (float)Math.PI) * -range, 0);
			}
			stack.translate(-entity.wheelx[t1], -entity.wheely[t1], -entity.wheelz[t1]);//
			render_part(stack, entity, "wheel_" + tu1);
			stack.popPose();
		}
		{
			stack.pushPose();
			stack.translate(0F, 0F, 0F);
			stack.mulPose(Axis.YP.rotationDegrees(180.0F - (entity.turretYawO + (entity.turretYaw - entity.turretYawO) * partialTicks) -(180.0F - (entity.yHeadRotO + (entity.yHeadRot - entity.yHeadRotO) * partialTicks))));
			stack.translate(-0F, -0F, -0F);
			render_part(stack, entity,"mat4");
			//render_part(stack, entity,"open");
			obj.renderPart("head_light");
			
			//render_part(stack, entity,"close");
			
			if(iii<360F){
				++iii;
			}else{
				iii = 0;
			}
			for(int t1 = 0; t1 < entity.radercount; ++t1){
				stack.pushPose();
				String tu1 = String.valueOf(t1 + 1);
				stack.translate(entity.raderx[t1], entity.radery[t1], entity.raderz[t1]);//
				stack.mulPose(Axis.YP.rotationDegrees(iii));
				stack.translate(-entity.raderx[t1], -entity.radery[t1], -entity.raderz[t1]);//
				obj.renderPart("rader"+tu1);
				stack.popPose();
			}
			
			float size2 = partialTicks * 0.3F + 1;
			float rotet = 180.0F - (entity.turretYawO1 + (entity.turretYaw1 - entity.turretYawO1) * partialTicks)-(180.0F - entity.turretYaw);
			float rotetp = entity.turretPitchO1 + (entity.turretPitch1 - entity.turretPitchO1) * partialTicks;
			//seat turret
			if(entity.mgtex!=null){
				mgrt = RenderTypeVehicle.objrender(entity.mgtex);
				mg.setRender(mgrt, null, stack, packedLight);
				render_turret(stack, entity, entity.mgx, entity.mgy, entity.mgz, entity.mgbz, rotet, rotetp, entity.anim3, 0, 1);
				obj.setRenderType(rt);
				obj.setRenderLight(packedLight);
			}
			
			stack.translate(0F, entity.fireposY1, entity.firebaseZ);
			stack.mulPose(Axis.XP.rotationDegrees(entity.turretPitchO + (entity.turretPitch - entity.turretPitchO) * partialTicks));
			stack.translate(0F, -entity.fireposY1, -entity.firebaseZ);
			render_part(stack, entity,"mat5");
			
			stack.pushPose();
			if(entity.anim1 < 2){
				if(fire_rote<360F){
					++fire_rote;
				}else{
					fire_rote = 0;
				}
			}
			stack.translate(entity.fireposX1, entity.fireposY1, entity.fireposZ1);
			stack.mulPose(Axis.ZP.rotationDegrees(fire_rote*20F));
			stack.translate(-entity.fireposX1, -entity.fireposY1, -entity.fireposZ1);
			obj.renderPart("rote");
			stack.popPose();
			
			obj.renderPart("barrel_light");
			
			{
				stack.pushPose();
				obj.setRender(f1,null,null,0xF000F0);
				stack.translate(-entity.fireposX2, entity.fireposY2, entity.fireposZ2);
				stack.scale(size2, size2, size2);
				if(entity.anim2<5)obj.renderPart("flash2");
				stack.popPose();
				
				stack.pushPose();
				obj.setRender(f2,null,null,0xF000F0);
				stack.translate(entity.fireposX1, entity.fireposY1, entity.fireposZ1);
				if(entity.anim1 <4){
					stack.pushPose();
					stack.scale(size2*1.5F, size2*1.5F, 1);
					if(entity.level().random.nextInt(2)==1){
						obj.renderPart("mat_1");
					}else if(entity.level().random.nextInt(2)==2){
						obj.renderPart("mat_2");
					}else{
						obj.renderPart("mat_3");
					}
					stack.popPose();
				}
				if(entity.anim1 < 4){
					float size = (1 + partialTicks * 0.1F)*(float)(2+entity.anim1) / 3F;
					stack.scale(size,size,size);
				}
				if(entity.anim1 >= 4 && entity.anim1<9){
					float size = (1 + partialTicks * 0.1F)*(float)(10-entity.anim1) / 4F;
					stack.scale(size,size,size);
				}
				stack.translate(-entity.fireposX1, -entity.fireposY1, -entity.fireposZ1);
				stack.translate(entity.fireposX1, entity.fireposY1, entity.fireposZ1);
				if(entity.anim1<9)obj.renderPart("flash1");
				stack.popPose();
				obj.setRenderType(rt);
				obj.setRenderLight(packedLight);
			}
			if(entity.anim1 >= 0 && entity.anim1 < 4*entity.w1barrelsize){
				stack.translate(0.0F, 0.0F, -entity.anim1 * 0.3F*entity.w1barrelsize);
			}
			if(entity.anim1 >= 4 && entity.anim1 < 16*entity.w1barrelsize){
				stack.translate(0.0F, 0.0F, -1.2F*entity.w1barrelsize);
				stack.translate(0.0F, 0.0F, entity.anim1 * 0.1F*entity.w1barrelsize);
			}
			render_part(stack, entity,"mat6");
			stack.popPose();
		}
		//GL11.glColor4f(1F, 1F, 1F, 1F);
		RenderSystem.setShaderColor(1, 1, 1, 1);
		stack.popPose();
	    stack.popPose();
    }
	
    public float F6(LivingEntity entity, float partialTicks){
 		float f6 = 0;
 		if (!entity.isPassenger()){
            f6 = entity.walkAnimation.position() - entity.walkAnimation.speed() * (1.0F - partialTicks);
        }
 		return f6;
 	}
 	public float F5(LivingEntity entity, float partialTicks){
 		float f5 = 0;
 		if (!entity.isPassenger()){
            f5 = entity.walkAnimation.speed();
            if (f5 > 1.0F){
                f5 = 1.0F;
            }
        }
 		return f5;
 	}
}