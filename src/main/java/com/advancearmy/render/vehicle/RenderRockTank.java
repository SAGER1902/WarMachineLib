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
import net.minecraft.client.CameraType;
import org.joml.Matrix4f;
import advancearmy.entity.land.EntitySA_RockTank;
import advancearmy.render.ModelNoneVehicle;
@OnlyIn(Dist.CLIENT)
public class RenderRockTank extends MobRenderer<EntitySA_RockTank, ModelNoneVehicle<EntitySA_RockTank>>
{
	public ResourceLocation tex = ResourceLocation.tryParse("advancearmy:textures/mob/rocktank.png");
	private static final SAObjModel obj = new SAObjModel("advancearmy:textures/mob/rocktank.obj");
	public static final ResourceLocation ENCHANT_GLINT_LOCATION = ResourceLocation.tryParse("wmlib:textures/misc/vehicle_glint.png");

    public RenderRockTank(EntityRendererProvider.Context renderManagerIn)
    {
    	super(renderManagerIn, new ModelNoneVehicle(),0);
	}

    public ResourceLocation getTextureLocation(EntitySA_RockTank entity)
    {
    	return tex;
	}
    
    public boolean shouldRender(EntitySA_RockTank entity, Frustum camera, double camX, double camY, double camZ) {
        return super.shouldRender(entity, camera, camX, camY, camZ)||entity.distanceToSqr(camX,camY,camZ)<10;
    }
	RenderType rt = RenderTypeVehicle.objrender(tex);
	RenderType hide = SARenderState.getBlendDepthWrite(tex);//getBlendDepthWrite_NoLight
	RenderType light = SARenderState.getBlendDepthWrite(tex);
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
	
    private void render_part(PoseStack stack,EntitySA_RockTank entity, String name){
        obj.renderPart(name);
        if (entity.getEnc() > 0){
            renderEnchantGlint(stack, name);
			if(rt!=null)obj.setRenderType(rt);
        }
    }
	
	void renderFloat(PoseStack stack,EntitySA_RockTank entity,int packedLightIn,float x,float y,float z,float size){
		stack.pushPose();
		stack.translate(x, y, z);
		stack.mulPose(Axis.ZP.rotationDegrees(entity.rote_wheelz));
		stack.mulPose(Axis.XP.rotationDegrees(entity.rote_wheelx));
		stack.translate(-x, -y, -z);
		stack.translate(x, y, z);
		render_part(stack, entity, "rote");
		obj.setRender(light,null,stack,0xF000F0);
		obj.renderPart("rote_light");
		
		stack.scale(size,size,size);
		obj.renderPart("ring");
		obj.setRender(rt,null,stack,packedLightIn);
		stack.popPose();
	}
	
    float iii;
	private float recoilTime = 0f; // 用于三角函数的时间参数
	private float recoilIntensity = 0f; // 震动强度
	private float[] randomFactors = new float[3]; // 存储随机因素
	public static double noise(double x, double y) {
		return Math.sin(x * 10 + y * 5) * 0.5 + 0.5; // 简化版本
	}

    public void render(EntitySA_RockTank entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource bufferIn, int packedLightIn)
    {
		super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
	    Minecraft mc = Minecraft.getInstance();
		stack.pushPose();
		
		RenderRote.setRote(stack,180F, 0.0F, 1.0F, 0.0F);
		obj.setRender(rt,null,stack,packedLightIn);
		if(entity.anim1 < 12) {
			if(entity.anim1 == 0) {
				recoilTime = 0f;
				recoilIntensity = 1.0f; // 初始强度
				for(int i = 0; i < 3; i++) {
					randomFactors[i] = (entity.level().random.nextFloat() - 0.5f) * 0.4f;
				}
			}
			float progress = entity.anim1 / 12.0f;
			recoilTime += 0.2f; // 控制振荡速度
			recoilIntensity = (1.0f - progress) * (0.5f + 0.5f * (float)Math.sin(recoilTime));
			float highFreqShake = (float)Math.sin(recoilTime * 3f) * 0.1f * (1.0f - progress);
		} else {
			recoilIntensity = 0f;
		}
		{
			{
				RenderRote.setRote(stack,180.0F - entity.turretYaw, 0.0F, 1.0F, 0.0F);
				if(entity.anim1 < 12) {
					float progress = entity.anim1 / 12.0f;
					// 使用不同的三角函数创建多轴震动
					float pitchShake = (float)Math.sin(recoilTime) * recoilIntensity * (1.0f + randomFactors[0]);
					float yawShake = (float)Math.sin(recoilTime * 0.8f + 0.5f) * recoilIntensity * (0.7f + randomFactors[1]);
					float rollShake = (float)Mth.cos(recoilTime * 1.2f) * recoilIntensity * 0.3f * randomFactors[2];
					
					RenderRote.setRote(stack,pitchShake * 6f *entity.w1barrelsize, 1.0F, 0.0F, 0.0F);
					RenderRote.setRote(stack,-yawShake * 5f *entity.w1barrelsize, 0.0F, 1.0F, 0.0F);
					RenderRote.setRote(stack,rollShake * 4f *entity.w1barrelsize, 0.0F, 0.0F, 1.0F);
					
					float pushBack = (float)Mth.cos(recoilTime * 0.5f) * recoilIntensity * 0.02f;
					stack.translate(0, 0, -pushBack);
					if(recoilIntensity > 0.3f) {
						float noiseX = (float)this.noise(recoilTime * 10f, 0) * 0.005f * recoilIntensity;
						float noiseY = (float)this.noise(0, recoilTime * 10f) * 0.005f * recoilIntensity;
						float noiseZ = (float)this.noise(recoilTime * 10f, recoilTime * 10f) * 0.005f * recoilIntensity;
						stack.translate(noiseX, noiseY, noiseZ);
					}
				}
				RenderRote.setRote(stack,-(180.0F - entity.turretYaw), 0.0F, 1.0F, 0.0F);
			}
			{
				RenderRote.setRote(stack,180.0F - (entity.yHeadRotO + (entity.yHeadRot - entity.yHeadRotO) * partialTicks), 0.0F, 1.0F, 0.0F);
			}
			RenderRote.setRote(stack,entity.currentTilt, 1.0F, 0.0F, 0.0F);
			float limbSwing = this.F6(entity, partialTicks);//
			float limbSwingAmount = this.F5(entity, partialTicks);//
			float Ax1 = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * limbSwingAmount;//
			stack.translate(0, Ax1 * (180F / (float)Math.PI) * 0.0002F , 0);
			RenderRote.setRote(stack,Ax1 * (180F / (float)Math.PI) * 0.0002F, 1.0F, 0.0F, 0.0F);
			RenderRote.setRote(stack,Ax1 * (180F / (float)Math.PI) * 0.0002F, 0.0F, 1.0F, 0.0F);
			if(entity.getTargetType()==2){
				RenderSystem.setShaderColor(0.7F, 0.4F, 0.4F,1F);
				obj.setColor(-999, 0, 0, 1F);
			}
			if(entity.deathTime > 0){
				RenderSystem.setShaderColor(0.1F, 0.1F, 0.1F, 1F);
				obj.setColor(-999, 0, 0, 1F);
			}
			
			int mirageCount = 1;
			if(entity.getArmyType2()==1){
				if(iii<1)iii+=0.02F;
				mirageCount=4;
			}else{
				iii=0;
			}
			for(int i = 0; i < mirageCount; i++){
				stack.pushPose();
				if(i==2)stack.translate(-8F*iii, 0F, 0F);
				if(i==3)stack.translate(8F*iii, 0F, 0F);
				if(i==4)stack.translate(0F, 0F, -8F*iii);
				float size = 1+partialTicks*0.1F;
				renderFloat(stack, entity, packedLightIn,2.13F,0.35F,2.1F, size);
				renderFloat(stack, entity, packedLightIn,-2.13F,0.35F,2.1F, size);
				
				renderFloat(stack, entity, packedLightIn,2.13F,0.35F,-0.37F, size);
				renderFloat(stack, entity, packedLightIn,-2.13F,0.35F,-0.37F, size);
				
				renderFloat(stack, entity, packedLightIn,2.13F,0.65F,-2.52F, size);
				renderFloat(stack, entity, packedLightIn,-2.13F,0.65F,-2.52F, size);
				
				render_part(stack, entity,"body");
				{
					obj.setRender(light,null,stack,0xF000F0);
					obj.renderPart("body_light");
					obj.setRender(rt,null,stack,packedLightIn);
				}
				{
					float size2 = entity.level().random.nextInt(4) * 0.3F + 1;
					stack.pushPose();//glstart
					stack.translate(0F, 0F, 0F);
					RenderRote.setRote(stack,180.0F - (entity.turretYawO + (entity.turretYaw - entity.turretYawO) * partialTicks) -(180.0F - (entity.yHeadRotO + (entity.yHeadRot - entity.yHeadRotO) * partialTicks)), 0.0F, 1.0F, 0.0F);
					stack.translate(-0F, -0F, -0F);
					render_part(stack, entity,"turret");
					obj.setRender(light,null,stack,0xF000F0);
					obj.renderPart("turret_light");
					obj.setRender(rt,null,stack,packedLightIn);
					
					stack.translate(0F, 2.9F, 1.71F);
					RenderRote.setRote(stack,(entity.turretPitchO + (entity.turretPitch - entity.turretPitchO) * partialTicks), 1.0F, 0.0F, 0.0F);
					stack.translate(0F, -2.9F, -1.71F);
					render_part(stack, entity,"cannon");
					obj.setRender(light,null,stack,0xF000F0);
					obj.renderPart("cannon_light");
					stack.popPose();//glend
				}
				stack.popPose();
			}
		}
		RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
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
            f5 = entity.walkAnimation.speed() + (entity.walkAnimation.speed() - entity.walkAnimation.speed()) * partialTicks;
            if (f5 > 1.0F)
            {
                f5 = 1.0F;
            }
        }
 		return f5;
 	}
}