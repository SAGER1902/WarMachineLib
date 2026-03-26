package advancearmy.render.mob;
import net.minecraft.client.model.EntityModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.client.renderer.entity.layers.EnergySwirlLayer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import advancearmy.entity.mob.ERO_Creeper;
@OnlyIn(Dist.CLIENT)
public class CreeperChargeLayer extends EnergySwirlLayer<ERO_Creeper, CreeperModel<ERO_Creeper>> {
	private static final ResourceLocation POWER_LOCATION = ResourceLocation.tryParse("textures/entity/creeper/creeper_armor.png");
    private final CreeperModel<ERO_Creeper> model;

    public CreeperChargeLayer(RenderLayerParent<ERO_Creeper, CreeperModel<ERO_Creeper>> renderer, EntityModelSet $$1) {
        super(renderer);
        this.model = new CreeperModel($$1.bakeLayer(ModelLayers.CREEPER_ARMOR));
    }

    @Override
    protected float xOffset(float $$0) {
        return $$0 * 0.01f;
    }

    @Override
    protected ResourceLocation getTextureLocation() {
        return POWER_LOCATION;
    }

    @Override
    protected EntityModel<ERO_Creeper> model() {
        return this.model;
    }
}