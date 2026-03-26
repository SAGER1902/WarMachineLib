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
import advancearmy.entity.land.EntitySA_Mirage;
import advancearmy.render.ModelNoneVehicle;
@OnlyIn(Dist.CLIENT)
public class RenderMirage extends MobRenderer<EntitySA_Mirage, ModelNoneVehicle<EntitySA_Mirage>>
{
	public ResourceLocation tex = ResourceLocation.tryParse("advancearmy:textures/mob/mirage.png");
	private static final SAObjModel obj = new SAObjModel("advancearmy:textures/mob/mirage.obj");
	public static final ResourceLocation ENCHANT_GLINT_LOCATION = ResourceLocation.tryParse("wmlib:textures/misc/vehicle_glint.png");
	public ResourceLocation ttex = ResourceLocation.tryParse("advancearmy:textures/mob/track.png");
	RenderType rttrack = RenderTypeVehicle.objrender_track(ttex);
	RenderType hidetrack = SARenderState.getBlendDepthWrite(ttex);
    private static final ResourceLocation log = ResourceLocation.tryParse("textures/block/oak_log.png");
    private static final ResourceLocation log2 = ResourceLocation.tryParse("textures/block/birch_log.png");
    private static final ResourceLocation log3 = ResourceLocation.tryParse("textures/block/spruce_log.png");
	
    private static final ResourceLocation leaf = ResourceLocation.tryParse("advancearmy:textures/mob/oak_leaves.png");
    private static final ResourceLocation leaf2 = ResourceLocation.tryParse("advancearmy:textures/mob/birch_leaves.png");
    private static final ResourceLocation leaf3 = ResourceLocation.tryParse("advancearmy:textures/mob/spruce_leaves.png");
	
    private static final ResourceLocation stone = ResourceLocation.tryParse("textures/block/cobblestone.png");
    private static final ResourceLocation sand = ResourceLocation.tryParse("textures/block/sand.png");
    private static final ResourceLocation snow = ResourceLocation.tryParse("textures/block/snow.png");
	private static final ResourceLocation dirt = ResourceLocation.tryParse("textures/block/dirt.png");
	public ResourceLocation ptex = ResourceLocation.tryParse("advancearmy:textures/entity/dun3.png");
    public RenderMirage(EntityRendererProvider.Context renderManagerIn)
    {
    	super(renderManagerIn, new ModelNoneVehicle(),0);
	}

    public ResourceLocation getTextureLocation(EntitySA_Mirage entity)
    {
    	return tex;
	}
    
    public boolean shouldRender(EntitySA_Mirage entity, Frustum camera, double camX, double camY, double camZ) {
        return super.shouldRender(entity, camera, camX, camY, camZ)||entity.distanceToSqr(camX,camY,camZ)<10;
    }
	RenderType rt = RenderTypeVehicle.objrender(tex);
	RenderType hide = SARenderState.getBlendDepthWrite(tex);//getBlendDepthWrite_NoLight
	RenderType dun = SARenderState.getBlendDepthWrite(ptex);
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
	
    private void render_part(PoseStack stack,EntitySA_Mirage entity, String name){
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

    public void render(EntitySA_Mirage entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource bufferIn, int packedLightIn)
    {
		super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
	    Minecraft mc = Minecraft.getInstance();
		stack.pushPose();
		stack.pushPose();
		RenderRote.setRote(stack,180F, 0.0F, 1.0F, 0.0F);
		obj.setRender(rt,null,stack,packedLightIn);
		
		
		if(entity.miragehide && entity.getArmyType2()==0 && 
		(entity.getControllingPassenger()!=mc.player||
		mc.options.getCameraType() != CameraType.FIRST_PERSON)){
			if(entity.wztype == 0){
				obj.setRenderType(RenderTypeVehicle.objrender(log));
				//tex.bind(log);		
				obj.renderPart("log");
				obj.setRenderType(RenderTypeVehicle.objrender_blend(leaf));
				//tex.bind(leaf);				
				obj.renderPart("leaf");
			}else if(entity.wztype == 1){		
				obj.setRenderType(RenderTypeVehicle.objrender(log2));
				//tex.bind(log2);		
				obj.renderPart("log2");
				obj.setRenderType(RenderTypeVehicle.objrender_blend(leaf2));
				//tex.bind(leaf2);			
				obj.renderPart("leaf2");
			}else if(entity.wztype == 2){		
				obj.setRenderType(RenderTypeVehicle.objrender(log3));
				//tex.bind(log3);		
				obj.renderPart("log3");
				obj.setRenderType(RenderTypeVehicle.objrender_blend(leaf3));
				//tex.bind(leaf3);			
				obj.renderPart("leaf3");
			}else if(entity.wztype == 3){
				obj.setRenderType(RenderTypeVehicle.objrender(stone));
				//tex.bind(stone);
				obj.renderPart("stone");
			}else if(entity.wztype == 4){		
				obj.setRenderType(RenderTypeVehicle.objrender(sand));
				//tex.bind(sand);
				obj.renderPart("sand");
			}else{	
				obj.setRenderType(RenderTypeVehicle.objrender(dirt));
				//tex.bind(dirt);
				obj.renderPart("snow");
			}
		}
		
		if(!entity.miragehide||entity.getOwner()==mc.player||entity.getArmyType2()>0){
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

			if(entity.miragehide && entity.getArmyType2()==0){
				obj.setRenderType(hide);
			}else{
				obj.setRender(rt,null,stack,packedLightIn);
			}
			if(entity.getTargetType()==2){
				RenderSystem.setShaderColor(0.7F, 0.4F, 0.4F,1F);
				obj.setColor(-999, 0, 0, 1F);
			}
			if(entity.deathTime > 0){
				RenderSystem.setShaderColor(0.1F, 0.1F, 0.1F, 1F);
				obj.setColor(-999, 0, 0, 1F);
			}

			render_part(stack, entity,"body");
			{
				if(entity.getArmyType2()>0){
					obj.setRender(dun,null,stack,0xF000F0);
					obj.renderPart("dun");
					obj.setRender(rt,null,stack,packedLightIn);
				}
				if(!entity.miragehide || entity.getArmyType2()==1)obj.setRender(light,null,stack,0xF000F0);
				obj.renderPart("body_light");
				if(!entity.miragehide || entity.getArmyType2()==1)obj.setRender(rt,null,stack,packedLightIn);
			}
			
			if(!entity.miragehide || entity.getArmyType2()==1){
				obj.setRender(rttrack,null,stack,packedLightIn);
			}else{
				obj.setRenderType(hidetrack);
			}
			Matrix4f mover = new Matrix4f().translation(0, -entity.throttleRight, 0.0f);
			RenderSystem.setTextureMatrix(mover);
			obj.renderPart("track_r");
			RenderSystem.resetTextureMatrix();
			Matrix4f movel = new Matrix4f().translation(0, -entity.throttleLeft, 0.0f);
			RenderSystem.setTextureMatrix(movel);
			obj.renderPart("track_l");
			RenderSystem.resetTextureMatrix();
			float range = 0;
			{
				range = 0.0005F;
			}
			if(!entity.miragehide || entity.getArmyType2()==1){
				obj.setRender(rt,null,stack,packedLightIn);
			}else{
				obj.setRenderType(hide);
			}

			for(int t1 = 0; t1 < entity.wheelcount; ++t1){
				stack.pushPose();
				String tu1 = String.valueOf(t1 + 1);
				stack.translate(entity.wheelx[t1], entity.wheely[t1], entity.wheelz[t1]);//
				RenderRote.setRote(stack,entity.thpera*entity.wheel_rotex, 1.0F, 0.0F, 0.0F);
				RenderRote.setRote(stack,entity.thpera*entity.wheel_rotey, 0.0F, 1.0F, 0.0F);
				RenderRote.setRote(stack,entity.thpera*entity.wheel_rotez, 0.0F, 0.0F, 1.0F);
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
				float size2 = entity.level().random.nextInt(4) * 0.3F + 1;
				stack.pushPose();//glstart
				stack.translate(0F, 0F, 0F);
				RenderRote.setRote(stack,180.0F - (entity.turretYawO + (entity.turretYaw - entity.turretYawO) * partialTicks) -(180.0F - (entity.yHeadRotO + (entity.yHeadRot - entity.yHeadRotO) * partialTicks)), 0.0F, 1.0F, 0.0F);
				stack.translate(-0F, -0F, -0F);
				
				render_part(stack, entity,"turret");
				
				stack.translate(0F, 3.45F, 0F);
				RenderRote.setRote(stack,(entity.turretPitchO + (entity.turretPitch - entity.turretPitchO) * partialTicks), 1.0F, 0.0F, 0.0F);
				stack.translate(0F, -3.45F, -0F);
				render_part(stack, entity,"barrel");
				
				if(!entity.miragehide || entity.getArmyType2()==1)obj.setRender(light,null,stack,0xF000F0);
				obj.renderPart("barrel_light");
				
				stack.popPose();//glend
			}
		}
		RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
		stack.popPose();
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