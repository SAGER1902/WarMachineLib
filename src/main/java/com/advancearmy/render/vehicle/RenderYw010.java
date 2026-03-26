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
import advancearmy.entity.EntitySA_HeliBase;
import advancearmy.render.ModelNoneVehicle;
@OnlyIn(Dist.CLIENT)
public class RenderYw010 extends MobRenderer<EntitySA_HeliBase, ModelNoneVehicle<EntitySA_HeliBase>>
{
	public ResourceLocation tex = ResourceLocation.tryParse("advancearmy:textures/mob/yw010_t2.jpg");
	public ResourceLocation mgtex = ResourceLocation.tryParse("advancearmy:textures/gun/gun17_t.jpg");
	public SAObjModel obj = new SAObjModel("advancearmy:textures/mob/yw010.obj");
	private static final ResourceLocation fire_tex = ResourceLocation.tryParse("advancearmy:textures/entity/flash/flash.png");
	RenderType f1 = SARenderState.getBlendDepthWrite(fire_tex);
	RenderType rt = RenderTypeVehicle.objrender(tex);
	RenderType head = SARenderState.getBlendDepthWrite(tex);
	RenderType rtmg = RenderTypeVehicle.objrender(mgtex);
    public RenderYw010(EntityRendererProvider.Context renderManagerIn)
    {
    	super(renderManagerIn, new ModelNoneVehicle(),4F);
        this.shadowStrength = 4F;
    }
    public ResourceLocation getTextureLocation(EntitySA_HeliBase entity)
    {
		return tex;
    }
    public boolean shouldRender(EntitySA_HeliBase entity, Frustum camera, double camX, double camY, double camZ) {
        return super.shouldRender(entity, camera, camX, camY, camZ)||entity.distanceToSqr(camX,camY,camZ)<10;
    }
	private void render_turret(PoseStack stack,EntitySA_HeliBase entity, float x, float y, float z, float bz, float rote, float pitch, float fire, float tick, String id){
		float size2 = entity.level().random.nextInt(4) * 0.3F + 1;
		stack.pushPose();//glstart
		stack.translate(x, y, z);//
		RenderRote.setRote(stack,rote, 0.0F, 1.0F, 0.0F);
		stack.translate(-x, -y, -z);//
		
		stack.pushPose();//glstart
		stack.translate(x, y, z);
		obj.renderPart("turret"+id);
		stack.popPose();//glend
		
		stack.translate(x, y, z+bz);//
		RenderRote.setRote(stack,pitch, 1.0F, 0.0F, 0.0F);
		stack.translate(-x, -y, -z-bz);//
		
		stack.pushPose();//glstart
		stack.translate(x, y, z);
		obj.renderPart("barrel"+id);
		stack.popPose();//glend
		
		stack.pushPose();//glstart
		stack.translate(x, y, z);//
		RenderRote.setRote(stack,tick*10F, 0.0F, 0.0F, 1.0F);
		stack.translate(-x, -y, -z);
		stack.translate(x, y, z);
		obj.renderPart("rote"+id);
		if(fire <4){
			stack.pushPose();//glstart
			obj.setRender(f1,null,stack,0xF000F0);
			
			stack.scale(size2*1.5F, size2*1.5F, 1);
			{
				if(entity.level().random.nextInt(2)==1){
					obj.renderPart("mat_1");
				}else if(entity.level().random.nextInt(2)==2){
					obj.renderPart("mat_2");
				}else{
					obj.renderPart("mat_3");
				}
			}
			stack.popPose();//glend
		}
		stack.popPose();//glend
		stack.popPose();//glend
	}
	
    float iii;
	float iii2;
    public void render(EntitySA_HeliBase entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource bufferIn, int packedLightIn)
    {
		super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
		if(entity.obj!=null)obj = entity.obj;
		if(entity.mgobj!=null)obj = entity.mgobj;
		if(entity.tex!=null)tex = entity.tex;
		if(entity.getTargetType()==2){
			if(entity.enemytex!=null){
				tex = entity.enemytex;
			}else{
				RenderSystem.setShaderColor(0.5F, 0F, 0F, 1F);
			}
		}
		Minecraft mc = Minecraft.getInstance();
	    stack.pushPose();
		stack.pushPose();
		
		
		
    	obj.setRender(rt,null,stack,packedLightIn);
		

		RenderRote.setRote(stack,180F, 0.0F, 1.0F, 0.0F);

		RenderRote.setRote(stack,180.0F - (entity.yHeadRotO + (entity.yHeadRot - entity.yHeadRotO) * partialTicks), 0.0F, 1.0F, 0.0F);
		stack.translate(0, (float)entity.seatPosY[0]+1.32F, (float)entity.seatPosZ[0]);
		RenderRote.setRote(stack,entity.flyPitch, 1.0F, 0.0F, 0.0F);
		RenderRote.setRote(stack,entity.flyRoll, 0.0F, 0.0F, 1.0F);
		stack.translate(0, (float)-entity.seatPosY[0]-1.32F, (float)-entity.seatPosZ[0]);

		if(!entity.isZoom||mc.options.getCameraType() != CameraType.FIRST_PERSON){
			obj.renderPart("body");
			obj.setRender(head,null,stack,packedLightIn);
			obj.renderPart("head");
			obj.setRender(rt,null,stack,packedLightIn);
		}
		if(entity.getHealth()>0){
			for(int t1 = 0; t1 < entity.rotorcount; ++t1){
				stack.pushPose();
				String tu1 = String.valueOf(t1 + 1);
				stack.translate(entity.rotorx[t1], entity.rotory[t1], entity.rotorz[t1]);//
				RenderRote.setRote(stack,entity.thpera*entity.rotor_rotex[t1], 1.0F, 0.0F, 0.0F);
				RenderRote.setRote(stack,entity.thpera*entity.rotor_rotey[t1], 0.0F, 1.0F, 0.0F);
				RenderRote.setRote(stack,entity.thpera*entity.rotor_rotez[t1], 0.0F, 0.0F, 1.0F);
				stack.translate(-entity.rotorx[t1], -entity.rotory[t1], -entity.rotorz[t1]);//
				obj.renderPart("pera" + tu1);
				stack.popPose();
			}
		}
		{
			float size2 = partialTicks * 0.3F + 1;
			float rotet = 180.0F - (entity.turretYawO + (entity.turretYaw - entity.turretYawO) * partialTicks)-(180.0F - entity.getYRot());
			float rotet1 = 180.0F - (entity.turretYawO1 + (entity.turretYaw1 - entity.turretYawO1) * partialTicks)-(180.0F - entity.getYRot());
			float rotet2 = 180.0F - entity.turretYaw2-(180.0F - entity.getYRot());
			float rotetp = entity.turretPitchO + (entity.turretPitch - entity.turretPitchO) * partialTicks -entity.flyPitch;
			float rotetp1 = entity.turretPitchO1 + (entity.turretPitch1 - entity.turretPitchO1) * partialTicks -entity.flyPitch;
			float rotetp2 = entity.turretPitch2 -entity.flyPitch;
			if(entity.anim2<3){
				if(iii<360){
					++iii;
				}else{
					iii=0;
				}
			}
			if(entity.anim3<3){
				if(iii2<360){
					++iii2;
				}else{
					iii2=0;
				}
			}
			render_turret(stack,entity, 0, 0, 0, -0.43F, rotet, rotetp, entity.anim1, 0, "1");
			obj.setRender(rtmg,null,stack,packedLightIn);
			render_turret(stack,entity, -1.46F, 0.9F, -1.9F, 0F, rotet1, rotetp1, entity.anim2, iii, "2");
			obj.setRender(rtmg,null,stack,packedLightIn);
			render_turret(stack,entity, 1.46F, 0.9F, -1.9F, 0F, rotet2, rotetp2, entity.anim3, iii2, "3");
			//tex
		}
		RenderSystem.setShaderColor(1, 1, 1, 1F);
		stack.popPose();
	    stack.popPose();
    }
}