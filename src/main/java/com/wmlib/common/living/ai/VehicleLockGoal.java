package wmlib.common.living.ai;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.entity.ai.goal.Goal;
import wmlib.common.living.EntityWMSeat;
import wmlib.common.living.EntityWMVehicleBase;
import wmlib.util.TurretAngle;
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
   private int failedpathfinderPenalty = 0;
   private boolean canPenalize = false;
   public VehicleLockGoal(EntityWMVehicleBase ent, boolean see) {
      this.mob = ent;
      this.notsee = see;
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
		} else /*if (!this.notsee) {
		 return !this.mob.getNavigation().isDone();
		} else */if (!this.mob.isWithinRestriction(livingentity.blockPosition())) {
		 return false;
		} else {
			if(!(livingentity instanceof Player) || !livingentity.isSpectator() && !((Player)livingentity).isCreative()){
				return TurretAngle.targetRange(this.mob, livingentity, 
				this.mob.attack_height_max, this.mob.attack_height_min, this.mob.getYHeadRot(), this.mob.minyaw, this.mob.maxyaw, 
				this.mob.attack_range_min, this.mob.turretPitchMax, this.mob.turretPitchMin);
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
		this.mob.getNavigation().stop();
		this.mob.setAttacking(false);
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
