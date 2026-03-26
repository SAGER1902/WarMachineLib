package advancearmy.render.mob;

import com.mojang.blaze3d.vertex.PoseStack;
//import net.minecraft.client.model.PhantomModel;
import advancearmy.entity.mob.ERO_Phantom;
import net.minecraft.resources.ResourceLocation;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import wmlib.client.obj.SAObjModel;

import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.math.Axis;
import org.lwjgl.opengl.GL12;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.Mth;
import net.minecraft.core.BlockPos;
import wmlib.client.render.SARenderHelper;
import wmlib.client.render.SARenderHelper.RenderTypeSA;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.RenderType;
import com.mojang.blaze3d.vertex.VertexConsumer;
import wmlib.client.render.SARenderState;
import org.joml.Matrix4f;

@OnlyIn(Dist.CLIENT)
public class EvilPhantomRenderer extends MobRenderer<ERO_Phantom, PhantomModel<ERO_Phantom>> {
	private static final ResourceLocation PHANTOM_LOCATION = ResourceLocation.tryParse("advancearmy:textures/mob/ero/phantom.png");
	private static final ResourceLocation PHANTOM_EXP = ResourceLocation.tryParse("advancearmy:textures/mob/ero/phantom2.png");
	private static final ResourceLocation PHANTOM_LASER = ResourceLocation.tryParse("advancearmy:textures/mob/ero/phantom3.png");
	public EvilPhantomRenderer(EntityRendererProvider.Context context) {
        super(context, new PhantomModel(context.bakeLayer(ModelLayers.PHANTOM)), 0.75f);
        //this.addLayer(new EvilPhantomEyesLayer<ERO_Phantom>(this));
    }
	
	/*public ResourceLocation tex = ResourceLocation.tryParse("advancearmy:textures/mob/soldier/aoh.png");
    private static final SAObjModel obj = new SAObjModel("advancearmy:textures/mob/soldier/aoh.obj");
	RenderType rt = SARenderState.getBlendDepthWrite(tex);
    public void render(ERO_Phantom entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource bufferIn, int packedLight)
    {
		super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLight);
		obj.setRender(rt, null, stack, packedLight);
		obj.renderPart("head");
	}*/
	
	@Override
	public ResourceLocation getTextureLocation(ERO_Phantom entity) {
	   if(entity.getEvilPhantomSize()<3){
		   return PHANTOM_EXP;
	   }else if(entity.getEvilPhantomSize()<6){
		   return PHANTOM_LOCATION;
	   }else{
		   return PHANTOM_LASER;
	   }
	}

    @Override
    protected void scale(ERO_Phantom livingEntity, PoseStack matrixStack, float partialTickTime) {
        int $$3 = livingEntity.getEvilPhantomSize();
        float $$4 = 1.0f + 0.15f * (float)$$3;
        matrixStack.scale($$4, $$4, $$4);
        matrixStack.translate(0.0f, 1.3125f, 0.1875f);
    }

    @Override
    protected void setupRotations(ERO_Phantom entityLiving, PoseStack matrixStack, float ageInTicks, float rotationYaw, float partialTicks) {
        super.setupRotations(entityLiving, matrixStack, ageInTicks, rotationYaw, partialTicks);
        matrixStack.mulPose(Axis.XP.rotationDegrees(entityLiving.getXRot()));
    }
}