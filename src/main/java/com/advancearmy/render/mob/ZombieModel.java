package advancearmy.render.mob;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
//import advancearmy.entity.mob.ERO_Zombie;
import net.minecraft.world.entity.PathfinderMob;
@OnlyIn(Dist.CLIENT)
public class ZombieModel<T extends PathfinderMob> extends AbstractZombieModel<T> {
    public ZombieModel(ModelPart root) {
        super(root);
    }

   public boolean isAggressive(T p_212850_1_) {
      return p_212850_1_.isAggressive();
   }
}