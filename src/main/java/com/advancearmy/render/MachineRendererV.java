package advancearmy.render;
import java.util.List;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.Mth;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import com.mojang.math.Axis;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.world.entity.Entity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import com.mojang.blaze3d.systems.RenderSystem;
import wmlib.client.obj.SAObjModel;
import net.minecraft.world.entity.player.Player;
import advancearmy.entity.building.VehicleMachine;
import advancearmy.item.ItemSpawn;
import wmlib.client.render.SARenderHelper;
import wmlib.client.render.SARenderHelper.RenderTypeSA;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.HumanoidArm;
import wmlib.client.event.RenderEntityEvent;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderType;
import wmlib.client.render.SARenderState;
import wmlib.client.render.RenderTypeVehicle;
import org.joml.Matrix4f;
@OnlyIn(Dist.CLIENT)
public class MachineRendererV extends MobRenderer<VehicleMachine, ModelNone<VehicleMachine>>{
	private static final ResourceLocation dtex = ResourceLocation.tryParse("wmlib:textures/hud/count.png");
	private static final SAObjModel model = new SAObjModel("wmlib:textures/hud/digit.obj");
	private static final ResourceLocation tex = ResourceLocation.tryParse("advancearmy:textures/mob/building/vehiclebuilding.png");
	private static final SAObjModel obj = new SAObjModel("advancearmy:textures/mob/building/vehiclebuilding.obj");
	private static final ResourceLocation glint = ResourceLocation.tryParse("wmlib:textures/misc/vehicle_made1.png");
	RenderType rt = RenderTypeVehicle.objrender(tex);
	RenderType rt1 = SARenderState.getBlendDepthWrite(tex);
	RenderType f1 = SARenderState.getBlendDepthWrite(dtex);
	RenderType gl = SARenderState.getBlendGlowGlint(glint);
	
    public ResourceLocation getTextureLocation(VehicleMachine entity)
    {
		return tex;
    }
    public MachineRendererV(EntityRendererProvider.Context context)
    {
    	super(context, new ModelNone(),0F);
    }

   private void renderArmWithItem(VehicleMachine ent, ItemStack items, ItemDisplayContext display, HumanoidArm arm, PoseStack stack, MultiBufferSource buffer, int packedLight) {
      if (!items.isEmpty()) {
			stack.pushPose();
			//this.getParentModel().translateToHand(arm, stack);
			//stack.mulPose(Axis.XP.rotationDegrees(-90.0F));
			stack.mulPose(Axis.YP.rotationDegrees(180.0F));
			stack.translate(2, 2, 0);
			Minecraft.getInstance().getEntityRenderDispatcher().getItemInHandRenderer().renderItem(ent, items, display, true, stack, buffer, packedLight);
			stack.popPose();
      }
   }
   
	private static void setupGlintTexturing(float p_228548_0_) {

	}
   
    float iii;
	static boolean glow = true;
	static float shock =0;
	public SAObjModel show = null;
    public void render(VehicleMachine entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight)
    {
		super.render(entity, entityYaw, partialTicks, stack, buffer, packedLight);
		stack.pushPose();
		stack.mulPose(Axis.YP.rotationDegrees(entity.getYawRoteNBT()));
		Minecraft mc = Minecraft.getInstance();
		stack.pushPose();
		{
			if(entity.getMainHandItem()!=null){
				ItemStack this_heldItem = entity.getMainHandItem();
				Item item = this_heldItem.getItem();
				if(item instanceof ItemSpawn){
					ItemSpawn ispawn = (ItemSpawn)item;
					if(ispawn.obj_model!=null)this.show=ispawn.obj_model;
				}
			}
			{
				EntityRenderDispatcher manager = mc.getEntityRenderDispatcher();
				float size1 = 2F;
				int count = entity.getMoney();
				stack.pushPose();
				int f = entity.finish_time;
				int ready = 0;
				if(f>0)ready = (entity.getBuild() * 100)/f;
				//RenderSystem.enableBlend();
				stack.pushPose();
				obj.setRender(rt, null, stack, packedLight);
				obj.renderPart("head");
				obj.renderPart("body");
				if(count<=0||entity.isCover){
					RenderSystem.setShaderColor(1F, 0.3F, 0.3F, 1);
				}else{
					RenderSystem.setShaderColor(0.6F, 1, 0.6F, 1);
				}
				obj.setRender(rt1, null, null, 0xF000F0);
				obj.renderPart("head_light");
				obj.renderPart("body_light");
				if(entity.getMainHandItem()!=null)obj.renderPart("cover");
				if(entity.getBuild()>0){
					if(count>0&&!entity.isCover){
						if(!glow && this.shock<45){
							++this.shock;
						}
						if(this.shock>=45)glow = true;
						if(glow && this.shock>-45){
							--this.shock;
						}
						if(this.shock<=-45)glow = false;
					}
					stack.translate(0F, 2F, 0.9F);
					stack.mulPose(Axis.XP.rotationDegrees(shock));
					stack.translate(0F, -2F, -0.9F);
					obj.renderPart("barrel");
				}
				stack.popPose();
				if(count<=0||entity.isCover){
					RenderSystem.setShaderColor(1F, 0.3F, 0.3F, 0.01F+(float)entity.getBuild()/entity.finish_time);
				}else{
					RenderSystem.setShaderColor(0.6F, 1, 0.6F, 0.01F+(float)entity.getBuild()/entity.finish_time);
				}
				if(this.show!=null){
					show.setRender(gl, null, stack, 0xF000F0);
					stack.pushPose();
					stack.translate(0F, 0F, 6F);
					long time = (long)((double)Util.getMillis() * Minecraft.getInstance().options.glintSpeed().get() * 8.0);
					float u = (float)(time % 110000L) / 110000.0f;
					float v = (float)(time % 30000L) / 30000.0f;
					Matrix4f move = new Matrix4f().translation(u, v, 0.0f);
					RenderSystem.setTextureMatrix(move);
					show.renderAll();
					RenderSystem.resetTextureMatrix();
					stack.popPose();
				}
				if(count<=0){
					RenderSystem.setShaderColor(1F, 0.3F, 0.3F, 1);
				}else{
					RenderSystem.setShaderColor(0.6F, 1, 0.6F, 1);
				}
				
				stack.mulPose(Axis.YP.rotationDegrees(-entity.getYawRoteNBT()));
				stack.mulPose(Axis.YP.rotationDegrees(-manager.camera.getYRot()+180F));
				stack.translate(0, 0, 0);
				stack.mulPose(Axis.XP.rotationDegrees(-manager.camera.getXRot()));
				stack.translate(0, 0, 0);
				stack.translate(0F, 4F, 0F);
				model.setRender(f1, null, stack, 0xF000F0);
				
				stack.pushPose();
				renderCount(count,stack);
				stack.popPose();
				
				stack.pushPose();
				stack.translate(-2F, 1F, 0F);
				renderCount(entity.getVehicleC(),stack);
				stack.popPose();
				
				stack.pushPose();
				stack.translate(0F, 1F, 0F);
				renderCount(ready,stack);
				stack.translate(0.4F*size1,0,0);
				model.renderPart("rate");//
				stack.popPose();
					
				RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
				//RenderSystem.disableBlend();
				//-SARenderHelper.disableBlendMode(RenderType.ALPHA);//
				stack.popPose();
			}
		}
		if(entity.getMainHandItem()!=null){
			ItemStack this_heldItem = entity.getMainHandItem();
			this.renderArmWithItem(entity, this_heldItem, ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, HumanoidArm.RIGHT, stack, buffer, packedLight);
		}
		stack.popPose();
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