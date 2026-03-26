package advancearmy.render.soldier;
import advancearmy.entity.soldier.EntitySA_OFG;
import net.minecraftforge.api.distmarker.Dist;
import com.mojang.math.Axis;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.RenderType;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider; // 新增/变化的导入
import net.minecraft.client.renderer.entity.LivingEntityRenderer; // 类名LivingRenderer改为LivingEntityRenderer
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer; // HumanoidArmorLayer
import net.minecraft.client.renderer.block.model.ItemTransforms; // ItemTransforms
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.world.entity.LivingEntity; // net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Entity; // net.minecraft.world.entity.Entity
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.core.BlockPos; // net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceLocation; // net.minecraft.resources.ResourceLocation
import net.minecraft.util.Mth; // Mth
import net.minecraft.world.entity.HumanoidArm; // HandSide
import net.minecraft.world.InteractionHand; // Hand
import net.minecraft.client.Minecraft;
import wmlib.client.render.SARenderState;
import wmlib.client.render.RenderTypeVehicle;
import wmlib.client.obj.SAObjModel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.Util;
import org.joml.Matrix4f;
import advancearmy.render.ModelNone;
@OnlyIn(Dist.CLIENT)
public class RenderOFG extends MobRenderer<EntitySA_OFG, ModelNone<EntitySA_OFG>>
{
	public ResourceLocation tex = ResourceLocation.tryParse("advancearmy:textures/mob/soldier/ofg.png");
    private static final SAObjModel obj = new SAObjModel("advancearmy:textures/mob/soldier/ofg.obj");
	
    private static final SAObjModel gun4 = new SAObjModel("advancearmy:textures/gun/lasergun1.obj");
    private static final ResourceLocation gun4_t1 = ResourceLocation.tryParse("advancearmy:textures/gun/lasergun1.png");

    private static final SAObjModel rpg2 = new SAObjModel("advancearmy:textures/gun/huweimaopao.obj");
    private static final ResourceLocation rpg2t = ResourceLocation.tryParse("advancearmy:textures/gun/huweimaopao_t.png");
	
    private static final SAObjModel sci = new SAObjModel("advancearmy:textures/gun/sci.obj");
    private static final ResourceLocation sci_t = ResourceLocation.tryParse("advancearmy:textures/gun/sci_t2.png");
	
	private static final ResourceLocation fire_tex = ResourceLocation.tryParse("advancearmy:textures/entity/flash/fireflash1.png");
	private static final ResourceLocation fire_tex2 = ResourceLocation.tryParse("advancearmy:textures/entity/flash/fireflash3_4.png");
	
	private static final ResourceLocation glint = ResourceLocation.tryParse("wmlib:textures/misc/vehicle_made1.png");
	RenderType gl = SARenderState.getBlendGlowGlint(glint);
	private static final SAObjModel dun = new SAObjModel("advancearmy:textures/entity/hudun.obj");
	
	RenderType rt = RenderTypeVehicle.objrender(tex);
	RenderType rtgun4t = RenderTypeVehicle.objrender(gun4_t1);
	RenderType rtrpg2t = RenderTypeVehicle.objrender(rpg2t);
	RenderType rtscit = RenderTypeVehicle.objrender(sci_t);
	RenderType f1 = SARenderState.getBlendDepthWrite(fire_tex);
	RenderType f2 = SARenderState.getBlendDepthWrite(fire_tex2);
	
	
    public RenderOFG(EntityRendererProvider.Context context)
    {
    	super(context, new ModelNone(),0.5F);
    }

    public ResourceLocation getTextureLocation(EntitySA_OFG entity)
    {
		return tex;
    }
    private void render_light(EntitySA_OFG entity, String name){
		obj.renderPart(name);
    }
	boolean change;
    float iii;
    public void render(EntitySA_OFG entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource bufferIn, int packedLight)
    {
		super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLight);
		if(entity.hide)return;
	    stack.pushPose();
    	float limbSwing = this.F6(entity, partialTicks);
		float limbSwingAmount = this.F5(entity, partialTicks);
		Minecraft mc = Minecraft.getInstance();
		if(entity.getHealth()==0){
			stack.pushPose();
			stack.scale(entity.particleSize,entity.particleSize,entity.particleSize);
			dun.setRender(gl, null, stack, 0xF000F0);
			stack.pushPose();
			//stack.translate(0F, 0F, 6F);
			long time = (long)((double)Util.getMillis() * Minecraft.getInstance().options.glintSpeed().get() * 20.0);
			float u = (float)(time % 110000L) / 110000.0f;
			float v = (float)(time % 30000L) / 30000.0f;
			Matrix4f move = new Matrix4f().translation(u, v, 0.0f);
			Matrix4f move1 = new Matrix4f().translation(-u, -v, 0);
			Matrix4f move2 = new Matrix4f().translation(-u, v, 0);
			Matrix4f move3 = new Matrix4f().translation(u, -v, 0);
			RenderSystem.setShaderColor(1F, 1F, 1F, entity.particleAlpha*0.6F);
			RenderSystem.setTextureMatrix(move);
			dun.renderPart("dun");
			//stack.translate(8F, 0.75F, 0.0F);
			RenderSystem.setShaderColor(1F, 1F, 1F, entity.particleAlpha*0.3F);
			RenderSystem.setTextureMatrix(move1);
			dun.renderPart("dun");
			//stack.translate(8F, 0.75F, 0.0F);
			RenderSystem.setShaderColor(0.1F, 0.3F, 1F, entity.particleAlpha);
			RenderSystem.setTextureMatrix(move2);
			dun.renderPart("dun");
			//stack.translate(8F, 0.75F, 0.0F);
			RenderSystem.setShaderColor(0.1F, 0.3F, 1F, entity.particleAlpha*0.5F);
			RenderSystem.setTextureMatrix(move3);
			dun.renderPart("dun");
			RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
			RenderSystem.resetTextureMatrix();
			stack.popPose();
			stack.popPose();
		}else{
			obj.setRender(rt, null, stack, packedLight);
			stack.mulPose(Axis.YP.rotationDegrees(180.0F));

			stack.mulPose(Axis.YP.rotationDegrees(180.0F - (entity.yBodyRotO + (entity.yBodyRot - entity.yBodyRotO) * partialTicks)));
			
			/*if(entity.getHealth()==0){
				int dt = entity.deathTime;
				if(dt>8)dt=8;
				stack.translate(0F, 0.15F, -1.5F);
				stack.mulPose(Axis.XP.rotationDegrees(-90F*dt/8F));
			}else*/{
				if(entity.getRemain2()==1){
					stack.translate(0F, 0.15F, -1.5F);
					stack.mulPose(Axis.XP.rotationDegrees(90F));
				}
			}
			
			{
				stack.pushPose();
				this.renderlegs(stack, entity, limbSwing, limbSwingAmount, partialTicks);
				stack.popPose();
			}
			
			if(entity.isPassenger()&&!entity.stand||entity.sit_aim && entity.getRemain2()!=1) {//
				stack.translate(0F, -0.5F, 0.0F);
			}
			
			float Ax1 = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * limbSwingAmount;//
			stack.translate( 0, Ax1 * (180F / (float)Math.PI) * 0.001F, 0);//
			{
				stack.translate(0F, 0.75F, 0.0F);
				stack.mulPose(Axis.XP.rotationDegrees(-Ax1 * (180F / (float)Math.PI) * 0.05F));
				if(entity.getRemain2()==2)stack.mulPose(Axis.XP.rotationDegrees(20));
				if(entity.anim1<5)stack.mulPose(Axis.XP.rotationDegrees(-entity.anim1));
				stack.translate(0F, -0.75F, 0.0F);
			}
			{
				stack.pushPose();
				stack.translate(0F, 1.5F, 0.0F);
				stack.mulPose(Axis.YP.rotationDegrees(180.0F - (entity.yHeadRotO + (entity.yHeadRot - entity.yHeadRotO) * partialTicks) -(180.0F - (entity.yBodyRotO + (entity.yBodyRot - entity.yBodyRotO) * partialTicks))));
				if(entity.getRemain2()==1){//
					stack.mulPose(Axis.XP.rotationDegrees(entity.getXRot()-60F));
				}else{
					stack.mulPose(Axis.XP.rotationDegrees(entity.getXRot()));
				}
				if(entity.getRemain2()==2 && entity.getRemain1()!=0 && entity.isAttacking())stack.mulPose(Axis.XP.rotationDegrees(15F));
				stack.translate(0F, -1.5F, 0.0F);
				obj.renderPart("head");
				
				//-SARenderHelper.enableBlendMode(RenderType.ALPHA);//
				obj.renderPart("head_light");
				obj.renderPart("head_light");
				//-SARenderHelper.disableBlendMode(RenderType.ALPHA);//
				stack.popPose();
			}
			
			{
				stack.pushPose();
				this.renderbody(entity, limbSwing, limbSwingAmount, partialTicks, stack, bufferIn,packedLight);
				stack.popPose();
			}
		}
	    stack.popPose();
    }
	
	
	
    public float move_size = 1F;//
    private void renderlegs(PoseStack stack, EntitySA_OFG entity, float limbSwing, float limbSwingAmount, float partialTicks){
		float Ax = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * limbSwingAmount;
		float motion =  Ax * (180F / (float)Math.PI) * this.move_size;
    	stack.pushPose();
    	{
			stack.translate(0.15F, 0.625F, 0.0F);//stack.translate(0.15F, 0.75F, 0.0F);
			if(entity.isPassenger()&&!entity.stand||entity.sit_aim && entity.getRemain2()!=1) {
				stack.translate(0F, -0.5F, 0.0F);
				stack.mulPose(Axis.XP.rotationDegrees(-90));
				stack.mulPose(Axis.ZP.rotationDegrees(20));
			}else {
				stack.mulPose(Axis.XP.rotationDegrees(motion));
			}
			if(entity.getRemain2()==2 && entity.isAttacking())stack.mulPose(Axis.XP.rotationDegrees(-30F));
			stack.translate(-0.15F, -0.625F, 0.0F);
			obj.renderPart("knee_l");
			
			stack.translate(0.15F, 0.4F, 0.05F);
			if(motion<0){
				stack.mulPose(Axis.XP.rotationDegrees(-motion));
			}else{
				stack.mulPose(Axis.XP.rotationDegrees(motion));
			}
			if(entity.getRemain2()==2 && entity.isAttacking())stack.mulPose(Axis.XP.rotationDegrees(25F));
			stack.translate(-0.15F, -0.4F, -0.05F);
			obj.renderPart("leg_l");
			this.render_light(entity, "leg_l_light");
		}
		stack.popPose();
		
		stack.pushPose();
		{
			stack.translate(-0.15F, 0.625F, 0.0F);
			if(entity.isPassenger()&&!entity.stand||entity.sit_aim && entity.getRemain2()!=1) {
				stack.translate(0F, -0.5F, 0.0F);
				stack.mulPose(Axis.XP.rotationDegrees(-90));
				stack.mulPose(Axis.ZP.rotationDegrees(-20));
			}else {
				stack.mulPose(Axis.XP.rotationDegrees(-motion));//
			}
			if(entity.getRemain2()==2 && entity.isAttacking())stack.mulPose(Axis.XP.rotationDegrees(15F));;
			stack.translate(0.15F, -0.625F, 0.0F);
			obj.renderPart("knee_r");
			
			stack.translate(-0.15F, 0.4F, 0.05F);
			if(motion>0){
				stack.mulPose(Axis.XP.rotationDegrees(motion));
			}else{
				stack.mulPose(Axis.XP.rotationDegrees(-motion));
			}
			if(entity.getRemain2()==2 && entity.isAttacking())stack.mulPose(Axis.XP.rotationDegrees(20F));
			stack.translate(0.15F, -0.4F, -0.05F);
			obj.renderPart("leg_r");
			this.render_light(entity, "leg_r_light");
		}
		stack.popPose();
	}
    
    private void renderbody(EntitySA_OFG entity, float limbSwing, float limbSwingAmount, float partialTicks, PoseStack stack, MultiBufferSource bufferIn, int packedLight){
		stack.pushPose();
		Minecraft minecraft = Minecraft.getInstance();
    	obj.renderPart("body");
		this.render_light(entity, "body_light");
		SAObjModel wait_model = gun4;
		wait_model.setRender(rtgun4t, null, stack, packedLight);
		if(entity.getWeaponId()==2){
			wait_model = rpg2;
			wait_model.setRender(rtrpg2t, null, stack, packedLight);
		}
		if(entity.getWeaponId()==3){
			wait_model = sci;
			wait_model.setRender(rtscit, null, stack, packedLight);
		}
		if(entity.mainWeaponId!=0){
			stack.pushPose();
			stack.scale(0.1875F, 0.1875F, 0.1875F);
			{//ar / 1.2 rpg
				stack.mulPose(Axis.XP.rotationDegrees(-90F));
				stack.mulPose(Axis.ZP.rotationDegrees(180F));
				stack.translate(-1.2F, -2.2F, 6F);//x,z,y
			}
			wait_model.renderPart("mat1");
			wait_model.renderPart("mat2");
			wait_model.renderPart("mat3");
			stack.popPose();
		}
		obj.setRenderType(rt);
		stack.popPose();

		float arm_l_rotex = 0;
		float arm_l_rotez = 0;
		float arm_l_rotey = 0;
		float arm_r_rotex = 0;
		float arm_r_rotez = 0;
		float arm_r_rotey = 0;
		boolean move_arm = false;
		 
    	stack.pushPose();
    	{	
			float Ax = Mth.cos(limbSwing * 0.6662F) * limbSwingAmount;
			stack.translate(0.37F, 1.375F, 0.0F);
			if(entity.getRemain2()==2 && !(entity.isAttacking()) && !(entity.getRemain1()==0)){
			stack.mulPose(Axis.XP.rotationDegrees(-30F));
			}else if(entity.getRemain2()==2){
			stack.mulPose(Axis.XP.rotationDegrees(-20F));
			}
			if(entity.getRemain1()==0||entity.getRemain2()==2 && !(entity.isAttacking())){
				if(entity.getRemain2()!=1)arm_l_rotex = -20;
				arm_l_rotez = -50;
			}
			else if(entity.isAttacking() && !(entity.deathTime > 0) && !(entity.getRemain1()==0)) {
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
				stack.mulPose(Axis.XP.rotationDegrees(arm_l_rotex+180F));
			}else{
				stack.mulPose(Axis.XP.rotationDegrees(arm_l_rotex));
			}
			stack.mulPose(Axis.YP.rotationDegrees(arm_l_rotey));
			stack.mulPose(Axis.ZP.rotationDegrees(arm_l_rotez));
			
			stack.translate(-0.37F, -1.375F, 0.0F);
			
			obj.renderPart("elbow_l");
			if(move_arm){
				stack.translate(0.37F, 1.12F, -0.07F);
				if(Ax<0){
					stack.mulPose(Axis.XP.rotationDegrees(arm_l_rotex));
				}else{
					stack.mulPose(Axis.XP.rotationDegrees(-arm_l_rotex));
				}
				stack.translate(-0.37F, -1.12F, 0.07F);
			}
			obj.renderPart("arm_l");

			this.render_light(entity, "arm_l_light");
			/*if(entity.getHealth() > 0) {
				stack.pushPose();
				stack.mulPose(Axis.XP.rotationDegrees(90F));
				stack.popPose();
			}*/
		}
		stack.popPose();

		stack.pushPose();
		{
			float Ax = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * limbSwingAmount;
			stack.translate(-0.37F, 1.375F, 0.0F);
			if(entity.getRemain2()==2 && !(entity.isAttacking()) && !(entity.getRemain1()==0)){
			stack.mulPose(Axis.XP.rotationDegrees(-30F));
			}else if(entity.getRemain2()==2){
			stack.mulPose(Axis.XP.rotationDegrees(-20F));
			}
			if(entity.getRemain1()==0||entity.getRemain2()==2 && !(entity.isAttacking())){
				arm_r_rotez = 10;
				if(entity.getRemain2()!=1)arm_r_rotex = -20;
			}
			else 
			if(entity.isAttacking() && !(entity.deathTime > 0) && !(entity.getRemain1()==0)) {
				if(entity.anim1<5){
	    			stack.mulPose(Axis.XP.rotationDegrees(-entity.anim1));
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
			
			if(entity.getRemain2()==1){//
				stack.mulPose(Axis.XP.rotationDegrees(arm_r_rotex+180F));
			}else{
				stack.mulPose(Axis.XP.rotationDegrees(arm_r_rotex));
			}
			stack.mulPose(Axis.YP.rotationDegrees(arm_r_rotey));
			stack.mulPose(Axis.ZP.rotationDegrees(arm_r_rotez));
			
			stack.translate(0.37F, -1.375F, 0.0F);
//			stack.pushPose();
			obj.renderPart("elbow_r");
			if(move_arm){
				stack.translate(-0.37F, 1.12F, -0.07F);
				if(Ax<0){
					stack.mulPose(Axis.XP.rotationDegrees(arm_r_rotex));
				}else{
					stack.mulPose(Axis.XP.rotationDegrees(-arm_r_rotex));
				}
				stack.translate(0.37F, -1.12F, 0.07F);
			}
			obj.renderPart("arm_r");
			this.render_light(entity, "arm_r_light");
			if(entity.canfire){
				SAObjModel gun_model = gun4;
				gun_model.setRender(rtgun4t, null, stack, packedLight);
				if(entity.getWeaponId()==2){
					gun_model = sci;
					gun_model.setRender(rtscit, null, stack, packedLight);
				}
				if(entity.getWeaponId()==3){
					gun_model = rpg2;
					gun_model.setRender(rtrpg2t, null, stack, packedLight);
				}
				stack.pushPose();
				stack.scale(0.1875F, 0.1875F, 0.1875F);
				{
					stack.mulPose(Axis.XP.rotationDegrees(90F));
					stack.translate(-1.9F, 0 , -4.33F);//x,z,y
				}
				gun_model.renderPart("mat1");
				gun_model.renderPart("mat2");
				if(entity.getRemain1()>0)gun_model.renderPart("mat3");
				
				gun_model.renderPart("mat1_light");
				
				stack.popPose();
				stack.pushPose();
				stack.mulPose(Axis.XP.rotationDegrees(90F));
				if(entity.getWeaponId()==3){
					obj.setRender(f2, null, stack, 0xF000F0);
				}else{
					obj.setRender(f1, null, stack, 0xF000F0);
				}
				
				stack.translate(-entity.fireposX, 0.23F, entity.fireposZ);
				float size = entity.level().random.nextInt(3) * 0.25F + 1;
				stack.scale(size, size, size);
				if(entity.anim1<5 && entity.getRemain1() > 0 && entity.isAttacking())
				{
					if(entity.level().random.nextInt(6)<2){
						obj.renderPart("flash1");
					}else if(entity.level().random.nextInt(6)<4){
						obj.renderPart("flash2");
					}else{
						obj.renderPart("flash3");
					}
				}
				//tex---(tex);
				stack.popPose();
			}
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
            f5 = entity.walkAnimation.speed();
            if (f5 > 1.0F)
            {
                f5 = 1.0F;
            }
        }
 		return f5;
 	}
}