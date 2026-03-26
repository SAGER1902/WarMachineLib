package advancearmy.render.soldier;

import com.mojang.math.Axis;
import org.lwjgl.opengl.GL12;
import advancearmy.entity.soldier.EntitySA_MWDrone;
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
import advancearmy.render.ModelNone;
@OnlyIn(Dist.CLIENT)
public class RenderMWDrone extends MobRenderer<EntitySA_MWDrone, ModelNone<EntitySA_MWDrone>>
{
	private static final ResourceLocation tex = ResourceLocation.tryParse("advancearmy:textures/mob/soldier/mwdrone.png");
	private static SAObjModel obj = new SAObjModel("advancearmy:textures/mob/soldier/mwdrone.obj");
	private static final ResourceLocation fire_tex = ResourceLocation.tryParse("advancearmy:textures/entity/flash/tankmflash.png");
	public static final ResourceLocation ENCHANT_GLINT_LOCATION = ResourceLocation.tryParse("wmlib:textures/misc/vehicle_glint.png");

	private static final SAObjModel objf = new SAObjModel("advancearmy:textures/mob/flash1.obj");
	RenderType f1 = SARenderState.getBlendDepthWrite(fire_tex);

	RenderType rt = RenderTypeVehicle.objrender(tex);
	RenderType rtlight = SARenderState.getBlendDepthWrite(tex);

	RenderType glint = SARenderState.getBlendGlowGlint(ENCHANT_GLINT_LOCATION);
	
    public RenderMWDrone(EntityRendererProvider.Context context)
    {
    	super(context, new ModelNone(),2F);
        this.shadowRadius = 2F;
    }

    public ResourceLocation getTextureLocation(EntitySA_MWDrone entity)
    {
		return tex;
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

    public void render(EntitySA_MWDrone entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight)
    {
		super.render(entity, entityYaw, partialTicks, stack, buffer, packedLight);
		stack.pushPose();
		
		obj.setRender(rt, null, stack, packedLight);
		objf.setRender(f1,null,stack,0xF000F0);
		Minecraft mc = Minecraft.getInstance();
		
		stack.mulPose(Axis.YP.rotationDegrees(180.0F));
		
		if(entity.deathTime > 0){
			RenderSystem.setShaderColor(0.1F, 0.1F, 0.1F, 1F);
			obj.setColor(-999, 0, 0, 1F);
		}

		stack.mulPose(Axis.YP.rotationDegrees(180.0F - (entity.yBodyRotO + (entity.yBodyRot - entity.yBodyRotO) * partialTicks)));

		float limbSwing = this.F6(entity, partialTicks);//
		float limbSwingAmount = this.F5(entity, partialTicks);//
		float Ax1 = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * limbSwingAmount;//
		stack.translate(0, Ax1 * (180F / (float)Math.PI) * 0.0002F , 0);
		RenderRote.setRote(stack,Ax1 * (180F / (float)Math.PI) * 0.0002F, 1.0F, 0.0F, 0.0F);
		RenderRote.setRote(stack,Ax1 * (180F / (float)Math.PI) * 0.0002F, 0.0F, 1.0F, 0.0F);

		obj.renderPart("body");
		obj.setRender(rtlight, null, stack, 0xF000F0);
		obj.renderPart("body_light");
		obj.setRender(rt, null, stack, packedLight);
		{
			stack.pushPose();
			stack.translate(0F, 0F, 0.07F);
			stack.mulPose(Axis.YP.rotationDegrees(180.0F - (entity.yHeadRotO + (entity.yHeadRot - entity.yHeadRotO) * partialTicks) -(180.0F - (entity.yBodyRotO + (entity.yBodyRot - entity.yBodyRotO) * partialTicks))));
			stack.translate(0F, 0F, -0.07F);
			obj.renderPart("turret");
			
			obj.setRender(rtlight, null, stack, 0xF000F0);
			obj.renderPart("turret_light");
			obj.setRender(rt, null, stack, packedLight);
			
			stack.translate(0, 1.7F, 0.02F);
			stack.mulPose(Axis.XP.rotationDegrees(entity.xRotO + (entity.getXRot() - entity.xRotO) * partialTicks));
			stack.translate(0, -1.7F, -0.02F);
			obj.renderPart("cannon");
			if(entity.anim1 <5){
				RenderSystem.setShaderColor(0.1F, 0.5F, 1F, 1F);
				float size = (float)(5-entity.anim1) / 3F;
				float fireX = 0.66F;
				if(entity.getRemain1()%2==0){
					fireX = 0.66F;
				}else{
					fireX = -0.66F;
				}
				renderFire(stack, size*0.5F, fireX, 1.7F, 0.97F);
				RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
			}
			obj.setRender(rtlight, null, stack, 0xF000F0);
			obj.renderPart("cannon_light");
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