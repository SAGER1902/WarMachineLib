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
import advancearmy.entity.map.RandomPoint;
@OnlyIn(Dist.CLIENT)
public class InvasionRender extends EntityRenderer<RandomPoint>{
	/*private static final ResourceLocation dtex = ResourceLocation.tryParse("wmlib:textures/hud/count.png");
	private static final SAObjModel digit = new SAObjModel("wmlib:textures/hud/digit.obj");
	*/
	private static final ResourceLocation tex = ResourceLocation.tryParse("wmlib:textures/marker/point1.png");
	private static final SAObjModel obj = new SAObjModel("wmlib:textures/marker/point1.obj");
	RenderType f1 = SARenderState.getBlendDepthWrite(tex);
    public ResourceLocation getTextureLocation(RandomPoint entity)
    {
		return tex;
    }
    public InvasionRender(EntityRendererProvider.Context context)
    {
    	super(context);
    }
	
    float iii;
	static boolean glow = true;
	static float shock =0;
    public void render(RandomPoint entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight)
    {
		super.render(entity, entityYaw, partialTicks, stack, buffer, packedLight);
		stack.pushPose();
		Minecraft mc = Minecraft.getInstance();
		stack.pushPose();
		{
			{
				EntityRenderDispatcher manager = mc.getEntityRenderDispatcher();
				//RandomPoint entity = (RandomPoint)entity;
				//RenderSystem.enableBlend();
				if(!glow && iii<10F){
					++iii;
				}else{
					glow = true;
				}
				if(glow && iii>0){
					--iii;
				}else{
					glow = false;
				}
				obj.setRender(f1,null,stack,0xF000F0);
				stack.scale(1+iii*0.02F, 1+iii*0.02F, 1+iii*0.02F);
				if(entity.getSummonID()<11){
					obj.renderPart("support2");
				}else{
					obj.renderPart("support1");
				}
				stack.mulPose(Axis.YP.rotationDegrees(-manager.camera.getYRot()+180F));
				stack.translate(0, 0, 0);
				stack.mulPose(Axis.XP.rotationDegrees(-manager.camera.getXRot()));
				stack.translate(0, 0, 0);
				if(entity.getSummonID()<11){
					obj.renderPart("show2");
				}else{
					obj.renderPart("show1");
				}
			}
		}
		stack.popPose();
		stack.popPose();
	}
}