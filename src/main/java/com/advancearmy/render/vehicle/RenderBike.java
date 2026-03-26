package advancearmy.render.vehicle;

import advancearmy.entity.land.EntitySA_Bike;
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
public class RenderBike extends MobRenderer<EntitySA_Bike, ModelNoneVehicle<EntitySA_Bike>>
{
	private static final ResourceLocation tex = ResourceLocation.tryParse("advancearmy:textures/mob/lav25.png");
	private static final SAObjModel obj = new SAObjModel("advancearmy:textures/mob/bike.obj");
	public static final ResourceLocation ENCHANT_GLINT_LOCATION = ResourceLocation.tryParse("wmlib:textures/misc/vehicle_glint.png");
	RenderType rt = RenderTypeVehicle.objrender(tex);
	RenderType glint = SARenderState.getBlendGlowGlint(ENCHANT_GLINT_LOCATION);
    private void render_part(EntitySA_Bike entity, String name, PoseStack stack) {
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

    public RenderBike(EntityRendererProvider.Context context) {
        super(context, new ModelNoneVehicle(),4.0F);
        this.shadowRadius = 1.0F;
    }
    @Override
    public ResourceLocation getTextureLocation(EntitySA_Bike entity) {
        return tex;
    }
	
	public void render(EntitySA_Bike entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight) 
    {
		super.render(entity, entityYaw, partialTicks, stack, buffer, packedLight);
	    stack.pushPose();
		stack.mulPose(Axis.YP.rotationDegrees(180.0F));
		obj.setRender(rt, null, stack, packedLight);
		if(entity.getTargetType()==2){
			RenderSystem.setShaderColor(0.7F, 0.4F, 0.4F,1F);
			obj.setColor(-999, 0, 0, 1F);
		}
		if(entity.deathTime > 0){
			RenderSystem.setShaderColor(0.1F, 0.1F, 0.1F, 1F);
			obj.setColor(-999, 0, 0, 1F);
		}
		
		
		float limbSwing = this.F6(entity, partialTicks);//
		float limbSwingAmount = this.F5(entity, partialTicks);//
		float Ax1 = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * limbSwingAmount;//
		stack.translate(0, Ax1 * (180F / (float)Math.PI) * 0.0002F , 0);
		stack.mulPose(Axis.XP.rotationDegrees(Ax1 * (180F / (float)Math.PI) * 0.0002F));
		stack.mulPose(Axis.YP.rotationDegrees(Ax1 * (180F / (float)Math.PI) * 0.0002F));
		stack.mulPose(Axis.YP.rotationDegrees(180.0F - (entity.yHeadRotO + (entity.yHeadRot - entity.yHeadRotO) * partialTicks)));

		obj.renderPart("mat1");
		
		if(entity.throttle>2||entity.throttle<-2){
			obj.renderPart("wait2");
		}else{
			obj.renderPart("wait1");
		}
		
		stack.pushPose();//glstart
		stack.mulPose(Axis.XP.rotationDegrees(-25F));
		stack.pushPose();//glstart
		stack.translate(0, 0.39F-0.5F, 1.1F);
		stack.translate(0, 0, 0);
		stack.mulPose(Axis.YP.rotationDegrees(entity.rote_wheel));
		stack.translate(0, 0, 0);
		obj.renderPart("hand");
		stack.popPose();//glend
		
		stack.pushPose();//glstart
		stack.translate(0, 0.39F-0.5F, 1.1F);
		stack.translate(0, 0, 0);//
		stack.mulPose(Axis.YP.rotationDegrees(entity.rote_wheel));
		stack.mulPose(Axis.XP.rotationDegrees(entity.thpera*2F));
		stack.translate(0, 0, 0);//
		obj.renderPart("wheel_1");
		stack.popPose();//glend
		stack.popPose();//glend
		
		stack.pushPose();//glstart
		stack.translate(0, 0.39F, -0.8F);
		stack.mulPose(Axis.XP.rotationDegrees(entity.thpera*2F));
		stack.translate(0, -0.39F, 0.8F);
		stack.translate(0, 0.39F, -0.8F);
		obj.renderPart("wheel_2");
		stack.popPose();//glend
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