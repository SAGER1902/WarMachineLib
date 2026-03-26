package advancearmy.render;

import com.mojang.math.Axis;
import org.lwjgl.opengl.GL12;

import advancearmy.entity.EntitySA_TurretBase;
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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.texture.OverlayTexture;
import wmlib.client.obj.SAObjModel;
import net.minecraft.client.CameraType;
import org.joml.Matrix4f;
import net.minecraft.Util;
import com.mojang.blaze3d.systems.RenderSystem;
import wmlib.client.render.SARenderState;
import wmlib.client.render.RenderTypeVehicle;
import net.minecraft.client.renderer.RenderType;
import advancearmy.entity.EntitySA_Seat;
@OnlyIn(Dist.CLIENT)
public class RenderTurretBase extends MobRenderer<EntitySA_TurretBase, ModelNoneVehicle<EntitySA_TurretBase>>
{
	public ResourceLocation tex = ResourceLocation.tryParse("advancearmy:textures/mob/t90.png");
	public ResourceLocation gtex = null;
	public SAObjModel obj = new SAObjModel("advancearmy:textures/mob/kord.obj");

	public ResourceLocation fire_tex1 = ResourceLocation.tryParse("advancearmy:textures/entity/flash/tankmflash.png");
	public ResourceLocation fire_tex2 = ResourceLocation.tryParse("advancearmy:textures/entity/flash/muzzleflash3.png");
	private static final ResourceLocation ENCHANT_GLINT_LOCATION = ResourceLocation.tryParse("wmlib:textures/misc/vehicle_glint.png");
	
	RenderType rt;
	RenderType f1;
	RenderType f2;
	RenderType glint = SARenderState.getBlendGlowGlint(ENCHANT_GLINT_LOCATION);
	
    public RenderTurretBase(EntityRendererProvider.Context context)
    {
    	super(context, new ModelNoneVehicle(),1F);
        this.shadowStrength = 1F;
    }
    public ResourceLocation getTextureLocation(EntitySA_TurretBase entity)
    {
		return tex;
    }
    public boolean shouldRender(EntitySA_TurretBase entity, Frustum camera, double camX, double camY, double camZ) {
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
    private void render_part(PoseStack stack, EntitySA_TurretBase entity, String name){
        obj.renderPart(name);
        if (entity.getEnc() > 0){
            renderEnchantGlint(stack, name);
			if(rt!=null)obj.setRenderType(rt);
        }
    }
	
    float iii;
	private float recoilTime = 0f; // 用于三角函数的时间参数
	private float recoilIntensity = 0f; // 震动强度
	private float[] randomFactors = new float[3]; // 存储随机因素
	public static double noise(double x, double y) {
		return Math.sin(x * 10 + y * 5) * 0.5 + 0.5; // 简化版本
	}
    public void render(EntitySA_TurretBase entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight0)
    {
		super.render(entity, entityYaw, partialTicks, stack, buffer, packedLight0);
		if(entity.obj!=null)obj = entity.obj;
		if(entity.tex!=null)tex = entity.tex;
		if(entity.guntex!=null)gtex=entity.guntex;
		if(entity.fire1tex!=null)fire_tex1=entity.fire1tex;
		if(entity.fire2tex!=null)fire_tex2=entity.fire2tex;
		
		f1= SARenderState.getBlendDepthWrite(fire_tex1);
		f2= SARenderState.getBlendDepthWrite(fire_tex2);
		
		if(entity.getTargetType()==2){
			RenderSystem.setShaderColor(0.7F, 0.4F, 0.4F,1F);
			obj.setColor(-999, 0, 0, 1F);
		}
		if(entity.deathTime > 0){
			RenderSystem.setShaderColor(0.1F, 0.1F, 0.1F, 1F);
			obj.setColor(-999, 0, 0, 1F);
		}
	    stack.pushPose();
		stack.pushPose();
		Minecraft mc = Minecraft.getInstance();
		
    	int packedLight=packedLight0;
		//if(entity.anim1<3)packedLight=0xF000F0;
		rt = RenderTypeVehicle.objrender(tex);
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
		
		float yaw = 0;
		//yaw=(entity.yHeadRotO + (entity.yHeadRot - entity.yHeadRotO) * partialTicks);
		
		//render_part(stack, entity,"defend");
		if(entity.getVehicle()!=null && entity.getVehicle() instanceof LivingEntity){
			LivingEntity living = (LivingEntity)entity.getVehicle();
			yaw=(living.yRotO + (living.getYRot() - living.yRotO) * partialTicks);
			stack.mulPose(Axis.YP.rotationDegrees(180.0F));
			stack.mulPose(Axis.YP.rotationDegrees(180.0F - yaw));
		}

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
		{
			stack.pushPose();
			stack.translate(0F, 0F, 0F);
			stack.mulPose(Axis.YP.rotationDegrees(180.0F - (entity.turretYawO + (entity.turretYaw - entity.turretYawO) * partialTicks) -(180.0F - yaw)));
			stack.translate(-0F, -0F, -0F);
			render_part(stack, entity,"turret");
			//render_part(stack, entity,"open");
			
			obj.renderPart("head_light");
			
			
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
			float fireYaw = entity.fireposY1;
			if(entity.firebaseY!=0)fireYaw=entity.firebaseY;
			stack.translate(0F, fireYaw, entity.firebaseZ);
			stack.mulPose(Axis.XP.rotationDegrees(entity.turretPitchO + (entity.turretPitch - entity.turretPitchO) * partialTicks));
			stack.translate(0F, -fireYaw, -entity.firebaseZ);
			if(!entity.hidebarrel1 || mc.options.getCameraType() != CameraType.FIRST_PERSON ||
			entity.getFirstSeat()!=null && (entity.getFirstSeat().getControllingPassenger()!=null && 
			entity.getFirstSeat().getControllingPassenger() != mc.player||entity.getFirstSeat().getControllingPassenger()==null) ){
				render_part(stack, entity,"barrel");
				if(gtex!=null){
					obj.setRender(RenderTypeVehicle.objrender(gtex), null, stack, packedLight);
					render_part(stack, entity,"barrelgun");
					obj.setRender(rt, null, stack, packedLight);
				}
			}
			stack.pushPose();
			stack.translate(0F, 0.09F, 1.55F);
			if(entity.turretPitch>-45){
				stack.mulPose(Axis.XP.rotationDegrees(entity.turretPitch*0.8F));
			}else{
				stack.mulPose(Axis.XP.rotationDegrees((-entity.turretPitch-90)*0.8F));
			}
			stack.translate(0F, -0.09F, -1.55F);
			render_part(stack, entity,"tripod");
			stack.popPose();
			
			
			obj.renderPart("barrel_light");
			
			if(entity.ammo){
				obj.renderPart("box");
				if(entity.count==0)obj.renderPart("ammo1");
				if(entity.count==1)obj.renderPart("ammo2");
				if(entity.count==2)obj.renderPart("ammo3");
				if(entity.anim1<4){
					if(entity.level().random.nextInt(4)==1){
						obj.renderPart("shell1");
					}
					if(entity.level().random.nextInt(4)==2){
						obj.renderPart("shell2");
					}
					if(entity.level().random.nextInt(4)==3){
						obj.renderPart("shell3");
					}
					if(entity.level().random.nextInt(4)==0){
						obj.renderPart("shell4");
					}
				}
			}
			{//mgflash
				stack.pushPose();
				obj.setRender(f2,null,null,0xF000F0);
				stack.translate(-entity.fireposX2, entity.fireposY2, entity.fireposZ2);
				stack.scale(size2, size2, size2);
				
				if(entity.anim2<5)obj.renderPart("flash2");
				
				stack.popPose();
				
				stack.pushPose();
				obj.setRender(f1,null,null,0xF000F0);
				stack.translate(entity.fireposX1, entity.fireposY1, entity.fireposZ1);
				if(entity.anim1 <4){
					stack.pushPose();
					
					stack.scale(size2*1.5F, size2*1.5F, 1);
					obj.renderPart("flash");
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
				
				if(entity.anim1<9){
					obj.renderPart("flash1");
				}
				stack.popPose();
				obj.setRender(rt, null, stack, packedLight);
			}
			if(entity.anim1 >= 0 && entity.anim1 < 4*entity.w1barrelsize){
				stack.translate(0.0F, 0.0F, -entity.anim1 * 0.3F*entity.w1barrelsize);
			}
			if(entity.anim1 >= 4 && entity.anim1 < 16*entity.w1barrelsize){
				stack.translate(0.0F, 0.0F, -1.2F*entity.w1barrelsize);
				stack.translate(0.0F, 0.0F, entity.anim1 * 0.1F*entity.w1barrelsize);
			}
			render_part(stack, entity,"barrel1");
			stack.popPose();
		}
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