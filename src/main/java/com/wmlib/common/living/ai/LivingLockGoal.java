package wmlib.common.living.ai;

import java.util.EnumSet;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.Hand;
import net.minecraft.entity.ai.goal.Goal;
import wmlib.common.living.EntityWMSeat;
import wmlib.common.living.EntityWMVehicleBase;
public class LivingLockGoal extends Goal {
   protected final CreatureEntity mob;
   private final double speedModifier;
   private final boolean followingTargetEvenIfNotSeen;
   private Path path;
   private double pathedTargetX;
   private double pathedTargetY;
   private double pathedTargetZ;
   private int ticksUntilNextPathRecalculation;
   private int ticksUntilNextAttack;
   private final int attackInterval = 20;
   private long lastCanUseCheck;
   private int failedPathFindingPenalty = 0;
   public LivingLockGoal(CreatureEntity ent, double sp, boolean see) {
      this.mob = ent;
      this.speedModifier = sp;
      this.followingTargetEvenIfNotSeen = see;
      //this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
   }
	public boolean canUse() {
		long i = this.mob.level.getGameTime();
		if (i - this.lastCanUseCheck < 20L) {
		 return false;
		} else {
			this.lastCanUseCheck = i;
			LivingEntity livingentity = this.mob.getTarget();
			if (livingentity == null) {
				return false;
			} else if (!livingentity.isAlive()) {
				return false;
			} else {
				return true;
			}
		}
	}

	public boolean canContinueToUse() {
		LivingEntity livingentity = this.mob.getTarget();
		if(this.mob.getVehicle()!=null && this.mob.getVehicle() instanceof EntityWMSeat){
			EntityWMSeat seat  = (EntityWMSeat)this.mob.getVehicle();
			if(LivingLockGoal.this.TargetRange(this.mob, livingentity, seat.attack_height_max, seat.attack_height_min, seat.yRot, -seat.minyaw, seat.maxyaw)){
				if (livingentity == null) {
				 return false;
				} else if (!livingentity.isAlive()||livingentity.getHealth()<=0) {
				 return false;
				} else if (!this.followingTargetEvenIfNotSeen) {
				 return !this.mob.getNavigation().isDone();
				} else if (!this.mob.isWithinRestriction(livingentity.blockPosition())) {
				 return false;
				} else {
					return !(livingentity instanceof PlayerEntity) || !livingentity.isSpectator() && !((PlayerEntity)livingentity).isCreative();
				}
			}else{
				return false;
			}
		}else{
			if (livingentity == null) {
			 return false;
			} else if (!livingentity.isAlive()||livingentity.getHealth()<=0) {
			 return false;
			} else if (!this.followingTargetEvenIfNotSeen) {
			 return !this.mob.getNavigation().isDone();
			} else if (!this.mob.isWithinRestriction(livingentity.blockPosition())) {
			 return false;
			} else {
				return !(livingentity instanceof PlayerEntity) || !livingentity.isSpectator() && !((PlayerEntity)livingentity).isCreative();
			}
		}
	}
	public void start() {

	}
	public void stop() {
		LivingEntity livingentity = this.mob.getTarget();
		this.mob.setTarget((LivingEntity)null);
		this.mob.setAggressive(false);
		this.mob.getNavigation().stop();
	}

	protected boolean TargetRange(LivingEntity living, LivingEntity target, double maxy, double miny, float yawbase,  float maxyaw, float minyaw) {
		boolean task = false;
		if(target!=null){
			double dyp = living.getY() + maxy;
			double dym = living.getZ() - miny;
			double d5 = target.getX() - living.getX();
			double d7 = target.getZ() - living.getZ();
			float yaw= -((float) Math.atan2(d5, d7)) * 180.0F / (float) Math.PI;
			if (dyp > target.getY() && dym < target.getY()) {
				//task = true;
				boolean ok = false;
				if(yaw > yawbase - minyaw && yaw < yawbase + maxyaw){
					ok = true;
				}
				if(yawbase - minyaw < -180){
					float y = yawbase - minyaw + 360;
					if(yaw < yawbase + maxyaw && yaw + 360 > y) {
						ok = true;
					}
				}
				if(yawbase + maxyaw > 180){
					float y = yawbase + maxyaw - 360;
					if(yaw > yawbase - minyaw && yaw - 360 < y) {
						ok = true;
					}
				}
				if(ok) {
					task = true;
				}
			}
		}
		return task;
	}

	public void tick() {
	}
	protected void checkAndPerformAttack(LivingEntity p_190102_1_, double p_190102_2_) {
	}
	protected void resetAttackCooldown() {
	  this.ticksUntilNextAttack = 20;
	}
	protected boolean isTimeToAttack() {
	  return this.ticksUntilNextAttack <= 0;
	}
	protected int getTicksUntilNextAttack() {
	  return this.ticksUntilNextAttack;
	}
	protected int getAttackInterval() {
	  return 20;
	}
	protected double getAttackReachSqr(LivingEntity p_179512_1_) {
	  return (double)(this.mob.getBbWidth() * 2.0F * this.mob.getBbWidth() * 2.0F + p_179512_1_.getBbWidth());
	}
}
