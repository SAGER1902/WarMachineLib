package wmlib.client.event;
import java.awt.Color;
import java.util.List;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraft.resources.ResourceLocation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.world.effect.MobEffects;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.world.item.ItemStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.util.Mth;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.VertexConsumer;
import wmlib.common.item.ItemMouse;
import wmlib.client.render.SARenderState;
import wmlib.client.render.RenderTypeVehicle;
import wmlib.api.IArmy;
import wmlib.api.IHealthBar;
import wmlib.common.living.EntityWMVehicleBase;
import wmlib.common.living.EntityWMSeat;
import wmlib.common.living.AI_MissileLock;
import wmlib.client.obj.SAObjModel;
import wmlib.client.render.RenderRote;
import wmlib.rts.RtsMoShiScreen;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
//import wmlib.common.item.ItemGun;
import wmlib.api.IPara;
import wmlib.WMConfig;

import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
public class RenderEntityEvent {
	private static final ResourceLocation color = ResourceLocation.tryParse("wmlib:textures/marker/color.png");
	
	
	private static final SAObjModel line = new SAObjModel("wmlib:textures/marker/line.obj");
	private static final SAObjModel para = new SAObjModel("wmlib:textures/entity/parachute.obj");
	private static final ResourceLocation parat = ResourceLocation.tryParse("wmlib:textures/entity/parachute.png");
	RenderType rtpara = RenderTypeVehicle.objrender(parat);
	
    private static final SAObjModel normal_class = new SAObjModel("wmlib:textures/marker/normal_class.obj");
	private static final SAObjModel skill = new SAObjModel("wmlib:textures/marker/skill.obj");
	private static final SAObjModel choose = new SAObjModel("wmlib:textures/marker/choose.obj");
	private static final SAObjModel health = new SAObjModel("wmlib:textures/marker/teamhealth.obj");
	private static final SAObjModel lock = new SAObjModel("wmlib:textures/marker/lock.obj");
	
	private static final ResourceLocation class_0 = ResourceLocation.tryParse("wmlib:textures/marker/marker0.png");
	private static final ResourceLocation class_1 = ResourceLocation.tryParse("wmlib:textures/marker/marker1.png");
	private static final ResourceLocation class_2 = ResourceLocation.tryParse("wmlib:textures/marker/marker2.png");
	private static final ResourceLocation class_3 = ResourceLocation.tryParse("wmlib:textures/marker/marker3.png");
	private static final ResourceLocation class_4 = ResourceLocation.tryParse("wmlib:textures/marker/marker4.png");
    private static final SAObjModel marker = new SAObjModel("wmlib:textures/marker/marker.obj");
	
	private static final ResourceLocation tex = ResourceLocation.tryParse("wmlib:textures/hud/count.png");
	RenderType rttex = SARenderState.getBlendDepthWrite(tex);//getBlendDepthWrite_NoLight
	static RenderType rtcolor = SARenderState.getBlendDepthWrite(color);//getBlendDepthWrite_NoLight
	RenderType rtdef = SARenderState.getBlendDepthWrite(tex);
	private static final SAObjModel digit = new SAObjModel("wmlib:textures/hud/digit.obj");
	@OnlyIn(Dist.CLIENT)
	protected void render_count(PoseStack stack, Minecraft mc, int count) {
		float size = 1F;
		float size1 = 1F;
		stack.pushPose();
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
		stack.translate(-0.1F,-1.5F,0);
		if(model!=null){
			model.setRender(rttex, null, stack, 0xF000F0);
			model.setColor(0F, 1F, 0F, 1F);
			{
				if(count<10){
					model.renderPart("obj" + t1);
				}else if(count<100)
				{
					model.renderPart("obj" + t2);
					stack.translate(0.4F*size1,0,0);
					model.renderPart("obj" + t1);
				}else if(count<1000)
				{
					model.renderPart("obj" + t3);//
					stack.translate(0.4F*size1,0,0);
					model.renderPart("obj" + t2);
					stack.translate(0.4F*size1,0,0);
					model.renderPart("obj" + t1);
				}else if(count<10000)
				{
					model.renderPart("obj" + t4);//
					stack.translate(0.4F*size1,0,0);
					model.renderPart("obj" + t3);//
					stack.translate(0.4F*size1,0,0);
					model.renderPart("obj" + t2);
					stack.translate(0.4F*size1,0,0);
					model.renderPart("obj" + t1);
				}else{
					model.renderPart("obj9");//
					stack.translate(0.4F*size1,0,0);
					model.renderPart("obj9");//
					stack.translate(0.4F*size1,0,0);
					model.renderPart("obj9");//
					stack.translate(0.4F*size1,0,0);
					model.renderPart("obj9");//
					stack.translate(0.4F*size1,0,0);
					model.renderPart("add");//+
				}
			}
		}
		model.setColor(1F, 1F, 1F, 1F);
		stack.popPose();
	}
	
    /*@OnlyIn(Dist.CLIENT)
    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onRender(RenderPlayerEvent.Pre event) {
        if (event.getEntity() != (Minecraft.getInstance()).player) {
            return;
        }
        if ((Minecraft.getInstance()).screen instanceof InventoryScreen) {
            WMConfig.clientRender = true;
        }
    }*/
	
	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public void renderRidding(RenderLivingEvent.Pre event)//
	{
		Minecraft mc = Minecraft.getInstance();
		LivingEntity living = event.getEntity();
		if(living != null){
			if (living.getVehicle() instanceof EntityWMSeat && living.getVehicle() != null) {
				EntityWMSeat seat = (EntityWMSeat) living.getVehicle();
				if(seat.getVehicle()!=null && seat.getVehicle() instanceof EntityWMVehicleBase){
					EntityWMVehicleBase ve = (EntityWMVehicleBase) (seat.getVehicle());
					//PoseStack stack = event.getPoseStack();
					//stack.pushPose();//angle rote
					/*stack.translate(0, (float)ve.seatPosY[0]+1.32F, (float)ve.seatPosZ[0]);
					stack.mulPose(Axis.XP.rotationDegrees(ve.flyPitch));
					stack.mulPose(Axis.ZP.rotationDegrees(ve.flyRoll));
					stack.translate(0, (float)-ve.seatPosY[0]-1.32F, (float)-ve.seatPosZ[0]);*/
					if(seat.seatHide)event.setCanceled(true);
					
					/*if(WMConfig.clientRender){
						WMConfig.clientRender=false;
						return;
					}
					if(ve.VehicleType>2)event.setCanceled(true);*/
				}
			}
		}
	}
	int flarecd;
	private static final SAObjModel rader = new SAObjModel("wmlib:textures/hud/rader.obj");
	static int showtime = 0;
	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public void renderclass(RenderLivingEvent.Post event) {
		LivingEntity entity = (LivingEntity) event.getEntity();
		Minecraft mc = Minecraft.getInstance();
		Player entityplayer = mc.player;
		EntityRenderDispatcher manager = mc.getEntityRenderDispatcher();
		PoseStack stack = event.getPoseStack();
		stack.pushPose();
		float bwidth = 1;
		float bheight = 2;
		bwidth=entity.getBbWidth();
		bheight=entity.getBbHeight();
		if(entity != null && entity instanceof Player){
			if(mc.screen!=null && mc.screen instanceof RtsMoShiScreen && ((Player)entity == mc.player||((Player)entity).getTeam() == mc.player.getTeam() && mc.player.getTeam()!=null)){
				stack.pushPose();
				RenderRote.setRote(stack,-manager.camera.getYRot()+180F, 0.0F, 1.0F, 0.0F);
				stack.translate(0, 0, 0);
				RenderRote.setRote(stack,-manager.camera.getXRot()+180F, 1.0F, 0.0F, 0.0F);
				stack.translate(0, 0, 0);
				stack.translate(0, -4, 0);
				stack.scale(3F, 3F, 3F);
				rader.setRender(rtdef,null,stack,0xF000F0);
				rader.renderPart("defend");
				stack.popPose();
			}
		}
		if(entity != null && entity instanceof LivingEntity && entity.getVehicle()==null) {
			/*if(entityplayer.getMainHandItem()!=null && entityplayer.getVehicle()==null){
				ItemStack main = entityplayer.getMainHandItem();
				if(!main.isEmpty() && main.getItem() instanceof ItemGun){
					ItemGun gun = (ItemGun)main.getItem();
					int block_height = entityplayer.level().getHeight(Heightmap.Types.WORLD_SURFACE, (int)entityplayer.getX(), (int)entityplayer.getZ());
					if(gun.aim_time>=gun.time_aim && gun.bulletid==4 && 
					(gun.bullettype==5 && entity.getY() > block_height + 5)||(gun.bullettype<5 && entity.getY() < block_height + 10)){
						stack.pushPose();
						
						RenderRote.setRote(stack,-manager.camera.getYRot()+180F, 0.0F, 1.0F, 0.0F);
						stack.translate(0, 0, 1.5F);
						stack.translate(0, 0, 0);
						RenderRote.setRote(stack,-manager.camera.getXRot(), 1.0F, 0.0F, 0.0F);
						stack.translate(0, 0, 0);
						stack.translate(0, bheight, 0);
						//Minecraft.getInstance().getTextureManager().bind(color);
						
						RenderSystem.enableBlend();
						//RenderSystem.color4f(1.0F, 1.0F, 1.0F, 0.8F);
						int dis = (int)entityplayer.distanceTo(entity);
						float size2 = dis*0.01F;;
						if(size2>4F)size2=4F;
						stack.scale((2F+size2) * bheight, (2F+size2) * bheight, (2F+size2) * bheight);
						if(gun.bullettype==5){
							lock.renderPart("mat1");
							if(gun.mitarget == entity){
								lock.renderPart("lock");
							}else{
								if(!AI_MissileLock.CanLock(entityplayer,entity))lock.renderPart("friend");
								stack.scale(0.4F, 0.4F, 0.4F);
								this.render_count(stack,mc,dis);
							}
						}else{
							if(gun.mitarget == entity){
								lock.renderPart("lock_land");
							}
						}
						//RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1F);
						RenderSystem.disableBlend();
						stack.popPose();
					}
				}
			}*/
			if (entityplayer.getVehicle() != null && entityplayer.getVehicle() instanceof EntityWMSeat) {// 1
				EntityWMSeat seat = (EntityWMSeat)entityplayer.getVehicle();
				if(seat.getVehicle()!=null && seat.getVehicle() instanceof EntityWMVehicleBase){
					
					//stack.popPose();//angle rote
					
					EntityWMVehicleBase vehicle = (EntityWMVehicleBase) seat.getVehicle();
					int block_height = vehicle.level().getHeight(Heightmap.Types.WORLD_SURFACE, (int)vehicle.getX(), (int)vehicle.getZ());
					if(seat.canlock && entity!=vehicle/* && entity!=seat*/) {
						if((seat.is_aa && entity.getY() > block_height + 5)||(!seat.is_aa && entity.getY() < block_height + 10)){
							stack.pushPose();
							RenderRote.setRote(stack,-manager.camera.getYRot()+180F, 0.0F, 1.0F, 0.0F);
							stack.translate(0, 0, 1.5F);
							stack.translate(0, 0, 0);
							RenderRote.setRote(stack,-manager.camera.getXRot(), 1.0F, 0.0F, 0.0F);
							stack.translate(0, 0, 0);
							stack.translate(0, bheight, 0);
							lock.setRender(rtcolor,null,stack,0xF000F0);
							lock.setColor(1.0F, 1.0F, 1.0F, 0.8F);
							int dis = (int)vehicle.distanceTo(entity);
							float size2 = dis*0.01F;;
							if(size2>4F)size2=4F;
							stack.scale((2F+size2) * bheight, (2F+size2) * bheight, (2F+size2) * bheight);
							if(seat.is_aa){
								lock.renderPart("mat1");
								if(seat.mitarget == entity){
									lock.renderPart("lock");
								}else{
									if(!AI_MissileLock.CanLock(entityplayer,entity))lock.renderPart("friend");
									stack.scale(0.4F, 0.4F, 0.4F);
									this.render_count(stack,mc,dis);
								}
							}else{
								if(seat.mitarget == entity){
									lock.renderPart("lock_land");
								}
							}
							lock.setColor(1.0F, 1.0F, 1.0F, 1F);
							stack.popPose();
						}
					}
				}
			}
		}
		
		if(entity instanceof IPara && entity.getHealth() > 0.0){//bar
			IPara cap = (IPara)entity;
			if(cap.isDrop()){
				stack.scale(1.1F * bwidth, 1.1F * bwidth, 1.1F * bwidth);
				para.setRender(rtpara,null,stack,0xF000F0);
				para.renderPart("body");
				para.renderPart("mat1_color");
			}
		}
		if(entity instanceof IHealthBar && !entity.hasEffect(MobEffects.INVISIBILITY)){//bar
			IHealthBar cap = (IHealthBar)entity;
			if(cap.isShow()){
				stack.pushPose();
				health.setRender(rtcolor,null,stack,0xF000F0);
				if(cap.getBarType()==0){
					if(cap.getBarOwner()==entityplayer){
						RenderSystem.setShaderColor(0.6F, 1, 0.6F, 0.9F);
					}else if((entity.getTeam()!=null && entity.getTeam()==entityplayer.getTeam())||entity instanceof IArmy && entity.getTeam()==null){
						RenderSystem.setShaderColor(0, 0.8F, 1F, 0.9F);
					}else{
						RenderSystem.setShaderColor(1, 1, 1, 0.9F);
					}
				}else{
					RenderSystem.setShaderColor(1, 0.3F, 0.3F, 0.9F);
				}
				if(bheight>1.9F && bheight<10F){
					stack.translate(-bwidth*0.5F, bheight*1.8F, 0);
				}else{
					stack.translate(-bwidth*0.5F, bheight*1.3F, 0);
				}
				
				stack.translate(0, 0, 0);
				RenderRote.setRote(stack,-manager.camera.getYRot()+180F, 0.0F, 1.0F, 0.0F);
				RenderRote.setRote(stack,-manager.camera.getXRot(), 1.0F, 0.0F, 0.0F);
				stack.translate(0, 0, 0);
				stack.scale(1.1F * bwidth, 1.1F * bwidth, 1.1F * bwidth);
				//health.setRender(rtcolor,null,stack,0xF000F0);
				health.renderPart("mat2");
				float size1 = entity.getHealth()/entity.getMaxHealth();
				stack.scale(size1, 1, 1);
				health.renderPart("mat1");
				
				stack.popPose();
				RenderSystem.setShaderColor(1, 1, 1, 1);
			}
		}
		if(entity instanceof IArmy && entity.getVehicle()==null && entity.getHealth() > 0.0F){//
			IArmy cap = (IArmy) entity;
			//if(cap.getLockTarget()!=null)renderBeam(stack, entity, 1, (float)cap.getLockTarget().getX(),(float)cap.getLockTarget().getY()+1,(float)cap.getLockTarget().getZ());
			stack.pushPose();
			
			if(cap.getArmyOwner()==entityplayer && !cap.isDrive()){
				/*int block_height = entity.level().getHeight(Heightmap.Types.WORLD_SURFACE, (int)Math.floor(entity.getX()), (int)Math.floor(entity.getZ()));
				if(block_height<0)block_height=-block_height;*/
				stack.pushPose();
				if(cap.getSelect()){
					if(cap.getUnitType()==1){
						//renderBeam(stack,entity, 5, (int)entity.getX(), (int)entity.getY()-block_height, (int)entity.getZ());
						renderBeam(stack,entity, 5, cap.getArmyMoveX(),cap.getArmyMoveY()+1,cap.getArmyMoveZ());
					}else{
						if(cap.getArmyMoveT()==2){
							renderBeam(stack,entity, 0, cap.getArmyMoveX(),cap.getArmyMoveY()+1,cap.getArmyMoveZ());
						}
					}
					if(cap.getLockTarget()!=null)renderBeam(stack, entity, 1, (float)cap.getLockTarget().getX(),(float)cap.getLockTarget().getY()+1,(float)cap.getLockTarget().getZ());
				}else{
					if(/*cap.getArmyMoveT()==0 || */cap.getUnitType()==1){
						ItemStack heldItem1 = entityplayer.getMainHandItem();
						ItemStack heldItem2 = entityplayer.getOffhandItem();
						if(!heldItem1.isEmpty() && heldItem1.getItem() instanceof ItemMouse||!heldItem2.isEmpty() && heldItem2.getItem() instanceof ItemMouse){
							renderBeam(stack,entity, 6, cap.getArmyMoveX(),cap.getArmyMoveY()+1,cap.getArmyMoveZ());
						}
					}
				}
				
				choose.setRender(rtcolor,null,stack,0xF000F0);
				stack.scale(1.1F * bwidth, 1F, 1.1F * bwidth);
				choose.setColor(1, 1, 1, 1);
				stack.pushPose();
				/*if(cap.getUnitType()==1){
					stack.translate(entity.getX(), -Math.abs(entity.getY()-block_height), entity.getZ());
				}*/
				if(cap.getSelect()){
					choose.renderPart("mat2");
				}else{
					choose.renderPart("mat1");
				}
				if(cap.getUnitType()==2 && cap.getArmyMoveT()==3)choose.renderPart("mat3");
				
				stack.popPose();
				
				stack.popPose();
				{
					stack.pushPose();
					RenderRote.setRote(stack,-manager.camera.getYRot()+180F, 0.0F, 1.0F, 0.0F);
					stack.translate(0, bheight*1.5F, 0);
					stack.translate(0, 0, 0);
					RenderRote.setRote(stack,-manager.camera.getXRot(), 1.0F, 0.0F, 0.0F);
					stack.translate(0, 0, 0);
					if (cap.getArmyMoveT() == 1 ) {
						marker.setRender(SARenderState.getBlendDepthWrite(class_1),null,stack,0xF000F0);//getBlendDepthWrite_NoLight
					}else if (cap.getArmyMoveT() == 2) {
						marker.setRender(SARenderState.getBlendDepthWrite(class_2),null,stack,0xF000F0);
					}else if (cap.getArmyMoveT() == 3) {
						marker.setRender(SARenderState.getBlendDepthWrite(class_3),null,stack,0xF000F0);
					}else {
						marker.setRender(SARenderState.getBlendDepthWrite(class_0),null,stack,0xF000F0);
					}
					marker.renderPart("mat1");
					if(cap.getSelect()){
						stack.translate(0, 4, 0);
						if(cap.getTeamCount()>0)this.render_count(stack,mc,cap.getTeamCount());
					}
					stack.popPose();
				}
			}
			stack.popPose();
		}
		stack.popPose();
	}
	public static void renderBeam(PoseStack stack, Entity entity, int typeid, float movex, float movey, float movez){
		float height = 1.2f;
		float size1 = 0.03f;
		{
			Minecraft mc = Minecraft.getInstance();
			line.setRender(rtcolor,null,stack,0xF000F0);
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
			}
			double d5 = xPos2 - xPos1;
			double d6 = yPos2 - yPos1;
			double d7 = zPos2 - zPos1;
			double d3 = (double) Mth.sqrt((float)(d5 * d5 + d7 * d7));
			float dis = (float) Mth.sqrt((float)(d5 * d5 + d7 * d7 + d6 * d6));
			float f11 = (float) ((Math.atan2(d6, d3) * 180.0D / Math.PI));
			float f12 = (float) Math.atan2(d5, d7) * 180.0F / (float) Math.PI;
			stack.pushPose();
			
			RenderRote.setRote(stack,f12, 0.0F, 1.0F, 0.0F);
			RenderRote.setRote(stack,-f11, 1.0F, 0.0F, 0.0F);
			
			stack.scale(0.1F,0.1F,dis);
			if(typeid==0)line.renderPart("move");
			if(typeid==1)line.renderPart("attack");
			if(typeid==2)line.renderPart("friend");
			if(typeid==3)line.renderPart("enemy");
			if(typeid==4)line.renderPart("tank");
			if(typeid==5)line.renderPart("heli");
			if(typeid==6)line.renderPart("jet");
			stack.popPose();
		}
	}
}