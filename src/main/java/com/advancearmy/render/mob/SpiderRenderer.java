package advancearmy.render.mob;

import com.mojang.blaze3d.vertex.PoseStack;
//import net.minecraft.client.renderer.entity.layers.SpiderChargeLayer;
//import net.minecraft.client.renderer.entity.model.SpiderModel;
//import net.minecraft.world.entity.monster.SpiderEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import advancearmy.entity.mob.ERO_Spider;

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
public class SpiderRenderer<T extends ERO_Spider>extends MobRenderer<T, SpiderModel<T>> {
	private static final ResourceLocation SPIDER_LOCATION = ResourceLocation.tryParse("advancearmy:textures/mob/ero/evil_spider.png");
	private static final ResourceLocation SPIDER_LOCATION2 = ResourceLocation.tryParse("advancearmy:textures/mob/ero/evil_spider2.png");
    public SpiderRenderer(EntityRendererProvider.Context context) {
        this(context, ModelLayers.SPIDER);
    }

    public SpiderRenderer(EntityRendererProvider.Context context, ModelLayerLocation layer) {
        super(context, new SpiderModel(context.bakeLayer(layer)), 0.8f);
        this.addLayer(new SpiderChargeLayer(this));
    }
	protected boolean isShaking(T entity) {
		return entity.getArmorValue()>5D && entity.isAggressive();
	}
	
    /*@Override
    protected float getFlipDegrees(T livingEntity) {
        return 180.0f;
    }*/
	/*float size = 1F;
	@Override
    protected void scale(ERO_Spider livingEntity, PoseStack matrixStack, float partialTickTime) {
		if(livingEntity.randomType==2)this.size=2F;
        matrixStack.scale(this.size, this.size, this.size);
    }*/
    /**
     * Returns the location of an entity's texture.
     */
    @Override
    public ResourceLocation getTextureLocation(T entity) {
		if(entity.getArmorValue()>5D)return SPIDER_LOCATION2;
        return SPIDER_LOCATION;
    }
}