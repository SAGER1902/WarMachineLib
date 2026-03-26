package advancearmy.render.vehicle;
import advancearmy.entity.land.EntitySA_Ember;
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
import advancearmy.render.ModelNone;
@OnlyIn(Dist.CLIENT)
public class RenderEmber extends MobRenderer<EntitySA_Ember, ModelNone<EntitySA_Ember>>
{
	private static final ResourceLocation tex = ResourceLocation.tryParse("advancearmy:textures/mob/hj3.png");
    private static final SAObjModel obj = new SAObjModel("advancearmy:textures/mob/ember_ii.obj");
	private static final ResourceLocation fire_tex = ResourceLocation.tryParse("advancearmy:textures/entity/flash/flash.png");
	private static final ResourceLocation cloud_tex = ResourceLocation.tryParse("advancearmy:textures/entity/flash/thruster_b.png");
	private static final ResourceLocation wave_tex = ResourceLocation.tryParse("advancearmy:textures/entity/flash/sward_wave1.png");
	private static final ResourceLocation wave_tex2 = ResourceLocation.tryParse("advancearmy:textures/entity/flash/sward_wave2.png");
	private static final ResourceLocation wave_tex3 = ResourceLocation.tryParse("advancearmy:textures/entity/flash/sward_wave3.png");
	
	private static final ResourceLocation gun_tex = ResourceLocation.tryParse("advancearmy:textures/gun/hj_gun1_t.png");
	private static final ResourceLocation sward_tex = ResourceLocation.tryParse("advancearmy:textures/mob/wanderer2.png");
	
	public static final ResourceLocation ENCHANT_GLINT_LOCATION = ResourceLocation.tryParse("wmlib:textures/misc/vehicle_made1.png");
	RenderType glint = SARenderState.getBlendGlowGlint(ENCHANT_GLINT_LOCATION);
    private void renderEnchantGlint(PoseStack stack, String name, int color) {
		if(color==1)RenderSystem.setShaderColor(1, 0.6F, 0.6F, 1);
		obj.setRender(glint, null, null, 15728880);
        stack.scale(1.01F, 1.01F, 1.01F);
        long time = (long)((double)Util.getMillis() * Minecraft.getInstance().options.glintSpeed().get() * 8.0);
        float u = (float)(time % 110000L) / 110000.0f;
        float v = (float)(time % 30000L) / 30000.0f;
		Matrix4f move = new Matrix4f().translation(u, v, 0.0f);
        RenderSystem.setTextureMatrix(move);
        obj.renderPart(name);
		RenderSystem.resetTextureMatrix();
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }
	
	RenderType f1 = SARenderState.getBlendDepthWrite(fire_tex);
	RenderType cloud = SARenderState.getBlendDepthWrite(cloud_tex);
	RenderType wave = SARenderState.getBlendGlow(wave_tex);
	RenderType wave2 = SARenderState.getBlendGlow(wave_tex2);
	RenderType wave3 = SARenderState.getBlendGlow(wave_tex3);
	
	
	RenderType rt = RenderTypeVehicle.objrender(tex);
	RenderType body = SARenderState.getBlendDepthWrite(tex);
	RenderType gun2 = SARenderState.getBlendDepthWrite(tex);
	RenderType rtgun = RenderTypeVehicle.objrender(gun_tex);
	RenderType rtsw = RenderTypeVehicle.objrender(sward_tex);
	RenderType sward = SARenderState.getBlendDepthWrite(sward_tex);
	
	public ResourceLocation dtex = ResourceLocation.tryParse("wmlib:textures/hud/box.png");
    public RenderEmber(EntityRendererProvider.Context renderManagerIn)
    {
    	super(renderManagerIn, new ModelNone(),2.5F);
    }
    public ResourceLocation getTextureLocation(EntitySA_Ember entity)
    {
		return tex;
    }
    private void render_light(EntitySA_Ember entity, String name){
    }
	public void render_cloud(PoseStack stack, Minecraft mc, float x,float y,float z,String name,float tick){//
		stack.pushPose();//
		//tex.bind(cloud_tex);//
		stack.translate(x, y, z);//
		stack.translate(0, 0, 0);//
		RenderRote.setRote(stack,tick, 0.0F, 1.0F, 0.0F);//
		stack.translate(0, 0, 0);//
		obj.renderPart(name);
		stack.popPose();//
	}
    public float kneex = 1.8F;
	public float kneey = 4.2F;
	public float kneez = 0F;
	public float legx = 1.8F;
	public float legy = 3.03F;
	public float legz = 1.19F;
	public float bodyx = 0.0F;
	public float bodyy = 4.37F;
	public float bodyz = 0.0F;
	public float headx = 0.0F;
	public float heady = 5.2F;
	public float headz = 1.2F;
	public float elbowx = 1.8F;
	public float elbowy = 7.2F;
	public float elbowz = 0.09F;
	public float armx = 1.8F;
	public float army = 5.69F;
	public float armz = 0.09F;
	public float move_size = 0.4F;
	public float leg_rotez = 5;
	public float leg_rotex = 2;
	public float saber_x = 1.9F;
	public float saber_y = 1F;
	public float saber_z = -4.5F;
	public float saber_rote = 45;
	public float fire_x = 1.95F;
	public float fire_y = 4.8F;
	public float fire_z = 2.8F;
	
    float iii;
    public void render(EntitySA_Ember entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource bufferIn, int packedLightIn)
    {
		super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
		Minecraft mc = Minecraft.getInstance();
		stack.pushPose();
		stack.pushPose();
		
		float limbSwing = this.F6(entity, partialTicks);
		float limbSwingAmount = this.F5(entity, partialTicks);
		
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
		
		if(iii < 360F){
			iii = iii + 10F;
		}else{
			iii = 0F;
		}
		RenderRote.setRote(stack,180.0F - (entity.yHeadRotO + (entity.yHeadRot - entity.yHeadRotO) * partialTicks), 0.0F, 1.0F, 0.0F);
		
		stack.pushPose();
		
		this.renderlegs(packedLightIn, stack, entity, limbSwing, limbSwingAmount, partialTicks);
		stack.popPose();

		float Ax1 = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * limbSwingAmount;//
		/*if(!entity.onGround())*/stack.translate( 0, Ax1 * (180F / (float)Math.PI) * 0.001F, 0);//
		{
			stack.pushPose();
			float body_rotex = 0;
			float body_rotey = 0;
			stack.translate(bodyx, bodyy, bodyz);
			if(entity.move_mode == 1)RenderRote.setRote(stack,entity.moveAnim*0.75F, 1.0F, 0.0F, 0.0F);
			if(entity.move_mode == 2)RenderRote.setRote(stack,-entity.moveAnim*0.75F, 1.0F, 0.0F, 0.0F);
			if(entity.move_mode == 3)RenderRote.setRote(stack,entity.moveAnim*0.5F, 0.0F, 0.0F, 1.0F);
			if(entity.move_mode == 4)RenderRote.setRote(stack,entity.moveAnim*0.5F, 0.0F, 0.0F, -1.0F);
			if(entity.attackAnim>0 && entity.attackAnim<13){
				body_rotex = -20 + entity.attackAnim * 3;
				stack.pushPose();
				RenderRote.setRote(stack,180.0F - (entity.turretYawO + (entity.turretYaw - entity.turretYawO) * partialTicks) -(180.0F - (entity.yHeadRotO + (entity.yHeadRot - entity.yHeadRotO) * partialTicks)), 0.0F, 1.0F, 0.0F);
				stack.translate(0, 9.49F, -2.13F);
				RenderRote.setRote(stack,(entity.xRotO + (entity.getXRot() - entity.xRotO) * partialTicks), 1.0F, 0.0F, 0.0F);
				stack.translate(0, - 9.49F, 2.13F);
				RenderSystem.setShaderColor(1F, 1F, 1F, (8F-entity.attackAnim)/8F);
				stack.translate(0, 0, entity.attackAnim*0.5F);//
				
				obj.setRender(wave,null,stack,0xF000F0);
				if(entity.getMovePosY()==1){
					if(entity.getMoveMode()>0)obj.setRenderType(wave3);
					if(entity.attackAnim<8)obj.renderPart("wave1");
				}else if(entity.getMovePosY()==3){
					if(entity.getMoveMode()>0)obj.setRenderType(wave2);
					if(entity.attackAnim<8)obj.renderPart("wave2");
					body_rotey = 30-entity.attackAnim * 4;
				}else{
					if(entity.getMoveMode()>0)obj.setRenderType(wave2);
					if(entity.attackAnim<8)obj.renderPart("wave3");
					body_rotey = -30+entity.attackAnim * 8;
				}
				RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
				obj.setRender(rt,null,stack,packedLightIn);
				stack.popPose();
			}
			float rote = 180.0F - (entity.turretYawO + (entity.turretYaw - entity.turretYawO) * partialTicks) -(180.0F - (entity.yHeadRotO + (entity.yHeadRot - entity.yHeadRotO) * partialTicks));
			RenderRote.setRote(stack,rote, 0.0F, 1.0F, 0.0F);
			RenderRote.setRote(stack,Ax1 * (180F / (float)Math.PI) * 0.001F, - 1.0F, 0.0F, 0.0F);
			RenderRote.setRote(stack,body_rotex, 1.0F, 0.0F, 0.0F);
			RenderRote.setRote(stack,body_rotey, 0.0F, 1.0F, 0.0F);
			stack.translate(-bodyx, -bodyy, -bodyz);
			
			stack.pushPose();
			RenderRote.setRote(stack,-rote, 0.0F, 1.0F, 0.0F);
			obj.renderPart("waist");
			if(entity.shieldHealth>0 && entity.hurtTime>0)this.renderEnchantGlint(stack, "waist",0);
			stack.popPose();
			
			obj.setRenderType(rt);
			
			this.renderbody(packedLightIn, entity, limbSwing, limbSwingAmount, partialTicks, stack, bufferIn);
			if(!entity.onGround()/* && entity.getMoveTypeype()==5*/){
				RenderRote.setRote(stack,saber_rote, 1.0F, 0.0F, 0.0F);
				obj.setRender(cloud,null,stack,0xF000F0);
				this.render_cloud(stack, mc,-saber_x,saber_y,saber_z,"cloud",iii);//
				this.render_cloud(stack, mc,saber_x,saber_y,saber_z,"cloud",iii);//
			}
			stack.popPose();
		}
		RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
		
		stack.popPose();
		stack.popPose();
    }
	
    private void renderlegs(int packedLightIn, PoseStack stack, EntitySA_Ember entity, float limbSwing, float limbSwingAmount, float partialTicks){
		float Ax = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * limbSwingAmount;
		float motion =  Ax * (180F / (float)Math.PI) * this.move_size;
		if(!entity.onGround()){
			motion = 15F;
		}
    	stack.pushPose();
    	{
			stack.translate(kneex, kneey, kneez);
			RenderRote.setRote(stack,leg_rotez, 0.0F, 0.0F, 1.0F);
			if(entity.move_mode == 1)RenderRote.setRote(stack,entity.moveAnim*0.75F, 1.0F, 0.0F, 0.0F);
			if(entity.move_mode == 2)RenderRote.setRote(stack,-entity.moveAnim*0.75F, 1.0F, 0.0F, 0.0F);
			if(entity.move_mode == 3) {
				RenderRote.setRote(stack,motion * 1F, 1.0F, 0.0F, 0.0F);
				RenderRote.setRote(stack,10, 0.0F, 0.0F, 1.0F);
			}else if(entity.move_mode == 4) {
				RenderRote.setRote(stack,motion * 1F, 1.0F, 0.0F, 0.0F);
				RenderRote.setRote(stack,-10, 0.0F, 0.0F, 1.0F);
			}else{
				RenderRote.setRote(stack,motion * 1F, 1.0F, 0.0F, 0.0F);
			}
			if(entity.isAttacking())RenderRote.setRote(stack,leg_rotex, 1.0F, 0.0F, 0.0F);
			stack.translate( - kneex,  - kneey, -  kneez);
			obj.renderPart("knee_l");
			if(entity.shieldHealth>0 && entity.hurtTime>0)this.renderEnchantGlint(stack, "knee_l",0);
			obj.setRenderType(rt);
			
			stack.translate(legx, legy, legz);
			if(entity.move_mode == 3) {
				RenderRote.setRote(stack,motion * 1F, -1.0F, 0.0F, 0.0F);
				RenderRote.setRote(stack,10, 0.0F, 0.0F, 1.0F);
			}else if(entity.move_mode == 4) {
				RenderRote.setRote(stack,motion * 1F, -1.0F, 0.0F, 0.0F);
				RenderRote.setRote(stack,-10, 0.0F, 0.0F, 1.0F);
			}else{
				if(motion<0){
					RenderRote.setRote(stack,motion * 1F, -1.0F, 0.0F, 0.0F);
				}else{
					RenderRote.setRote(stack,motion * 1F, 1.0F, 0.0F, 0.0F);
				}
			}
			if(entity.isAttacking())RenderRote.setRote(stack,-leg_rotex, 1.0F, 0.0F, 0.0F);
			stack.translate(- legx, - legy, - legz);
			obj.renderPart("leg_l");
			if(entity.shieldHealth>0 && entity.hurtTime>0)this.renderEnchantGlint(stack, "leg_l",0);
			obj.setRenderType(rt);
			
			obj.renderPart("feet_l");
			if(entity.shieldHealth>0 && entity.hurtTime>0)this.renderEnchantGlint(stack, "feet_l",0);
			obj.setRenderType(rt);
		}
		stack.popPose();
		
		stack.pushPose();
		{
			stack.translate(-kneex, kneey, kneez);
			RenderRote.setRote(stack,-leg_rotez, 0.0F, 0.0F, 1.0F);
			if(entity.move_mode == 1)RenderRote.setRote(stack,entity.moveAnim*0.75F, 1.0F, 0.0F, 0.0F);
			if(entity.move_mode == 2)RenderRote.setRote(stack,-entity.moveAnim*0.75F, 1.0F, 0.0F, 0.0F);
			if(entity.move_mode == 3) {
				RenderRote.setRote(stack,motion * 1F, 1.0F, 0.0F, 0.0F);
				RenderRote.setRote(stack,10, 0.0F, 0.0F, 1.0F);
			}else if(entity.move_mode == 4) {
				RenderRote.setRote(stack,motion * 1F, 1.0F, 0.0F, 0.0F);
				RenderRote.setRote(stack,-10, 0.0F, 0.0F, 1.0F);
			}else{
				RenderRote.setRote(stack,motion * 1F, -1.0F, 0.0F, 0.0F);//
			}
			if(entity.isAttacking())RenderRote.setRote(stack,leg_rotex, 1.0F, 0.0F, 0.0F);
			stack.translate( kneex,  - kneey, -  kneez);
			obj.renderPart("knee_r");
			if(entity.shieldHealth>0 && entity.hurtTime>0)this.renderEnchantGlint(stack, "knee_r",0);
			obj.setRenderType(rt);
			
			stack.translate(-legx, legy, legz);
			if(entity.move_mode == 3) {
				RenderRote.setRote(stack,motion * 1F, 1.0F, 0.0F, 0.0F);
				RenderRote.setRote(stack,10, 0.0F, 0.0F, 1.0F);
			}else if(entity.move_mode == 4) {
				RenderRote.setRote(stack,motion * 1F, 1.0F, 0.0F, 0.0F);
				RenderRote.setRote(stack,-10, 0.0F, 0.0F, 1.0F);
			}else{
				if(motion>0){
					RenderRote.setRote(stack,motion * 1F, 1.0F, 0.0F, 0.0F);
				}else{
					RenderRote.setRote(stack,motion * 1F, -1.0F, 0.0F, 0.0F);
				}
			}
			if(entity.isAttacking())RenderRote.setRote(stack,-leg_rotex, 1.0F, 0.0F, 0.0F);
			stack.translate(legx, - legy, - legz);
			obj.renderPart("leg_r");
			if(entity.shieldHealth>0 && entity.hurtTime>0)this.renderEnchantGlint(stack, "leg_r",0);
			obj.setRenderType(rt);
			obj.renderPart("feet_r");
			if(entity.shieldHealth>0 && entity.hurtTime>0)this.renderEnchantGlint(stack, "feet_r",0);
		}
		stack.popPose();
	}
	
	float rote;
    private void renderbody(int packedLightIn, EntitySA_Ember entity, float limbSwing, float limbSwingAmount, float partialTicks, PoseStack stack, MultiBufferSource bufferIn){
		Minecraft minecraft = Minecraft.getInstance();
		stack.pushPose();
    	obj.renderPart("body");
		
		if(entity.shieldHealth>0 && entity.hurtTime>0)this.renderEnchantGlint(stack, "body",0);
		
		if(entity.getArmyType2()==1){
			obj.setRender(rtgun,null,stack,packedLightIn);
			obj.renderPart("gun_wait");
		}else{
			obj.setRender(rtsw,null,stack,packedLightIn);
			obj.renderPart("sward_wait");
			
            if(entity.getMoveMode()==1)this.renderEnchantGlint(stack, "sward_wait",1);
			
			obj.setRender(sward,null,stack,0xF000F0);
			obj.renderPart("sward_wait_light");
		}
		obj.setRender(body,null,stack,0xF000F0);
		obj.renderPart("body_light");
		obj.setRender(rt,null,stack,packedLightIn);
		stack.popPose();
		
		float size = 1;
		if(entity.anim2 < 4){
			size = (1 + partialTicks * 0.1F)*(float)(4+entity.anim2) / 4F;
		}
		if(entity.anim2 >= 4 && entity.anim2<6){
			size = (1 + partialTicks * 0.1F)*(float)(6-entity.anim2) / 2F;
		}
		
		stack.pushPose();
		stack.translate(0, 9.49F, -2.13F);
		RenderRote.setRote(stack,(entity.xRotO + (entity.getXRot() - entity.xRotO) * partialTicks), 1.0F, 0.0F, 0.0F);
		stack.translate(0, - 9.49F, 2.13F);
		obj.renderPart("gun2");
		if(entity.getMoveMode()==1)this.renderEnchantGlint(stack, "gun2",1);
		obj.setRender(gun2,null,stack,0xF000F0);
		obj.renderPart("gun2_light");
		obj.setRender(f1,null,stack,0xF000F0);
		//RenderRote.setRote(stack,90, 1.0F, 0.0F, 0.0F);
		stack.pushPose();
		stack.translate(1.31F, 9.17F, 0.57F);
		stack.scale(size, size, size);
		if(entity.anim2<6){
			if(entity.level().random.nextInt(6)<2){
				obj.renderPart("flash1");
			}else if(entity.level().random.nextInt(6)<4){
				obj.renderPart("flash2");
			}else{
				obj.renderPart("flash3");
			}
		}
		stack.popPose();
		stack.pushPose();
		stack.translate(-1.31F, 9.17F, 0.57F);
		stack.scale(size, size, size);
		if(entity.anim2<6){
			if(entity.level().random.nextInt(6)<2){
				obj.renderPart("flash1");
			}else if(entity.level().random.nextInt(6)<4){
				obj.renderPart("flash2");
			}else{
				obj.renderPart("flash3");
			}
		}
		obj.setRender(rt,null,stack,packedLightIn);
		stack.popPose();
		//
		stack.popPose();
		float size1 = partialTicks * 0.3F + 1;
		float arm_l_rotex = 0;
		float arm_l_rotez = 0;
		float arm_l_rotey = 0;
		float arm_r_rotex = 0;
		float arm_r_rotez = 0;
		float arm_r_rotey = 0;
    	stack.pushPose();
    	{
			float Ax = Mth.cos(limbSwing * 0.6662F) * limbSwingAmount;
			stack.translate(elbowx, elbowy, elbowz);
			if(entity.getArmyType2()==2){
				RenderRote.setRote(stack,15, 1.0F, 0.0F, 0.0F);
			}
			if(entity.getArmyType2()==1){
				if(entity.attackAnim>0){
					if(entity.getMovePosY()==1){
						arm_l_rotex = -140 + entity.attackAnim * 17;
					}else if(entity.getMovePosY()==3){
						arm_l_rotex = -140 + entity.attackAnim * 14;
						arm_l_rotey = 5-entity.attackAnim * 2;
						arm_l_rotez = 50-entity.attackAnim * 8;//
					}else{
						arm_l_rotex = -140 + entity.attackAnim * 13;
						//arm_l_rotey = 5+entity.attackAnim * 2;
						arm_l_rotez = -50+entity.attackAnim * 10;//
					}
				}
			}else{
				arm_l_rotex = -41 + (entity.xRotO + (entity.getXRot() - entity.xRotO) * partialTicks);
			}
			
			RenderRote.setRote(stack,arm_l_rotex, 1.0F, 0.0F, 0.0F);
			RenderRote.setRote(stack,arm_l_rotey, 0.0F, 1.0F, 0.0F);
			RenderRote.setRote(stack,arm_l_rotez, 0.0F, 0.0F, 1.0F);
			
			if(entity.anim1<5){
				stack.translate(armx, army, armz);
				RenderRote.setRote(stack,-entity.anim1, 0.0F, 1.0F, 0.0F);
				stack.translate(-armx, -army, -armz);
			}
			
			stack.translate(- elbowx, - elbowy, - elbowz);
			
			obj.renderPart("elbow_l");
			if(entity.shieldHealth>0 && entity.hurtTime>0)this.renderEnchantGlint(stack, "elbow_l",0);
			obj.setRenderType(rt);
			
			if(entity.getArmyType2()==1 && entity.attackAnim==0){
				stack.translate(armx, army, armz);
				RenderRote.setRote(stack,15, 0.0F, 1.0F, 0.0F);
				stack.translate(-armx, -army, -armz);
			}else{
				if(entity.anim1<5){
					stack.translate(armx, army, armz);
					RenderRote.setRote(stack,entity.anim1, 0.0F, 1.0F, 0.0F);
					stack.translate(-armx, -army, -armz);
				}
			}
			obj.renderPart("arm_l");
			if(entity.shieldHealth>0 && entity.hurtTime>0)this.renderEnchantGlint(stack, "arm_l",0);
			obj.setRenderType(rt);
			
			stack.pushPose();
			if(entity.getArmyType2()==1){
				obj.setRender(rtsw,null,stack,packedLightIn);
				obj.renderPart("sward_l");
				
				if(entity.getMoveMode()==1)this.renderEnchantGlint(stack, "sward_l",1);
				
				obj.setRender(sward,null,stack,0xF000F0);
				obj.renderPart("sward_l_light");
				obj.setRender(rt,null,stack,packedLightIn);
			}else{
				obj.setRender(rtgun,null,stack,packedLightIn);
				obj.renderPart("gun_l");
			}
			stack.popPose();
			
			if(entity.getArmyType2()==0){
				stack.pushPose();
				RenderRote.setRote(stack,41, 1.0F, 0.0F, 0.0F);
				obj.setRender(f1,null,stack,0xF000F0);
				stack.translate(fire_x, fire_y, fire_z);
				stack.scale(size1, size1, size1);
				if(entity.anim1>0 && entity.anim1<5){
					if(entity.level().random.nextInt(6)<2){
						obj.renderPart("mat_1");
					}else if(entity.level().random.nextInt(6)<4){
						obj.renderPart("mat_2");
					}else{
						obj.renderPart("mat_3");
					}
				}
				obj.setRender(rt,null,stack,packedLightIn);
				stack.popPose();
			}
			//tex(tex);
		}
		stack.popPose();
		
		stack.pushPose();
		{
			float Ax = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * limbSwingAmount;
			stack.translate(-elbowx, elbowy, elbowz);
			if(entity.getArmyType2()==2){
				RenderRote.setRote(stack,15, 1.0F, 0.0F, 0.0F);
			}

			if(entity.getArmyType2()==1){
				if(entity.attackAnim>0){
					if(entity.getMovePosY()==1){
						arm_r_rotex = -140 + entity.attackAnim * 17;
					}else if(entity.getMovePosY()==3){
						arm_r_rotex = -140 + entity.attackAnim * 14;
						arm_r_rotey = 5-entity.attackAnim * 2;
						arm_r_rotez = 50-entity.attackAnim * 8;//
					}else{
						arm_r_rotex = -140 + entity.attackAnim * 12;
						//arm_r_rotey = -5+entity.attackAnim * 2;
						arm_r_rotez = -50+entity.attackAnim * 8;//
					}
				}
			}else{
				//if(EntitySA_Ember.gun_count1<5)RenderRote.setRote(stack,-EntitySA_Ember.gun_count1, 1.0F, 0.0F, 0.0F);
				arm_r_rotex = -41 + (entity.xRotO + (entity.getXRot() - entity.xRotO) * partialTicks);
			}

			RenderRote.setRote(stack,arm_r_rotex, 1.0F, 0.0F, 0.0F);
			RenderRote.setRote(stack,arm_r_rotey, 0.0F, 1.0F, 0.0F);
			RenderRote.setRote(stack,arm_r_rotez, 0.0F, 0.0F, 1.0F);
			
			if(entity.anim1<5){
				stack.translate(-armx, army, armz);
				RenderRote.setRote(stack,entity.anim1, 0.0F, 1.0F, 0.0F);
				stack.translate(armx, -army, -armz);
			}
			
			stack.translate(elbowx, - elbowy, - elbowz);
			obj.renderPart("elbow_r");
			if(entity.shieldHealth>0 && entity.hurtTime>0)this.renderEnchantGlint(stack, "elbow_r",0);
			obj.setRenderType(rt);
			
			if(entity.getArmyType2()==1 && entity.attackAnim==0){
				stack.translate(-armx, army, armz);
				RenderRote.setRote(stack,-15, 0.0F, 1.0F, 0.0F);
				stack.translate(armx, -army, -armz);
			}else{
				if(entity.anim1<5){
					stack.translate(-armx, army, armz);
					RenderRote.setRote(stack,-entity.anim1, 0.0F, 1.0F, 0.0F);
					stack.translate(armx, -army, -armz);
				}
			}
			obj.renderPart("arm_r");
			if(entity.shieldHealth>0 && entity.hurtTime>0)this.renderEnchantGlint(stack, "arm_r",0);
			obj.setRenderType(rt);
			
			stack.pushPose();
			if(entity.getArmyType2()==1){
				obj.setRender(rtsw,null,stack,packedLightIn);
				obj.renderPart("sward_r");
				
				if(entity.getMoveMode()==1)this.renderEnchantGlint(stack, "sward_r",1);
				
				obj.setRender(sward,null,stack,0xF000F0);
				obj.renderPart("sward_r_light");
				obj.setRender(rt,null,stack,packedLightIn);
			}else{
				obj.setRender(rtgun,null,stack,packedLightIn);
				obj.renderPart("gun_r");
			}
			stack.popPose();
			
			if(entity.getArmyType2()==0){
				stack.pushPose();
				RenderRote.setRote(stack,41, 1.0F, 0.0F, 0.0F);
				obj.setRender(f1,null,stack,0xF000F0);
				stack.translate(-fire_x, fire_y, fire_z);
				stack.scale(size1, size1, size1);
				if(entity.anim1>0 && entity.anim1<5){
					if(entity.level().random.nextInt(6)<2){
						obj.renderPart("mat_1");
					}else if(entity.level().random.nextInt(6)<4){
						obj.renderPart("mat_2");
					}else if(entity.level().random.nextInt(6)==4){
						obj.renderPart("mat_3");
					}
				}
				obj.setRender(rt,null,stack,packedLightIn);
				stack.popPose();
			}
			//tex(tex);
		}
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