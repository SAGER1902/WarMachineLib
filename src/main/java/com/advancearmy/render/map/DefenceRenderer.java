package advancearmy.render.map;
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
//import advancearmy.entity.map.ArmyMovePoint;
import advancearmy.entity.building.SandBag;
import net.minecraft.client.renderer.RenderType;
import wmlib.client.render.SARenderState;
import wmlib.client.render.RenderTypeVehicle;
import advancearmy.entity.map.DefencePoint;
import advancearmy.render.ModelNone;
@OnlyIn(Dist.CLIENT)
public class DefenceRenderer extends MobRenderer<DefencePoint, ModelNone<DefencePoint>>{
	/*private static final ResourceLocation dtex = ResourceLocation.tryParse("wmlib:textures/hud/count.png");
	private static final SAObjModel digit = new SAObjModel("wmlib:textures/hud/digit.obj");*/
	private static final SAObjModel obj = new SAObjModel("wmlib:textures/marker/point1.obj");
	private static final ResourceLocation tex = ResourceLocation.tryParse("wmlib:textures/marker/point1.png");
	private static final SAObjModel obj1 = new SAObjModel("wmlib:textures/marker/defence.obj");
	private static final ResourceLocation tex1 = ResourceLocation.tryParse("wmlib:textures/marker/soldier_type.png");
	RenderType rt = RenderTypeVehicle.objrender(tex1);
	RenderType f1 = SARenderState.getBlendDepthWrite(tex);
    public ResourceLocation getTextureLocation(DefencePoint entity)
    {
		return tex;
    }
    public DefenceRenderer(EntityRendererProvider.Context context)
    {
    	super(context, new ModelNone(),0F);
    }
	
    float iii;
	static boolean glow = true;
	static float shock =0;
    public void render(DefencePoint entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight)
    {
		super.render(entity, entityYaw, partialTicks, stack, buffer, packedLight);
		stack.pushPose();
		Minecraft mc = Minecraft.getInstance();
		stack.pushPose();
		EntityRenderDispatcher manager = mc.getEntityRenderDispatcher();
		obj1.setRender(rt,null,stack,0xF000F0);
		RenderSystem.setShaderColor(0.6F, 1, 0.6F, 1);
		obj1.renderPart("body");
		if(entity.flag_time==0)obj1.renderPart("flag1");
		if(entity.flag_time==1)obj1.renderPart("flag2");
		if(entity.flag_time==2)obj1.renderPart("flag3");
		if(entity.flag_time==3)obj1.renderPart("flag4");
		if(entity.flag_time==4)obj1.renderPart("flag5");
		if(entity.flag_time==5)obj1.renderPart("flag6");
		stack.mulPose(Axis.YP.rotationDegrees(-manager.camera.getYRot()+180F));
		stack.translate(0, 5, 0);
		stack.mulPose(Axis.XP.rotationDegrees(-manager.camera.getXRot()));
		stack.translate(0, -5, 0);
		stack.translate(0, 5, 0);
		obj.setRender(f1,null,stack,0xF000F0);
		if(entity.isAttack){
			obj.renderPart("show2");
		}else{
			obj.renderPart("show1");
		}
		RenderSystem.setShaderColor(1, 1, 1, 1);
		stack.popPose();
		stack.popPose();
	}
}