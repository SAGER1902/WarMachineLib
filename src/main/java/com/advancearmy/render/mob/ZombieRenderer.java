package advancearmy.render.mob;

//import net.minecraft.client.renderer.entity.model.ZombieModel;
//import net.minecraft.world.entity.monster.ZombieEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelLayerLocation;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.Mth;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import com.mojang.math.Axis;
import org.lwjgl.opengl.GL12;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import advancearmy.entity.mob.ERO_Zombie;
import advancearmy.entity.mob.ERO_Husk;
import advancearmy.entity.mob.ERO_Giant;
@OnlyIn(Dist.CLIENT)
public class ZombieRenderer extends AbstractZombieRenderer<PathfinderMob, ZombieModel<PathfinderMob>> {
	private float size=1F;
    public ZombieRenderer(EntityRendererProvider.Context context) {
        this(context, ModelLayers.ZOMBIE, ModelLayers.ZOMBIE_INNER_ARMOR, ModelLayers.ZOMBIE_OUTER_ARMOR);
    }

    public ZombieRenderer(EntityRendererProvider.Context context, ModelLayerLocation zombieLayer, ModelLayerLocation innerArmor, ModelLayerLocation outerArmor) {
        super(context, new ZombieModel(context.bakeLayer(zombieLayer)), new ZombieModel(context.bakeLayer(innerArmor)), new ZombieModel(context.bakeLayer(outerArmor)));
    }
	
    /*@Override
    protected void scale(PathfinderMob livingEntity, PoseStack matrixStack, float partialTickTime) {
		if(livingEntity instanceof ERO_Giant)this.size=10F;
        matrixStack.scale(this.size, this.size, this.size);
    }*/
}