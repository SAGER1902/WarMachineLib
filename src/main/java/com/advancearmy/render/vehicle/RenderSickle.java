package advancearmy.render.vehicle;
import advancearmy.entity.land.EntitySA_Sickle;
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
public class RenderSickle extends MobRenderer<EntitySA_Sickle, ModelNoneVehicle<EntitySA_Sickle>>
{
	private static final ResourceLocation tex = ResourceLocation.tryParse("advancearmy:textures/mob/sickle.png");
	private static final SAObjModel obj = new SAObjModel("advancearmy:textures/mob/sickle.obj");
	private static final ResourceLocation fire_tex = ResourceLocation.tryParse("advancearmy:textures/entity/flash/muzzleflash3.png");
	public static final ResourceLocation ENCHANT_GLINT_LOCATION = ResourceLocation.tryParse("wmlib:textures/misc/vehicle_glint.png");
	
	
    public RenderSickle(EntityRendererProvider.Context renderManagerIn)
    {
    	super(renderManagerIn, new ModelNoneVehicle(),1F);
        this.shadowStrength = 1F;
    }

    public ResourceLocation getTextureLocation(EntitySA_Sickle entity)
    {
		return tex;
    }
    
    public boolean shouldRender(EntitySA_Sickle entity, Frustum camera, double camX, double camY, double camZ) {
        return super.shouldRender(entity, camera, camX, camY, camZ)||entity.distanceToSqr(camX,camY,camZ)<10;
    }
	RenderType f1 = SARenderState.getBlendDepthWrite(fire_tex);
	RenderType light = SARenderState.getBlendDepthWrite(tex);
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
	
    private void render_part(PoseStack stack,EntitySA_Sickle entity, String name){
        obj.renderPart(name);
        if (entity.getEnc() > 0){
            renderEnchantGlint(stack, name);
			if(rt!=null)obj.setRenderType(rt);
        }
    }

	public void renderleg(PoseStack stack,int id,EntitySA_Sickle entity, float x, float y, float z, String name, float partialTicks, float hox, float hoy, float hoz, float level,
		float x1, float y1, float z1){
    	float limbSwing = this.F6(entity, partialTicks);
		float limbSwingAmount = this.F5(entity, partialTicks);
    	stack.pushPose();
    	stack.translate(x, y, z);
		float jumprote = 0;
		float xrote = 0;
    	float Ax = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * limbSwingAmount;
		if(entity.getMovePitch()>0){
			Ax = 0;
			if(entity.getDeltaMovement().y<0){
				jumprote = -30F;
			}else{
				jumprote = 60F;
			}
		}
		if (entity.getStrafingMove()>0.1F){
			xrote = 10F;
			Ax = -Ax;
		}else
		if (entity.getStrafingMove()<-0.1F){
			xrote = -10F;
		}
		if(id==2)jumprote=-jumprote;
		RenderRote.setRote(stack,jumprote * level, 0, 0, 1F);
    	RenderRote.setRote(stack,Ax * (180F / (float)Math.PI) * level, hox, hoy, hoz);
		RenderRote.setRote(stack,Ax * (180F / (float)Math.PI) * level*0.5F+xrote, 0, 0, 1F);
		stack.translate(-x, -y, -z);
		render_part(stack, entity,name);
		
		stack.translate(x1, y1, z1);
		RenderRote.setRote(stack,jumprote * level, 0, 0, 1F);
		if(Ax>0){
			RenderRote.setRote(stack,-Ax * (180F / (float)Math.PI) * level*1.25F+xrote, 0, 0, 1F);
		}else{
			RenderRote.setRote(stack,Ax * (180F / (float)Math.PI) * level*1.25F+xrote, 0, 0, 1F);
		}
		stack.translate(-x1, -y1, -z1);
		render_part(stack, entity,name+"1");
		stack.popPose();
		
    }
	static int tick1 = 0;
	static int tick2 = 0;
	static int tick3 = 0;
	private void render_turret(int packedLightIn, PoseStack stack,EntitySA_Sickle entity, String name, float x, float y, float z, float rote, float pitch, float fire, float tick, int id){
		stack.pushPose();//glstart
		stack.translate(x, y, z);//
		RenderRote.setRote(stack,rote, 0.0F, 1.0F, 0.0F);
		stack.translate(-x, -y, -z);//
		
		if(id!=2)render_part(stack, entity,name);
		if(id==1){
			obj.setRender(light,null,stack,0xF000F0);
			obj.renderPart("turret_light");
			obj.setRender(rt,null,stack,packedLightIn);
		}
		
		stack.translate(x, y, z+0.44F);//
		RenderRote.setRote(stack,pitch, 1.0F, 0.0F, 0.0F);
		stack.translate(-x, -y, -z-0.44F);//
		if(id==1){
			render_part(stack, entity,"barrel");
		}
		if(id==3){
			stack.pushPose();//glstart
			//stack.translate(x, y, z);
			render_part(stack, entity,"barrel2");
			stack.popPose();//glend
			stack.pushPose();//glstart
			obj.setRender(f1,null,stack,0xF000F0);
			
			stack.translate(entity.turretX, y, 0.8F);//
			if(fire < 3){
				float size = (float)(2+fire) / 3F;
				stack.scale(size,size,size);
			}
			if(fire >= 3 && fire<5){
				float size = (float)(6-fire) / 4F;
				stack.scale(size,size,size);
			}
			stack.translate(-entity.turretX, -y, -0.8F);//
			stack.translate(entity.turretX, y, 0.8F);
			if(fire<5)obj.renderPart("flash1");
			
			obj.setRender(rt,null,stack,packedLightIn);
			stack.popPose();//glend
		}
		if(id==2){
			stack.pushPose();//glstart
			stack.translate(x, y, z);//
			RenderRote.setRote(stack,tick*10F, 0.0F, 0.0F, 1.0F);
			stack.translate(-x, -y, -z);//
			render_part(stack, entity,name);
			stack.translate(x, y, z+0.2F);
			if(fire <4){
				stack.pushPose();//glstart
				obj.setRender(f1,null,stack,0xF000F0);
				if(entity.level().random.nextInt(4)==1){
					obj.renderPart("mat_1");
				}else if(entity.level().random.nextInt(4)==2){
					obj.renderPart("mat_2");
				}else if(entity.level().random.nextInt(4)==0){
					obj.renderPart("mat_3");
				}
				obj.setRender(rt,null,stack,packedLightIn);
				stack.popPose();//glend
			}
			stack.popPose();//glend
		}
		stack.popPose();//glend
	}
	
	static boolean glow = true;
	static int shock =0;
    public void render(EntitySA_Sickle entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource bufferIn, int packedLightIn)
    {
		super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
	    Minecraft mc = Minecraft.getInstance();
		stack.pushPose();
		stack.pushPose();
		
		if(entity.getTargetType()==2){
			RenderSystem.setShaderColor(0.7F, 0.4F, 0.4F,1F);
			obj.setColor(-999, 0, 0, 1F);
		}
		if(entity.deathTime > 0){
			RenderSystem.setShaderColor(0.1F, 0.1F, 0.1F, 1F);
			obj.setColor(-999, 0, 0, 1F);
		}

		
    	obj.setRender(rt,null,stack,packedLightIn);
		RenderRote.setRote(stack,180F, 0.0F, 1.0F, 0.0F);

		{//开炮抖动
			if(entity.hurtTime>0){
				if(!glow && this.shock<12){
					++this.shock;
				}
				if(this.shock>=12)glow = true;
				if(glow && this.shock>0){
					--this.shock;
				}
				if(this.shock<=0)glow = false;
			}else{
				this.shock=0;
			}
		}
		{
			RenderRote.setRote(stack,180.0F - entity.turretYaw, 0.0F, 1.0F, 0.0F);
			if(entity.hurtTime>0){
				float count = 24-entity.hurtTime;
				RenderRote.setRote(stack,-this.shock * count/50F, 1.0F, 0.0F, 0.0F);
				RenderRote.setRote(stack,-this.shock * count/60F, 0.0F, 1.0F, 0.0F);
			}
			RenderRote.setRote(stack,-(180.0F - entity.turretYaw), 0.0F, 1.0F, 0.0F);
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
		
		float limbSwing = this.F6(entity, partialTicks);//
		float limbSwingAmount = this.F5(entity, partialTicks);//
		float Ax1 = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * limbSwingAmount;//
		
		//float size1 = entity.level().random.nextInt(10) * 0.01F;
		renderleg(stack,1,entity, -0.67F, 1F, 0.65F, "leg1", partialTicks, 1, -1F, 0F, 0.3F, -1.66F,1.72F,1.06F);
		renderleg(stack,1,entity, -0.67F, 1F, -0.67F, "leg2", partialTicks, -1, 1F, 0F, 0.3F, -1.66F,1.72F,-1.06F);
		
		renderleg(stack,2,entity, 0.67F, 1F, 0.65F, "leg3", partialTicks, 1, -1F, 0F, 0.4F, 1.66F,1.72F,1.06F);
		renderleg(stack,2,entity, 0.67F, 1F, -0.67F, "leg4", partialTicks, -1, 1F, 0F, 0.4F, 1.66F,1.72F,-1.06F);
		
		stack.translate(0, Ax1 * (180F / (float)Math.PI) * 0.0005F , 0);
		render_part(stack, entity,"body");

		float rote1 = 180.0F - (entity.turretYawO + (entity.turretYaw - entity.turretYawO) * partialTicks) -(180.0F - (entity.yHeadRotO + (entity.yHeadRot - entity.yHeadRotO) * partialTicks));
		float rote2 = 180.0F - entity.turretYaw1 -(180.0F - (entity.turretYawO + (entity.turretYaw - entity.turretYawO) * partialTicks));
		if(entity.anim1<5){
			if(tick1<36F){
				++tick1;
			}else{
				tick1 = 0;
			}
		}
		if(entity.anim3<5){
			if(tick2<36F){
				++tick2;
			}else{
				tick2 = 0;
			}
		}
		
		render_turret(packedLightIn, stack,entity, "turret", 0F, 3.11F, 0F, rote1, (entity.xRotO + (entity.getXRot() - entity.xRotO) * partialTicks), 0, 0, 1);
		
		stack.translate(0F, 0F, 0F);
		RenderRote.setRote(stack,180.0F - (entity.turretYawO + (entity.turretYaw - entity.turretYawO) * partialTicks) -(180.0F - (entity.yHeadRotO + (entity.yHeadRot - entity.yHeadRotO) * partialTicks)), 0.0F, 1.0F, 0.0F);
		stack.translate(-0F, -0F, -0F);

		render_turret(packedLightIn, stack,entity, "rote1", -1.25F, 2.83F, 0F, 0, (entity.xRotO + (entity.getXRot() - entity.xRotO) * partialTicks), entity.anim1, tick1, 2);
		render_turret(packedLightIn, stack,entity, "rote2", 1.25F, 2.83F, 0F, 0, (entity.xRotO + (entity.getXRot() - entity.xRotO) * partialTicks), entity.anim1, tick1, 2);

		render_turret(packedLightIn, stack,entity, "turret2", 0, 4.04F, -0.54F, rote2, entity.turretPitch1, entity.anim3, tick2, 3);
		
		
		
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