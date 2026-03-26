package advancearmy.render.mob;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import advancearmy.entity.mob.EvilPortalOnce;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import com.mojang.math.Axis;
import org.lwjgl.opengl.GL12;
import com.mojang.blaze3d.platform.GlStateManager;
import wmlib.client.obj.SAObjModel;
import net.minecraft.world.entity.Entity;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.ModelPart;
import java.util.Random;
import java.util.stream.IntStream;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import java.util.List;
import net.minecraft.Util;
import wmlib.client.obj.SAObjModel;
import wmlib.client.render.SARenderState;
import net.minecraft.client.renderer.ShaderInstance;
import wmlib.client.ClientRenderHandler;
import java.util.Random;

import wmlib.client.render.RenderTypeVehicle;
@OnlyIn(Dist.CLIENT)
public class PortalRenderer1 extends EntityRenderer<EvilPortalOnce> {
    private static final ResourceLocation END_CRYSTAL_LOCATION = ResourceLocation.tryParse("textures/entity/end_crystal/end_crystal.png");
    private static final RenderType RENDER_TYPE = RenderType.entityCutoutNoCull(END_CRYSTAL_LOCATION);
	
	private static final SAObjModel obj = new SAObjModel("wmlib:textures/misc/portal.obj");
	private static final ResourceLocation EvilPortalOnceTex = ResourceLocation.tryParse("wmlib:textures/misc/portal1.png");
	private static final ResourceLocation EvilPortalOnceTex2 = ResourceLocation.tryParse("wmlib:textures/misc/portal2.png");
	private static final ResourceLocation EvilPortalOnceTex3 = ResourceLocation.tryParse("wmlib:textures/misc/portal3.png");
    RenderType RENDER_TYPE1 = SARenderState.getPortal(EvilPortalOnceTex,EvilPortalOnceTex2);
	RenderType RENDER_TYPE2 = SARenderState.getPortal(EvilPortalOnceTex,EvilPortalOnceTex3);
	
	public ResourceLocation getTextureLocation(EvilPortalOnce entity)
    {
		return EvilPortalOnceTex;
    }
	public PortalRenderer1(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(EvilPortalOnce entity, float entityYaw, float partialTick, PoseStack stack, MultiBufferSource buffer, int packedLight) {
		stack.pushPose();
		stack.mulPose(Axis.YP.rotationDegrees(90F));
		if(entity.deathTime > 0) {
			stack.scale(1, (60-entity.deathTime)/60F, 1);
			RenderSystem.setShaderColor((60-entity.deathTime)/60F, (60-entity.deathTime)/60F, (60-entity.deathTime)/60F, (60-entity.deathTime)/60F);
		}else{
			stack.scale(1, entity.startTime/60F, 1);
		}
		
		if(entity.getPortalType()%2==0){
			obj.setRender(RENDER_TYPE1, null, stack, 0xF000F0);
		}else{
			obj.setRender(RENDER_TYPE2, null, stack, 0xF000F0);
		}
		{
			String tu1 = String.valueOf(entity.getPortalType());
			if(entity.getPortalType()==0){
				obj.renderPart("type1");
			}else{
				obj.renderPart("type"+ tu1);
			}
		}
		RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
		stack.popPose();
        super.render(entity, entityYaw, partialTick, stack, buffer, packedLight);
    }

    @Override
    public boolean shouldRender(EvilPortalOnce entity, Frustum camera, double camX, double camY, double camZ) {
        return super.shouldRender(entity, camera, camX, camY, camZ)||entity.distanceToSqr(camX,camY,camZ)<10;
    }
}