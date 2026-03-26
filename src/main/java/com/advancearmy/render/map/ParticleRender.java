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
import advancearmy.entity.map.ParticlePoint;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.Util;
import org.joml.Matrix4f;
@OnlyIn(Dist.CLIENT)
public class ParticleRender extends EntityRenderer<ParticlePoint>{
	private static final ResourceLocation glint = ResourceLocation.tryParse("wmlib:textures/misc/vehicle_made1.png");
	RenderType gl = SARenderState.getBlendGlowGlint(glint);
	private static final SAObjModel dun = new SAObjModel("advancearmy:textures/entity/hudun.obj");
	
    public ParticleRender(EntityRendererProvider.Context context)
    {
    	super(context);
    }
    public ResourceLocation getTextureLocation(ParticlePoint entity)
    {
		return glint;
    }
    @Override
    public boolean shouldRender(ParticlePoint livingEntity, Frustum camera, double camX, double camY, double camZ) {
        //return super.shouldRender(livingEntity, camera, camX, camY, camZ) || livingEntity.getBeamTarget() != null;
		return true;
    }
    public void render(ParticlePoint entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight)
    {
		super.render(entity, entityYaw, partialTicks, stack, buffer, packedLight);
		stack.pushPose();
		Minecraft mc = Minecraft.getInstance();
		{
			if(entity.getPType()>0){
				stack.scale(entity.particleSize,entity.particleSize,entity.particleSize);
				dun.setRender(gl, null, stack, 0xF000F0);
				stack.pushPose();
				//stack.translate(0F, 0F, 6F);
				long time = (long)((double)Util.getMillis() * Minecraft.getInstance().options.glintSpeed().get() * 20.0);
				float u = (float)(time % 110000L) / 110000.0f;
				float v = (float)(time % 30000L) / 30000.0f;
				Matrix4f move = new Matrix4f().translation(u, v, 0.0f);
				Matrix4f move1 = new Matrix4f().translation(-u, -v, 0);
				Matrix4f move2 = new Matrix4f().translation(-u, v, 0);
				Matrix4f move3 = new Matrix4f().translation(u, -v, 0);
				RenderSystem.setShaderColor(1F, 1F, 1F, entity.particleAlpha*0.6F);
				RenderSystem.setTextureMatrix(move);
				dun.renderPart("dun");
				//stack.translate(8F, 0.75F, 0.0F);
				RenderSystem.setShaderColor(1F, 1F, 1F, entity.particleAlpha*0.3F);
				RenderSystem.setTextureMatrix(move1);
				dun.renderPart("dun");
				//stack.translate(8F, 0.75F, 0.0F);
				RenderSystem.setShaderColor(0.1F, 0.3F, 1F, entity.particleAlpha);
				RenderSystem.setTextureMatrix(move2);
				dun.renderPart("dun");
				//stack.translate(8F, 0.75F, 0.0F);
				RenderSystem.setShaderColor(0.1F, 0.3F, 1F, entity.particleAlpha*0.5F);
				RenderSystem.setTextureMatrix(move3);
				dun.renderPart("dun");
				RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
				RenderSystem.resetTextureMatrix();
				stack.popPose();
			}
		}
		stack.popPose();
	}
}