package advancearmy.render.mob;

import com.mojang.blaze3d.vertex.PoseStack;
//import net.minecraft.client.renderer.entity.layers.SpiderChargeLayer;
//import net.minecraft.client.renderer.entity.model.RavagerModel;
//import net.minecraft.world.entity.monster.SpiderEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import advancearmy.entity.mob.ERO_Ravager;

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
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
@OnlyIn(Dist.CLIENT)
public class RavagerRenderer<T extends ERO_Ravager>extends MobRenderer<T, RavagerModel<T>> {
    private static final ResourceLocation TEXTURE_LOCATION = ResourceLocation.tryParse("advancearmy:textures/mob/ero/ero_ravager.png");
    public RavagerRenderer(EntityRendererProvider.Context context) {
        super(context, new RavagerModel(context.bakeLayer(ModelLayers.RAVAGER)), 1.1f);
    }
	@Override
    protected void scale(ERO_Ravager livingEntity, PoseStack matrixStack, float partialTickTime) {
        matrixStack.scale(2F, 2F, 2F);
    }
    /**
     * Returns the location of an entity's texture.
     */
    @Override
    public ResourceLocation getTextureLocation(ERO_Ravager entity) {
        return TEXTURE_LOCATION;
    }
}