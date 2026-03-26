package advancearmy.entity.ai;

import javax.annotation.Nullable;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.phys.Vec3;
import advancearmy.entity.EntitySA_SoldierBase;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.Goal;

public class WaterAvoidingRandomWalkingGoalSA extends RandomStrollGoal {
   protected final float probability;

   public WaterAvoidingRandomWalkingGoalSA(PathfinderMob p_i47301_1_, double p_i47301_2_) {
      this(p_i47301_1_, p_i47301_2_, 0.001F);
   }

   public WaterAvoidingRandomWalkingGoalSA(PathfinderMob p_i47302_1_, double p_i47302_2_, float p_i47302_4_) {
      super(p_i47302_1_, p_i47302_2_);
      this.probability = p_i47302_4_;
   }
   public boolean canContinueToUse() {
      return !this.mob.getNavigation().isDone() && !this.mob.isVehicle() && mob.getTarget()==null;
   }
   @Nullable
   protected Vec3 getPosition() {
      if (this.mob.isInWaterOrBubble()) {
         Vec3 vector3d = LandRandomPos.getPos(this.mob, 15, 7);
         return vector3d == null ? super.getPosition() : vector3d;
      } else {
		  if(mob.getTarget()==null){
			  return this.mob.getRandom().nextFloat() >= this.probability ? LandRandomPos.getPos(this.mob, 10, 7) : super.getPosition();
		  }else{
			  return null;
		  }
      }
   }
}