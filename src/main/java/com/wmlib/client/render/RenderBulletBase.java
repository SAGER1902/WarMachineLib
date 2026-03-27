package wmlib.client.render;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.matrix.MatrixStack;
import wmlib.common.bullet.EntityBulletBase;
import wmlib.common.bullet.EntityMissile;
import net.minecraft.entity.Entity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;

import wmlib.client.render.SARenderHelper;
import wmlib.client.render.SARenderHelper.RenderTypeSA;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import wmlib.client.obj.SAObjModel;
import net.minecraft.client.renderer.ActiveRenderInfo;

public class RenderBulletBase extends EntityRenderer<EntityBulletBase>
{
	public SAObjModel obj = new SAObjModel("wmlib:textures/entity/bullet/bullet.obj");
	public ResourceLocation tex = new ResourceLocation("wmlib:textures/entity/bullet/bullet.png");

    public RenderBulletBase(EntityRendererManager renderManager)
    {
        super(renderManager);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityBulletBase entity)
    {
        return null;
    }

	public ResourceLocation glowtex = ResourceLocation.tryParse("wmlib:textures/entity/glow.png");
    private static final SAObjModel glowobj = new SAObjModel("wmlib:textures/entity/glow.obj");
    @Override
    public void render(EntityBulletBase entity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int light)
    {
		if(entity.obj!=null){
			obj = entity.obj;
		}
		if(entity.tex!=null){
			tex = entity.tex;
		}
        if(entity.time <= 1)
        {
            return;
        }
		Minecraft mc = Minecraft.getInstance();
		ActiveRenderInfo activeRenderInfoIn = Minecraft.getInstance().getEntityRenderDispatcher().camera;
		activeRenderInfoIn.setup(mc.level, (Entity)(mc.getCameraEntity() == null ? mc.player : mc.getCameraEntity()), 
		!mc.options.getCameraType().isFirstPerson(), mc.options.getCameraType().isMirrored(), partialTicks);
		Vector3d avector3d = activeRenderInfoIn.getPosition();
		double camx = avector3d.x();
		double camy = avector3d.y();
		double camz = avector3d.z();
		double d0 = MathHelper.lerp((double)partialTicks, entity.xOld, entity.getX());
		double d1 = MathHelper.lerp((double)partialTicks, entity.yOld, entity.getY());
		double d2 = MathHelper.lerp((double)partialTicks, entity.zOld, entity.getZ());
		double xIn = d0 - camx;
		double yIn = d1 - camy;
		double zIn = d2 - camz;
        matrixStack.pushPose();
        {
			GL11.glPushMatrix();//glstart
			net.minecraftforge.client.event.EntityViewRenderEvent.CameraSetup cameraSetup = net.minecraftforge.client.ForgeHooksClient.onCameraSetup(mc.gameRenderer, activeRenderInfoIn, partialTicks);
			activeRenderInfoIn.setAnglesInternal(cameraSetup.getYaw(), cameraSetup.getPitch());
			GL11.glRotatef(cameraSetup.getRoll(), 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(activeRenderInfoIn.getXRot(), 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(activeRenderInfoIn.getYRot() + 180.0F, 0.0F, 1.0F, 0.0F);
			
			GL11.glTranslatef((float) xIn, (float) yIn, (float) zIn);
			GlStateManager._rotatef(entity.yRotO + (entity.yRot-entity.yRotO)*partialTicks, 0.0F, 1.0F, 0.0F);
			GlStateManager._rotatef(-entity.xRot, 1.0F, 0.0F, 0.0F);
			mc.getTextureManager().bind(tex);
			obj.renderPart("bullet_no_rote");
			GlStateManager._disableCull();//
			//GlStateManager._depthMask(false);//
			
			
			GL11.glPushMatrix();//glstart
			GlStateManager._rotatef(entity.time, 0.0F, 0.0F, 1.0F);
			obj.renderPart("bullet");
			GL11.glPopMatrix();//glend
			
			SARenderHelper.enableBlendMode(RenderTypeSA.ADDITIVE);//
			
			if(entity instanceof EntityMissile){
				mc.getTextureManager().bind(glowtex);
				GlStateManager._pushMatrix();
				float size = partialTicks * 0.8F + 1;//0.4F
				GlStateManager._scalef(size, size, size);
				EntityRendererManager manager = mc.getEntityRenderDispatcher();
				GlStateManager._rotatef(-manager.camera.getYRot()+180F, 0.0F, 1.0F, 0.0F);
				GlStateManager._rotatef(-manager.camera.getXRot(), 1.0F, 0.0F, 0.0F);
				glowobj.renderPart("glow");
				GlStateManager._popMatrix();
				mc.getTextureManager().bind(tex);
			}
			float speed = (float)entity.getDeltaMovement().x;
			speed = Math.min(speed, 2.5f);
			float size = partialTicks * 0.5F + 1;//0.4F
			float size2 = partialTicks * 0.8F + 1;//0.4F
			float dynamicFactor = entity.level.random.nextInt(4) * 0.3F + speed;
			float size3 = Math.max(dynamicFactor, 2f);
			GlStateManager._pushMatrix();
			GlStateManager._scalef(size2, size2, size2);
			if(entity.time>1)obj.renderPart("fire");
			GlStateManager._popMatrix();
			GlStateManager._pushMatrix();
			GlStateManager._scalef(size, size, size3);
			if(entity.time>1)obj.renderPart("trail");
			GlStateManager._popMatrix();
			//GlStateManager._depthMask(true);
			GlStateManager._enableCull();
			SARenderHelper.disableBlendMode(RenderTypeSA.ADDITIVE);//
			GL11.glPopMatrix();//glend
        }
        matrixStack.popPose();
    }
}
