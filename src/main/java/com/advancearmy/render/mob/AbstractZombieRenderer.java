package advancearmy.render.mob;
import java.util.List;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
//import net.minecraft.client.renderer.entity.model.ZombieModel;
//import net.minecraft.world.entity.monster.ZombieEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
//import advancearmy.entity.mob.ERO_Zombie;
import advancearmy.entity.mob.ERO_Husk;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;

import net.minecraft.world.entity.PathfinderMob;
@OnlyIn(Dist.CLIENT)
public abstract class AbstractZombieRenderer<T extends PathfinderMob, M extends ZombieModel<T>>extends HumanoidMobRenderer<T, M> {
	private static final ResourceLocation HUSK_ENEMY = ResourceLocation.tryParse("advancearmy:textures/mob/ero/evil_husk.png");
	private static final ResourceLocation ZOMBIE_ENEMY = ResourceLocation.tryParse("advancearmy:textures/mob/ero/evil_zombie.png");
    protected AbstractZombieRenderer(EntityRendererProvider.Context context, M model, M innerModel, M outerModel) {
        super(context, model, 0.5f);
        this.addLayer(new HumanoidArmorLayer(this, innerModel, outerModel, context.getModelManager()));
    }

	public ResourceLocation getTextureLocation(PathfinderMob ent) {
		if(ent instanceof ERO_Husk){
			return HUSK_ENEMY;
		}else{
			return ZOMBIE_ENEMY;
		}
	}

	protected boolean isShaking(T p_230495_1_) {
		//return false;
		return true;
		//return p_230495_1_.isUnderWaterConverting();
	}
	

	/*public void render(T entity, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn)
	{
		super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}*/
}