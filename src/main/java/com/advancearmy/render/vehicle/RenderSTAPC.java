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
import net.minecraft.Util;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.CameraType;
import org.joml.Matrix4f;
import advancearmy.entity.land.EntitySA_STAPC;
import advancearmy.render.ModelNoneVehicle;
@OnlyIn(Dist.CLIENT)
public class RenderSTAPC extends MobRenderer<EntitySA_STAPC, ModelNoneVehicle<EntitySA_STAPC>>
{
	public ResourceLocation tex = ResourceLocation.tryParse("advancearmy:textures/mob/stapc.png");
	private static final SAObjModel obj = new SAObjModel("advancearmy:textures/mob/stapc.obj");
	public static final ResourceLocation ENCHANT_GLINT_LOCATION = ResourceLocation.tryParse("wmlib:textures/misc/vehicle_glint.png");

    public RenderSTAPC(EntityRendererProvider.Context renderManagerIn)
    {
    	super(renderManagerIn, new ModelNoneVehicle(),0);
	}

    public ResourceLocation getTextureLocation(EntitySA_STAPC entity)
    {
    	return tex;
	}
    
    public boolean shouldRender(EntitySA_STAPC entity, Frustum camera, double camX, double camY, double camZ) {
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
	
    private void render_part(PoseStack stack,EntitySA_STAPC entity, String name){
        obj.renderPart(name);
        if (entity.getEnc() > 0){
            renderEnchantGlint(stack, name);
			if(rt!=null)obj.setRenderType(rt);
        }
    }
	void renderFloat(PoseStack stack,EntitySA_STAPC entity,int packedLightIn,float x,float y,float z,float size){
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

    public void render(EntitySA_STAPC entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource bufferIn, int packedLightIn)
    {
		super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
	    Minecraft mc = Minecraft.getInstance();
		stack.pushPose();
		stack.pushPose();
		RenderRote.setRote(stack,180F, 0.0F, 1.0F, 0.0F);
		obj.setRender(rt,null,stack,packedLightIn);
		{
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
			RenderRote.setRote(stack,180.0F - (entity.yHeadRotO + (entity.yHeadRot - entity.yHeadRotO) * partialTicks), 0.0F, 1.0F, 0.0F);
			
			float size = 1+partialTicks*0.1F;
			renderFloat(stack, entity, packedLightIn,2F,-0.3F,2.24F, size);
			renderFloat(stack, entity, packedLightIn,-2F,-0.3F,2.24F, size);
			
			renderFloat(stack, entity, packedLightIn,2F,-0.08F,0F, size);
			renderFloat(stack, entity, packedLightIn,-2F,-0.08F,0, size);
			
			renderFloat(stack, entity, packedLightIn,2F,0.24F,-2.57F, size);
			renderFloat(stack, entity, packedLightIn,-2F,0.24F,-2.57F, size);
			
			render_part(stack, entity,"body");
			
			stack.pushPose();//glstart
			stack.translate(0, 0.74F, -3.06F);
			RenderRote.setRote(stack,entity.roteDoor*2, 1.0F, 0.0F, 0.0F);
			stack.translate(0, -0.74F, 3.06F);
			render_part(stack, entity,"door");
			stack.popPose();//glend
			
			
			{
				obj.setRender(light,null,stack,0xF000F0);
				obj.renderPart("body_light");
				obj.setRender(rt,null,stack,packedLightIn);
			}
			
			{
				float size2 = entity.level().random.nextInt(4) * 0.3F + 1;
				stack.pushPose();//glstart
				stack.translate(entity.seatPosX[1], entity.seatPosY[1], entity.seatPosZ[1]);
				RenderRote.setRote(stack,180.0F - (entity.turretYawO1 + (entity.turretYaw1 - entity.turretYawO1) * partialTicks) -(180.0F - (entity.yHeadRotO + (entity.yHeadRot - entity.yHeadRotO) * partialTicks)), 0.0F, 1.0F, 0.0F);
				stack.translate(-entity.seatPosX[1], -entity.seatPosY[1], -entity.seatPosZ[1]);
				render_part(stack, entity,"turret");
				obj.setRender(light,null,stack,0xF000F0);
				obj.renderPart("turret_light");
				obj.setRender(rt,null,stack,packedLightIn);
				
				stack.translate(entity.seatPosX[1], 1.9F, entity.seatPosZ[1]);
				RenderRote.setRote(stack,(entity.turretPitchO1 + (entity.turretPitch1 - entity.turretPitchO1) * partialTicks), 1.0F, 0.0F, 0.0F);
				stack.translate(-entity.seatPosX[1], -1.9F, -entity.seatPosZ[1]);
				render_part(stack, entity,"cannon");
				obj.setRender(light,null,stack,0xF000F0);
				obj.renderPart("cannon_light");
				
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