package advancearmy.render.mob;

import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
//import net.minecraft.client.renderer.entity.model.SkeletonModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import advancearmy.entity.mob.ERO_Skeleton;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.util.Mth;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import com.mojang.math.Axis;
import org.lwjgl.opengl.GL12;
import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.world.entity.Entity;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
@OnlyIn(Dist.CLIENT)
public class SkeletonRenderer extends HumanoidMobRenderer<ERO_Skeleton, SkeletonModel<ERO_Skeleton>> {
	private static final ResourceLocation SKELETON_LOCATION = ResourceLocation.tryParse("advancearmy:textures/mob/ero/evil_skeleton.png");
    public SkeletonRenderer(EntityRendererProvider.Context context) {
        this(context, ModelLayers.SKELETON, ModelLayers.SKELETON_INNER_ARMOR, ModelLayers.SKELETON_OUTER_ARMOR);
    }

    public SkeletonRenderer(EntityRendererProvider.Context context, ModelLayerLocation $$1, ModelLayerLocation $$2, ModelLayerLocation $$3) {
        super(context, new SkeletonModel(context.bakeLayer($$1)), 0.5f);
        this.addLayer(new HumanoidArmorLayer(this, new SkeletonModel(context.bakeLayer($$2)), new SkeletonModel(context.bakeLayer($$3)), context.getModelManager()));
    }

    /**
     * Returns the location of an entity's texture.
     */
    @Override
    public ResourceLocation getTextureLocation(ERO_Skeleton entity) {
        return SKELETON_LOCATION;
    }

    @Override
    protected boolean isShaking(ERO_Skeleton entity) {
        return false;
    }
}