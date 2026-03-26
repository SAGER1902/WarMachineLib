package advancearmy.render.mob;
import advancearmy.entity.mob.EntityAohuan;
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

import advancearmy.render.ModelNone;
@OnlyIn(Dist.CLIENT)
public class RenderAohuan extends MobRenderer<EntityAohuan, ModelNone<EntityAohuan>>
{
	/*public ResourceLocation tex2 = ResourceLocation.tryParse("advancearmy:textures/mob/soldier/aohuan2.png");
    private static final SAObjModel obj2 = new SAObjModel("advancearmy:textures/mob/soldier/aohuan2.obj");*/
	
	public ResourceLocation tex = ResourceLocation.tryParse("advancearmy:textures/mob/soldier/aoh.png");
    private static final SAObjModel obj = new SAObjModel("advancearmy:textures/mob/soldier/aoh.obj");
	
    private static final SAObjModel huoshe = new SAObjModel("advancearmy:textures/gun/huoshe.obj");
    private static final ResourceLocation huoshe_t1 = ResourceLocation.tryParse("advancearmy:textures/gun/huoshe_t1.jpg");
    private static final SAObjModel gema = new SAObjModel("advancearmy:textures/gun/gema.obj");
    private static final ResourceLocation gema_t = ResourceLocation.tryParse("advancearmy:textures/gun/gema_t.jpg");

    private static final SAObjModel rpg1 = new SAObjModel("advancearmy:textures/gun/echo.obj");
    private static final ResourceLocation rpg1t = ResourceLocation.tryParse("advancearmy:textures/gun/echo_t.jpg");
    private static final SAObjModel yingyan = new SAObjModel("advancearmy:textures/gun/yingyan.obj");
    private static final ResourceLocation yingyan_t = ResourceLocation.tryParse("advancearmy:textures/gun/yingyan_t.jpg");
	private static final ResourceLocation fire_tex = ResourceLocation.tryParse("advancearmy:textures/entity/flash/flash.png");
	
	RenderType rt = RenderTypeVehicle.objrender(tex);
	RenderType rtht = RenderTypeVehicle.objrender(huoshe_t1);
	RenderType rtgt = RenderTypeVehicle.objrender(gema_t);
	RenderType rtrpgt = RenderTypeVehicle.objrender(rpg1t);
	RenderType rtyt = RenderTypeVehicle.objrender(yingyan_t);
	RenderType f1 = SARenderState.getBlendDepthWrite(fire_tex);
	RenderType rtglow = SARenderState.getBlendDepthWrite(tex);
	
    public RenderAohuan(EntityRendererProvider.Context context)
    {
    	super(context, new ModelNone(),0.5F);
    }

    public ResourceLocation getTextureLocation(EntityAohuan entity)
    {
		return tex;
    }
    private void render_light(EntityAohuan entity, String name){
		obj.renderPart(name);
    }
	
    float iii;
    public void render(EntityAohuan entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource bufferIn, int packedLight)
    {
		super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLight);
		if(entity.hide)return;
			
		{
		stack.pushPose();

    	float limbSwing = this.F6(entity, partialTicks);
		float limbSwingAmount = this.F5(entity, partialTicks);
		obj.setRender(rt, null, stack, packedLight);
		Minecraft mc = Minecraft.getInstance();

		stack.mulPose(Axis.YP.rotationDegrees(180.0F));
		if(iii < 360F){
			iii = iii + 0.1F;
		}else{
			iii = 0F;
		}
		stack.mulPose(Axis.YP.rotationDegrees(180.0F - (entity.yBodyRotO + (entity.yBodyRot - entity.yBodyRotO) * partialTicks)));
		
		if(entity.getHealth()==0){
			int dt = entity.deathTime;
			if(dt>8)dt=8;
			stack.translate(0F, 0.15F, -1.5F);
			stack.mulPose(Axis.XP.rotationDegrees(-90F*dt/8F));
		}else{
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
		
		if(entity.isPassenger()||entity.sit_aim && entity.getRemain2()!=1) {//
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

			obj.setRender(rtglow, null, stack, 0xF000F0);
			obj.renderPart("head_light");
			//obj.renderPart("head_light");
			obj.setRender(rt, null, stack, packedLight);

			stack.popPose();
		}
		
		{
			stack.pushPose();
			this.renderbody(entity, limbSwing, limbSwingAmount, partialTicks, stack, bufferIn,packedLight);
			stack.popPose();
		}
		
	    stack.popPose();
		
		}
    }
	
	
	
    public float move_size = 1F;//
    private void renderlegs(PoseStack stack, EntityAohuan entity, float limbSwing, float limbSwingAmount, float partialTicks){
		float Ax = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * limbSwingAmount;
		float motion =  Ax * (180F / (float)Math.PI) * this.move_size;
    	stack.pushPose();
    	{
			stack.translate(0.15F, 0.625F, 0.0F);
			if(entity.isPassenger()||entity.sit_aim && entity.getRemain2()!=1) {
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
			
		}
		stack.popPose();
		
		stack.pushPose();
		{
			stack.translate(-0.15F, 0.625F, 0.0F);
			if(entity.isPassenger()||entity.sit_aim && entity.getRemain2()!=1) {
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
			
		}
		stack.popPose();
	}
    
    private void renderbody(EntityAohuan entity, float limbSwing, float limbSwingAmount, float partialTicks, PoseStack stack, MultiBufferSource bufferIn, int packedLight){
		Minecraft minecraft = Minecraft.getInstance();
		stack.pushPose();
    	obj.renderPart("body");
		SAObjModel wait_model = huoshe;
		wait_model.setRender(rtht, null, stack, packedLight);
		if(entity.getWeaponId()==1){
			wait_model = rpg1;
			wait_model.setRender(rtrpgt, null, stack, packedLight);
		}
		if(entity.getWeaponId()==3){
			wait_model = gema;
			wait_model.setRender(rtgt, null, stack, packedLight);
		}
		if(entity.getWeaponId()==4){
			wait_model = yingyan;
			wait_model.setRender(rtyt, null, stack, packedLight);
		}
		if(entity.mainWeaponId!=0){
			stack.pushPose();
			stack.scale(0.1875F, 0.1875F, 0.1875F);
			if(entity.getWeaponId()==3){//pistol
				stack.mulPose(Axis.XP.rotationDegrees(90F));
				stack.translate(-1.5F, 0 , -4F);//x,z,y
			}else if(entity.getWeaponId()==4){//sniper
				stack.mulPose(Axis.XP.rotationDegrees(-90F));
				stack.mulPose(Axis.YP.rotationDegrees(28F));
				stack.mulPose(Axis.ZP.rotationDegrees(90F));
				stack.translate(1.2F, 1.8F, 3F);//x,z,y
			}else{//ar / 1.2 rpg
				stack.mulPose(Axis.XP.rotationDegrees(-90F));
				stack.mulPose(Axis.ZP.rotationDegrees(180F));
				stack.translate(-1.2F, -2.2F, 8F);//x,z,y
			}
			wait_model.renderPart("mat1");
			wait_model.renderPart("mat2");
			wait_model.renderPart("mat3");
			stack.popPose();
		}
		obj.setRender(rt, null, stack, packedLight);
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



			if(entity.getHealth() > 0) {
			stack.pushPose();
			stack.mulPose(Axis.XP.rotationDegrees(90F));
			stack.popPose();
			}
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

			obj.renderPart("elbow_r");
			stack.translate(-0.37F, 1.12F, -0.07F);
			if(move_arm){
				if(Ax<0){
					stack.mulPose(Axis.XP.rotationDegrees(arm_r_rotex));
				}else{
					stack.mulPose(Axis.XP.rotationDegrees(-arm_r_rotex));
				}
			}
			if(!entity.isAttacking())stack.mulPose(Axis.XP.rotationDegrees(-15));
			stack.translate(0.37F, -1.12F, 0.07F);
			obj.renderPart("arm_r");
			if(entity.canfire){
				SAObjModel gun_model = huoshe;
				gun_model.setRender(rtht, null, stack, packedLight);
				if(entity.getWeaponId()==2){
					gun_model = rpg1;
					gun_model.setRender(rtrpgt, null, stack, packedLight);
				}
				if(entity.getWeaponId()==3){
					gun_model = yingyan;
					gun_model.setRender(rtyt, null, stack, packedLight);
				}
				if(entity.getWeaponId()==4){
					gun_model = gema;
					gun_model.setRender(rtgt, null, stack, packedLight);
				}
				stack.pushPose();
				stack.scale(0.1875F, 0.1875F, 0.1875F);
				if(!entity.isAttacking()&&entity.getWeaponId()==3){
					stack.mulPose(Axis.XP.rotationDegrees(51));
					stack.mulPose(Axis.YP.rotationDegrees(71F));
					stack.mulPose(Axis.ZP.rotationDegrees(-59F));
					stack.translate(-1.9F, 3.1F, -3F);//x,z,y
				}else{
					stack.mulPose(Axis.XP.rotationDegrees(90F));
					stack.translate(-1.9F, 0 , -4.33F);//x,z,y
				}
				gun_model.renderPart("mat1");
				gun_model.renderPart("mat2");
				if(entity.getRemain1()>0)gun_model.renderPart("mat3");
				stack.popPose();
				stack.pushPose();
				stack.mulPose(Axis.XP.rotationDegrees(90F));
				
				obj.setRender(f1, null, stack, packedLight);
				
				stack.translate(-entity.fireposX, 0.23F, entity.fireposZ);
				float size = entity.level().random.nextInt(3) * 0.25F + 1;
				stack.scale(size, size, size);
				if(entity.anim1<3 && entity.getRemain1() > 0 && entity.isAttacking())
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