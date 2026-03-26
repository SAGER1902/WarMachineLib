package advancearmy.render.mob;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import advancearmy.entity.mob.EvilPortal;

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
public class PortalRenderer extends EntityRenderer<EvilPortal> {
    private static final ResourceLocation END_CRYSTAL_LOCATION = ResourceLocation.tryParse("textures/entity/end_crystal/end_crystal.png");
    private static final RenderType RENDER_TYPE = RenderType.entityCutoutNoCull(END_CRYSTAL_LOCATION);
	
	private static final SAObjModel obj = new SAObjModel("wmlib:textures/misc/portal.obj");
	private static final ResourceLocation EvilPortalTex = ResourceLocation.tryParse("wmlib:textures/misc/portal1.png");
	private static final ResourceLocation EvilPortalTex2 = ResourceLocation.tryParse("wmlib:textures/misc/portal2.png");
	private static final ResourceLocation EvilPortalTex3 = ResourceLocation.tryParse("wmlib:textures/misc/portal3.png");
    RenderType RENDER_TYPE1 = SARenderState.getPortal(EvilPortalTex,EvilPortalTex2);
	RenderType RENDER_TYPE2 = SARenderState.getPortal(EvilPortalTex,EvilPortalTex3);
	
	public ResourceLocation getTextureLocation(EvilPortal entity)
    {
		return EvilPortalTex;
    }
    private static final float SIN_45 = (float)Math.sin(0.7853981633974483);
    private static final String GLASS = "glass";
    private static final String BASE = "base";
    private final ModelPart cube;
    private final ModelPart glass;
    private final ModelPart base;
	public PortalRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0.5f;
        ModelPart $$1 = context.bakeLayer(ModelLayers.END_CRYSTAL);
        this.glass = $$1.getChild(GLASS);
        this.cube = $$1.getChild("cube");
        this.base = $$1.getChild(BASE);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition $$0 = new MeshDefinition();
        PartDefinition $$1 = $$0.getRoot();
        $$1.addOrReplaceChild(GLASS, CubeListBuilder.create().texOffs(0, 0).addBox(-4.0f, -4.0f, -4.0f, 8.0f, 8.0f, 8.0f), PartPose.ZERO);
        $$1.addOrReplaceChild("cube", CubeListBuilder.create().texOffs(32, 0).addBox(-4.0f, -4.0f, -4.0f, 8.0f, 8.0f, 8.0f), PartPose.ZERO);
        $$1.addOrReplaceChild(BASE, CubeListBuilder.create().texOffs(0, 16).addBox(-6.0f, 0.0f, -6.0f, 12.0f, 4.0f, 12.0f), PartPose.ZERO);
        return LayerDefinition.create($$0, 64, 32);
    }
    @Override
    public void render(EvilPortal entity, float entityYaw, float partialTick, PoseStack stack, MultiBufferSource buffer, int packedLight) {
		stack.pushPose();
		float $$6 = PortalRenderer.getY(entity, partialTick);
		float $$7 = ((float)entity.rote + partialTick) * 3.0f;
		VertexConsumer cubetype = buffer.getBuffer(RENDER_TYPE);
		stack.pushPose();
		stack.scale(2.0f, 2.0f, 2.0f);
		stack.translate(0.0f, 0, 0.0f);
		int over = OverlayTexture.NO_OVERLAY;
		this.base.render(stack, cubetype, packedLight, over);
		stack.mulPose(Axis.YP.rotationDegrees($$7));
		stack.translate(0.0f, 1.5f + $$6 / 2.0f, 0.0f);
		stack.mulPose(new Quaternionf().setAngleAxis(1.0471976f, SIN_45, 0.0f, SIN_45));
		this.glass.render(stack, cubetype, packedLight, over);
		float $$10 = 0.875f;
		stack.scale(0.875f, 0.875f, 0.875f);
		stack.mulPose(new Quaternionf().setAngleAxis(1.0471976f, SIN_45, 0.0f, SIN_45));
		stack.mulPose(Axis.YP.rotationDegrees($$7));
		VertexConsumer eye = buffer.getBuffer(RENDER_TYPE1);
		this.glass.render(stack, eye, packedLight, over);
		stack.scale(0.875f, 0.875f, 0.875f);
		stack.mulPose(new Quaternionf().setAngleAxis(1.0471976f, SIN_45, 0.0f, SIN_45));
		stack.mulPose(Axis.YP.rotationDegrees($$7));
		VertexConsumer eye1 = buffer.getBuffer(RENDER_TYPE2);
		this.cube.render(stack, eye1, packedLight, over);
		stack.popPose();
		
		stack.translate(0, 6F, 0);
        /*BlockPos $$11 = entity.getBeamTarget();
        if ($$11 != null) {
            float $$12 = (float)$$11.getX() + 0.5f;
            float $$13 = (float)$$11.getY() + 0.5f;
            float $$14 = (float)$$11.getZ() + 0.5f;
            float $$15 = (float)((double)$$12 - entity.getX());
            float $$16 = (float)((double)$$13 - entity.getY());
            float $$17 = (float)((double)$$14 - entity.getZ());
            stack.translate($$15, $$16, $$17);
            EnderDragonRenderer.renderCrystalBeams(-$$15, -$$16 + $$6, -$$17, partialTick, entity.summonTime, stack, buffer, packedLight);
        }*/
		
		
		/*ShaderInstance shader = ClientRenderHandler.CUSTOM_PORTAL_SHADER;
		if (shader != null) {
			// 激活着色器
			shader.apply();
			// 设置uniform
			shader.safeGetUniform("EndPortalLayers").set(15); // 设置层数
			// 注意：GameTime等uniform可能由Minecraft自动设置
		}*/
		stack.mulPose(Axis.YP.rotationDegrees(90F));
		if(entity.deathTime > 0) {
			stack.scale(1, (120F-entity.deathTime)/120F, 1);
			RenderSystem.setShaderColor((120F-entity.deathTime)/120F, (120F-entity.deathTime)/120F, (120F-entity.deathTime)/120F, (120F-entity.deathTime)/120F);
		}else{
			stack.scale(1, entity.startTime/120F, 1);
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
		/*if (shader != null) {
			shader.clear();
		}*/
		RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
		stack.popPose();
        super.render(entity, entityYaw, partialTick, stack, buffer, packedLight);
    }

    public static float getY(EvilPortal endCrystal, float partialTick) {
        float $$2 = (float)endCrystal.summonTime + partialTick;
        float $$3 = Mth.sin($$2 * 0.2f) / 2.0f + 0.5f;
        $$3 = ($$3 * $$3 + $$3) * 0.4f;
        return $$3 - 1.4f;
    }
    @Override
    public boolean shouldRender(EvilPortal entity, Frustum camera, double camX, double camY, double camZ) {
        return super.shouldRender(entity, camera, camX, camY, camZ)||entity.distanceToSqr(camX,camY,camZ)<10;
    }
}