package advancearmy.entity.ai;
//import net.minecraft.util.Hand;

import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.PathfinderMob;
public class ZombieAttackGoalSA extends MeleeAttackGoal {
   private final PathfinderMob zombie;
   private int raiseArmTicks;

   public ZombieAttackGoalSA(PathfinderMob p_i46803_1_, double p_i46803_2_, boolean p_i46803_4_) {
      super(p_i46803_1_, p_i46803_2_, p_i46803_4_);
      this.zombie = p_i46803_1_;
   }

   public void start() {
      super.start();
      this.raiseArmTicks = 0;
   }

   public void stop() {
      super.stop();
      this.zombie.setAggressive(false);
   }

   public void tick() {
      super.tick();
      ++this.raiseArmTicks;
      if (this.raiseArmTicks >= 5 && this.getTicksUntilNextAttack() < this.getAttackInterval() / 2) {
         this.zombie.setAggressive(true);
		 //this.zombie.swing(Hand.OFF_HAND);
      } else {
         this.zombie.setAggressive(false);
		//this.zombie.swing(Hand.MAIN_HAND);
      }
   }
}