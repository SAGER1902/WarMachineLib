package advancearmy.render.vehicle;

import com.mojang.math.Axis;
import org.lwjgl.opengl.GL12;
import advancearmy.entity.land.EntitySA_LAVAA;
import advancearmy.entity.EntitySA_LandBase;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;

import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.core.BlockPos;

import net.minecraft.client.renderer.RenderType;
import wmlib.client.render.SARenderHelper;
import wmlib.client.render.SARenderHelper.RenderTypeSA;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;


import wmlib.client.render.RenderRote;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.player.Player;
import wmlib.client.obj.SAObjModel;
import net.minecraft.client.CameraType;
import org.joml.Matrix4f;
import wmlib.client.render.SARenderState;
import wmlib.client.render.RenderTypeVehicle;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.Util;
import com.mojang.blaze3d.systems.RenderSystem;
import advancearmy.render.ModelNoneVehicle;
@OnlyIn(Dist.CLIENT)
public class RenderLAV extends MobRenderer<EntitySA_LandBase, ModelNoneVehicle<EntitySA_LandBase>>
{
	private static final ResourceLocation tex = ResourceLocation.tryParse("advancearmy:textures/mob/lav25.png");
	private static SAObjModel obj = new SAObjModel("advancearmy:textures/mob/lav25.obj");
	private static final ResourceLocation fire_tex = ResourceLocation.tryParse("advancearmy:textures/entity/flash/muzzleflash3.png");
	private static final ResourceLocation tankmflash = ResourceLocation.tryParse("advancearmy:textures/entity/flash/tankmflash.png");
	public static final ResourceLocation ENCHANT_GLINT_LOCATION = ResourceLocation.tryParse("wmlib:textures/misc/vehicle_glint.png");

	RenderType rt = RenderTypeVehicle.objrender(tex);
	RenderType f1 = SARenderState.getBlendDepthWrite(fire_tex);
	RenderType f2 = SARenderState.getBlendDepthWrite(tankmflash);
	RenderType glint = SARenderState.getBlendGlowGlint(ENCHANT_GLINT_LOCATION);
	
    public RenderLAV(EntityRendererProvider.Context context)
    {
    	super(context, new ModelNoneVehicle(),2.5F);
        this.shadowRadius = 2.5F;
    }

    public ResourceLocation getTextureLocation(EntitySA_LandBase entity)
    {
		return tex;
    }
    
    public boolean shouldRender(EntitySA_LandBase entity, Frustum camera, double camX, double camY, double camZ) {
        return super.shouldRender(entity, camera, camX, camY, camZ)||entity.distanceToSqr(camX,camY,camZ)<10;
    }
	
    private void renderEnchantGlint(PoseStack stack, String name) {
		obj.setRender(glint, null, stack, 15728880);
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
			obj.setRenderType(rt);
        }
    }
	int iii=0;
	private void render_wheel(PoseStack stack, EntitySA_LandBase entity, String name, float x, float y, float z){
		stack.pushPose();
		stack.translate(x, y, z);
		RenderRote.setRote(stack,(float)entity.thpera*10F, 1.0F, 0.0F, 0.0F);
		stack.translate(-x, -y, -z);
		stack.translate(x, y, z);
		obj.renderPart(name);
		stack.popPose();
		
		stack.pushPose();
		stack.translate(-x, y, z);
		stack.mulPose(Axis.YP.rotationDegrees(180.0F));
		RenderRote.setRote(stack,(float)entity.thpera*10F, 1.0F, 0.0F, 0.0F);
		stack.translate(x, -y, -z);
		stack.translate(-x, y, z);
		obj.renderPart(name);
		stack.popPose();
	}

    public void render(EntitySA_LandBase entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight)
    {
		super.render(entity, entityYaw, partialTicks, stack, buffer, packedLight);
		stack.pushPose();
		
		if(entity.obj!=null)obj = entity.obj;
		obj.setRender(rt, null, stack, packedLight);
		
		Minecraft mc = Minecraft.getInstance();
		
		stack.mulPose(Axis.YP.rotationDegrees(180.0F));
		
		if(entity.getTargetType()==2){
			RenderSystem.setShaderColor(0.7F, 0.4F, 0.4F,1F);
			obj.setColor(-999, 0, 0, 1F);
		}
		if(entity.deathTime > 0){
			RenderSystem.setShaderColor(0.1F, 0.1F, 0.1F, 1F);
			obj.setColor(-999, 0, 0, 1F);
		}

		stack.mulPose(Axis.YP.rotationDegrees(180.0F - (entity.yHeadRotO + (entity.yHeadRot - entity.yHeadRotO) * partialTicks)));

		float limbSwing = this.F6(entity, partialTicks);//
		float limbSwingAmount = this.F5(entity, partialTicks);//
		float Ax1 = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * limbSwingAmount;//
		stack.translate(0, Ax1 * (180F / (float)Math.PI) * 0.0002F , 0);
		RenderRote.setRote(stack,Ax1 * (180F / (float)Math.PI) * 0.0002F, 1.0F, 0.0F, 0.0F);
		RenderRote.setRote(stack,Ax1 * (180F / (float)Math.PI) * 0.0002F, 0.0F, 1.0F, 0.0F);

		render_part(stack, entity,"mat1");
		
		float range = 0;
		{
			range = 0.0002F;
		}
		
		stack.pushPose();
		stack.translate(1.12F, 0.55F, 2.19F);
		stack.translate(0, Ax1 * (180F / (float)Math.PI) * -range, 0);
		RenderRote.setRote(stack,entity.rote_wheel, 0.0F, 1.0F, 0.0F);
		RenderRote.setRote(stack,(float)entity.thpera*10F, 1.0F, 0.0F, 0.0F);
		stack.translate(-1.12F, -0.55F, -2.19F);
		stack.translate(1.12F, 0.55F, 2.19F);
		obj.renderPart("wheel");
		stack.popPose();
		
		stack.pushPose();
		stack.translate(-1.12F, 0.55F, 2.19F);
		stack.translate(0, Ax1 * (180F / (float)Math.PI) * range, 0);
		RenderRote.setRote(stack,entity.rote_wheel, 0.0F, 1.0F, 0.0F);
		stack.mulPose(Axis.YP.rotationDegrees(180.0F));
		RenderRote.setRote(stack,(float)entity.thpera*10F, 1.0F, 0.0F, 0.0F);
		stack.translate(1.12F, -0.55F, -2.19F);
		stack.translate(-1.12F, 0.55F, 2.19F);
		obj.renderPart("wheel");
		stack.popPose();
		
		stack.pushPose();
		stack.translate(1.12F, 0.55F, 0.79F);
		stack.translate(0, Ax1 * (180F / (float)Math.PI) * -range, 0);
		RenderRote.setRote(stack,entity.rote_wheel, 0.0F, 1.0F, 0.0F);
		RenderRote.setRote(stack,(float)entity.thpera*10F, 1.0F, 0.0F, 0.0F);
		stack.translate(-1.12F, -0.55F, -0.79F);
		stack.translate(1.12F, 0.55F, 0.79F);
		obj.renderPart("wheel");
		stack.popPose();
		
		stack.pushPose();
		stack.translate(-1.12F, 0.55F, 0.79F);
		stack.translate(0, Ax1 * (180F / (float)Math.PI) * range, 0);
		RenderRote.setRote(stack,entity.rote_wheel, 0.0F, 1.0F, 0.0F);
		stack.mulPose(Axis.YP.rotationDegrees(180.0F));
		RenderRote.setRote(stack,(float)entity.thpera*10F, 1.0F, 0.0F, 0.0F);
		stack.translate(1.12F, -0.55F, -0.79F);
		stack.translate(-1.12F, 0.55F, 0.79F);
		obj.renderPart("wheel");
		stack.popPose();
		
		stack.pushPose();
		stack.translate(0, Ax1 * (180F / (float)Math.PI) * -range, 0);
		render_wheel(stack,entity, "wheel", 1.12F, 0.55F, -0.91F);
		stack.popPose();
		stack.pushPose();
		stack.translate(0, Ax1 * (180F / (float)Math.PI) * range, 0);
		render_wheel(stack,entity, "wheel", 1.12F, 0.55F, -2.18F);
		stack.popPose();
		{
			stack.pushPose();
			stack.translate(0.15F, 0F, -1.08F);
			RenderRote.setRote(stack,180.0F - (entity.turretYawO + (entity.turretYaw - entity.turretYawO) * partialTicks) -(180.0F - (entity.yHeadRotO + (entity.yHeadRot - entity.yHeadRotO) * partialTicks)), 0.0F, 1.0F, 0.0F);
			stack.translate(-0.15F, -0F, 1.08F);
			render_part(stack, entity,"turret");
			
			stack.translate(0, entity.fireposY1, -entity.fireposZ1);
			stack.mulPose(Axis.XP.rotationDegrees(entity.turretPitchO + (entity.turretPitch - entity.turretPitchO) * partialTicks));
			stack.translate(0, -entity.fireposY1, entity.fireposZ1);
			render_part(stack, entity,"barrel");
			
			if(entity instanceof EntitySA_LAVAA){
				stack.translate(0.24F, 2.87F, 0);
				if(entity.anim1 <4)++iii;
				RenderRote.setRote(stack,iii*10F, 0.0F, 0.0F, 1.0F);
				stack.translate(-0.24F, -2.87F, 0);
				stack.translate(0.24F, 2.87F, 0);
			}
			
			if(entity.anim1 <4){
				stack.pushPose();
				if(entity instanceof EntitySA_LAVAA){
					obj.setRender(f1, null, stack, 0xF000F0);
				}else{
					obj.setRender(f2, null, stack, 0xF000F0);
				}
				
				//tex---(fire_tex);
				if(entity.level().random.nextInt(2)==1){
					obj.renderPart("mat_1");
				}else if(entity.level().random.nextInt(2)==2){
					obj.renderPart("mat_2");
				}else{
					obj.renderPart("mat_3");
				}
				stack.popPose();
			}
			obj.setRender(rt, null, stack, packedLight);
			obj.renderPart("barrel_rote");
			
			if(entity.anim1 >= 0 && entity.anim1 < 4){
				stack.translate(0.0F, 0.0F, -entity.anim1 * 0.3F*0.2F);
			}
			if(entity.anim1 >= 4 && entity.anim1 < 8){
				stack.translate(0.0F, 0.0F, -1.2F*0.2F);
				stack.translate(0.0F, 0.0F, entity.anim1 * 0.1F*0.2F);
			}
			obj.renderPart("barrel_1");
			stack.popPose();
		}
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