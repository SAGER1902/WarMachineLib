package advancearmy.render;

import com.mojang.math.Axis;
import org.lwjgl.opengl.GL12;

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
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix4f;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.texture.OverlayTexture;
import advancearmy.entity.EntitySA_HeliBase;
import wmlib.client.obj.SAObjModel;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.CameraType;
import net.minecraft.Util;
import wmlib.client.render.SARenderState;
import wmlib.client.render.RenderTypeVehicle;
import wmlib.client.render.RenderRote;

import net.minecraft.client.renderer.entity.MobRenderer;
@OnlyIn(Dist.CLIENT)
public class RenderHeliBase extends MobRenderer<EntitySA_HeliBase, ModelNoneVehicle<EntitySA_HeliBase>>
{
	public ResourceLocation rotor1 = ResourceLocation.tryParse("advancearmy:textures/mob/ah64rotor.png");
	public ResourceLocation rotor2 = ResourceLocation.tryParse("advancearmy:textures/mob/ah64rotor2.png");
	public ResourceLocation tex = ResourceLocation.tryParse("advancearmy:textures/mob/ah64.png");
	public SAObjModel obj = new SAObjModel("advancearmy:textures/mob/ah64.obj");
	public SAObjModel mg = new SAObjModel("advancearmy:textures/mob/30mm.obj");
	private static final ResourceLocation fire_tex = ResourceLocation.tryParse("advancearmy:textures/entity/flash/mflash.png");
	public ResourceLocation dtex = ResourceLocation.tryParse("advancearmy:textures/mob/drive.png");
	
	RenderType rt;
	private static final ResourceLocation ENCHANT_GLINT_LOCATION = ResourceLocation.tryParse("wmlib:textures/misc/vehicle_glint.png");
	RenderType glint = SARenderState.getBlendGlowGlint(ENCHANT_GLINT_LOCATION);
    public RenderHeliBase(EntityRendererProvider.Context context)
    {
    	super(context, new ModelNoneVehicle(),4.0F);
        this.shadowRadius = 4.0F;
    }

    public ResourceLocation getTextureLocation(EntitySA_HeliBase entity)
    {
		return tex;
    }
    
    public boolean shouldRender(EntitySA_HeliBase entity, Frustum camera, double camX, double camY, double camZ) {
        return super.shouldRender(entity, camera, camX, camY, camZ)||entity.distanceToSqr(camX,camY,camZ)<10;
    }
    private void render_part(EntitySA_HeliBase entity, String name, MultiBufferSource buffer, PoseStack stack, int packedLight) {
        obj.renderPart(name);
        if (entity.getEnc() > 0){
            renderEnchantGlint(stack, name);
			if(rt!=null)obj.setRenderType(rt);
        }
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
	
	private void render_turret(PoseStack stack, MultiBufferSource buffer, RenderType ftype, EntitySA_HeliBase entity, float x, float y, float z, float bz, float rote, float pitch, float firet, float tick, int id){
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
		stack.popPose();
		
		stack.pushPose();
		stack.translate(x, y, z);//
		stack.mulPose(Axis.ZP.rotationDegrees(tick*10F));
		stack.translate(-x, -y, -z);
		stack.translate(x, y, z);
		mg.renderPart("rote");
		if(firet <4){
			stack.pushPose();
			mg.setRenderType(ftype);
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
    public void render(EntitySA_HeliBase entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight)
    {
		super.render(entity, entityYaw, partialTicks, stack, buffer, packedLight);
		if(entity.obj!=null)obj = entity.obj;
		if(entity.mgobj!=null)mg = entity.mgobj;
		if(entity.rotortex1!=null)rotor1 = entity.rotortex1;
		if(entity.rotortex2!=null)rotor2 = entity.rotortex2;
		if(entity.tex!=null)tex = entity.tex;
		if(entity.drivetex!=null)dtex = entity.drivetex;
		if(entity.getTargetType()==2){
			RenderSystem.setShaderColor(0.7F, 0.4F, 0.4F,1F);
			obj.setColor(-999, 0, 0, 1F);
		}
		if(entity.deathTime > 0){
			RenderSystem.setShaderColor(0.1F, 0.1F, 0.1F, 1F);
			obj.setColor(-999, 0, 0, 1F);
		}
		
		rt = RenderTypeVehicle.objrender(tex);
		RenderType rtglow = SARenderState.getBlend(tex);
		RenderType rtglass = SARenderState.getBlend(tex);
		
		RenderType rt1 = SARenderState.getBlendWrite_NoLight(rotor1);
		RenderType rt2 = SARenderState.getBlendWrite_NoLight(rotor2);
		
		RenderType fire = SARenderState.getBlendDepthWrite(fire_tex);
		RenderType drive = RenderTypeVehicle.objrender_blend(dtex);
		
	    stack.pushPose();
		stack.pushPose();
		Minecraft mc = Minecraft.getInstance();
		obj.setRender(rt, null, stack, packedLight);
		stack.mulPose(Axis.YP.rotationDegrees(180.0F));
		{
			//stack.mulPose(Axis.YP.rotationDegrees(180.0F - (entity.yBodyRotO + (entity.yBodyRot - entity.yBodyRotO) * partialTicks)));
			/*if(!entity.onGround())*/{
				stack.mulPose(Axis.YP.rotationDegrees(180.0F - (entity.yHeadRotO + (entity.yHeadRot - entity.yHeadRotO) * partialTicks)));
				stack.translate(0, (float)entity.seatPosY[0]+1.32F, (float)entity.seatPosZ[0]);
				stack.mulPose(Axis.XP.rotationDegrees(entity.flyPitch));
				//stack.mulPose(Axis.XP.rotationDegrees(entity.xRotO + (entity.getXRot() - entity.xRotO) * partialTicks));
				//stack.mulPose(Axis.XP.rotationDegrees(entity.getXRot()));
				stack.mulPose(Axis.ZP.rotationDegrees(entity.flyRoll));
				stack.translate(0, (float)-entity.seatPosY[0]-1.32F, (float)-entity.seatPosZ[0]);
			}
			//entity.yRotO + (entity.getYRot() - entity.yRotO) * partialTicks
			RenderRote.renderPassengr(mc, entity, tex, entityYaw, partialTicks, stack, buffer, packedLight);
		}

		if(!entity.isZoom||mc.options.getCameraType() == CameraType.FIRST_PERSON){
			render_part(entity,"body", buffer, stack, packedLight);
			render_part(entity,"seat", buffer, stack, packedLight);
			if(dtex!=null)obj.setRenderType(drive);
			render_part(entity,"drive", buffer, stack, packedLight);
			obj.setRenderType(rt);
			if(entity.movePower>=5F && !entity.onGround()){
				render_part(entity,"close", buffer, stack, packedLight);
			}else{
				render_part(entity,"gear", buffer, stack, packedLight);
			}
			if(entity.getRemain1()>0)obj.renderPart("missile_1");
			if(entity.getRemain1()>1)obj.renderPart("missile_2");
			if(entity.getRemain1()>2)obj.renderPart("missile_3");
			if(entity.getRemain1()>3)obj.renderPart("missile_4");
			{
				obj.setRenderType(rtglass);
				obj.setRenderLight(0xF000F0);//0xF000F0
				/*if(entity.getControllingPassenger()!=null && entity.getControllingPassenger().getControllingPassenger()!=mc.player)*/render_part(entity,"head", buffer, stack, packedLight);
				obj.setRenderType(rtglow);
				obj.renderPart("body_light");
				obj.setRenderType(rt);
				obj.setRenderLight(packedLight);//0xF000F0
			}
		}
		if(entity.getHealth()>0){
			for(int t1 = 0; t1 < entity.rotorcount; ++t1){
				stack.pushPose();
				String tu1 = String.valueOf(t1 + 1);
				stack.translate(entity.rotorx[t1], entity.rotory[t1], entity.rotorz[t1]);//
				stack.mulPose(Axis.XP.rotationDegrees(entity.thpera*entity.rotor_rotex[t1]));
				stack.mulPose(Axis.YP.rotationDegrees(entity.thpera*entity.rotor_rotey[t1]));
				stack.mulPose(Axis.ZP.rotationDegrees(entity.thpera*entity.rotor_rotez[t1]));
				stack.translate(-entity.rotorx[t1], -entity.rotory[t1], -entity.rotorz[t1]);//
				if(entity.movePower<4||!entity.change_roter){
					render_part(entity,"pera" + tu1, buffer, stack, packedLight);
				}else{
					if(t1==0)obj.setRenderType(rt1);
					if(t1==1)obj.setRenderType(rt2);
					obj.renderPart("rote" + tu1);
				}
				stack.popPose();
			}
			obj.setRenderType(rt);
			stack.pushPose();
			obj.setRenderType(fire);
			obj.setRenderLight(0xF000F0);//0xF000F0
			if(entity.getRemain1()%2==0){
				stack.translate(entity.fireposX1, entity.fireposY1, entity.fireposZ1);
			}else{
				stack.translate(-entity.fireposX1, entity.fireposY1, entity.fireposZ1);
			}
			float size = 1F;
			int time = entity.anim2;
			if(time > 0 && time < 10){
				if(time>=0 && time<=4){
					size = time * 0.5F + 1;
				}
				if(time>4 && time<=8){//10 11 12 ... 19 <---
					size = (8 - time) * 0.3F + 1;
				}
			}
			stack.scale(size, size, size);
			
			if(entity.anim2<8 && (entity.isAttacking()||entity.getControllingPassenger()!=null))obj.renderPart("flash1");
			
			stack.popPose();
			obj.setRenderType(rt);
			obj.setRenderLight(packedLight);//0xF000F0
		}
		{
			float size2 = partialTicks * 0.3F + 1;
			float rotet = 180.0F - (entity.turretYawO1 + (entity.turretYaw1 - entity.turretYawO1) * partialTicks)-(180.0F - entity.getYRot());
			float rotetp = entity.turretPitchO1 + (entity.turretPitch1 - entity.turretPitchO1) * partialTicks -entity.flyPitch;
			if(entity.anim3<3){
				if(iii<360){
					++iii;
				}else{
					iii=0;
				}
			}
			//seat turret
			if(entity.mgobj!=null){
				if(entity.mgtex!=null){
					mg.setRender(RenderTypeVehicle.objrender(entity.mgtex), null, stack, packedLight);
				}else{
					mg.setRender(rt, null, stack, packedLight);
				}
				render_turret(stack, buffer, fire, entity, entity.mgx, entity.mgy, entity.mgz, entity.mgbz, rotet, rotetp, entity.anim3, iii, 1);
				//tex---(tex);
			}
		}
		RenderSystem.setShaderColor(1, 1, 1, 1);
		stack.popPose();
	    stack.popPose();
    }
}