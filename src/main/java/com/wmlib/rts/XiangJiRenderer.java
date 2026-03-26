package wmlib.rts;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.model.SlimeModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import wmlib.rts.XiangJiEntity;
import javax.annotation.Nonnull;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.RenderType;
import wmlib.client.obj.SAObjModel;
import wmlib.client.render.SARenderState;
import wmlib.client.render.RenderTypeVehicle;
import net.minecraft.client.renderer.entity.EntityRenderer;
public class XiangJiRenderer extends EntityRenderer<XiangJiEntity> {
	public ResourceLocation tex = ResourceLocation.tryParse("wmlib:textures/entity/drone.png");
	private static final SAObjModel obj = new SAObjModel("wmlib:textures/entity/drone.obj");
	RenderType rt = RenderTypeVehicle.objrender(tex);
	RenderType light = SARenderState.getBlendDepthWrite(tex);
	public XiangJiRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.shadowRadius = 0.5F;
	}
	
	@Override
    public void render(XiangJiEntity entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight0) {
        super.render(entity, entityYaw, partialTicks, stack, buffer, packedLight0);
		int packedLight=packedLight0;
		stack.pushPose();
		Minecraft mc = Minecraft.getInstance();
		stack.mulPose(Axis.YP.rotationDegrees(180.0F));
		stack.mulPose(Axis.YP.rotationDegrees(180.0F - (entity.yHeadRotO + (entity.yHeadRot - entity.yHeadRotO) * partialTicks)));
		stack.translate(0,1.02F,0);
		stack.mulPose(Axis.XP.rotationDegrees((entity.xRotO + (entity.getXRot() - entity.xRotO) * partialTicks)));
		stack.translate(0,-1.02F,0);
		obj.setRender(rt, null, stack, packedLight);
		obj.renderPart("head");
		obj.setRender(light, null, stack, 0xF000F0);
		RenderSystem.setShaderColor(0.1F, 0.6F, 1F,1F);
		obj.renderPart("head_light");
		RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
		stack.popPose();//glend
	}
	
	@Override
	@Nonnull
	public ResourceLocation getTextureLocation(@Nonnull XiangJiEntity entity) {
		return ResourceLocation.tryParse("wmlib:textures/entity/sandbag.png");
	}
}
