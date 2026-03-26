package advancearmy.render.vehicle;

import com.mojang.math.Axis;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
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
import wmlib.client.render.RenderRote;
@OnlyIn(Dist.CLIENT)
public class RenderAH6 extends MobRenderer<EntitySA_HeliBase, ModelNoneVehicle<EntitySA_HeliBase>>
{
	public ResourceLocation rotor1 = ResourceLocation.tryParse("advancearmy:textures/mob/ah64rotor.png");
	public ResourceLocation rotor2 = ResourceLocation.tryParse("advancearmy:textures/mob/ah64rotor2.png");
	public ResourceLocation tex = ResourceLocation.tryParse("advancearmy:textures/mob/ah64.png");
	public SAObjModel obj = new SAObjModel("advancearmy:textures/mob/ah6.obj");
	private static final ResourceLocation fire_tex = ResourceLocation.tryParse("advancearmy:textures/entity/flash/mflash.png");
	RenderType rt = RenderTypeVehicle.objrender(tex);
	RenderType head = SARenderState.getBlend(tex);
	RenderType rt1 = SARenderState.getBlendWrite_NoLight(rotor1);
	RenderType rt2 = SARenderState.getBlendWrite_NoLight(rotor2);
	RenderType f1 = SARenderState.getBlendDepthWrite(fire_tex);
    public RenderAH6(EntityRendererProvider.Context renderManagerIn)
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
	
    float iii;
	float iii2;
    public void render(EntitySA_HeliBase entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight)
    {
		super.render(entity, entityYaw, partialTicks, stack, buffer, packedLight);
		if(entity.obj!=null)obj = entity.obj;
		if(entity.tex!=null)tex = entity.tex;
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

		
    	obj.setRender(rt,null,stack,packedLight);
		RenderRote.setRote(stack,180F, 0.0F, 1.0F, 0.0F);

		RenderRote.setRote(stack,180.0F - (entity.yHeadRotO + (entity.yHeadRot - entity.yHeadRotO) * partialTicks), 0.0F, 1.0F, 0.0F);
		stack.translate(0, (float)entity.seatPosY[0]+1.32F, (float)entity.seatPosZ[0]);
		RenderRote.setRote(stack,entity.flyPitch, 1.0F, 0.0F, 0.0F);
		RenderRote.setRote(stack,entity.flyRoll, 0.0F, 0.0F, 1.0F);
		stack.translate(0, (float)-entity.seatPosY[0]-1.32F, (float)-entity.seatPosZ[0]);
		RenderRote.renderPassengr(mc, entity, tex, entityYaw, partialTicks, stack, buffer, packedLight);
		
		if(!entity.isZoom||mc.options.getCameraType() != CameraType.FIRST_PERSON){
			obj.renderPart("body");
			obj.renderPart("seat");
			obj.setRender(head,null,stack,packedLight);
			obj.renderPart("head");
			obj.setRender(rt,null,stack,packedLight);
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
				if(entity.movePower<4){
					obj.renderPart("pera" + tu1);
				}else{
					RenderSystem.depthMask(false);
					RenderSystem.enableBlend();
					if(t1==0)obj.setRender(rt1,null,stack,packedLight);
					if(t1==1)obj.setRender(rt2,null,stack,packedLight);
					obj.renderPart("rote" + tu1);
					RenderSystem.disableBlend();
					RenderSystem.depthMask(true);
				}
				stack.popPose();
			}
			stack.pushPose();//glstart
			obj.setRender(f1,null,stack,0xF000F0);
			if(entity.getRemain2()%2==0){
				stack.translate(entity.fireposX2, entity.fireposY2, entity.fireposZ2);
			}else{
				stack.translate(-entity.fireposX2, entity.fireposY2, entity.fireposZ2);
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
			
			stack.popPose();//glend
			obj.setRender(rt,null,stack,packedLight);
		}
		{
			if(entity.anim1<3){
				if(iii<360){
					++iii;
				}else{
					iii=0;
				}
			}
			float size2 = entity.level().random.nextInt(4) * 0.3F + 1;
			stack.pushPose();//glstart
			stack.translate(-1.4F, 0.67F, -0.83F);//
			RenderRote.setRote(stack,iii*10F, 0.0F, 0.0F, 1.0F);
			stack.translate(1.4F, -0.67F, 0.83F);
			stack.translate(-1.4F, 0.67F, -0.83F);
			obj.renderPart("barrel");
			if(entity.anim1 <4){
				stack.pushPose();//glstart
				obj.setRender(f1,null,stack,0xF000F0);
				
				stack.scale(size2*1.5F, size2*1.5F, 1);
				if(entity.level().random.nextInt(2)==1){
					obj.renderPart("mat_1");
				}else if(entity.level().random.nextInt(2)==2){
					obj.renderPart("mat_2");
				}else{
					obj.renderPart("mat_3");
				}
				stack.popPose();//glend
				obj.setRender(rt,null,stack,packedLight);
			}
			stack.popPose();//glend
			stack.pushPose();//glstart
			stack.translate(1.4F, 0.67F, -0.83F);//
			RenderRote.setRote(stack,iii*10F, 0.0F, 0.0F, 1.0F);
			stack.translate(-1.4F, -0.67F, 0.83F);
			stack.translate(1.4F, 0.67F, -0.83F);
			obj.renderPart("barrel");
			if(entity.anim1 <4){
				stack.pushPose();//glstart
				obj.setRender(f1,null,stack,0xF000F0);
				stack.scale(size2*1.5F, size2*1.5F, 1);
				if(entity.level().random.nextInt(2)==1){
					obj.renderPart("mat_1");
				}else if(entity.level().random.nextInt(2)==2){
					obj.renderPart("mat_2");
				}else{
					obj.renderPart("mat_3");
				}
				
				//tex
				stack.popPose();//glend
			}
			stack.popPose();//glend
		}
		RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
		stack.popPose();
	    stack.popPose();
    }
}