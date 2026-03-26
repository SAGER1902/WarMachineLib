package advancearmy.entity.ai;

import javax.annotation.Nullable;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.LivingEntity;
import advancearmy.entity.mob.EntityMobSoldierBase;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.scores.Team;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.TamableAnimal;
public abstract class MobTargetGoalSA extends Goal {
   protected final EntityMobSoldierBase mob;
   protected final boolean mustSee;
   private final boolean mustReach;
   private int reachCache;
   private int reachCacheTime;
   private int unseenTicks;
   protected LivingEntity targetMob;
   protected int unseenMemoryTicks = 60;

   public MobTargetGoalSA(EntityMobSoldierBase soldier, boolean ishasLineOfSight) {
      this(soldier, ishasLineOfSight, false);
   }

   public MobTargetGoalSA(EntityMobSoldierBase soldier, boolean ishasLineOfSight, boolean isReach) {
      this.mob = soldier;
      this.mustSee = ishasLineOfSight;
      this.mustReach = isReach;
   }

   public boolean canContinueToUse() {
      LivingEntity livingentity = this.mob.getTarget();
      if (livingentity == null) {
         livingentity = this.targetMob;
      }
      if (livingentity == null) {
         return false;
      } else if (!livingentity.isAlive()) {
         return false;
      } else {
         Team team = this.mob.getTeam();
         Team team1 = livingentity.getTeam();
         if (team != null && team1 == team) {
            return false;
         } else {
            double d0 = this.getFollowDistance();
            if (this.mob.distanceToSqr(livingentity) > d0 * d0) {
               return false;
            } else {
               if (this.mustSee) {
                  if (this.mob.getSensing().hasLineOfSight(livingentity)) {
                     this.unseenTicks = 0;
                  } else if (++this.unseenTicks > this.unseenMemoryTicks) {
                     return false;
                  }
               }
               if (livingentity instanceof Player && ((Player)livingentity).getAbilities().invulnerable) {
                  return false;
               } else {
                  this.mob.setTarget(livingentity);
				  //this.mob.setAttacking(true);
                  return true;
               }
            }
         }
      }
   }

	protected double getAADistance() {
		return this.mob.attack_range_max;//attack_height_max
	}
	protected double getFollowDistance() {
		return this.mob.attack_range_max;
	}

   public void start() {
      this.reachCache = 0;
      this.reachCacheTime = 0;
      this.unseenTicks = 0;
   }

   public void stop() {
      this.mob.setTarget((LivingEntity)null);
      this.targetMob = null;
   }

   protected boolean canAttack(@Nullable LivingEntity attackentity, TargetingConditions predicate) {
      if (attackentity == null) {
         return false;
      } else if (!predicate.test(this.mob, attackentity)) {
         return false;
      } else if (!this.mob.isWithinRestriction(attackentity.blockPosition())) {
         return false;
      } else {
        return true;
      }
   }

   private boolean canReach(LivingEntity attackentity) {
		return true;
   }

   public MobTargetGoalSA setUnseenMemoryTicks(int count) {
      this.unseenMemoryTicks = count;
      return this;
   }
}