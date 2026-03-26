package wmlib.client.render;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;

import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import wmlib.client.obj.SAObjModel;
import wmlib.common.bullet.EntityBulletBase;
import wmlib.common.bullet.EntityMissile;
import wmlib.client.render.SARenderHelper;
import wmlib.client.render.SARenderHelper.RenderTypeSA;
import wmlib.client.render.SARenderState;
import wmlib.client.render.RenderTypeVehicle;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderStateShard.TextureStateShard;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.*;
import java.awt.*;
import java.util.function.Function;

public class RenderBulletBase extends EntityRenderer<EntityBulletBase> {
    public SAObjModel obj = new SAObjModel("wmlib:textures/entity/bullet/bullet.obj");
    public ResourceLocation tex = ResourceLocation.tryParse("wmlib:textures/entity/bullet/bullet.png");
	
	public ResourceLocation glowtex = ResourceLocation.tryParse("wmlib:textures/entity/glow.png");
    private static final SAObjModel glowobj = new SAObjModel("wmlib:textures/entity/glow.obj");
    public RenderBulletBase(EntityRendererProvider.Context context) {
        super(context);
    }
    @Override
    public ResourceLocation getTextureLocation(EntityBulletBase entity) {
        return tex;
    }
	
	RenderType rt;
	RenderType rtglow;
	RenderType glow = SARenderState.getBlendGlow(glowtex);
    @Override
    public void render(EntityBulletBase entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight) {
        if (entity.obj != null) {
            obj = entity.obj;
        }
        if (entity.tex != null) {
            tex = entity.tex;
        }
		Minecraft mc = Minecraft.getInstance();
		EntityRenderDispatcher dispatcher = mc.getEntityRenderDispatcher();
		
        if (entity.time > 1) {
			if(entity instanceof EntityMissile){
				stack.pushPose();
				float size = partialTicks * 0.8F + 1;//0.4F
				stack.scale(size, size, size);
				stack.mulPose(Axis.YP.rotationDegrees(-dispatcher.camera.getYRot() + 180F));
				stack.mulPose(Axis.XP.rotationDegrees(-dispatcher.camera.getXRot()));
				glowobj.setRender(glow, null, stack, 0xF000F0);
				glowobj.renderPart("glow");
				stack.popPose();
			}
			
			rt = RenderTypeVehicle.objrender(tex);
			rtglow = SARenderState.getBlendDepthWrite(tex);
			stack.pushPose();
			{
				stack.mulPose(Axis.YP.rotationDegrees(entity.yRotO + (entity.getYRot() - entity.yRotO) * partialTicks));
				stack.mulPose(Axis.XP.rotationDegrees(-entity.getXRot()));
				
				if(entity.time%10==0){
					obj.setRender(rtglow, null, stack, packedLight);
					obj.renderPart("warn");
				}
				
				obj.setRender(rt, null, stack, packedLight);
				//RenderSystem.disableCull();
				obj.renderPart("bullet_no_rote");
				stack.pushPose();
				{
					stack.mulPose(Axis.ZP.rotationDegrees(entity.time));
					obj.renderPart("bullet");
				}
				stack.popPose();
				float speed = (float) entity.getDeltaMovement().x;
				speed = Math.min(speed, 2.5f);
				float size = partialTicks * 0.5f + 1.0f; 
				float size2 = partialTicks * 0.8f + 1.0f;
				float dynamicFactor = entity.level().getRandom().nextInt(4) * 0.3f + speed;
				float size3 = Math.max(dynamicFactor, 2f);
				obj.setRender(rtglow, null, stack, 0xF000F0);
				stack.pushPose();
				stack.scale(size2, size2, size2);
				obj.renderPart("fire");
				stack.popPose();

				stack.pushPose();
				//obj.renderPart("trail_normal");
				stack.scale(size, size, size3);
				obj.renderPart("trail");
				stack.popPose();
			}
			stack.popPose();
		}
    }
}