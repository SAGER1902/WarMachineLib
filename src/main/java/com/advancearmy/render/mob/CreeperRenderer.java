package advancearmy.render.mob;

import com.mojang.blaze3d.vertex.PoseStack;
//import net.minecraft.client.renderer.entity.layers.CreeperChargeLayer;
//import net.minecraft.client.renderer.entity.model.CreeperModel;
//import net.minecraft.world.entity.monster.CreeperEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import advancearmy.entity.mob.ERO_Creeper;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.Mth;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import com.mojang.math.Axis;
import org.lwjgl.opengl.GL12;
import com.mojang.blaze3d.platform.GlStateManager;
import wmlib.client.obj.SAObjModel;
import net.minecraft.world.entity.Entity;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
@OnlyIn(Dist.CLIENT)
public class CreeperRenderer extends MobRenderer<ERO_Creeper, CreeperModel<ERO_Creeper>> {
   private static final ResourceLocation CREEPER_LOCATION = ResourceLocation.tryParse("advancearmy:textures/mob/ero/evil_creeper.png");
   public CreeperRenderer(EntityRendererProvider.Context context) {
        super(context, new CreeperModel(context.bakeLayer(ModelLayers.CREEPER)), 0.5f);
        this.addLayer(new CreeperChargeLayer(this, context.getModelSet()));
    }

    @Override
    protected void scale(ERO_Creeper livingEntity, PoseStack matrixStack, float partialTickTime) {
        float $$3 = livingEntity.getSwelling(partialTickTime);
        float $$4 = 1.0f + Mth.sin($$3 * 100.0f) * $$3 * 0.01f;
        $$3 = Mth.clamp($$3, 0.0f, 1.0f);
        $$3 *= $$3;
        $$3 *= $$3;
        float $$5 = (1.0f + $$3 * 0.4f) * $$4;
        float $$6 = (1.0f + $$3 * 0.1f) / $$4;
        matrixStack.scale($$5, $$6, $$5);
    }

    @Override
    protected float getWhiteOverlayProgress(ERO_Creeper livingEntity, float partialTicks) {
        float $$2 = livingEntity.getSwelling(partialTicks);
        if ((int)($$2 * 10.0f) % 2 == 0) {
            return 0.0f;
        }
        return Mth.clamp($$2, 0.5f, 1.0f);
    }

    /**
     * Returns the location of an entity's texture.
     */
    @Override
    public ResourceLocation getTextureLocation(ERO_Creeper entity) {
        return CREEPER_LOCATION;
    }
}