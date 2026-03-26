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
//import advancearmy.entity.map.ArmyMovePoint;
import advancearmy.entity.building.SandBag;
import net.minecraft.client.renderer.RenderType;
import wmlib.client.render.SARenderState;
import wmlib.client.render.RenderTypeVehicle;
import advancearmy.entity.map.RewardBox;
@OnlyIn(Dist.CLIENT)
public class BoxRenderer extends EntityRenderer<RewardBox>{
	private ResourceLocation tex = ResourceLocation.tryParse("advancearmy:textures/entity/box1.jpg");
	private SAObjModel obj = new SAObjModel("advancearmy:textures/entity/box1.obj");
	
	private static final ResourceLocation tex2 = ResourceLocation.tryParse("advancearmy:textures/entity/box2.jpg");
	private static final SAObjModel obj2 = new SAObjModel("advancearmy:textures/entity/box2.obj");
	
	private static final ResourceLocation tex3 = ResourceLocation.tryParse("advancearmy:textures/entity/box3.png");
	private static final SAObjModel obj3 = new SAObjModel("advancearmy:textures/entity/box3.obj");
	
	private static final ResourceLocation tex4 = ResourceLocation.tryParse("advancearmy:textures/entity/box4.png");
	private static final SAObjModel obj4 = new SAObjModel("advancearmy:textures/entity/box4.obj");
	RenderType rt = RenderTypeVehicle.objrender(tex);
	RenderType rt2 = RenderTypeVehicle.objrender(tex2);
	RenderType rt3 = RenderTypeVehicle.objrender(tex3);
	RenderType rt4 = RenderTypeVehicle.objrender(tex4);
	RenderType f1 = SARenderState.getBlendDepthWrite(tex);
	RenderType f2 = SARenderState.getBlendDepthWrite(tex2);
	RenderType f3 = SARenderState.getBlendDepthWrite(tex3);
	RenderType f4 = SARenderState.getBlendDepthWrite(tex4);
    public ResourceLocation getTextureLocation(RewardBox entity)
    {
		return tex;
    }
    public BoxRenderer(EntityRendererProvider.Context context)
    {
    	super(context);
    }
	
    float iii;
	static boolean glow = true;
	static float shock =0;
    public void render(RewardBox entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight)
    {
		super.render(entity, entityYaw, partialTicks, stack, buffer, packedLight);
		stack.pushPose();
		Minecraft mc = Minecraft.getInstance();
		if(entity.getBoxID()>2 && entity.getBoxID()<5){
			obj2.setRender(rt2,null,stack,packedLight);
			obj2.renderPart("body");
			obj2.setRender(f2,null,stack,0xF000F0);
			obj2.renderPart("body_light");
		}else if(entity.getBoxID()>4 && entity.getBoxID()<7){
			obj3.setRender(rt3,null,stack,packedLight);
			obj3.renderPart("body");
			obj3.setRender(f3,null,stack,0xF000F0);
			obj3.renderPart("body_light");
		}else if(entity.getBoxID()>6){
			obj4.setRender(rt4,null,stack,packedLight);
			obj4.renderPart("body");
			obj4.setRender(f4,null,stack,0xF000F0);
			obj4.renderPart("body_light");
		}else{
			obj.setRender(rt,null,stack,packedLight);
			obj.renderPart("body");
			obj.setRender(f1,null,stack,0xF000F0);
			obj.renderPart("body_light");
		}
		stack.popPose();
	}
}