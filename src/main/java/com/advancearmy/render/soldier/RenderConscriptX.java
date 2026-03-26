package advancearmy.render.soldier;
import advancearmy.entity.soldier.EntitySA_ConscriptX;
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
import net.minecraftforge.api.distmarker.Dist;
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
import wmlib.common.living.EntityWMSeat;
import advancearmy.render.ModelNone;
@OnlyIn(Dist.CLIENT)
public class RenderConscriptX extends MobRenderer<EntitySA_ConscriptX, ModelNone<EntitySA_ConscriptX>>
{
	private static final ResourceLocation tex1 = ResourceLocation.tryParse("advancearmy:textures/mob/soldier/conscript.png");
	private ResourceLocation tex = ResourceLocation.tryParse("advancearmy:textures/mob/soldier/conscriptx.jpg");
    private SAObjModel obj = new SAObjModel("advancearmy:textures/mob/soldier/conscriptx.obj");
	private static final SAObjModel obj1 = new SAObjModel("advancearmy:textures/mob/soldier/conscript.obj");
    private static final ResourceLocation gun2 = ResourceLocation.tryParse("advancearmy:textures/gun/gun.png");
	private static final ResourceLocation gun1 = ResourceLocation.tryParse("advancearmy:textures/gun/ak470.jpg");
    private static final SAObjModel sward = new SAObjModel("advancearmy:textures/mob/soldier/minesward.obj");
    private static final ResourceLocation swardtex = ResourceLocation.tryParse("advancearmy:textures/mob/soldier/warminer.png");
	
	private static final ResourceLocation fire_tex = ResourceLocation.tryParse("advancearmy:textures/entity/flash/mflash.png");
	private static final ResourceLocation cloud_tex = ResourceLocation.tryParse("advancearmy:textures/entity/flash/thruster_b.png");
	
	RenderType rt = RenderTypeVehicle.objrender(tex);
	RenderType rtgun1 = RenderTypeVehicle.objrender(gun1);
	RenderType rtgun2 = RenderTypeVehicle.objrender(gun2);
	RenderType rtswardtex = RenderTypeVehicle.objrender(swardtex);
	RenderType f1 = SARenderState.getBlendDepthWrite(fire_tex);
	RenderType c1 = SARenderState.getBlendDepthWrite(cloud_tex);
    public RenderConscriptX(EntityRendererProvider.Context context)
    {
    	super(context, new ModelNone(),0.5F);
    }

    public ResourceLocation getTextureLocation(EntitySA_ConscriptX entity)
    {
		return tex;
    }
    private void render_light(EntitySA_ConscriptX entity, String name){
		obj.renderPart(name);
    }
	
	public void render_cloud(PoseStack stack, Minecraft mc, float x,float y,float z,String name,float tick){//
		stack.pushPose();//
		obj.setRender(c1, null, stack, 0xF000F0);
		
		stack.translate(x, y, z);//
		stack.translate(0, 0, 0);//
		stack.mulPose(Axis.YP.rotationDegrees(tick));
		stack.translate(0, 0, 0);//
		obj.renderPart(name);
		//-SARenderHelper.disableBlendMode(RenderType.ADDITIVE);
		stack.popPose();//
	}
	
    public float kneex = 0.25F;
	public float kneey = 1.36F;
	public float kneez = 0F;
	
	public float legx = 0.25F;//
	public float legy = 0.7F;
	public float legz = 0F;
	
	public float bodyx = 0.0F;//
	public float bodyy = 1.38F;
	public float bodyz = 0.0F;
	
	public float headx = 0.0F;//
	public float heady = 2.7F;
	public float headz = 0F;
	
	public float elbowx = 0.65F;//
	public float elbowy = 2.3F;
	public float elbowz = 0.09F;
	
	public float armx = 0.65F;//
	public float army = 1.7F;
	public float armz = 0F;
    
	public float move_size = 1F;

	public float leg_rotez = 2;//
	public float leg_rotex = 2;//
	
	public float saber_x = 0.56F;
	public float saber_y = 2.23F;
	public float saber_z = -0.47F;
	public float saber_rote = 0;
	
	public float gun_x = -0.65F;
	public float gun_y = 0.1F;
	public float gun_z = -1.5F;
	
	public float fire_x = -0.65F;
	public float fire_y = 0.3F;
	public float fire_z = 0.2F;
	
    float iii;
    public void render(EntitySA_ConscriptX entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource bufferIn, int packedLight)
    {
		super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLight);
		if(entity.hide)return;
		{
			/*if(entity.getRemain2()>5){
				obj = obj1;
				tex = tex1;
				this.kneex = 0.15F;
				this.kneey = 0.625F;
				this.kneez = 0F;
				this.legx = 0.15F;
				this.legy = 0.4F;
				this.legz = 0.05F;
				this.bodyx = 0.0F;
				this.bodyy = 0.75F;
				this.bodyz = 0.0F;
				this.headx = 0.0F;//
				this.heady = 1.5F;
				this.headz = 0F;
				this.elbowx = 0.37F;//
				this.elbowy = 1.375F;
				this.elbowz = 0F;
				this.armx = 0.37F;
				this.army = 1.12F;
				this.armz = -0.07F;
				this.move_size = 1F;
				this.leg_rotez = 2;//
				this.leg_rotex = 2;//
				this.fire_x = -0.36F;
				this.fire_y = 1.75F;
				this.fire_z = 1.8F;
				this.gun_x = -0.375F;
				this.gun_y = 0;
				this.gun_z = -0.85F;
			}else{
				
			}*/
			stack.pushPose();
			
			
			float limbSwing = this.F6(entity, partialTicks);
			float limbSwingAmount = this.F5(entity, partialTicks);
			Minecraft mc = Minecraft.getInstance();

			
			obj.setRender(rt, null, stack, packedLight);
			
			stack.mulPose(Axis.YP.rotationDegrees(180.0F));
			
			if(iii < 360F){
				iii = iii + 10F;
			}else{
				iii = 0F;
			}
			stack.mulPose(Axis.YP.rotationDegrees(180.0F - (entity.yBodyRotO + (entity.yBodyRot - entity.yBodyRotO) * partialTicks)));
			
			if(entity.getRemain2()==1){
				stack.translate(0F, 0.15F, -1.5F);
				stack.mulPose(Axis.XP.rotationDegrees(90F));
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
				stack.pushPose();
				float body_rotex = 0;
				float body_rotey = 0;
				stack.translate(bodyx, bodyy, bodyz);
				if(entity.getRemain2()==2)stack.mulPose(Axis.XP.rotationDegrees(12));
				if(entity.attackAnim>0){
					body_rotex = -20 + entity.attackAnim * 3;
					if(entity.getMovePosY()==1){
						
					}else if(entity.getMovePosY()==3){
						body_rotey = 30-entity.attackAnim * 4;
					}else{
						body_rotey = -30+entity.attackAnim * 8;;
					}
				}
		    	stack.mulPose(Axis.XP.rotationDegrees(-Ax1 * (180F / (float)Math.PI) * 0.05F));
				stack.mulPose(Axis.XP.rotationDegrees(body_rotex));
				stack.mulPose(Axis.YP.rotationDegrees(body_rotey));
				stack.translate(-bodyx, -bodyy, -bodyz);
				{
					stack.pushPose();
					stack.translate(headx, heady, headz);
					stack.mulPose(Axis.YP.rotationDegrees(180.0F - (entity.yHeadRotO + (entity.yHeadRot - entity.yHeadRotO) * partialTicks) -(180.0F - (entity.yBodyRotO + (entity.yBodyRot - entity.yBodyRotO) * partialTicks))));
					if(entity.getRemain2()==1){//
						stack.mulPose(Axis.XP.rotationDegrees(entity.getXRot()-60F));
					}else{
						stack.mulPose(Axis.XP.rotationDegrees(entity.getXRot()));
					}
					if(entity.getMovePosY()==0 && entity.getRemain2()==2 && entity.getRemain1()!=0 && entity.isAttacking())stack.mulPose(Axis.XP.rotationDegrees(15F));
					stack.translate(- headx, - heady, - headz);
					obj.renderPart("head");
					obj.renderPart("head_light");
					stack.popPose();
				}
				this.renderbody(entity, limbSwing, limbSwingAmount, partialTicks, stack, bufferIn,packedLight);
				if(!entity.onGround() && entity.getMoveTypeX()==5){
					this.render_cloud(stack, mc,-saber_x,saber_y,saber_z,"cloud",iii);//
					this.render_cloud(stack, mc,saber_x,saber_y,saber_z,"cloud",iii);//
				}
				stack.popPose();
			}
			
			stack.popPose();
		
		}
    }
	
    private void renderlegs(PoseStack stack, EntitySA_ConscriptX entity, float limbSwing, float limbSwingAmount, float partialTicks){
		float Ax = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * limbSwingAmount;
		float motion =  Ax * (180F / (float)Math.PI) * this.move_size;
    	stack.pushPose();
    	{
			stack.translate(kneex, kneey, kneez);
			if(entity.isPassenger()&&!entity.stand||entity.sit_aim && entity.getRemain2()!=1) {
				stack.translate(0F, -0.5F, 0.0F);
				stack.mulPose(Axis.XP.rotationDegrees(-90));
				stack.mulPose(Axis.ZP.rotationDegrees(20));
			}else {
				stack.mulPose(Axis.XP.rotationDegrees(motion));
			}
			if(entity.getRemain2()==2 && entity.isAttacking())stack.mulPose(Axis.XP.rotationDegrees(-30F));
			if(entity.isAttacking())stack.mulPose(Axis.XP.rotationDegrees(leg_rotex));
			stack.translate( - kneex,  - kneey, -  kneez);
			obj.renderPart("knee_l");
			
			stack.translate(legx, legy, legz);
			if(motion<0){
				stack.mulPose(Axis.XP.rotationDegrees(-motion));
			}else{
				stack.mulPose(Axis.XP.rotationDegrees(motion));
			}
			if(entity.getRemain2()==2 && entity.isAttacking())stack.mulPose(Axis.XP.rotationDegrees(25F));
			if(entity.isAttacking())stack.mulPose(Axis.XP.rotationDegrees(-leg_rotex));
			stack.translate(- legx, - legy, - legz);
			obj.renderPart("leg_l");
			//this.render_light(entity, "leg_l_light");
		}
		stack.popPose();
		
		stack.pushPose();
		{
			stack.translate(-kneex, kneey, kneez);
			if(entity.isPassenger()&&!entity.stand||entity.sit_aim && entity.getRemain2()!=1) {
				stack.translate(0F, -0.5F, 0.0F);
				stack.mulPose(Axis.XP.rotationDegrees(-90));
				stack.mulPose(Axis.ZP.rotationDegrees(-20));
			}else {
				stack.mulPose(Axis.XP.rotationDegrees(-motion));//
			}
			if(entity.getRemain2()==2 && entity.isAttacking())stack.mulPose(Axis.XP.rotationDegrees(15F));;
			if(entity.isAttacking())stack.mulPose(Axis.XP.rotationDegrees(leg_rotex));
			stack.translate( kneex,  - kneey, -  kneez);
			obj.renderPart("knee_r");
			
			stack.translate(-legx, legy, legz);
			if(motion>0){
				stack.mulPose(Axis.XP.rotationDegrees(motion));
			}else{
				stack.mulPose(Axis.XP.rotationDegrees(-motion));
			}
			if(entity.getRemain2()==2 && entity.isAttacking())stack.mulPose(Axis.XP.rotationDegrees(20F));
			if(entity.isAttacking())stack.mulPose(Axis.XP.rotationDegrees(-leg_rotex));
			stack.translate(legx, - legy, - legz);
			obj.renderPart("leg_r");
			//this.render_light(entity, "leg_r_light");
		}
		stack.popPose();
	}
	float rote;
    private void renderbody(EntitySA_ConscriptX entity, float limbSwing, float limbSwingAmount, float partialTicks, PoseStack stack, MultiBufferSource bufferIn, int packedLight){
		Minecraft minecraft = Minecraft.getInstance();
		stack.pushPose();
    	obj.renderPart("body");
		obj.renderPart("jet");
		obj.renderPart("waist");
		obj.setRender(rtgun2, null, stack, packedLight);
		obj.renderPart("wait1");
		if(entity.getMovePosY()>0){

		}else{
			obj.setRender(rtswardtex, null, stack, packedLight);
			obj.renderPart("wait2");
		}
		obj.setRender(rt, null, stack, packedLight);
		//this.render_light(entity, "body_light");
		stack.popPose();

		float arm_l_rotex = 0;
		float arm_l_rotez = 0;
		float arm_l_rotey = 0;
		float arm_r_rotex = 0;
		float arm_r_rotez = 0;
		float arm_r_rotey = 0;
		boolean turret = false;
		if (entity.getVehicle() instanceof EntityWMSeat && entity.getVehicle() != null) {
			EntityWMSeat vehicle = (EntityWMSeat) entity.getVehicle();
			turret = true;
		}
		boolean move_arm = false;
    	stack.pushPose();
    	{
			float Ax = Mth.cos(limbSwing * 0.6662F) * limbSwingAmount;
			stack.translate(elbowx, elbowy, elbowz);
			if(entity.getRemain2()==2 && entity.getMovePosY()==0 && !(entity.isAttacking()) && !(entity.getRemain1()==0)){
				stack.mulPose(Axis.XP.rotationDegrees(-40));
			}else if(entity.getRemain2()==2 && entity.getMovePosY()==0){
				stack.mulPose(Axis.XP.rotationDegrees(-20F));
			}
			
			if(entity.getMovePosY()>0){
				arm_l_rotex = -15+Ax * (180F / (float)Math.PI) * 1.5F;
			}else if(entity.getRemain1()==0||entity.getRemain2()==2 && !(entity.isAttacking())){
				arm_l_rotex = -20;
				arm_l_rotez = -50;
			}else if(entity.isAttacking() && !(entity.deathTime > 0) && !(entity.getRemain1()==0)) {
				if(entity.getRemain2()!=1)arm_l_rotex = -90 + entity.getXRot();
				arm_l_rotez = -45;
			}else if(entity.isPassenger()) {
				if(entity.getRemain2()!=1)arm_l_rotex = -30;
			}else if(entity.deathTime > 0){
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
			
			stack.translate(- elbowx, - elbowy, - elbowz);
			
			obj.renderPart("elbow_l");
			if(move_arm){
				stack.translate(armx, army, armz);
				if(Ax<0){
					stack.mulPose(Axis.XP.rotationDegrees(arm_l_rotex));
				}else{
					stack.mulPose(Axis.XP.rotationDegrees(-arm_l_rotex));
				}
				stack.mulPose(Axis.ZP.rotationDegrees(arm_l_rotez/2));
				stack.translate(-armx, -army, -armz);
			}
			obj.renderPart("arm_l");
		}
		stack.popPose();
		
		
		stack.pushPose();
		{
			float Ax = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * limbSwingAmount;
			stack.translate(-elbowx, elbowy, elbowz);
			if(entity.getRemain2()==2 && !(entity.isAttacking()) && !(entity.getRemain1()==0)){
				stack.mulPose(Axis.XP.rotationDegrees(-30F));
			}else if(entity.getRemain2()==2){
				stack.mulPose(Axis.XP.rotationDegrees(-20F));
			}

			if(entity.getMovePosY()>0){
				if(entity.attackAnim>0){
					arm_r_rotex = -160 + entity.attackAnim * 20;
					if(entity.getMovePosY()==1){
						
					}else if(entity.getMovePosY()==3){
						arm_r_rotey = -5+entity.attackAnim * 2;
						arm_r_rotez = 50-entity.attackAnim * 10;//
					}else{
						arm_r_rotey = 5-entity.attackAnim * 2;
						arm_r_rotez = -50+entity.attackAnim * 12;//
					}
				}
				//move_arm = true;
			}else if(entity.getRemain1()==0||entity.getRemain2()==2 && !(entity.isAttacking())){
				arm_r_rotez = 10;
				if(entity.getRemain2()!=1)arm_r_rotex = -20;
			}else if(entity.isAttacking() && !(entity.deathTime > 0) && !(entity.getRemain1()==0)) {
				if(entity.anim1<5)stack.mulPose(Axis.XP.rotationDegrees(-entity.anim1));
				if(entity.getRemain2()!=1)arm_r_rotex = -90 + entity.getXRot();
				arm_r_rotez = 10;
			}else if(entity.isPassenger()) {
				if(entity.getRemain2()!=1)arm_r_rotex = -30;
			}else if(entity.deathTime > 0){
				if(entity.getRemain2()!=1)arm_r_rotex = -20;
			}else {
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
			stack.translate(elbowx, - elbowy, - elbowz);
			obj.renderPart("elbow_r");
			if(move_arm){
				stack.translate(-armx, army, armz);
				if(Ax<0){
					stack.mulPose(Axis.XP.rotationDegrees(arm_r_rotex));
				}else{
					stack.mulPose(Axis.XP.rotationDegrees(-arm_r_rotex));
				}
				stack.translate(armx, -army, -armz);
			}
			obj.renderPart("arm_r");

			if(!turret){
				stack.pushPose();
				stack.mulPose(Axis.XP.rotationDegrees(90F));
				if(entity.getMovePosY()>0){
					if(entity.isAttacking()){
						if(rote<2){
							++rote;
						}else{
							rote = 0;
						}
					}
					stack.translate(gun_x, gun_y, gun_z+0.4F);
					obj.setRender(rtswardtex, null, stack, packedLight);
					obj.renderPart("base");
					if(rote==0)obj.renderPart("mat1");
					if(rote==1)obj.renderPart("mat2");
					if(rote==2)obj.renderPart("mat3");
				}else{
					stack.translate(gun_x, gun_y, gun_z);
					obj.setRender(rtgun1, null, stack, packedLight);
					obj.renderPart("gun1");
				}
				stack.popPose();
				if(entity.getMovePosY()==0){
					stack.pushPose();
					stack.mulPose(Axis.XP.rotationDegrees(90F));
					obj.setRender(f1, null, stack, 0xF000F0);
					stack.translate(fire_x, fire_y, fire_z);
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
					
					stack.popPose();
				}
			}
		}
		obj.setRender(rt, null, stack, 0xF000F0);
		stack.popPose();
		this.render_light(entity, "jet_light");
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