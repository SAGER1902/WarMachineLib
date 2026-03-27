package wmlib.common.living.ai;

import java.util.EnumSet;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.Hand;
import net.minecraft.entity.ai.goal.Goal;
import wmlib.common.living.EntityWMSeat;
import wmlib.common.living.EntityWMVehicleBase;
public class VehicleLockGoal extends Goal {
   protected final EntityWMVehicleBase mob;
   private final boolean notsee;
   private Path path;
   private double pathedTargetX;
   private double pathedTargetY;
   private double pathedTargetZ;
   private int ticksUntilNextPathRecalculation;
   private int ticksUntilNextAttack;
   private final int attackInterval = 20;
   private long lastCanUseCheck;
   private int failedPathFindingPenalty = 0;
   private boolean canPenalize = false;
   public VehicleLockGoal(EntityWMVehicleBase ent, boolean see) {
      this.mob = ent;
      this.notsee = see;
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
		if(VehicleLockGoal.this.TargetRange(this.mob, livingentity, this.mob.attack_height_max, this.mob.attack_height_min, this.mob.yRot, -this.mob.minyaw, this.mob.maxyaw)
			||this.mob.minyaw==-360F&&this.mob.maxyaw==360F){
			if (livingentity == null) {
			 return false;
			} else if (!livingentity.isAlive()||livingentity.getHealth()<=0) {
			 return false;
			} else /*if (!this.notsee) {
			 return !this.mob.getNavigation().isDone();
			} else */if (!this.mob.isWithinRestriction(livingentity.blockPosition())) {
			 return false;
			} else {
				return !(livingentity instanceof PlayerEntity) || !livingentity.isSpectator() && !((PlayerEntity)livingentity).isCreative();
			}
		}else{
			return false;
		}
	}
	public void start() {
	}
	public void stop() {
		LivingEntity livingentity = this.mob.getTarget();
		this.mob.setTarget((LivingEntity)null);
		this.mob.getNavigation().stop();
		this.mob.setAttacking(false);
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
}
