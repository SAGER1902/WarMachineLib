package wmlib.client.event;

import java.awt.Color;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import wmlib.client.obj.SAObjModel;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.MobEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;

import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.BipedModel;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.potion.Effects;

import wmlib.api.IArmy;
import wmlib.api.IHealthBar;
import wmlib.client.render.SARenderHelper;
import wmlib.client.render.SARenderHelper.RenderTypeSA;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import wmlib.common.living.EntityWMVehicleBase;
import wmlib.common.living.EntityWMSeat;
import wmlib.common.living.AI_MissileLock;

import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.nbt.CompoundNBT;

import net.minecraft.entity.monster.IMob;
import net.minecraft.world.gen.Heightmap;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import wmlib.common.item.ItemMouse;
import wmlib.common.item.ItemGun;
import net.minecraft.item.ItemStack;
import net.minecraft.util.HandSide;
import wmlib.api.IPara;
public class RenderEntityEvent {
	private static final ResourceLocation color = new ResourceLocation("wmlib:textures/marker/color.png");
	private static final SAObjModel line = new SAObjModel("wmlib:textures/marker/line.obj");
	
	private static final SAObjModel para = new SAObjModel("wmlib:textures/entity/parachute.obj");
	private static final ResourceLocation parat = new ResourceLocation("wmlib:textures/entity/parachute.png");
	
    private static final SAObjModel normal_class = new SAObjModel("wmlib:textures/marker/normal_class.obj");
	private static final SAObjModel skill = new SAObjModel("wmlib:textures/marker/skill.obj");
	private static final ResourceLocation g1 = new ResourceLocation("wmlib:textures/marker/g1.png");
	private static final ResourceLocation g2 = new ResourceLocation("wmlib:textures/marker/g2.png");
	private static final SAObjModel choose = new SAObjModel("wmlib:textures/marker/choose.obj");
	private static final SAObjModel health = new SAObjModel("wmlib:textures/marker/teamhealth.obj");
	private static final SAObjModel lock = new SAObjModel("wmlib:textures/marker/lock.obj");
	
	private static final ResourceLocation class_0 = new ResourceLocation("wmlib:textures/marker/marker0.png");
	private static final ResourceLocation class_1 = new ResourceLocation("wmlib:textures/marker/marker1.png");
	private static final ResourceLocation class_2 = new ResourceLocation("wmlib:textures/marker/marker2.png");
	private static final ResourceLocation class_3 = new ResourceLocation("wmlib:textures/marker/marker3.png");
	private static final ResourceLocation class_4 = new ResourceLocation("wmlib:textures/marker/marker4.png");
    private static final SAObjModel marker = new SAObjModel("wmlib:textures/marker/marker.obj");
	
	private static final ResourceLocation tex = new ResourceLocation("wmlib:textures/hud/count.png");
	private static final SAObjModel digit = new SAObjModel("wmlib:textures/hud/digit.obj");
	//private static final SAObjModel unit = new SAObjModel("wmlib:textures/hud/unit.obj");
	@OnlyIn(Dist.CLIENT)
	protected void render_count(Minecraft mc, int count) {
		float size = 1F;
		float size1 = 1F;
		RenderSystem.pushMatrix();
		mc.getTextureManager().bind(tex);
		GL11.glDisable(GL11.GL_CULL_FACE);
		/*if(id==2){//
			GL11.glColor4f(0F+(1-color), 1F-(1-color), 0F, 1F);
		}*/
		String c = String.valueOf(count);
		int num = count;
		int shiwei=0,baiwei=0,qianwei=0,gewei=0;
		qianwei = num / 1000;
		baiwei = (num % 1000) / 100;
		shiwei = (num / 10 ) % 10;
		gewei = (num %100) % 10;
		String t1 = String.valueOf(gewei);
		String t2 = String.valueOf(shiwei);
		String t3 = String.valueOf(baiwei);
		String t4 = String.valueOf(qianwei);
		SAObjModel model = digit;
		GlStateManager._translatef(-0.1F,-1.5F,0);
		GL11.glColor4f(0F, 1F, 0F, 1F);
		if(model!=null){
			{
				if(count<10){
					model.renderPart("obj" + t1);
				}else if(count<100)
				{
					model.renderPart("obj" + t2);
					GlStateManager._translatef(0.4F*size1,0,0);
					model.renderPart("obj" + t1);
				}else if(count<1000)
				{
					model.renderPart("obj" + t3);//
					GlStateManager._translatef(0.4F*size1,0,0);
					model.renderPart("obj" + t2);
					GlStateManager._translatef(0.4F*size1,0,0);
					model.renderPart("obj" + t1);
				}else if(count<10000)
				{
					model.renderPart("obj" + t4);//
					GlStateManager._translatef(0.4F*size1,0,0);
					model.renderPart("obj" + t3);//
					GlStateManager._translatef(0.4F*size1,0,0);
					model.renderPart("obj" + t2);
					GlStateManager._translatef(0.4F*size1,0,0);
					model.renderPart("obj" + t1);
				}else{
					model.renderPart("obj9");//
					GlStateManager._translatef(0.4F*size1,0,0);
					model.renderPart("obj9");//
					GlStateManager._translatef(0.4F*size1,0,0);
					model.renderPart("obj9");//
					GlStateManager._translatef(0.4F*size1,0,0);
					model.renderPart("obj9");//
					GlStateManager._translatef(0.4F*size1,0,0);
					model.renderPart("add");//+
				}
			}
		}
		GL11.glColor4f(1F, 1F, 1F, 1F);
		RenderSystem.popMatrix();
	}
	
	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public void renderridding(RenderLivingEvent.Pre event)//
	{
		Minecraft mc = Minecraft.getInstance();
		//EntityPlayer entityplayer = event.getEntityPlayer();
		LivingEntity living = event.getEntity();
		if(living != null){
			if (living.getVehicle() instanceof EntityWMSeat && living.getVehicle() != null) {
				EntityWMSeat seat = (EntityWMSeat) living.getVehicle();
				if(seat.getVehicle()!=null && seat.getVehicle() instanceof EntityWMVehicleBase){
					EntityWMVehicleBase ve = (EntityWMVehicleBase) (seat.getVehicle());
					if(seat.seatHide/*||ve.getChange()>0 && (ve.getArmyType1()==0||ve.enc_control>0||ve.can_follow)*/) {
						event.setCanceled(true);
					}
				}
			}
		}
		/*EntityModel mainModel = event.getRenderer().getModel();
		if(mainModel instanceof BipedModel) {
			BipedModel biped = (BipedModel) mainModel;
			if(living.getMainHandItem()!=null){
				ItemStack main = living.getMainHandItem();
				//ItemStack off = living.getHeldItemOffhand();
				if(!main.isEmpty() && main.getItem() instanceof ItemGun){
					ItemGun gun = (ItemGun)main.getItem();
					{
						if(main.getItemDamage() != main.getMaxDamage())biped.rightArmPose = biped.rightArmPose.BOW_AND_ARROW;
					}
					//biped.leftArmPose = biped.leftArmPose.BOW_AND_ARROW;
				}
			}
		}*/
	}
	
	static int showtime = 0;
	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public void renderclass(RenderLivingEvent.Post event) {
		LivingEntity entity = (LivingEntity) event.getEntity();
		Minecraft mc = Minecraft.getInstance();
		PlayerEntity entityplayer = mc.player;
		EntityRendererManager manager = mc.getEntityRenderDispatcher();
		RenderSystem.pushMatrix();
		
		ActiveRenderInfo activeRenderInfoIn = Minecraft.getInstance().getEntityRenderDispatcher().camera;
		activeRenderInfoIn.setup(mc.level, (Entity)(mc.getCameraEntity() == null ? mc.player : mc.getCameraEntity()), 
		!mc.options.getCameraType().isFirstPerson(), mc.options.getCameraType().isMirrored(), event.getPartialRenderTick());
		Vector3d avector3d = activeRenderInfoIn.getPosition();
		double camx = avector3d.x();
		double camy = avector3d.y();
		double camz = avector3d.z();
		double d0 = MathHelper.lerp((double)event.getPartialRenderTick(), entity.xOld, entity.getX());
		double d1 = MathHelper.lerp((double)event.getPartialRenderTick(), entity.yOld, entity.getY());
		double d2 = MathHelper.lerp((double)event.getPartialRenderTick(), entity.zOld, entity.getZ());
		double xIn = d0 - camx;
		double yIn = d1 - camy;
		double zIn = d2 - camz;
		net.minecraftforge.client.event.EntityViewRenderEvent.CameraSetup cameraSetup = net.minecraftforge.client.ForgeHooksClient.onCameraSetup(mc.gameRenderer, activeRenderInfoIn, event.getPartialRenderTick());
		activeRenderInfoIn.setAnglesInternal(cameraSetup.getYaw(), cameraSetup.getPitch());
		RenderSystem.rotatef(cameraSetup.getRoll(), 0.0F, 0.0F, 1.0F);
		RenderSystem.rotatef(activeRenderInfoIn.getXRot(), 1.0F, 0.0F, 0.0F);
		RenderSystem.rotatef(activeRenderInfoIn.getYRot() + 180.0F, 0.0F, 1.0F, 0.0F);
		RenderSystem.translatef((float) xIn, (float) yIn, (float) zIn);
		//RenderSystem.rotatef(180F, 0.0F, 1.0F, 0.0F);
		
		if(entity != null && entity instanceof LivingEntity && entity.getVehicle()==null) {
			if(entityplayer.getMainHandItem()!=null && entityplayer.getVehicle()==null){
				ItemStack main = entityplayer.getMainHandItem();
				if(!main.isEmpty() && main.getItem() instanceof ItemGun){
					ItemGun gun = (ItemGun)main.getItem();
					int block_height = entityplayer.level.getHeight(Heightmap.Type.WORLD_SURFACE, (int)entityplayer.getX(), (int)entityplayer.getZ());
					if(gun.aim_time>=gun.time_aim && gun.bulletid==4 && 
					(gun.bullettype==5 && entity.getY() > block_height + 5)||(gun.bullettype<5 && entity.getY() < block_height + 10)){
						RenderSystem.pushMatrix();
						GL11.glEnable(GL12.GL_RESCALE_NORMAL);
						GlStateManager._rotatef(-manager.camera.getYRot()+180F, 0.0F, 1.0F, 0.0F);
						RenderSystem.translatef(0, 0, 1.5F);
						RenderSystem.translatef(0, 0, 0);
						RenderSystem.rotatef(-manager.camera.getXRot(), 1.0F, 0.0F, 0.0F);
						RenderSystem.translatef(0, 0, 0);
						RenderSystem.translatef(0, entity.getBbHeight(), 0);
						Minecraft.getInstance().getTextureManager().bind(color);
						GlStateManager._disableLighting();
						RenderSystem.enableBlend();
						RenderSystem.color4f(1.0F, 1.0F, 1.0F, 0.8F);
						int dis = (int)entityplayer.distanceTo(entity);
						float size2 = dis*0.01F;;
						if(size2>4F)size2=4F;
						GL11.glScalef((2F+size2) * entity.getBbHeight(), (2F+size2) * entity.getBbHeight(), (2F+size2) * entity.getBbHeight());
						if(gun.bullettype==5){
							lock.renderPart("mat1");
							if(gun.mitarget == entity){
								lock.renderPart("lock");
							}else{
								if(!AI_MissileLock.CanLock(entityplayer,entity))lock.renderPart("friend");
								GL11.glScalef(0.4F, 0.4F, 0.4F);
								this.render_count(mc,dis);
							}
						}else{
							if(gun.mitarget == entity){
								lock.renderPart("lock_land");
							}
						}
						RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1F);
						RenderSystem.disableBlend();
						GlStateManager._enableLighting();
						GL11.glDisable(GL12.GL_RESCALE_NORMAL);
						RenderSystem.popMatrix();
					}
				}
			}
			if (entityplayer.getVehicle() != null && entityplayer.getVehicle() instanceof EntityWMSeat) {// 1
				EntityWMSeat seat = (EntityWMSeat)entityplayer.getVehicle();
				if(seat.getVehicle()!=null && seat.getVehicle() instanceof EntityWMVehicleBase){
					EntityWMVehicleBase vehicle = (EntityWMVehicleBase) seat.getVehicle();
					int block_height = vehicle.level.getHeight(Heightmap.Type.WORLD_SURFACE, (int)vehicle.getX(), (int)vehicle.getZ());
					if(seat.canlock && entity!=vehicle && entity!=seat) {
						if((seat.is_aa && entity.getY() > block_height + 5)||(!seat.is_aa && entity.getY() < block_height + 10)){
							RenderSystem.pushMatrix();
							GL11.glEnable(GL12.GL_RESCALE_NORMAL);
							GlStateManager._rotatef(-manager.camera.getYRot()+180F, 0.0F, 1.0F, 0.0F);
							RenderSystem.translatef(0, 0, 1.5F);
							RenderSystem.translatef(0, 0, 0);
							RenderSystem.rotatef(-manager.camera.getXRot(), 1.0F, 0.0F, 0.0F);
							RenderSystem.translatef(0, 0, 0);
							RenderSystem.translatef(0, entity.getBbHeight(), 0);
							Minecraft.getInstance().getTextureManager().bind(color);
							GlStateManager._disableLighting();
							RenderSystem.enableBlend();
							RenderSystem.color4f(1.0F, 1.0F, 1.0F, 0.8F);
							int dis = (int)vehicle.distanceTo(entity);
							float size2 = dis*0.01F;;
							if(size2>4F)size2=4F;
							GL11.glScalef((2F+size2) * entity.getBbHeight(), (2F+size2) * entity.getBbHeight(), (2F+size2) * entity.getBbHeight());
							if(seat.is_aa){
								lock.renderPart("mat1");
								if(seat.mitarget == entity){
									lock.renderPart("lock");
								}else{
									if(!AI_MissileLock.CanLock(entityplayer,entity))lock.renderPart("friend");
									GL11.glScalef(0.4F, 0.4F, 0.4F);
									this.render_count(mc,dis);
								}
							}else{
								if(seat.mitarget == entity){
									lock.renderPart("lock_land");
								}
							}
							RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1F);
							RenderSystem.disableBlend();
							GlStateManager._enableLighting();
							GL11.glDisable(GL12.GL_RESCALE_NORMAL);
							RenderSystem.popMatrix();
						}
					}
				}
			}
		}
		
		if(entity instanceof IPara && entity.getHealth() > 0.0){
			IPara cap = (IPara)entity;
			if(cap.isDrop()){
				GL11.glScalef(1.1F * entity.getBbWidth(), 1.1F * entity.getBbWidth(), 1.1F * entity.getBbWidth());
				Minecraft.getInstance().getTextureManager().bind(parat);

				para.renderPart("body");
				para.renderPart("mat1_color");

			}
		}
		if(entity instanceof IHealthBar && !entity.hasEffect(Effects.INVISIBILITY)){//bar
			IHealthBar cap = (IHealthBar)entity;
			if(cap.isShow()){
				RenderSystem.pushMatrix();
				if(cap.getBarType()==0){
					if(cap.getBarOwner()==entityplayer){
						GL11.glColor4f(0.6F, 1, 0.6F, 0.9F);
					}else if((entity.getTeam()!=null && entity.getTeam()==entityplayer.getTeam())||entity instanceof IArmy && entity.getTeam()==null){
						GL11.glColor4f(0, 0.8F, 1F, 0.9F);
					}else{
						GL11.glColor4f(1, 1, 1, 0.9F);
					}
				}else{
					GL11.glColor4f(1, 0.3F, 0.3F, 0.9F);
				}
				//SARenderHelper.enableBlendMode(RenderTypeSA.ADDITIVE);//
				RenderSystem.disableDepthTest();
				RenderSystem.depthMask(false);
				RenderSystem.disableAlphaTest();
				GL11.glEnable(GL12.GL_RESCALE_NORMAL);
				GlStateManager._rotatef(-manager.camera.getYRot()+180F, 0.0F, 1.0F, 0.0F);
				if(entity.getBbHeight()>1.9F){
					RenderSystem.translatef(-entity.getBbWidth()*0.5F, entity.getBbHeight()*1.8F, 0);
				}else{
					RenderSystem.translatef(-entity.getBbWidth()*0.5F, entity.getBbHeight()*1.3F, 0);
				}
				
				RenderSystem.translatef(0, 0, 0);
				RenderSystem.rotatef(-manager.camera.getXRot(), 1.0F, 0.0F, 0.0F);
				RenderSystem.translatef(0, 0, 0);
				GL11.glScalef(1.1F * entity.getBbWidth(), 1.1F * entity.getBbWidth(), 1.1F * entity.getBbWidth());
				Minecraft.getInstance().getTextureManager().bind(color);
				health.renderPart("mat2");
				float size1 = entity.getHealth()/entity.getMaxHealth();
				GlStateManager._translatef(0,0,0);
				GlStateManager._scalef(size1, 1F, 1F);
				GlStateManager._translatef(0,0,0);
				health.renderPart("mat1");
				GL11.glDisable(GL12.GL_RESCALE_NORMAL);
				RenderSystem.depthMask(true);
				RenderSystem.enableAlphaTest();
				RenderSystem.enableDepthTest();
				//SARenderHelper.disableBlendMode(RenderTypeSA.ADDITIVE);//
				RenderSystem.popMatrix();
				GL11.glColor4f(1, 1, 1, 1);
			}
		}
		if(entity instanceof IArmy && entity.getVehicle()==null && entity.getHealth() > 0.0F){//
			IArmy cap = (IArmy) entity;
			RenderSystem.pushMatrix();
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			if(cap.getArmyOwner()==entityplayer && !cap.isDrive()){
				GlStateManager._disableLighting();
				RenderSystem.pushMatrix();
				if(cap.getSelect()){
					if(cap.getUnitType()==1){
						//renderBeam(stack,entity, 5, (int)entity.getX(), (int)entity.getY()-block_height, (int)entity.getZ());
						renderBeam(entity, 5, cap.getArmyMoveX(),cap.getArmyMoveY()+1,cap.getArmyMoveZ());
					}else{
						if(cap.getArmyMoveT()==2){
							renderBeam(entity, 0, cap.getArmyMoveX(),cap.getArmyMoveY()+1,cap.getArmyMoveZ());
						}
					}
				}else{
					if(/*cap.getArmyMoveT()==0 || */cap.getUnitType()==1){
						ItemStack heldItem1 = entityplayer.getMainHandItem();
						ItemStack heldItem2 = entityplayer.getOffhandItem();
						if(!heldItem1.isEmpty() && heldItem1.getItem() instanceof ItemMouse||!heldItem2.isEmpty() && heldItem2.getItem() instanceof ItemMouse){
							renderBeam(entity, 6, cap.getArmyMoveX(),cap.getArmyMoveY()+1,cap.getArmyMoveZ());
						}
					}
				}
				Minecraft.getInstance().getTextureManager().bind(color);
				SARenderHelper.enableBlendMode(RenderTypeSA.ADDITIVE);//ADDITIVE
				GL11.glScalef(1.1F * entity.getBbWidth(), 1F, 1.1F * entity.getBbWidth());
				if(cap.getSelect()){//getChoose()
					choose.renderPart("mat2");
				}else{
					choose.renderPart("mat1");
				}
				if(cap.getUnitType()==2 && cap.getArmyMoveT()==3)choose.renderPart("mat3");
				
				
				SARenderHelper.disableBlendMode(RenderTypeSA.ADDITIVE);//
				RenderSystem.popMatrix();
				//RenderSystem.translatef(0, entity.getBbHeight() + 2F, 0);
				{//
					RenderSystem.pushMatrix();
					SARenderHelper.enableBlendMode(RenderTypeSA.ALPHA);//ADDITIVE
					RenderSystem.rotatef(-manager.camera.getYRot()+180F, 0.0F, 1.0F, 0.0F);
					RenderSystem.translatef(0, entity.getBbHeight()*1.7F, 0);
					RenderSystem.translatef(0, 0, 0);
					RenderSystem.rotatef(-manager.camera.getXRot(), 1.0F, 0.0F, 0.0F);
					RenderSystem.translatef(0, 0, 0);
					if (cap.getArmyMoveT() == 1 ) {
						Minecraft.getInstance().getTextureManager().bind(class_1);
					}else if (cap.getArmyMoveT() == 2) {
						Minecraft.getInstance().getTextureManager().bind(class_2);
					}else if (cap.getArmyMoveT() == 3) {
						Minecraft.getInstance().getTextureManager().bind(class_3);
					}else {
						Minecraft.getInstance().getTextureManager().bind(class_0);
					}
					marker.renderPart("mat1");
					
					if(cap.getSelect()){
						//marker.renderPart("choose");
						RenderSystem.translatef(0, 4, 0);
						if(cap.getTeamCount()>0)this.render_count(mc,cap.getTeamCount());
					}
					SARenderHelper.disableBlendMode(RenderTypeSA.ALPHA);//
					RenderSystem.popMatrix();
				}
				
				GlStateManager._enableLighting();
			}
			GL11.glDisable(GL12.GL_RESCALE_NORMAL);
			RenderSystem.popMatrix();
		}
		RenderSystem.popMatrix();
	}
	public static void renderBeam(Entity entity, int typeid, float movex, float movey, float movez){
		float height = 1.2f;
		float size1 = 0.03f;
		{
			Minecraft mc = Minecraft.getInstance();
			mc.getTextureManager().bind(color);
			GlStateManager._pushMatrix();//
			double xPos2 = 0;
			double yPos2 = 0;
			double zPos2 = 0;
			double xPos1 = 0;
			double yPos1 = 0;
			double zPos1 = 0;
			xPos1 = entity.getX();
			yPos1 = entity.getY()+entity.getBbHeight()/2;
			zPos1 = entity.getZ();
			if(movex!=0||movey!=0||movez!=0){
				xPos2 = movex;
				yPos2 = movey+0.5F;
				zPos2 = movez;
				//GlStateManager._color4f(0.0F, 1.0F, 0.0F, 0.23F);
			}
			double d5 = xPos2 - xPos1;
			double d6 = yPos2 - yPos1;
			double d7 = zPos2 - zPos1;
			double d3 = (double) MathHelper.sqrt(d5 * d5 + d7 * d7);
			float dis = (float) MathHelper.sqrt(d5 * d5 + d7 * d7 + d6 * d6);
			float f11 = (float) ((Math.atan2(d6, d3) * 180.0D / Math.PI));
			float f12 = (float) Math.atan2(d5, d7) * 180.0F / (float) Math.PI;
			RenderSystem.rotatef(f12, 0.0F, 1.0F, 0.0F);
			RenderSystem.rotatef(-f11, 1.0F, 0.0F, 0.0F);
			RenderSystem.pushMatrix();
			RenderSystem.enableBlend();
			RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
			//RenderSystem.color4f(0.0F, 1.0F, 0.0F, 0.75F);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glScalef(0.1F,0.1F,dis);
			if(typeid==0)line.renderPart("move");
			if(typeid==1)line.renderPart("attack");
			if(typeid==2)line.renderPart("friend");
			if(typeid==3)line.renderPart("enemy");
			if(typeid==4)line.renderPart("tank");
			if(typeid==5)line.renderPart("heli");
			if(typeid==6)line.renderPart("jet");
			//GlStateManager._color4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glDisable(GL11.GL_BLEND);
			RenderSystem.disableBlend();
			RenderSystem.popMatrix();
			GlStateManager._popMatrix();//
		}
	}
}