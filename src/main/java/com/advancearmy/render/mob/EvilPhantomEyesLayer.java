package advancearmy.render.mob;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import advancearmy.entity.mob.ERO_Phantom;
import net.minecraft.world.entity.Entity;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
@OnlyIn(Dist.CLIENT)
public class EvilPhantomEyesLayer<T extends ERO_Phantom> extends EyesLayer<T, PhantomModel<T>> {
   private static final RenderType PHANTOM_EYES = RenderType.eyes(ResourceLocation.tryParse("advancearmy:textures/mob/phantom_eyes.png"));

    public EvilPhantomEyesLayer(RenderLayerParent<T, PhantomModel<T>> renderer) {
        super(renderer);
    }

    @Override
    public RenderType renderType() {
        return PHANTOM_EYES;
    }
}