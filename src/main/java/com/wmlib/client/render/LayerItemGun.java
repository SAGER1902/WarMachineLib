package wmlib.client.render;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import wmlib.WarMachineLib;
import wmlib.common.item.ItemGun;
import net.minecraft.client.Minecraft;
//import net.minecraft.client.model.ModelBiped;
//import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
//import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.HandSide;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.entity.player.PlayerEntity;

import wmlib.client.RenderParameters;
import static wmlib.client.RenderParameters.*;


import net.minecraft.nbt.CompoundNBT;

import wmlib.client.obj.SAObjModel;
/*import wmlib.client.gltf.SAEnhancedModel;
import mchhui.hegltfsa.GltfRenderModel.NodeAnimationBlender;*/
import wmlib.client.render.SARenderHelper;
import wmlib.client.render.SARenderHelper.RenderTypeSA;

import net.minecraft.client.renderer.entity.IEntityRenderer;

//import com.modularwarfare.api.RenderHeldItemLayerEvent;
import net.minecraftforge.common.MinecraftForge;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;

import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.IHasArm;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.entity.Entity;
@OnlyIn(Dist.CLIENT)
public class LayerItemGun<T extends LivingEntity, M extends EntityModel<T> & IHasArm> extends HeldItemLayer<T, M> {
//public class LayerItemGun<T extends LivingEntity, M extends EntityModel<T>> extends LayerRenderer<T, M> {
    public LayerItemGun(IEntityRenderer<T, M> entityRendererIn) {
        super(entityRendererIn);
    }
    //protected final RenderLivingBase<?> livingEntityRenderer;
    /*public LayerItemGun(RenderLivingBase<?> livingEntityRendererIn)
    {
    	super(livingEntityRendererIn);
        this.livingEntityRenderer = livingEntityRendererIn;
    }*/
    /*public LayerItemGun(RenderLivingBase<?> livingEntityRendererIn) {
        super(livingEntityRendererIn);
    }*/
	
	/*float amination_size = WarMachineLib.wmlibsa_amination_size*0.8F;
	boolean use_amination = WarMachineLib.wmlibsa_amination;*/
	float amination_size = 1;
	boolean use_amination = true;
	float trans = 0.0166931264864865F;
	float iii = 200F;
	float speed_tick = 0.5F;
	public boolean recoiled = false;
	public int cockingtime = 0;
	public float cock = 0;
	
   public void render(MatrixStack stack, IRenderTypeBuffer buf, int light, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
      boolean flag = entity.getMainArm() == HandSide.RIGHT;
      ItemStack itemstack = flag ? entity.getOffhandItem() : entity.getMainHandItem();
      ItemStack itemstack1 = flag ? entity.getMainHandItem() : entity.getOffhandItem();
      if (!itemstack.isEmpty() || !itemstack1.isEmpty()) {
        stack.pushPose();
		GL11.glPushMatrix();
		Minecraft mc = Minecraft.getInstance();
		ActiveRenderInfo activeRenderInfoIn = Minecraft.getInstance().getEntityRenderDispatcher().camera;
		activeRenderInfoIn.setup(mc.level, (Entity)(mc.getCameraEntity() == null ? mc.player : mc.getCameraEntity()), 
		!mc.options.getCameraType().isFirstPerson(), mc.options.getCameraType().isMirrored(), partialTicks);
		Vector3d avector3d = activeRenderInfoIn.getPosition();
		double camx = avector3d.x();
		double camy = avector3d.y();
		double camz = avector3d.z();
		double d0 = MathHelper.lerp((double)partialTicks, entity.xOld, entity.getX());
		double d1 = MathHelper.lerp((double)partialTicks, entity.yOld, entity.getY());
		double d2 = MathHelper.lerp((double)partialTicks, entity.zOld, entity.getZ());
		double xIn = d0 - camx;
		double yIn = d1 - camy;
		double zIn = d2 - camz;
    	//Minecraft.getInstance().getTextureManager().bind(tex);
		net.minecraftforge.client.event.EntityViewRenderEvent.CameraSetup cameraSetup = net.minecraftforge.client.ForgeHooksClient.onCameraSetup(mc.gameRenderer, activeRenderInfoIn, partialTicks);
		activeRenderInfoIn.setAnglesInternal(cameraSetup.getYaw(), cameraSetup.getPitch());
		GL11.glRotatef(cameraSetup.getRoll(), 0.0F, 0.0F, 1.0F);
		GL11.glRotatef(activeRenderInfoIn.getXRot(), 1.0F, 0.0F, 0.0F);
		GL11.glRotatef(activeRenderInfoIn.getYRot() + 180.0F, 0.0F, 1.0F, 0.0F);
		GL11.glTranslatef((float) xIn, (float) yIn, (float) zIn);
		 
		CompoundNBT nbt = itemstack1.getTag();
		//CompoundNBT nbt2 = itemstack.getTagCompound();
		if(nbt!=null){
			recoiled = nbt.getBoolean("Recoiled");
			cockingtime = nbt.getInt("CockingTime");
			//cock=cockRecoil;
		}
	 
		if(iii<250F){
			iii=iii+speed_tick;
		}else{
			iii=0;
		}
		 
		this.renderHeldItem(entity, itemstack1, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, HandSide.RIGHT);
		//this.renderHeldItem(entity, itemstack, ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, HandSide.LEFT);
		 GL11.glPopMatrix();
         stack.popPose();
      }
   }
	
    private void renderHeldItem(LivingEntity useliving, ItemStack itemstack, ItemCameraTransforms.TransformType p_188358_3_, HandSide handSide)
    {
        if (!itemstack.isEmpty())
        {
			////System.out.println(String.format("%1$3d", 3));
            GL11.glPushMatrix();
            if (!itemstack.isEmpty() && itemstack.getItem() instanceof ItemGun) {//item
				ItemGun gun = (ItemGun) itemstack.getItem();
				//if(!gun.use_gltf)gun.ModelLoad();
				boolean isreload = false;
				
				if (useliving.isCrouching())
				{
					GL11.glTranslatef(0.0F, 0.2F, 0.0F);
				}
				// Forge: moved this call down, fixes incorrect offset while sneaking.
				/*ModelBiped model = (ModelBiped)this.livingEntityRenderer.getMainModel();
				model.bipedLeftArm.isHidden = false;
				model.bipedRightLeg.isHidden = false;
				((ModelBiped)this.livingEntityRenderer.getMainModel()).postRenderArm(0.0625F, handSide);*/
				GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
				
				{
					GL11.glScalef(0.1475F, 0.1475F, 0.1475F);
				}
				
				boolean flag = handSide == HandSide.LEFT;
				GL11.glTranslatef((float)((flag ? -1 : 1) / 16.0F) * -5.33F, 0.125F * 1.33F, -0.625F * -4.5F);//-5.33,-3.33,-4.5
				//System.out.println(String.format("%1$3d", 4));
				/*if (useliving instanceof PlayerEntity && useliving != null) {//玩家的弹夹显示
	             	if(itemstack.getItemDamage() == itemstack.getMaxDamage()){//物品损坏值=物品最大耐久度
	             		isreload = true;
	             		if (!itemstack.isEmpty() && itemstack.getItem() instanceof ItemGun) {//item
	             			if(gun != null && gun.arm_l_posz > -1.0F) {
	             				GL11.glRotatef(80.0F, 0.0F, 1.0F, 0.0F);
	             				GL11.glRotatef(-50.0F, 0.0F, 0.0F, 1.0F);
	             			}
	             		}
	             	}
	            }*/
				

					GL11.glTranslatef(0.0F, 0.0F, playerRecoilPitch*gun.model_cock_z*0.08F*amination_size);


					GL11.glRotatef(gun.model_muzz_jump_x*playerRecoilPitch*0.2F*amination_size, 1.0F, 0.0F, 0.0F);

				
				{
					//GL11.glNewList(gllist, GL11.GL_COMPILE);
					Minecraft.getInstance().textureManager.bind(gun.obj_tex);
					{
						//System.out.println(String.format("%1$3d", 5));
						gun.obj_model.renderPart("mat1");
						gun.obj_model.renderPart("mat100");
						
						float cock_size = 0;
						/*if(gun.mat2_cock_z == 0){
							cock_size = gun.model_cock_z * 0.5F;
						}else{
							cock_size = gun.mat2_cock_z;
						}*/
						
						if (useliving instanceof PlayerEntity){
							GL11.glTranslatef(0.0F, 0.0F, cock*cock_size * 0.5F);
							gun.obj_model.renderPart("mat2");
							{
								{
									GL11.glDisable(GL11.GL_LIGHT1);
									GL11.glDisable(GL11.GL_LIGHTING);
								}
								gun.obj_model.renderPart("mat2_dot");
								{
									GL11.glEnable(GL11.GL_LIGHTING);
									GL11.glEnable(GL11.GL_LIGHT1);
								}
							}
							GL11.glTranslatef(0.0F, 0.0F, cock*-cock_size * 0.5F);
						}else{
							gun.obj_model.renderPart("mat2");
						}
						
						if(!isreload)gun.obj_model.renderPart("mat3");
						
						if (useliving instanceof PlayerEntity){
							if (playerRecoilPitch >0.1F||gun.powertime>0)
							{
								/*Layermat.mat31mat32(gun, itemstack, true);//加特林旋转
								if (itemstack.getItemDamage() != itemstack.getMaxDamage()) {
									Layermat.mat25(itemstack, gun, false, cockingtime);
								}else {
									Layermat.mat25(itemstack, gun, true, cockingtime);
								}
								if(!gun.mat2) {
									Layermat.rendersight(useliving, itemstack, gun);
								}*/
							}else{
								/*Layermat.mat31mat32(gun, itemstack, false);//加特林不旋转
								if (itemstack.getItemDamage() != itemstack.getMaxDamage()) {
									Layermat.mat25(itemstack, gun, false, cockingtime);
									Layermat.rendersight(useliving, itemstack, gun);
								}*/
							}
						}else{
							gun.obj_model.renderPart("mat31");
							gun.obj_model.renderPart("mat32");
						}
					}
				}
            }
            GL11.glPopMatrix();//gune
        }
    }
    
    public boolean shouldCombineTextures()
    {
        return false;
    }
}