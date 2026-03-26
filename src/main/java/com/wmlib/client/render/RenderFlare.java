package wmlib.client.render;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import wmlib.common.bullet.EntityFlare;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.client.Minecraft;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import wmlib.client.obj.SAObjModel;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderer;
import com.mojang.math.Axis;
import net.minecraft.world.phys.Vec3;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import com.mojang.blaze3d.systems.RenderSystem;
import wmlib.client.render.SARenderState;
@OnlyIn(Dist.CLIENT)
public class RenderFlare extends EntityRenderer<EntityFlare>
{
	public ResourceLocation tex = ResourceLocation.tryParse("wmlib:textures/entity/flare.png");
    private static final SAObjModel obj = new SAObjModel("wmlib:textures/entity/flare.obj");
	public ResourceLocation glowtex = ResourceLocation.tryParse("wmlib:textures/entity/glow.png");
    private static final SAObjModel glowobj = new SAObjModel("wmlib:textures/entity/glow.obj");
	RenderType f1 = SARenderState.getBlendDepthWrite(tex);
	RenderType glow = SARenderState.getBlendDepthWrite(glowtex);
    public RenderFlare(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0.5F;
    }

    public ResourceLocation getTextureLocation(EntityFlare entity)
    {
		return tex;
    }
	
    float iii;
    public void render(EntityFlare entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight)
    {
		super.render(entity, entityYaw, partialTicks, stack, buffer, packedLight);
		{
	    stack.pushPose();
		float size = partialTicks * 0.5F + 1;//0.4F
		stack.scale(size, size, size);
		
		Minecraft mc = Minecraft.getInstance();
		EntityRenderDispatcher dispatcher = mc.getEntityRenderDispatcher();
		stack.pushPose();
		stack.mulPose(Axis.YP.rotationDegrees(-dispatcher.camera.getYRot() + 180F));
		stack.mulPose(Axis.XP.rotationDegrees(-dispatcher.camera.getXRot()));
		glowobj.setRender(glow, null, stack, 0xF000F0);
		glowobj.renderPart("glow");
		stack.popPose();
		
		obj.setRender(f1, null, stack, 0xF000F0);
		obj.renderPart("flare");

	    stack.popPose();
		}
	}
}