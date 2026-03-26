package advancearmy.entity.ai;
import advancearmy.entity.mob.ERO_Creeper;
import java.util.EnumSet;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
public class CreeperSwellGoalSA extends Goal {
   private final ERO_Creeper creeper;
   private LivingEntity target;

   public CreeperSwellGoalSA(ERO_Creeper p_i1655_1_) {
      this.creeper = p_i1655_1_;
      this.setFlags(EnumSet.of(Goal.Flag.MOVE));
   }

   public boolean canUse() {
      LivingEntity livingentity = this.creeper.getTarget();
      return this.creeper.getSwellDir() > 0 || livingentity != null && this.creeper.distanceToSqr(livingentity) < 25.0D;
   }

   public void start() {
      this.creeper.getNavigation().stop();
      this.target = this.creeper.getTarget();
   }

   public void stop() {
      this.target = null;
	  this.creeper.setTarget(null);
   }

   public void tick() {
      if (this.target == null) {
         this.creeper.setSwellDir(-1);
      } else if (this.creeper.distanceToSqr(this.target) > 49.0D) {
         this.creeper.setSwellDir(-1);
      } else if (!this.creeper.getSensing().hasLineOfSight(this.target)) {
         this.creeper.setSwellDir(-1);
      } else {
         this.creeper.setSwellDir(1);
      }
   }
}