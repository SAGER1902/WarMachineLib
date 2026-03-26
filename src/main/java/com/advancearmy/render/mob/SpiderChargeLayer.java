package advancearmy.render.mob;
import net.minecraft.client.model.EntityModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.world.entity.Entity;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.RenderType;
import advancearmy.entity.mob.ERO_Spider;
@OnlyIn(Dist.CLIENT)
public class SpiderChargeLayer<T extends Entity, M extends SpiderModel<T>>extends EyesLayer<T, M> {
    private static final RenderType SPIDER_EYES = RenderType.eyes(ResourceLocation.tryParse("textures/entity/spider_eyes.png"));

    public SpiderChargeLayer(RenderLayerParent<T, M> renderer) {
        super(renderer);
    }

    @Override
    public RenderType renderType() {
        return SPIDER_EYES;
    }
}