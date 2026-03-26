package advancearmy.render.mob;
import advancearmy.entity.mob.ERO_Giant;
import com.mojang.math.Axis;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
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

import advancearmy.render.ModelNone;
@OnlyIn(Dist.CLIENT)
public class RenderGiant extends MobRenderer<ERO_Giant, ModelNone<ERO_Giant>>
{
	public ResourceLocation tex = ResourceLocation.tryParse("advancearmy:textures/mob/ero/evil_zombie.png");
    private static final SAObjModel obj = new SAObjModel("advancearmy:textures/mob/ero/giant.obj");
	
    /*private static final SAObjModel ak = new SAObjModel("advancearmy:textures/gun/ak47.obj");
    private static final ResourceLocation akt = ResourceLocation.tryParse("advancearmy:textures/gun/ak47.png");
    private static final SAObjModel rpg = new SAObjModel("advancearmy:textures/gun/rpg.obj");
    private static final ResourceLocation rpgt = ResourceLocation.tryParse("advancearmy:textures/gun/rpg.png");
	private static final SAObjModel svd = new SAObjModel("advancearmy:textures/gun/svd.obj");
	private static final SAObjModel m9 = new SAObjModel("advancearmy:textures/gun/m9.obj");
	private static final SAObjModel pkm = new SAObjModel("advancearmy:textures/gun/pkm.obj");*/
    private static final ResourceLocation gunt = ResourceLocation.tryParse("advancearmy:textures/gun/gun.png");
	private static final SAObjModel tool = new SAObjModel("advancearmy:textures/gun/tool.obj");
	//private static final ResourceLocation fire_tex = ResourceLocation.tryParse("advancearmy:textures/entity/flash/muzzleflash3.png");
	
	private static final ResourceLocation gun = ResourceLocation.tryParse("advancearmy:textures/gun/gun.png");
	private static final ResourceLocation laser_tex = ResourceLocation.tryParse("advancearmy:textures/entity/flash/aa_beam.png");
	private static final ResourceLocation wave_tex = ResourceLocation.tryParse("advancearmy:textures/entity/flash/sward_wave3.png");
	RenderType wave = SARenderState.getBlendDepthWrite(wave_tex);
	
	RenderType rt = RenderTypeVehicle.objrender(tex);
	RenderType rtarmor = RenderTypeVehicle.objrender(gun);
	RenderType glow = SARenderState.getBlendDepthWrite(tex);
	RenderType laser = SARenderState.getBlendDepthWrite(laser_tex);
	RenderType rtgunt = RenderTypeVehicle.objrender(gunt);
	//RenderType f1 = SARenderState.getBlendDepthWrite(fire_tex);
	
    public RenderGiant(EntityRendererProvider.Context context)
    {
    	super(context, new ModelNone(),5F);
    }
    public boolean shouldRender(ERO_Giant entity, Frustum camera, double camX, double camY, double camZ) {
        return super.shouldRender(entity, camera, camX, camY, camZ)||entity.distanceToSqr(camX,camY,camZ)<20;
    }
    public ResourceLocation getTextureLocation(ERO_Giant entity)
    {
		return tex;
    }
	int iii;
    public void render(ERO_Giant entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource bufferIn, int packedLight)
    {
		super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLight);
		{
	    stack.pushPose();
		stack.scale(10F,10F,10F);
    	float limbSwing = this.F6(entity, partialTicks);
		float limbSwingAmount = this.F5(entity, partialTicks);
		
		if(iii<360){
			++iii;
		}else{
			iii=0;
		}
		
		Minecraft mc = Minecraft.getInstance();
		obj.setRender(rt, null, stack, packedLight);
		stack.mulPose(Axis.YP.rotationDegrees(180.0F));
		stack.mulPose(Axis.YP.rotationDegrees(180.0F - (entity.yBodyRotO + (entity.yBodyRot - entity.yBodyRotO) * partialTicks)));
		
		if(entity.getHealth()==0){
			int dt = entity.deathTime;
			if(dt>25)dt=25;
			stack.translate(0F, 0.15F, -0.2F);
			stack.mulPose(Axis.XP.rotationDegrees(-90F*dt/25F));
		}
		
		{
			stack.pushPose();
			this.renderlegs(stack, entity, limbSwing, limbSwingAmount, partialTicks);
			stack.popPose();
		}
		
		float Ax1 = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * limbSwingAmount;//
		stack.translate( 0, Ax1 * (180F / (float)Math.PI) * 0.001F, 0);//
    	{
    		stack.translate(0F, 0.75F, 0.0F);
			float body_rotex = 0;
			float body_rotey = 0;
			{
				float size = 1F;
				if(entity.getXRot()>0)size=1+entity.getXRot()/90F;
				body_rotex = entity.attackAnim * 3 *size;
				if(entity.getAttackType()==1){
					body_rotey = entity.attackAnim * 2;
				}
			}
			stack.mulPose(Axis.XP.rotationDegrees(-Ax1 * (180F / (float)Math.PI) * 0.05F));
			stack.mulPose(Axis.XP.rotationDegrees(body_rotex));
			stack.mulPose(Axis.YP.rotationDegrees(body_rotey));
			if(entity.anim1<5)stack.mulPose(Axis.XP.rotationDegrees(-entity.anim1));
    		stack.translate(0F, -0.75F, 0.0F);
    	}
		{
			stack.pushPose();
			stack.translate(0F, 1.5F, 0.0F);
			stack.mulPose(Axis.YP.rotationDegrees(180.0F - (entity.yHeadRotO + (entity.yHeadRot - entity.yHeadRotO) * partialTicks) -(180.0F - (entity.yBodyRotO + (entity.yBodyRot - entity.yBodyRotO) * partialTicks))));
			stack.mulPose(Axis.XP.rotationDegrees(entity.getXRot()));
			stack.translate(0F, -1.5F, 0.0F);
			obj.renderPart("head");
			obj.setRenderType(rtarmor);
			if(entity.getArmorC()>250)obj.renderPart("h");
			obj.setRender(glow, null, stack, 0xF000F0);
			float size1 = 1;
			if(entity.lasercool>10)size1 = 1+partialTicks*0.5F;
			stack.pushPose();
			stack.translate(0.1F, 1.7F, 0.25F);
			stack.scale(size1,size1,size1);
			stack.translate(-0.1F, -1.7F, -0.25F);
			obj.renderPart("eyel");
			stack.popPose();
			
			stack.pushPose();
			stack.translate(-0.1F, 1.7F, 0.25F);
			stack.scale(size1,size1,size1);
			stack.translate(0.1F, -1.7F, -0.25F);
			obj.renderPart("eyer");
			stack.popPose();
			/*if(entity.getAttackType()==5)*/{
				if(entity.lasettime > 0){
					stack.pushPose();//glstart
					stack.translate( 0, -Ax1 * (180F / (float)Math.PI) * 0.001F, 0);//
					obj.setRender(laser, null, stack, 0xF000F0);
					float size = partialTicks * 0.6F+0.8F;
					stack.translate(0.1F, 1.7F, 0.25F);
					stack.pushPose();//glstart
					stack.translate(0F, 0F, 0F);
					stack.mulPose(Axis.ZP.rotationDegrees(iii * 60F));
					stack.scale(size, size, entity.getRange()*0.33F);
					stack.translate(0F, 0F, 0F);
					obj.renderPart("laser");
					stack.popPose();//glend
					stack.pushPose();//glstart
					stack.translate(0F, 0F, 0F);
					stack.mulPose(Axis.ZP.rotationDegrees(-iii * 60F));
					stack.scale(size, size, size);
					stack.translate(0F, 0F, 0F);
					obj.renderPart("laser1");
					stack.popPose();//glend
					stack.translate(0F, 0F, entity.getRange()*0.1F);
					stack.pushPose();//glstart
					stack.translate(0F, 0F, 0F);
					stack.mulPose(Axis.YP.rotationDegrees(iii * 60F));
					stack.scale(size, size, size);
					stack.translate(0F, 0F, 0F);
					obj.renderPart("laser2");
					stack.popPose();//glend
					stack.popPose();//glend
					
					stack.pushPose();//glstart
					stack.translate( 0, -Ax1 * (180F / (float)Math.PI) * 0.001F, 0);//
					obj.setRender(laser, null, stack, 0xF000F0);
					stack.translate(-0.1F, 1.7F, 0.25F);
					stack.pushPose();//glstart
					stack.translate(0F, 0F, 0F);
					stack.mulPose(Axis.ZP.rotationDegrees(iii * 60F));
					stack.scale(size, size, entity.getRange()*0.33F);
					stack.translate(0F, 0F, 0F);
					obj.renderPart("laser");
					stack.popPose();//glend
					stack.pushPose();//glstart
					stack.translate(0F, 0F, 0F);
					stack.mulPose(Axis.ZP.rotationDegrees(-iii * 60F));
					stack.scale(size, size, size);
					stack.translate(0F, 0F, 0F);
					obj.renderPart("laser1");
					stack.popPose();//glend
					stack.translate(0F, 0F, entity.getRange()*0.1F);
					stack.pushPose();//glstart
					stack.translate(0F, 0F, 0F);
					stack.mulPose(Axis.YP.rotationDegrees(iii * 60F));
					stack.scale(size, size, size);
					stack.translate(0F, 0F, 0F);
					obj.renderPart("laser2");
					stack.popPose();//glend
					stack.popPose();//glend
				}
			}
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
    private void renderlegs(PoseStack stack, ERO_Giant entity, float limbSwing, float limbSwingAmount, float partialTicks){
		float Ax = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * limbSwingAmount;
		float motion =  Ax * (180F / (float)Math.PI) * this.move_size;
    	stack.pushPose();
    	{
			stack.translate(0.15F, 0.625F, 0.0F);
			stack.mulPose(Axis.XP.rotationDegrees(motion));
			stack.translate(-0.15F, -0.625F, 0.0F);
			obj.renderPart("knee_l");
			obj.setRenderType(rtarmor);
			if(entity.getArmorC()>500)obj.renderPart("kl");
			obj.setRenderType(rt);
			stack.translate(0.15F, 0.4F, 0.05F);
			if(motion<0){
				stack.mulPose(Axis.XP.rotationDegrees(-motion));
			}else{
				stack.mulPose(Axis.XP.rotationDegrees(motion));
			}
			stack.translate(-0.15F, -0.4F, -0.05F);
			obj.renderPart("leg_l");
			obj.setRenderType(rtarmor);
			if(entity.getArmorC()>800)obj.renderPart("ll");
			obj.setRenderType(rt);
		}
		stack.popPose();
		
		stack.pushPose();
		{
			stack.translate(-0.15F, 0.625F, 0.0F);
			stack.mulPose(Axis.XP.rotationDegrees(-motion));//
			stack.translate(0.15F, -0.625F, 0.0F);
			obj.renderPart("knee_r");
			obj.setRenderType(rtarmor);
			if(entity.getArmorC()>550)obj.renderPart("kr");
			obj.setRenderType(rt);
			stack.translate(-0.15F, 0.4F, 0.05F);
			if(motion>0){
				stack.mulPose(Axis.XP.rotationDegrees(motion));
			}else{
				stack.mulPose(Axis.XP.rotationDegrees(-motion));
			}
			stack.translate(0.15F, -0.4F, -0.05F);
			obj.renderPart("leg_r");
			obj.setRenderType(rtarmor);
			if(entity.getArmorC()>700)obj.renderPart("lr");
			obj.setRenderType(rt);
		}
		stack.popPose();
	}
    
    private void renderbody(ERO_Giant entity, float limbSwing, float limbSwingAmount, float partialTicks, PoseStack stack, MultiBufferSource bufferIn, int packedLight){
		Minecraft minecraft = Minecraft.getInstance();
		stack.pushPose();
    	obj.renderPart("body");
		obj.setRenderType(rtarmor);
		if(entity.getArmorC()>0)obj.renderPart("b");
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
			if(entity.getAttackType()>0){
				arm_l_rotex = -15+Ax * (180F / (float)Math.PI) * 1.5F;
			}else if(entity.deathTime > 0){
				arm_l_rotex = -30;
				arm_l_rotez = -40;
			}else {
				arm_l_rotex = Ax * (180F / (float)Math.PI) * 1.5F;
				move_arm = true;
			}
			
			stack.mulPose(Axis.XP.rotationDegrees(arm_l_rotex));
			stack.mulPose(Axis.YP.rotationDegrees(arm_l_rotey));
			stack.mulPose(Axis.ZP.rotationDegrees(arm_l_rotez));
			
			stack.translate(-0.37F, -1.375F, 0.0F);
			
			obj.renderPart("elbow_l");
			obj.setRenderType(rtarmor);
			if(entity.getArmorC()>0)obj.renderPart("al");
			obj.setRenderType(rt);
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
			if(entity.getAttackType()>0){
				/*if(entity.attackAnim>0)*/{
					arm_r_rotex = entity.attackAnim * 12;
					if(entity.getAttackType()==1){
						arm_r_rotey = entity.attackAnim * 2;
						//arm_r_rotez = entity.attackAnim * 12;//
					}/*else if(entity.getAttackType()==3){
						arm_r_rotey = -5+entity.attackAnim * 2;
						arm_r_rotez = 50-entity.attackAnim * 10;//
					}else{

					}*/
				}
			}else if(entity.deathTime > 0){
				arm_r_rotex = -20;
			}else {
				arm_r_rotex = Ax * (180F / (float)Math.PI)*0.8F;
				move_arm = true;
			}
			
			stack.mulPose(Axis.XP.rotationDegrees(arm_r_rotex));
			stack.mulPose(Axis.YP.rotationDegrees(arm_r_rotey));
			stack.mulPose(Axis.ZP.rotationDegrees(arm_r_rotez));
			stack.translate(0.37F, -1.375F, 0.0F);

			obj.renderPart("elbow_r");
			obj.setRenderType(rtarmor);
			if(entity.getArmorC()>0)obj.renderPart("ar");
			obj.setRenderType(rt);
			
			stack.translate(-0.37F, 1.12F, -0.07F);
			if(move_arm){
				if(Ax<0){
					stack.mulPose(Axis.XP.rotationDegrees(arm_r_rotex));
				}else{
					stack.mulPose(Axis.XP.rotationDegrees(-arm_r_rotex));
				}
			}
			/*if(entity.isAggressive())*/stack.mulPose(Axis.XP.rotationDegrees(-15));
			stack.translate(0.37F, -1.12F, 0.07F);
			obj.renderPart("arm_r");
			{
				tool.setRender(rtgunt, null, stack, packedLight);
				tool.renderPart("weapon1");
			}
		}
		stack.popPose();
	}
	
    public float F6(LivingEntity entity, float partialTicks){
 		float f6 = 0;
        {
            f6 = entity.walkAnimation.position() - entity.walkAnimation.speed() * (1.0F - partialTicks);
        }
 		return f6;
 	}
 	public float F5(LivingEntity entity, float partialTicks){
 		float f5 = 0;
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