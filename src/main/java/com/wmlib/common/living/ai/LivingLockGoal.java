package wmlib.common.living.ai;

import java.util.EnumSet;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.entity.EntitySelector;

import net.minecraft.world.entity.ai.goal.Goal;
import wmlib.common.living.EntityWMSeat;
import net.minecraftforge.fml.ModList;

import wmlib.util.TurretAngle;
public class LivingLockGoal extends Goal {
   protected final PathfinderMob mob;
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
   private int failedpathfinderPenalty = 0;
   private boolean canPenalize = false;
   public LivingLockGoal(PathfinderMob ent, double MoveSpeed, boolean see) {
      this.mob = ent;
      this.speedModifier = MoveSpeed;
      this.followingTargetEvenIfNotSeen = see;
   }
	public boolean canUse() {
		long i = this.mob.level().getGameTime();
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
		if (livingentity == null) {
		 return false;
		} else if (!livingentity.isAlive()||livingentity.getHealth()<=0) {
		 return false;
		} else /*if (!this.followingTargetEvenIfNotSeen) {
		 return !this.mob.getNavigation().isDone();
		} else */if (!this.mob.isWithinRestriction(livingentity.blockPosition())) {
		 return false;
		} else {
			if(!(livingentity instanceof Player) || !livingentity.isSpectator() && !((Player)livingentity).isCreative()){
				/*if(this.mob.getVehicle()!=null && this.mob.getVehicle() instanceof EntityWMSeat){
					EntityWMSeat seat  = (EntityWMSeat)this.mob.getVehicle();
					return TurretAngle.targetRange(this.mob, livingentity, seat.attack_height_max, seat.attack_height_min, seat.getYRot(), seat.minyaw, seat.maxyaw, seat.attack_range_min, seat.turretPitchMax, seat.turretPitchMin);
				}else*/{
					return true;
				}
			}else{
				return false;
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
