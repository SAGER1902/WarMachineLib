package advancearmy.render.map;
import java.util.List;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.Mth;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import com.mojang.math.Axis;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Entity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.HumanoidArm;
import wmlib.client.event.RenderEntityEvent;
import wmlib.client.obj.SAObjModel;
import wmlib.client.render.SARenderHelper;
import wmlib.client.render.SARenderHelper.RenderTypeSA;
import advancearmy.entity.map.ArmyMovePoint;
import advancearmy.entity.map.CreatureRespawn;
import advancearmy.entity.map.VehicleRespawn;
import advancearmy.entity.building.SandBag;
import net.minecraft.client.renderer.RenderType;
import wmlib.client.render.SARenderState;
import wmlib.client.render.RenderTypeVehicle;
import net.minecraft.client.renderer.culling.Frustum;

@OnlyIn(Dist.CLIENT)
public class DefaultRender extends EntityRenderer<Mob>{
	private static final ResourceLocation tex = ResourceLocation.tryParse("wmlib:textures/hud/count.png");
	private static final SAObjModel model = new SAObjModel("wmlib:textures/hud/digit.obj");
	private static final ResourceLocation sbtex = ResourceLocation.tryParse("wmlib:textures/entity/sandbag.png");
	private static final SAObjModel sb = new SAObjModel("wmlib:textures/entity/sandbag.obj");
	private static final SAObjModel icon = new SAObjModel("wmlib:textures/entity/icon.obj");
	private static final ResourceLocation tex1 = ResourceLocation.tryParse("advancearmy:textures/item/maptool_respawnc.png");
	private static final ResourceLocation tex2 = ResourceLocation.tryParse("advancearmy:textures/item/maptool_respawnv.png");
	private static final ResourceLocation tex3 = ResourceLocation.tryParse("advancearmy:textures/item/maptool_movepoint.png");
	
	RenderType rt = RenderTypeVehicle.objrender(sbtex);
	RenderType f1 = SARenderState.getBlendDepthWrite(tex);
	
	RenderType mpc = RenderTypeVehicle.objrender_blend(tex1);
	RenderType mpv = RenderTypeVehicle.objrender_blend(tex2);
	RenderType mpp = RenderTypeVehicle.objrender_blend(tex3);
	
    public ResourceLocation getTextureLocation(Mob entity)
    {
		return tex;
    }
    public DefaultRender(EntityRendererProvider.Context context)
    {
    	super(context);
    }
	
    /*@Override
    public boolean shouldRender(Mob livingEntity, Frustum camera, double camX, double camY, double camZ) {
        //return super.shouldRender(livingEntity, camera, camX, camY, camZ) || livingEntity.getBeamTarget() != null;
		return true;
    }*/
	
   private void renderArmWithItem(Mob ent, ItemStack items, ItemDisplayContext display, HumanoidArm arm, PoseStack stack, MultiBufferSource buffer, int packedLight) {
      if (!items.isEmpty()) {
         stack.pushPose();
         //this.getParentModel().translateToHand(arm, stack);
         /*stack.mulPose(Axis.XP.rotationDegrees(-90.0F));
         stack.mulPose(Axis.YP.rotationDegrees(180.0F));
         boolean flag = arm == HumanoidArm.LEFT;
         stack.translate((double)((float)(flag ? -1 : 1) / 16.0F), 0.125D, -0.625D);*/
			stack.mulPose(Axis.YP.rotationDegrees(180.0F));
			stack.translate(0, 1, 0);
         Minecraft.getInstance().getEntityRenderDispatcher().getItemInHandRenderer().renderItem(ent, items, display, true, stack, buffer, packedLight);
         stack.popPose();
      }
   }
	
    public void render(Mob entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight)
    {
		super.render(entity, entityYaw, partialTicks, stack, buffer, packedLight);
		stack.pushPose();
		Minecraft mc = Minecraft.getInstance();
		Player entityplayer = mc.player;
		ItemStack itemstack1 = entityplayer.getItemBySlot(EquipmentSlot.HEAD);
        Item item1 = itemstack1.getItem();
		if(entity instanceof SandBag){
			stack.pushPose();
			sb.setRender(rt,null,stack,packedLight);
			stack.mulPose(Axis.YP.rotationDegrees(((SandBag)entity).getYawRote()));
			if(entity.getHealth()>60){
				sb.renderPart("mat1");
			}else if(entity.getHealth()>30&&entity.getHealth()<=60){
				sb.renderPart("mat2");
			}else if(entity.getHealth()<=30){
				sb.renderPart("mat3");
			}
			stack.popPose();
		}
		if(item1 != Items.DIAMOND_HELMET&&entityplayer.isCreative()){
			if(entity.getMainHandItem()!=null){
				ItemStack this_heldItem = entity.getMainHandItem();
				ItemStack this_heldItem2 = entity.getOffhandItem();
				if(entity instanceof VehicleRespawn)this.renderArmWithItem(entity, this_heldItem, ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, HumanoidArm.RIGHT, stack, buffer, packedLight);
				//this.renderArmWithItem(entity, this_heldItem2, ItemDisplayContext.THIRD_PERSON_LEFT_HAND, HumanoidArm.LEFT, stack, buffer, packedLight);
			}
			model.setRender(f1,null,stack,0xF000F0);
			if(entity instanceof CreatureRespawn){
				EntityRenderDispatcher manager = mc.getEntityRenderDispatcher();
				CreatureRespawn point = (CreatureRespawn)entity;
				icon.setRender(mpc,null,stack,0xF000F0);
				icon.renderPart("mat1");
				icon.setRender(f1,null,stack,0xF000F0);
				if(point.isEnemyRespawn){
					icon.renderPart("red");
					RenderSystem.setShaderColor(1, 0.6F, 0.6F, 1);
				}else{
					icon.renderPart("green");
					RenderSystem.setShaderColor(0.6F, 1, 0.6F, 1);
				}
				stack.pushPose();
				stack.mulPose(Axis.YP.rotationDegrees(-manager.camera.getYRot()+180F));
				stack.translate(0, 0, 0);
				stack.mulPose(Axis.XP.rotationDegrees(-manager.camera.getXRot()));
				stack.translate(0, 0, 0);
				stack.translate(0F, 1F, 0F);
				renderCount(point.getRespawnCount(),stack);
				stack.popPose();
				RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
			}
			if(entity instanceof VehicleRespawn){
				EntityRenderDispatcher manager = mc.getEntityRenderDispatcher();
				VehicleRespawn point = (VehicleRespawn)entity;
				icon.setRender(mpv,null,stack,0xF000F0);
				icon.renderPart("mat1");
				icon.setRender(f1,null,stack,0xF000F0);
				icon.renderPart("white");
				stack.pushPose();
				stack.mulPose(Axis.YP.rotationDegrees(-manager.camera.getYRot()+180F));
				stack.translate(0, 0, 0);
				stack.mulPose(Axis.XP.rotationDegrees(-manager.camera.getXRot()));
				stack.translate(0, 0, 0);
				stack.translate(0F, 1F, 0F);
				renderCount(point.getVehicleID(),stack);
				stack.popPose();
			}
			if(entity instanceof ArmyMovePoint){
				EntityRenderDispatcher manager = mc.getEntityRenderDispatcher();
				ArmyMovePoint point = (ArmyMovePoint)entity;
				icon.setRender(mpp,null,stack,0xF000F0);
				icon.renderPart("mat1");
				icon.setRender(f1,null,stack,0xF000F0);
				int count = point.getMoveId();
				stack.pushPose();
				int connectid = 2;
				if(point.isEnemyPoint && point.pointType==0)connectid = 3;
				if(point.pointType==1)connectid=4;
				if(point.pointType==2)connectid=5;
				if(point.pointType==3)connectid=6;
				List<Entity> list = point.level().getEntities(point, point.getBoundingBox().inflate(point.connectRange, point.connectRange*2F, point.connectRange));
				for(int k2 = 0; k2 < list.size(); ++k2) {
					Entity ent = list.get(k2);
					if(ent instanceof ArmyMovePoint && ((ArmyMovePoint)ent).getHealth()>0){
						ArmyMovePoint point1 = (ArmyMovePoint)ent;
						if(point.pointType==point1.pointType && (count+1 == point1.getMoveId())){
							if(point1.isEnemyPoint){
								if(point.isEnemyPoint)RenderEntityEvent.renderBeam(stack, point, connectid, (float)point1.getX(),(float)point1.getY()+0.2F,(float)point1.getZ());
							}else{
								if(!point.isEnemyPoint)RenderEntityEvent.renderBeam(stack, point, connectid, (float)point1.getX(),(float)point1.getY()+0.2F,(float)point1.getZ());
							}
						}
					}
				}
				if(point.isEnemyPoint){
					icon.renderPart("red");
					RenderSystem.setShaderColor(1, 0.6F, 0.6F, 1);
				}else{
					icon.renderPart("green");
					RenderSystem.setShaderColor(0.6F, 1, 0.6F, 1);
				}
				stack.mulPose(Axis.YP.rotationDegrees(-manager.camera.getYRot()+180F));
				stack.translate(0, 0, 0);
				stack.mulPose(Axis.XP.rotationDegrees(-manager.camera.getXRot()));
				stack.translate(0, 0, 0);
				stack.translate(0F, 1F, 0F);
				renderCount(count,stack);
				if(point.pointType==0)model.renderPart("man");//
				if(point.pointType==1)model.renderPart("tank");//
				if(point.pointType==2)model.renderPart("heli");//
				if(point.pointType==3)model.renderPart("jet");//
				RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
				stack.popPose();
			}
		}
		stack.popPose();
	}
	void renderCount(int count, PoseStack stack){
		float size1 = 2F;
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
		if(count<10){
			model.renderPart("obj" + t1);//
		}else if(count<100)
		{
			model.renderPart("obj" + t2);//
			stack.translate(0.4F*size1,0,0);
			model.renderPart("obj" + t1);//
		}else if(count<1000)
		{
			model.renderPart("obj" + t3);//
			stack.translate(0.4F*size1,0,0);
			model.renderPart("obj" + t2);//
			stack.translate(0.4F*size1,0,0);
			model.renderPart("obj" + t1);//
		}else if(count<10000)
		{
			model.renderPart("obj" + t4);//
			stack.translate(0.4F*size1,0,0);
			model.renderPart("obj" + t3);//
			stack.translate(0.4F*size1,0,0);
			model.renderPart("obj" + t2);//
			stack.translate(0.4F*size1,0,0);
			model.renderPart("obj" + t1);//
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