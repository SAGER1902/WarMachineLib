package advancearmy.render.soldier;

import com.mojang.math.Axis;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import advancearmy.entity.soldier.EntitySA_Soldier;

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
import net.minecraft.client.resources.model.BakedModel; // IBakedModel
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
/*import com.mrcrayfish.guns.client.GunRenderType;
import com.mrcrayfish.guns.client.render.gun.IOverrideModel;
import com.mrcrayfish.guns.client.render.gun.ModelOverrides;
import com.mrcrayfish.guns.client.util.RenderUtil;
import com.mrcrayfish.guns.common.Gun;
import com.mrcrayfish.guns.event.GunFireEvent;
import com.mrcrayfish.guns.init.ModSyncedDataKeys;
import com.mrcrayfish.guns.item.GrenadeItem;
import com.mrcrayfish.guns.item.GunItem;
import com.mrcrayfish.guns.item.attachment.IAttachment;
import com.mrcrayfish.guns.item.attachment.IBarrel;
import com.mrcrayfish.guns.item.attachment.impl.Barrel;
import com.mrcrayfish.guns.item.attachment.impl.Scope;
import com.mrcrayfish.guns.util.GunEnchantmentHelper;
import com.mrcrayfish.guns.util.GunModifierHelper;
import com.mrcrayfish.guns.util.OptifineHelper;
import com.mrcrayfish.obfuscate.client.event.PlayerModelEvent;
import com.mrcrayfish.obfuscate.client.event.RenderItemEvent;
import com.mrcrayfish.obfuscate.common.data.SyncedPlayerData;
import net.minecraftforge.fml.ModList;
import com.mrcrayfish.guns.client.handler.GunRenderingHandler;*/
@OnlyIn(Dist.CLIENT)
public class RenderSoldier extends MobRenderer<EntitySA_Soldier, ModelNone<EntitySA_Soldier>>
{
	public ResourceLocation tex = ResourceLocation.tryParse("advancearmy:textures/mob/soldier/us_body.png");
    private static final SAObjModel obj = new SAObjModel("advancearmy:textures/mob/soldier/us_body.obj");
	
	public ResourceLocation tex0 = ResourceLocation.tryParse("advancearmy:textures/mob/soldier/us_assult.png");
	private static final SAObjModel obj0 = new SAObjModel("advancearmy:textures/mob/soldier/us_assult.obj");
	
	public ResourceLocation tex1 = ResourceLocation.tryParse("advancearmy:textures/mob/soldier/us_engineer.png");
	private static final SAObjModel obj1 = new SAObjModel("advancearmy:textures/mob/soldier/us_engineer.obj");
	
	public ResourceLocation tex2 = ResourceLocation.tryParse("advancearmy:textures/mob/soldier/us_scout.png");
	private static final SAObjModel obj2 = new SAObjModel("advancearmy:textures/mob/soldier/us_scout.obj");
	
	public ResourceLocation tex3 = ResourceLocation.tryParse("advancearmy:textures/mob/soldier/us_support.png");
	private static final SAObjModel obj3= new SAObjModel("advancearmy:textures/mob/soldier/us_support.obj");
	
	public ResourceLocation tex4 = ResourceLocation.tryParse("advancearmy:textures/mob/soldier/us_medic.png");
	
	private static final SAObjModel tool = new SAObjModel("advancearmy:textures/gun/tool.obj");
	
    private static final SAObjModel gun1 = new SAObjModel("advancearmy:textures/gun/m16a4.obj");
    private static final ResourceLocation gun2 = ResourceLocation.tryParse("advancearmy:textures/gun/m16a4.png");
	private static final ResourceLocation fire_tex = ResourceLocation.tryParse("advancearmy:textures/entity/flash/muzzleflash3.png");
    private static final SAObjModel at4 = new SAObjModel("advancearmy:textures/gun/at4.obj");
    private static final ResourceLocation at4t = ResourceLocation.tryParse("advancearmy:textures/gun/at4.png");
    private static final SAObjModel javelin = new SAObjModel("advancearmy:textures/gun/javelin.obj");
    private static final ResourceLocation javelint = ResourceLocation.tryParse("advancearmy:textures/gun/javelin.png");
	private static final SAObjModel mp5 = new SAObjModel("advancearmy:textures/gun/mp5.obj");
    private static final SAObjModel smaw = new SAObjModel("advancearmy:textures/gun/smaw.obj");
    private static final SAObjModel m16a1 = new SAObjModel("advancearmy:textures/gun/m16a1.obj");
	private static final SAObjModel l96a1 = new SAObjModel("advancearmy:textures/gun/l96a1.obj");
	private static final SAObjModel m24 = new SAObjModel("advancearmy:textures/gun/m24.obj");	
	private static final SAObjModel m9 = new SAObjModel("advancearmy:textures/gun/m9.obj");
	private static final SAObjModel m60 = new SAObjModel("advancearmy:textures/gun/m60.obj");
	private static final SAObjModel shotgun = new SAObjModel("advancearmy:textures/gun/shotgun.obj");
    private static final ResourceLocation gunt = ResourceLocation.tryParse("advancearmy:textures/gun/gun.png");

	RenderType rt = RenderTypeVehicle.objrender(tex);
	RenderType rt0 = RenderTypeVehicle.objrender(tex0);
	RenderType rt1 = RenderTypeVehicle.objrender(tex1);
	RenderType rt2 = RenderTypeVehicle.objrender(tex2);
	RenderType rt3 = RenderTypeVehicle.objrender(tex3);
	RenderType rt4 = RenderTypeVehicle.objrender(tex4);
	RenderType rtm16 = RenderTypeVehicle.objrender(gun2);
	RenderType rtat4 = RenderTypeVehicle.objrender(at4t);
	RenderType rtjavelint = RenderTypeVehicle.objrender(javelint);
	RenderType rtgunt = RenderTypeVehicle.objrender(gunt);
	RenderType f1 = SARenderState.getBlendDepthWrite(fire_tex);
	
    private void render_part(EntitySA_Soldier entity, String name) {
        obj.renderPart(name);
        if (entity.soldierType == 1){
			obj1.setRenderType(rt1);
			obj1.renderPart(name);
        }else if (entity.soldierType == 2){
			obj2.setRenderType(rt2);
			obj2.renderPart(name);
        }else if (entity.soldierType == 3){
			obj3.setRenderType(rt3);
			obj3.renderPart(name);
        }else if (entity.soldierType == 4){
			obj0.setRenderType(rt4);
			obj0.renderPart(name);
        }else{
			obj0.setRenderType(rt0);
			obj0.renderPart(name);
		}
		obj.setRenderType(rt);
    }
	
    public RenderSoldier(EntityRendererProvider.Context context)
    {
    	super(context, new ModelNone(),0.5F);
    }

    public ResourceLocation getTextureLocation(EntitySA_Soldier entity)
    {
		return tex;
    }
	
    float iii;
    public void render(EntitySA_Soldier entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight0)
    {
		super.render(entity, entityYaw, partialTicks, stack, buffer, packedLight0);
		if(entity.hide)return;
		
		int packedLight=packedLight0;
		//if(entity.anim1<3)packedLight=0xF000F0;
		
		stack.pushPose();
    	float limbSwing = this.F6(entity, partialTicks);
		float limbSwingAmount = this.F5(entity, partialTicks);
		
		stack.mulPose(Axis.YP.rotationDegrees(180.0F));
		obj0.setRender(rt0, null, stack, packedLight);
		obj1.setRender(rt1, null, stack, packedLight);
		obj2.setRender(rt2, null, stack, packedLight);
		obj3.setRender(rt3, null, stack, packedLight);
		obj.setRender(rt, null, stack, packedLight);
		
		if(entity.getVehicle()!=null)stack.scale(0.8F, 0.8F, 0.8F);
		
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
			render_part(entity,"head");
			stack.popPose();
		}
		
		{
			stack.pushPose();
			this.renderbody(entity, limbSwing, limbSwingAmount, partialTicks, stack, buffer, packedLight);
			stack.popPose();
		}
		stack.popPose();
    }
	
	
	
    public float move_size = 1F;//
    private void renderlegs(PoseStack stack, EntitySA_Soldier entity, float limbSwing, float limbSwingAmount, float partialTicks){
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
			render_part(entity,"knee_l");
			
			stack.translate(0.15F, 0.4F, 0.05F);
			if(motion<0){
				stack.mulPose(Axis.XP.rotationDegrees(-motion));
			}else{
				stack.mulPose(Axis.XP.rotationDegrees(motion));
			}
			if(entity.getRemain2()==2 && entity.isAttacking())stack.mulPose(Axis.XP.rotationDegrees(25F));
			stack.translate(-0.15F, -0.4F, -0.05F);
			render_part(entity,"leg_l");
			//this.render_light(entity, "leg_l_light");
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
			render_part(entity,"knee_r");
			
			stack.translate(-0.15F, 0.4F, 0.05F);
			if(motion>0){
				stack.mulPose(Axis.XP.rotationDegrees(motion));
			}else{
				stack.mulPose(Axis.XP.rotationDegrees(-motion));
			}
			if(entity.getRemain2()==2 && entity.isAttacking())stack.mulPose(Axis.XP.rotationDegrees(20F));
			stack.translate(0.15F, -0.4F, -0.05F);
			render_part(entity,"leg_r");
			//this.render_light(entity, "leg_r_light");
		}
		stack.popPose();
	}
    
    private void renderbody(EntitySA_Soldier entity, float limbSwing, float limbSwingAmount, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight){
		Minecraft minecraft = Minecraft.getInstance();
		stack.pushPose();
    	render_part(entity,"body");
		SAObjModel wait_model = gun1;
		wait_model.setRender(rtm16, null, stack, packedLight);
		if(entity.getWeaponId()==14){
			wait_model = smaw;
			wait_model.setRender(rtgunt, null, stack, packedLight);
		}
		if(entity.getWeaponId()==2){
			wait_model = shotgun;
			wait_model.setRender(rtgunt, null, stack, packedLight);
		}
		if(entity.getWeaponId()==7){
			wait_model = m16a1;
			wait_model.setRender(rtgunt, null, stack, packedLight);
		}
		if(entity.getWeaponId()==13){
			wait_model = l96a1;
			wait_model.setRender(rtgunt, null, stack, packedLight);
		}
		if(entity.getWeaponId()==4){
			wait_model = m24;
			wait_model.setRender(rtgunt, null, stack, packedLight);
		}
		if(entity.getWeaponId()==3||entity.getWeaponId()==6){
			wait_model = m9;
			wait_model.setRender(rtgunt, null, stack, packedLight);
		}
		if(entity.getWeaponId()==8){
			wait_model = at4;
			wait_model.setRender(rtat4, null, stack, packedLight);
		}
		if(entity.getWeaponId()==9){
			wait_model = javelin;
			wait_model.setRender(rtjavelint, null, stack, packedLight);
		}
		if(entity.getWeaponId()==10){
			wait_model = mp5;
			wait_model.setRender(rtgunt, null, stack, packedLight);
		}
		if(entity.mainWeaponId!=0){
			stack.pushPose();
			stack.scale(0.1875F, 0.1875F, 0.1875F);
			if(entity.getWeaponId()==3||entity.getWeaponId()==6){//pistol
				stack.mulPose(Axis.XP.rotationDegrees(90F));
				stack.translate(-1.5F, 0 , -4F);//x,z,y
			}else if(entity.getWeaponId()==4||entity.getWeaponId()==13){//sniper
				stack.mulPose(Axis.XP.rotationDegrees(-92F));
				stack.mulPose(Axis.YP.rotationDegrees(28F));
				stack.mulPose(Axis.ZP.rotationDegrees(90F));
				stack.translate(1.2F, 1.8F, 3F);//x,z,y
			}else if(entity.getWeaponId()==9||entity.getWeaponId()==14||entity.getWeaponId()==8){//rpg
				stack.mulPose(Axis.XP.rotationDegrees(-92F));
				stack.mulPose(Axis.ZP.rotationDegrees(180F));
				stack.translate(-1.2F, -2.2F, 9F);//x,z,y
			}else{//ar
				stack.mulPose(Axis.XP.rotationDegrees(-92F));
				stack.mulPose(Axis.ZP.rotationDegrees(180F));
				stack.translate(1.2F, -2.2F, 6F);//x,z,y
			}
			/*RenderRote.setRote(entity.roteXanim, 1.0F, 0.0F, 0.0F);
			RenderRote.setRote(entity.roteYanim, 0.0F, 1.0F, 0.0F);
			RenderRote.setRote(entity.roteZanim, 0.0F, 0.0F, 1.0F);
			stack.translate(entity.posXanim, entity.posYanim, entity.posZanim);*///x,z,y
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
			
			render_part(entity,"elbow_l");
			if(move_arm){
				stack.translate(0.37F, 1.12F, -0.07F);
				if(Ax<0){
					stack.mulPose(Axis.XP.rotationDegrees(arm_l_rotex));
				}else{
					stack.mulPose(Axis.XP.rotationDegrees(-arm_l_rotex));
				}
				stack.translate(-0.37F, -1.12F, 0.07F);
			}
			render_part(entity,"arm_l");

			//this.render_light(entity, "arm_l_light");
			//render_color(entity, "color2");
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
				arm_r_rotex = -20;
			}else if(entity.isAttacking() && !(entity.deathTime > 0) && !(entity.getRemain1()==0)) {
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
			
			if(entity.attackAnim>0){
				arm_r_rotex = -160 + entity.attackAnim * 30;
				move_arm = false;
			}
			if(entity.getRemain2()==1 && entity.attackAnim<=0){//
				stack.mulPose(Axis.XP.rotationDegrees(arm_r_rotex+180F));
			}else{
				stack.mulPose(Axis.XP.rotationDegrees(arm_r_rotex));
			}
			stack.mulPose(Axis.YP.rotationDegrees(arm_r_rotey));
			stack.mulPose(Axis.ZP.rotationDegrees(arm_r_rotez));
			
			stack.translate(0.37F, -1.375F, 0.0F);
//			stack.pushPose();
			render_part(entity,"elbow_r");
			
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
			render_part(entity,"arm_r");
			//this.render_light(entity, "arm_r_light");
			//render_color(entity, "color1");
			//ItemStack heldItem = entity.getMainHandItem();
			
			if(entity.special_cool<50){
				tool.setRender(rtgunt, null, stack, packedLight);
				if (entity.soldierType == 1){
					tool.renderPart("wrench");
				}else if (entity.soldierType == 2){
					tool.renderPart("mine1_h");
				}else if (entity.soldierType == 3){
					tool.renderPart("gr");
				}else if (entity.soldierType == 4){
					tool.renderPart("medic");
				}else{
					tool.renderPart("gr");
				}
			}else
			if(entity.canfire){
				/*if(heldItem.isEmpty())*/{
					SAObjModel gun_model = gun1;
					gun_model.setRender(rtm16, null, stack, packedLight);
					if(entity.getWeaponId()==2){
						gun_model = smaw;
						gun_model.setRender(rtgunt, null, stack, packedLight);
					}
					if(entity.getWeaponId()==3){
						gun_model = l96a1;
						gun_model.setRender(rtgunt, null, stack, packedLight);
					}
					if(entity.getWeaponId()==4||entity.getWeaponId()==13){
						gun_model = m9;
						gun_model.setRender(rtgunt, null, stack, packedLight);
					}
					if(entity.getWeaponId()==5){
						gun_model = m60;
						gun_model.setRender(rtgunt, null, stack, packedLight);
					}
					if(entity.getWeaponId()==6){
						gun_model = m24;
						gun_model.setRender(rtgunt, null, stack, packedLight);
					}
					if(entity.getWeaponId()==7){
						gun_model = at4;
						gun_model.setRender(rtat4, null, stack, packedLight);
					}
					if(entity.getWeaponId()==8){
						gun_model = m16a1;
						gun_model.setRender(rtgunt, null, stack, packedLight);
					}
					if(entity.getWeaponId()==9){
						gun_model = mp5;
						gun_model.setRender(rtgunt, null, stack, packedLight);
					}
					if(entity.getWeaponId()==10){
						gun_model = javelin;
						gun_model.setRender(rtjavelint, null, stack, packedLight);
					}
					if(entity.getWeaponId()==14){
						gun_model = shotgun;
						gun_model.setRender(rtgunt, null, stack, packedLight);
					}
					stack.pushPose();
					stack.scale(0.1875F, 0.1875F, 0.1875F);
					if(!entity.isAttacking() && entity.getRemain2()!=2&&entity.getWeaponId()!=4&&entity.getWeaponId()!=13 && entity.getWeaponId()!=10&& entity.getWeaponId()!=2&& entity.getWeaponId()!=7&&entity.getWeaponId()!=9){
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
					gun_model.renderPart("mat25");
					gun_model.renderPart("mat31");
					stack.popPose();
					stack.pushPose();
					stack.mulPose(Axis.XP.rotationDegrees(90F));
					
					obj.setRender(f1, null, stack, 0xF000F0);
					
					stack.translate(-entity.fireposX, 0.23F, entity.fireposZ);
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
					//tex---(tex);
					stack.popPose();
				}/*else if(ModList.get().isLoaded("cgm")){
					if(heldItem.getItem() instanceof GunItem && (Gun.hasAmmo(heldItem))){
						GunItem item = (GunItem) heldItem.getItem();
						Gun modifiedGun = item.getModifiedGun(heldItem);
						if(modifiedGun != null)
						{
							stack.pushPose();
							stack.pushPose();
							if(entity.getRemain2()==1){
								stack.translate(0F, 0.15F, -1.5F);
								stack.mulPose(Vector3f.XP.rotationDegrees(90F));
							}
							
							stack.mulPose(Vector3f.YP.rotationDegrees(180.0F - entity.yBodyRot));
							
							stack.translate(0.37F, 1.375F, 0.0F);
							
							if(entity.anim1<5){
								stack.mulPose(Vector3f.XP.rotationDegrees(entity.anim1));
							}
							
							if(entity.getRemain2()==1){//
								stack.mulPose(Vector3f.XP.rotationDegrees(-arm_r_rotex+180F));
							}else{
								stack.mulPose(Vector3f.XP.rotationDegrees(-arm_r_rotex));
							}
							stack.mulPose(Vector3f.YP.rotationDegrees(arm_r_rotey));
							stack.mulPose(Vector3f.ZP.rotationDegrees(-arm_r_rotez));
							stack.translate(-0.37F, -1.375F, 0.0F);
							
							stack.pushPose();//
							stack.mulPose(Vector3f.XP.rotationDegrees(-90F));
							stack.translate(0.37F, 0.5F, 0.5F);
							boolean right = true;
							ItemTransforms.TransformType transformType = right ? ItemTransforms.TransformType.FIRST_PERSON_RIGHT_HAND : ItemTransforms.TransformType.FIRST_PERSON_LEFT_HAND;
							if(ModelOverrides.hasModel(heldItem))
							{
								IOverrideModel model = ModelOverrides.getModel(heldItem);
								if(model != null)
								{
									model.render(partialTicks, transformType, heldItem, ItemStack.EMPTY, entity, stack, buffer, 240, OverlayTexture.NO_OVERLAY);
								}
							}else
							{
								IBakedModel bakedModel = minecraft.getItemRenderer().getItemModelShaper().getItemModel(heldItem);
								minecraft.getItemRenderer().render(heldItem, ItemTransforms.TransformType.NONE, false, stack, buffer, 240, OverlayTexture.NO_OVERLAY, bakedModel);
							}
							stack.popPose();//
							
							stack.popPose();
							stack.popPose();
							
							stack.pushPose();
							//GL11.glColor4f(1F, 1F, 1F, 1F);
							stack.mulPose(Axis.XP.rotationDegrees(90F));
							
							//tex---(fire_tex);
							
							stack.translate(-0.36F, 0.23F, 0.5F);
							float size = entity.level().random.nextInt(3) * 0.25F + 1;
							stack.scale(size, size, size);
							if(entity.anim1<3 && entity.getRemain1() > 0 && entity.isAttacking())
							{
								if(entity.level().random.nextInt(6)<2){
									this.render_light(entity, "mat_1");
									//obj.renderPart("mat_1");
								}else if(entity.level().random.nextInt(6)<4){
									this.render_light(entity, "mat_2");
									//obj.renderPart("mat_2");
								}else{
									this.render_light(entity, "mat_3");
									//obj.renderPart("mat_3");
								}
							}
							
							//tex---(tex);
							stack.popPose();
						}
					}
				}*/
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