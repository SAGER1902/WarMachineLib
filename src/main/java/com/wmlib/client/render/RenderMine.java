package wmlib.client.render;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import wmlib.common.bullet.EntityMine;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

import wmlib.client.obj.SAObjModel;

import net.minecraft.client.renderer.entity.MobRenderer;

import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.math.vector.Vector3d;
import wmlib.client.render.SARenderHelper;
import wmlib.client.render.SARenderHelper.RenderTypeSA;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import com.mojang.blaze3d.systems.RenderSystem;

@OnlyIn(Dist.CLIENT)
public class RenderMine extends MobRenderer<EntityMine, ModelNone<EntityMine>>
{
	public ResourceLocation tex = new ResourceLocation("wmlib:textures/entity/mine.png");
    private static final SAObjModel obj = new SAObjModel("wmlib:textures/entity/mine.obj");
	
    public RenderMine(EntityRendererManager renderManagerIn)
    {
    	super(renderManagerIn, new ModelNone(),0);
    }

    public ResourceLocation getTextureLocation(EntityMine entity)
    {
		return tex;
    }
	
    float iii;
    public void render(EntityMine entity, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn)
    {
		super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
		{
	    matrixStackIn.pushPose();

		GL11.glPushMatrix();

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
    	Minecraft.getInstance().getTextureManager().bind(tex);
		net.minecraftforge.client.event.EntityViewRenderEvent.CameraSetup cameraSetup = net.minecraftforge.client.ForgeHooksClient.onCameraSetup(mc.gameRenderer, activeRenderInfoIn, partialTicks);
		activeRenderInfoIn.setAnglesInternal(cameraSetup.getYaw(), cameraSetup.getPitch());
		GL11.glRotatef(cameraSetup.getRoll(), 0.0F, 0.0F, 1.0F);
		GL11.glRotatef(activeRenderInfoIn.getXRot(), 1.0F, 0.0F, 0.0F);
		GL11.glRotatef(activeRenderInfoIn.getYRot() + 180.0F, 0.0F, 1.0F, 0.0F);
		GL11.glTranslatef((float) xIn, (float) yIn, (float) zIn);
		
		GL11.glRotatef(180F, 0.0F, 1.0F, 0.0F);
		/*float size = partialTicks * 0.5F + 1;//0.4F
		GlStateManager._scalef(size, size, size);*/
		GL11.glPushMatrix();//glstart
		
		if(entity.getMineID()==2||entity.getMineID()==4){
			GL11.glColor3f(1F, 0.1F, 0.1F);
		}
		
		//SARenderHelper.enableBlendMode(RenderTypeSA.ADDITIVE);//ALPHA
		if(entity.getMineID()==1||entity.getMineID()==2){
			obj.renderPart("mine2");
		}else{
			obj.renderPart("mine1");
		}
		//obj.renderPart("mineq");
		//SARenderHelper.disableBlendMode(RenderTypeSA.ADDITIVE);//ADDITIVE

		GL11.glPopMatrix();//glend
		GL11.glColor3f(1F, 1F, 1F);

		GL11.glPopMatrix();
	    matrixStackIn.popPose();
		}
	}
}