package advancearmy.render.soldier;
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
import advancearmy.entity.soldier.EntitySA_GAT;
import advancearmy.render.ModelNone;
@OnlyIn(Dist.CLIENT)
public class RenderGAT extends MobRenderer<EntitySA_GAT, ModelNone<EntitySA_GAT>>
{
	public ResourceLocation tex = ResourceLocation.tryParse("advancearmy:textures/mob/soldier/minigunner.png");
    private static final SAObjModel obj = new SAObjModel("advancearmy:textures/mob/soldier/minigunner.obj");
	private static final ResourceLocation fire_tex = ResourceLocation.tryParse("advancearmy:textures/entity/flash/mflash.png");
	
    private static final SAObjModel gun1 = new SAObjModel("advancearmy:textures/gun/cslm12.obj");
    private static final ResourceLocation gunt = ResourceLocation.tryParse("advancearmy:textures/gun/gun.png");
	public ResourceLocation star = ResourceLocation.tryParse("advancearmy:textures/mob/soldier/star.png");
    public RenderGAT(EntityRendererProvider.Context renderManagerIn)
    {
    	super(renderManagerIn, new ModelNone(),0.5F);
    }

    public ResourceLocation getTextureLocation(EntitySA_GAT entity)
    {
		return tex;
    }
    private void render_light(EntitySA_GAT entity, String name){
    }
	RenderType rt = RenderTypeVehicle.objrender(tex);
	RenderType rtgun = RenderTypeVehicle.objrender(gunt);
	RenderType rtglow = SARenderState.getBlendDepthWrite(tex);
	RenderType f1 = SARenderState.getBlendDepthWrite(fire_tex);
	RenderType rtstar = SARenderState.getBlendDepthWrite(star);
    float iii;
    public void render(EntitySA_GAT entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource bufferIn, int packedLight)
    {
		super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLight);
		if(entity.hide){
			
		}else{
		Minecraft mc = Minecraft.getInstance();
	    stack.pushPose();
		
		stack.pushPose();
		
    	float limbSwing = this.F6(entity, partialTicks);
		float limbSwingAmount = this.F5(entity, partialTicks);

		RenderRote.setRote(stack,180F, 0.0F, 1.0F, 0.0F);
		if(entity.getVehicle()!=null){
			stack.scale(0.8F, 0.8F, 0.8F);
		}else{
			if(entity.getSame()){
				obj.setRender(rtstar, null, stack, packedLight);
				obj.renderPart("star");
			}
		}
		obj.setRender(rt, null, stack, packedLight);
		
		
		if(iii < 360F){
			iii = iii + 0.1F;
		}else{
			iii = 0F;
		}
		RenderRote.setRote(stack,180.0F - (entity.yBodyRotO + (entity.yBodyRot - entity.yBodyRotO) * partialTicks), 0.0F, 1.0F, 0.0F);
		
		if(entity.getHealth()==0){
			int dt = entity.deathTime;
			if(dt>8)dt=8;
			stack.translate(0F, 0.15F, -1.5F);
			RenderRote.setRote(stack,-90F*dt/8F, 1.0F, 0.0F, 0.0F);//
		}else{
			if(entity.getRemain2()==1){
				stack.translate(0F, 0.15F, -1.5F);
				RenderRote.setRote(stack,90F, 1.0F, 0.0F, 0.0F);//
			}
		}
		
		{
			stack.pushPose();//glstart
			this.renderlegs(stack, entity, limbSwing, limbSwingAmount, partialTicks);
			stack.popPose();//glend
		}
		
		if(entity.isPassenger()||entity.sit_aim && entity.getRemain2()!=1) {//
			stack.translate(0F, -0.5F, 0.0F);
		}
		
		float Ax1 = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * limbSwingAmount;//
		stack.translate( 0, Ax1 * (180F / (float)Math.PI) * 0.001F, 0);//
    	{
    		stack.translate(0F, 0.75F, 0.0F);
			RenderRote.setRote(stack,Ax1 * (180F / (float)Math.PI) * 0.05F, - 1.0F, 0.0F, 0.0F);
    		if(entity.getRemain2()==2)RenderRote.setRote(stack,20, 1.0F, 0.0F, 0.0F);
			if(entity.anim1<5)RenderRote.setRote(stack,-entity.anim1, 1.0F, 0.0F, 0.0F);
    		stack.translate(0F, -0.75F, 0.0F);
    	}

		{
			stack.pushPose();//glstart
			stack.translate(0F, 1.5F, 0.0F);
			RenderRote.setRote(stack,180.0F - (entity.yHeadRotO + (entity.yHeadRot - entity.yHeadRotO) * partialTicks) -(180.0F - (entity.yBodyRotO + (entity.yBodyRot - entity.yBodyRotO) * partialTicks)), 0.0F, 1.0F, 0.0F);
			if(entity.getRemain2()==1){//
				RenderRote.setRote(stack,entity.getXRot()-60F, 1.0F, 0.0F, 0.0F);
			}else{
				RenderRote.setRote(stack,entity.getXRot(), 1.0F, 0.0F, 0.0F);
			}
			if(entity.getRemain2()==2 && entity.getRemain1()!=0 && entity.isAttacking())RenderRote.setRote(stack,15F, 0.0F, 0.0F, 1.0F);//
			stack.translate(0F, -1.5F, 0.0F);
			obj.renderPart("head");
			obj.setRender(rtglow, null, stack, 0xF000F0);
			obj.renderPart("head_light");
			obj.setRender(rt, null, stack, packedLight);
			stack.popPose();//glend
		}
		
		{
			stack.pushPose();//glstart
			this.renderbody(packedLight,entity, limbSwing, limbSwingAmount, partialTicks, stack, bufferIn);
			stack.popPose();//glend
		}
		
		RenderSystem.setShaderColor(1F, 1F, 1F, 1F);

		stack.popPose();
		
	    stack.popPose();
		
		}
    }
	
	
	
    public float move_size = 1F;//
    private void renderlegs(PoseStack stack, EntitySA_GAT entity, float limbSwing, float limbSwingAmount, float partialTicks){
		float Ax = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * limbSwingAmount;
		float motion =  Ax * (180F / (float)Math.PI) * this.move_size;
    	stack.pushPose();//glstart
    	{
			stack.translate(0.15F, 0.625F, 0.0F);
			if(entity.isPassenger()||entity.sit_aim && entity.getRemain2()!=1) {
				stack.translate(0F, -0.5F, 0.0F);
				RenderRote.setRote(stack,-90, 1.0F, 0.0F, 0.0F);
				RenderRote.setRote(stack,20, 0.0F, 0.0F, 1.0F);
			}else {
				RenderRote.setRote(stack,motion * 1F, 1.0F, 0.0F, 0.0F);//
			}
			if(entity.getRemain2()==2 && entity.isAttacking())RenderRote.setRote(stack,-30F, 1.0F, 0.0F, 0.0F);
			stack.translate(-0.15F, -0.625F, 0.0F);
			obj.renderPart("knee_l");
			
			stack.translate(0.15F, 0.4F, 0.05F);
			if(motion<0){
				RenderRote.setRote(stack,motion * 1F, -1.0F, 0.0F, 0.0F);
			}else{
				RenderRote.setRote(stack,motion * 1F, 1.0F, 0.0F, 0.0F);
			}
			if(entity.getRemain2()==2 && entity.isAttacking())RenderRote.setRote(stack,25F, 1.0F, 0.0F, 0.0F);
			stack.translate(-0.15F, -0.4F, -0.05F);
			obj.renderPart("leg_l");
			
		}
		stack.popPose();//glend
		
		stack.pushPose();//glstart
		{
			stack.translate(-0.15F, 0.625F, 0.0F);
			if(entity.isPassenger()||entity.sit_aim && entity.getRemain2()!=1) {
				stack.translate(0F, -0.5F, 0.0F);
				RenderRote.setRote(stack,-90, 1.0F, 0.0F, 0.0F);
				RenderRote.setRote(stack,-20, 0.0F, 0.0F, 1.0F);
			}else {
				RenderRote.setRote(stack,motion * 1F, -1.0F, 0.0F, 0.0F);//
			}
			if(entity.getRemain2()==2 && entity.isAttacking())RenderRote.setRote(stack,15F, 1.0F, 0.0F, 0.0F);
			stack.translate(0.15F, -0.625F, 0.0F);
			obj.renderPart("knee_r");
			
			stack.translate(-0.15F, 0.4F, 0.05F);
			if(motion>0){
				RenderRote.setRote(stack,motion * 1F, 1.0F, 0.0F, 0.0F);
			}else{
				RenderRote.setRote(stack,motion * 1F, -1.0F, 0.0F, 0.0F);
			}
			if(entity.getRemain2()==2 && entity.isAttacking())RenderRote.setRote(stack,20F, 1.0F, 0.0F, 0.0F);
			stack.translate(0.15F, -0.4F, -0.05F);
			obj.renderPart("leg_r");
			
		}
		stack.popPose();//glend
	}
    
    private void renderbody(int packedLight, EntitySA_GAT entity, float limbSwing, float limbSwingAmount, float partialTicks, PoseStack stack, MultiBufferSource bufferIn){
		Minecraft minecraft = Minecraft.getInstance();
		stack.pushPose();//glstart
    	obj.renderPart("body");
		if(entity.getWeaponId()==2){
			obj.renderPart("wait2");
		}else{
			obj.renderPart("wait1");
		}
		stack.popPose();//glend
		float arm_l_rotex = 0;
		float arm_l_rotez = 0;
		float arm_l_rotey = 0;
		float arm_r_rotex = 0;
		float arm_r_rotez = 0;
		float arm_r_rotey = 0;
		boolean move_arm = false;
		 
    	stack.pushPose();//glstart
    	{	
			float Ax = Mth.cos(limbSwing * 0.6662F) * limbSwingAmount;
			stack.translate(0.37F, 1.375F, 0.0F);
			if(entity.getRemain2()==2 && !(entity.isAttacking()) && !(entity.getRemain1()==0)){
			RenderRote.setRote(stack,-30, 1.0F, 0.0F, 0.0F);
			}else if(entity.getRemain2()==2){
			RenderRote.setRote(stack,-20, 1.0F, 0.0F, 0.0F);
			}
			if(entity.getRemain1()==0||entity.getRemain2()==2 && !(entity.isAttacking())){
				if(entity.getRemain2()!=1)arm_l_rotex = -20;
				arm_l_rotez = -50;
			}
			else if(entity.isAttacking() && !(entity.deathTime > 0) && !(entity.getRemain1()==0)||entity.sit_aim && !entity.isPassenger()) {
				if(entity.getRemain2()!=1)arm_l_rotex = -90 + entity.getXRot();
				arm_l_rotez = -30;
			}else if(entity.isPassenger()) {
				if(entity.getRemain2()!=1)arm_l_rotex = -30;
			}
			else if(entity.deathTime > 0){
				if(entity.getRemain2()!=1)arm_l_rotex = -30;
				arm_l_rotez = -40;
			}else {
				arm_l_rotex = Ax * (180F / (float)Math.PI) * 1.5F;
				move_arm = true;
			}
			
			if(entity.getRemain2()==1){//
				RenderRote.setRote(stack,arm_l_rotex+180F, 1.0F, 0.0F, 0.0F);
			}else{
				RenderRote.setRote(stack,arm_l_rotex, 1.0F, 0.0F, 0.0F);
			}
			RenderRote.setRote(stack,arm_l_rotey, 0.0F, 1.0F, 0.0F);
			RenderRote.setRote(stack,arm_l_rotez, 0.0F, 0.0F, 1.0F);
			
			stack.translate(-0.37F, -1.375F, 0.0F);
			
			obj.renderPart("elbow_l");
			if(move_arm && !entity.sit_aim){
				stack.translate(0.37F, 1.12F, -0.07F);
				if(Ax<0){
					RenderRote.setRote(stack,arm_l_rotex, 1.0F, 0.0F, 0.0F);
				}else{
					RenderRote.setRote(stack,arm_l_rotex, -1.0F, 0.0F, 0.0F);
				}
				stack.translate(-0.37F, -1.12F, 0.07F);
			}
			obj.renderPart("arm_l");
		}
		stack.popPose();//glend

		stack.pushPose();//glstart
		{
			float Ax = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * limbSwingAmount;
			stack.translate(-0.37F, 1.375F, 0.0F);
			if(entity.getRemain2()==2 && !(entity.isAttacking()) && !(entity.getRemain1()==0)){
			RenderRote.setRote(stack,-30, 1.0F, 0.0F, 0.0F);
			}else if(entity.getRemain2()==2){
			RenderRote.setRote(stack,-20, 1.0F, 0.0F, 0.0F);
			}
			if(entity.getRemain1()==0||entity.getRemain2()==2 && !(entity.isAttacking())){
				arm_r_rotez = 10;
				if(entity.getRemain2()!=1)arm_r_rotex = -20;
			}
			else 
			if(entity.isAttacking() && !(entity.deathTime > 0) && !(entity.getRemain1()==0)||entity.sit_aim && !entity.isPassenger()) {
				if(entity.anim1<5){
	    			RenderRote.setRote(stack,-entity.anim1*0.5F, 1.0F, 0.0F, 0.0F);
	    		}
				if(entity.getRemain2()!=1)arm_r_rotex = -90 + entity.getXRot();
			}else if(entity.isPassenger()) {
				if(entity.getRemain2()!=1)arm_r_rotex = -30;
			}
			else if(entity.deathTime > 0){
				if(entity.getRemain2()!=1)arm_r_rotex = -20;
			}
			else {
				arm_r_rotex = Ax * (180F / (float)Math.PI)*0.8F;
				move_arm = true;
			}
			
			if(entity.getRemain2()==1 && !entity.sit_aim){//
				RenderRote.setRote(stack,arm_r_rotex+180F, 1.0F, 0.0F, 0.0F);
			}else{
				RenderRote.setRote(stack,arm_r_rotex, 1.0F, 0.0F, 0.0F);
			}
			RenderRote.setRote(stack,arm_r_rotey, 0.0F, 1.0F, 0.0F);
			RenderRote.setRote(stack,arm_r_rotez, 0.0F, 0.0F, 1.0F);
			stack.translate(0.37F, -1.375F, 0.0F);

			obj.renderPart("elbow_r");
			stack.translate(-0.37F, 1.12F, -0.07F);
			if(move_arm && !entity.sit_aim){
				if(Ax<0){
					RenderRote.setRote(stack,arm_r_rotex, 1.0F, 0.0F, 0.0F);
				}else{
					RenderRote.setRote(stack,arm_r_rotex, -1.0F, 0.0F, 0.0F);
				}
			}
			if(!entity.isAttacking() && !entity.sit_aim)RenderRote.setRote(stack,- 15, 1.0F, 0.0F, 0.0F);
			stack.translate(0.37F, -1.12F, 0.07F);
			obj.renderPart("arm_r");
			if(entity.canfire){
				stack.pushPose();//glstart
				stack.scale(0.1875F, 0.1875F, 0.1875F);
				if(!entity.isAttacking()){
					RenderRote.setRote(stack,51F, 1.0F, 0.0F, 0.0F);
					RenderRote.setRote(stack,81F, 0.0F, 1.0F, 0.0F);
					RenderRote.setRote(stack,-79F, 0.0F, 0.0F, 1.0F);
					stack.translate(-1.9F, 3.3F, -3F);//x,z,y
				}else{
					RenderRote.setRote(stack,90F, 1.0F, 0.0F, 0.0F);
					stack.translate(-1.9F, -0.2F, -4.35F);//x,z,y
				}
				gun1.setRender(rtgun, null, stack, packedLight);
				gun1.renderPart("mat1");
				gun1.renderPart("mat2");
				if(entity.getRemain1()>0)gun1.renderPart("mat3");
				gun1.renderPart("mat25");
				gun1.renderPart("mat31");
				stack.popPose();//glend
				
				stack.pushPose();//glstart
				RenderRote.setRote(stack,90F, 1.0F, 0.0F, 0.0F);
				obj.setRender(f1, null, stack, 0xF000F0);
				stack.translate(-entity.fireposX, -0.1F, 1.1F);
				float size = entity.level().random.nextInt(3) * 0.25F + 1;
				stack.scale(size, size, size);
				if(entity.anim1<3 && entity.getRemain1() > 0 && entity.isAttacking())
				{
					if(entity.level().random.nextInt(6)<2){
						obj.renderPart("mat_1");
					}else if(entity.level().random.nextInt(6)<4){
						obj.renderPart("mat_2");
					}else{
						obj.renderPart("mat_3");
					}
				}
				//
				//tex(tex);
				stack.popPose();//glend
			}
		}
		stack.popPose();//glend
	}
    
    private void render_color(EntitySA_GAT entity, String name){
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