package wmlib.common.living.ai;

import javax.annotation.Nullable;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.passive.TameableEntity;
public abstract class LivingTargetGoalSA extends Goal {
   protected final MobEntity mob;
   protected final boolean mustSee;
   private final boolean mustReach;
   private int reachCache;
   private int reachCacheTime;
   private int unseenTicks;
   protected LivingEntity targetMob;
   protected int unseenMemoryTicks = 60;

   public LivingTargetGoalSA(MobEntity soldier, boolean isCanSee) {
      this(soldier, isCanSee, false);
   }

   public LivingTargetGoalSA(MobEntity soldier, boolean isCanSee, boolean isReach) {
      this.mob = soldier;
      this.mustSee = isCanSee;
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
                  if (this.mob.getSensing().canSee(livingentity)) {
                     this.unseenTicks = 0;
                  } else if (++this.unseenTicks > this.unseenMemoryTicks) {
                     return false;
                  }
               }
               if (livingentity instanceof PlayerEntity && ((PlayerEntity)livingentity).abilities.invulnerable) {
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

   protected double getFollowDistance() {
		return this.mob.getAttributeValue(Attributes.FOLLOW_RANGE);
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

   protected boolean canAttack(@Nullable LivingEntity attackentity, EntityPredicate predicate) {
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

   public LivingTargetGoalSA setUnseenMemoryTicks(int count) {
      this.unseenMemoryTicks = count;
      return this;
   }
}